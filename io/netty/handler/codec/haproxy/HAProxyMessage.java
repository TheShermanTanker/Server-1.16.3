package io.netty.handler.codec.haproxy;

import io.netty.util.NetUtil;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.util.CharsetUtil;
import io.netty.util.ByteProcessor;
import io.netty.buffer.ByteBuf;
import java.util.Collections;
import java.util.List;

public final class HAProxyMessage {
    private static final HAProxyMessage V1_UNKNOWN_MSG;
    private static final HAProxyMessage V2_UNKNOWN_MSG;
    private static final HAProxyMessage V2_LOCAL_MSG;
    private final HAProxyProtocolVersion protocolVersion;
    private final HAProxyCommand command;
    private final HAProxyProxiedProtocol proxiedProtocol;
    private final String sourceAddress;
    private final String destinationAddress;
    private final int sourcePort;
    private final int destinationPort;
    private final List<HAProxyTLV> tlvs;
    
    private HAProxyMessage(final HAProxyProtocolVersion protocolVersion, final HAProxyCommand command, final HAProxyProxiedProtocol proxiedProtocol, final String sourceAddress, final String destinationAddress, final String sourcePort, final String destinationPort) {
        this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, portStringToInt(sourcePort), portStringToInt(destinationPort));
    }
    
    private HAProxyMessage(final HAProxyProtocolVersion protocolVersion, final HAProxyCommand command, final HAProxyProxiedProtocol proxiedProtocol, final String sourceAddress, final String destinationAddress, final int sourcePort, final int destinationPort) {
        this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, sourcePort, destinationPort, (List<HAProxyTLV>)Collections.emptyList());
    }
    
    private HAProxyMessage(final HAProxyProtocolVersion protocolVersion, final HAProxyCommand command, final HAProxyProxiedProtocol proxiedProtocol, final String sourceAddress, final String destinationAddress, final int sourcePort, final int destinationPort, final List<HAProxyTLV> tlvs) {
        if (proxiedProtocol == null) {
            throw new NullPointerException("proxiedProtocol");
        }
        final HAProxyProxiedProtocol.AddressFamily addrFamily = proxiedProtocol.addressFamily();
        checkAddress(sourceAddress, addrFamily);
        checkAddress(destinationAddress, addrFamily);
        checkPort(sourcePort);
        checkPort(destinationPort);
        this.protocolVersion = protocolVersion;
        this.command = command;
        this.proxiedProtocol = proxiedProtocol;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.tlvs = (List<HAProxyTLV>)Collections.unmodifiableList((List)tlvs);
    }
    
    static HAProxyMessage decodeHeader(final ByteBuf header) {
        if (header == null) {
            throw new NullPointerException("header");
        }
        if (header.readableBytes() < 16) {
            throw new HAProxyProtocolException(new StringBuilder().append("incomplete header: ").append(header.readableBytes()).append(" bytes (expected: 16+ bytes)").toString());
        }
        header.skipBytes(12);
        final byte verCmdByte = header.readByte();
        HAProxyProtocolVersion ver;
        try {
            ver = HAProxyProtocolVersion.valueOf(verCmdByte);
        }
        catch (IllegalArgumentException e) {
            throw new HAProxyProtocolException((Throwable)e);
        }
        if (ver != HAProxyProtocolVersion.V2) {
            throw new HAProxyProtocolException("version 1 unsupported: 0x" + Integer.toHexString((int)verCmdByte));
        }
        HAProxyCommand cmd;
        try {
            cmd = HAProxyCommand.valueOf(verCmdByte);
        }
        catch (IllegalArgumentException e2) {
            throw new HAProxyProtocolException((Throwable)e2);
        }
        if (cmd == HAProxyCommand.LOCAL) {
            return HAProxyMessage.V2_LOCAL_MSG;
        }
        HAProxyProxiedProtocol protAndFam;
        try {
            protAndFam = HAProxyProxiedProtocol.valueOf(header.readByte());
        }
        catch (IllegalArgumentException e3) {
            throw new HAProxyProtocolException((Throwable)e3);
        }
        if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
            return HAProxyMessage.V2_UNKNOWN_MSG;
        }
        final int addressInfoLen = header.readUnsignedShort();
        int srcPort = 0;
        int dstPort = 0;
        final HAProxyProxiedProtocol.AddressFamily addressFamily = protAndFam.addressFamily();
        String srcAddress;
        String dstAddress;
        if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_UNIX) {
            if (addressInfoLen < 216 || header.readableBytes() < 216) {
                throw new HAProxyProtocolException(new StringBuilder().append("incomplete UNIX socket address information: ").append(Math.min(addressInfoLen, header.readableBytes())).append(" bytes (expected: 216+ bytes)").toString());
            }
            int startIdx = header.readerIndex();
            int addressEnd = header.forEachByte(startIdx, 108, ByteProcessor.FIND_NUL);
            int addressLen;
            if (addressEnd == -1) {
                addressLen = 108;
            }
            else {
                addressLen = addressEnd - startIdx;
            }
            srcAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
            startIdx += 108;
            addressEnd = header.forEachByte(startIdx, 108, ByteProcessor.FIND_NUL);
            if (addressEnd == -1) {
                addressLen = 108;
            }
            else {
                addressLen = addressEnd - startIdx;
            }
            dstAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
            header.readerIndex(startIdx + 108);
        }
        else {
            int addressLen;
            if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv4) {
                if (addressInfoLen < 12 || header.readableBytes() < 12) {
                    throw new HAProxyProtocolException(new StringBuilder().append("incomplete IPv4 address information: ").append(Math.min(addressInfoLen, header.readableBytes())).append(" bytes (expected: 12+ bytes)").toString());
                }
                addressLen = 4;
            }
            else {
                if (addressFamily != HAProxyProxiedProtocol.AddressFamily.AF_IPv6) {
                    throw new HAProxyProtocolException(new StringBuilder().append("unable to parse address information (unknown address family: ").append(addressFamily).append(')').toString());
                }
                if (addressInfoLen < 36 || header.readableBytes() < 36) {
                    throw new HAProxyProtocolException(new StringBuilder().append("incomplete IPv6 address information: ").append(Math.min(addressInfoLen, header.readableBytes())).append(" bytes (expected: 36+ bytes)").toString());
                }
                addressLen = 16;
            }
            srcAddress = ipBytesToString(header, addressLen);
            dstAddress = ipBytesToString(header, addressLen);
            srcPort = header.readUnsignedShort();
            dstPort = header.readUnsignedShort();
        }
        final List<HAProxyTLV> tlvs = readTlvs(header);
        return new HAProxyMessage(ver, cmd, protAndFam, srcAddress, dstAddress, srcPort, dstPort, tlvs);
    }
    
    private static List<HAProxyTLV> readTlvs(final ByteBuf header) {
        HAProxyTLV haProxyTLV = readNextTLV(header);
        if (haProxyTLV == null) {
            return (List<HAProxyTLV>)Collections.emptyList();
        }
        final List<HAProxyTLV> haProxyTLVs = (List<HAProxyTLV>)new ArrayList(4);
        do {
            haProxyTLVs.add(haProxyTLV);
            if (haProxyTLV instanceof HAProxySSLTLV) {
                haProxyTLVs.addAll((Collection)((HAProxySSLTLV)haProxyTLV).encapsulatedTLVs());
            }
        } while ((haProxyTLV = readNextTLV(header)) != null);
        return haProxyTLVs;
    }
    
    private static HAProxyTLV readNextTLV(final ByteBuf header) {
        if (header.readableBytes() < 4) {
            return null;
        }
        final byte typeAsByte = header.readByte();
        final HAProxyTLV.Type type = HAProxyTLV.Type.typeForByteValue(typeAsByte);
        final int length = header.readUnsignedShort();
        switch (type) {
            case PP2_TYPE_SSL: {
                final ByteBuf rawContent = header.retainedSlice(header.readerIndex(), length);
                final ByteBuf byteBuf = header.readSlice(length);
                final byte client = byteBuf.readByte();
                final int verify = byteBuf.readInt();
                if (byteBuf.readableBytes() >= 4) {
                    final List<HAProxyTLV> encapsulatedTlvs = (List<HAProxyTLV>)new ArrayList(4);
                    do {
                        final HAProxyTLV haProxyTLV = readNextTLV(byteBuf);
                        if (haProxyTLV == null) {
                            break;
                        }
                        encapsulatedTlvs.add(haProxyTLV);
                    } while (byteBuf.readableBytes() >= 4);
                    return new HAProxySSLTLV(verify, client, encapsulatedTlvs, rawContent);
                }
                return new HAProxySSLTLV(verify, client, (List<HAProxyTLV>)Collections.emptyList(), rawContent);
            }
            case PP2_TYPE_ALPN:
            case PP2_TYPE_AUTHORITY:
            case PP2_TYPE_SSL_VERSION:
            case PP2_TYPE_SSL_CN:
            case PP2_TYPE_NETNS:
            case OTHER: {
                return new HAProxyTLV(type, typeAsByte, header.readRetainedSlice(length));
            }
            default: {
                return null;
            }
        }
    }
    
    static HAProxyMessage decodeHeader(final String header) {
        if (header == null) {
            throw new HAProxyProtocolException("header");
        }
        final String[] parts = header.split(" ");
        final int numParts = parts.length;
        if (numParts < 2) {
            throw new HAProxyProtocolException("invalid header: " + header + " (expected: 'PROXY' and proxied protocol values)");
        }
        if (!"PROXY".equals(parts[0])) {
            throw new HAProxyProtocolException("unknown identifier: " + parts[0]);
        }
        HAProxyProxiedProtocol protAndFam;
        try {
            protAndFam = HAProxyProxiedProtocol.valueOf(parts[1]);
        }
        catch (IllegalArgumentException e) {
            throw new HAProxyProtocolException((Throwable)e);
        }
        if (protAndFam != HAProxyProxiedProtocol.TCP4 && protAndFam != HAProxyProxiedProtocol.TCP6 && protAndFam != HAProxyProxiedProtocol.UNKNOWN) {
            throw new HAProxyProtocolException("unsupported v1 proxied protocol: " + parts[1]);
        }
        if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
            return HAProxyMessage.V1_UNKNOWN_MSG;
        }
        if (numParts != 6) {
            throw new HAProxyProtocolException("invalid TCP4/6 header: " + header + " (expected: 6 parts)");
        }
        return new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, protAndFam, parts[2], parts[3], parts[4], parts[5]);
    }
    
    private static String ipBytesToString(final ByteBuf header, final int addressLen) {
        final StringBuilder sb = new StringBuilder();
        if (addressLen == 4) {
            sb.append(header.readByte() & 0xFF);
            sb.append('.');
            sb.append(header.readByte() & 0xFF);
            sb.append('.');
            sb.append(header.readByte() & 0xFF);
            sb.append('.');
            sb.append(header.readByte() & 0xFF);
        }
        else {
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
            sb.append(':');
            sb.append(Integer.toHexString(header.readUnsignedShort()));
        }
        return sb.toString();
    }
    
    private static int portStringToInt(final String value) {
        int port;
        try {
            port = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new HAProxyProtocolException("invalid port: " + value, (Throwable)e);
        }
        if (port <= 0 || port > 65535) {
            throw new HAProxyProtocolException("invalid port: " + value + " (expected: 1 ~ 65535)");
        }
        return port;
    }
    
    private static void checkAddress(final String address, final HAProxyProxiedProtocol.AddressFamily addrFamily) {
        if (addrFamily == null) {
            throw new NullPointerException("addrFamily");
        }
        switch (addrFamily) {
            case AF_UNSPEC: {
                if (address != null) {
                    throw new HAProxyProtocolException("unable to validate an AF_UNSPEC address: " + address);
                }
            }
            case AF_UNIX: {}
            default: {
                if (address == null) {
                    throw new NullPointerException("address");
                }
                switch (addrFamily) {
                    case AF_IPv4: {
                        if (!NetUtil.isValidIpV4Address(address)) {
                            throw new HAProxyProtocolException("invalid IPv4 address: " + address);
                        }
                        break;
                    }
                    case AF_IPv6: {
                        if (!NetUtil.isValidIpV6Address(address)) {
                            throw new HAProxyProtocolException("invalid IPv6 address: " + address);
                        }
                        break;
                    }
                    default: {
                        throw new Error();
                    }
                }
            }
        }
    }
    
    private static void checkPort(final int port) {
        if (port < 0 || port > 65535) {
            throw new HAProxyProtocolException(new StringBuilder().append("invalid port: ").append(port).append(" (expected: 1 ~ 65535)").toString());
        }
    }
    
    public HAProxyProtocolVersion protocolVersion() {
        return this.protocolVersion;
    }
    
    public HAProxyCommand command() {
        return this.command;
    }
    
    public HAProxyProxiedProtocol proxiedProtocol() {
        return this.proxiedProtocol;
    }
    
    public String sourceAddress() {
        return this.sourceAddress;
    }
    
    public String destinationAddress() {
        return this.destinationAddress;
    }
    
    public int sourcePort() {
        return this.sourcePort;
    }
    
    public int destinationPort() {
        return this.destinationPort;
    }
    
    public List<HAProxyTLV> tlvs() {
        return this.tlvs;
    }
    
    static {
        V1_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
        V2_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
        V2_LOCAL_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.LOCAL, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
    }
}
