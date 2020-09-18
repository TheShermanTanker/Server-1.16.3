package io.netty.handler.codec.socks;

import io.netty.channel.ChannelHandler;
import io.netty.util.NetUtil;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class SocksCmdResponseDecoder extends ReplayingDecoder<State> {
    private SocksCmdStatus cmdStatus;
    private SocksAddressType addressType;
    
    public SocksCmdResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        Label_0325: {
            switch (this.state()) {
                case CHECK_PROTOCOL_VERSION: {
                    if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
                        out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                        break;
                    }
                    this.checkpoint(State.READ_CMD_HEADER);
                }
                case READ_CMD_HEADER: {
                    this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
                    byteBuf.skipBytes(1);
                    this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
                    this.checkpoint(State.READ_CMD_ADDRESS);
                }
                case READ_CMD_ADDRESS: {
                    switch (this.addressType) {
                        case IPv4: {
                            final String host = NetUtil.intToIpAddress(byteBuf.readInt());
                            final int port = byteBuf.readUnsignedShort();
                            out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
                            break Label_0325;
                        }
                        case DOMAIN: {
                            final int fieldLength = byteBuf.readByte();
                            final String host2 = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
                            final int port2 = byteBuf.readUnsignedShort();
                            out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host2, port2));
                            break Label_0325;
                        }
                        case IPv6: {
                            final byte[] bytes = new byte[16];
                            byteBuf.readBytes(bytes);
                            final String host2 = SocksCommonUtils.ipv6toStr(bytes);
                            final int port2 = byteBuf.readUnsignedShort();
                            out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host2, port2));
                            break Label_0325;
                        }
                        case UNKNOWN: {
                            out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                            break Label_0325;
                        }
                        default: {
                            throw new Error();
                        }
                    }
                    break;
                }
                default: {
                    throw new Error();
                }
            }
        }
        ctx.pipeline().remove(this);
    }
    
    enum State {
        CHECK_PROTOCOL_VERSION, 
        READ_CMD_HEADER, 
        READ_CMD_ADDRESS;
    }
}
