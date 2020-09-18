package io.netty.channel.socket.oio;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import java.net.NetworkInterface;
import java.io.IOException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.StringUtil;
import java.nio.channels.NotYetConnectedException;
import io.netty.channel.AddressedEnvelope;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import java.net.SocketTimeoutException;
import java.net.InetAddress;
import java.util.List;
import java.net.InetSocketAddress;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.DatagramChannelConfig;
import java.net.DatagramSocket;
import java.net.SocketException;
import io.netty.util.internal.EmptyArrays;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import java.net.SocketAddress;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import io.netty.channel.ChannelMetadata;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.oio.AbstractOioMessageChannel;

public class OioDatagramChannel extends AbstractOioMessageChannel implements DatagramChannel {
    private static final InternalLogger logger;
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private final MulticastSocket socket;
    private final OioDatagramChannelConfig config;
    private final DatagramPacket tmpPacket;
    
    private static MulticastSocket newSocket() {
        try {
            return new MulticastSocket((SocketAddress)null);
        }
        catch (Exception e) {
            throw new ChannelException("failed to create a new socket", (Throwable)e);
        }
    }
    
    public OioDatagramChannel() {
        this(newSocket());
    }
    
    public OioDatagramChannel(final MulticastSocket socket) {
        super(null);
        this.tmpPacket = new DatagramPacket(EmptyArrays.EMPTY_BYTES, 0);
        boolean success = false;
        try {
            socket.setSoTimeout(1000);
            socket.setBroadcast(false);
            success = true;
        }
        catch (SocketException e) {
            throw new ChannelException("Failed to configure the datagram socket timeout.", (Throwable)e);
        }
        finally {
            if (!success) {
                socket.close();
            }
        }
        this.socket = socket;
        this.config = new DefaultOioDatagramChannelConfig(this, (DatagramSocket)socket);
    }
    
    public ChannelMetadata metadata() {
        return OioDatagramChannel.METADATA;
    }
    
    @Override
    public DatagramChannelConfig config() {
        return this.config;
    }
    
    public boolean isOpen() {
        return !this.socket.isClosed();
    }
    
    public boolean isActive() {
        return this.isOpen() && ((this.config.<Boolean>getOption(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION) && this.isRegistered()) || this.socket.isBound());
    }
    
    @Override
    public boolean isConnected() {
        return this.socket.isConnected();
    }
    
    protected SocketAddress localAddress0() {
        return this.socket.getLocalSocketAddress();
    }
    
    protected SocketAddress remoteAddress0() {
        return this.socket.getRemoteSocketAddress();
    }
    
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.socket.bind(localAddress);
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    protected void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.socket.bind(localAddress);
        }
        boolean success = false;
        try {
            this.socket.connect(remoteAddress);
            success = true;
        }
        finally {
            if (!success) {
                try {
                    this.socket.close();
                }
                catch (Throwable t) {
                    OioDatagramChannel.logger.warn("Failed to close a socket.", t);
                }
            }
        }
    }
    
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
    }
    
    protected void doClose() throws Exception {
        this.socket.close();
    }
    
    @Override
    protected int doReadMessages(final List<Object> buf) throws Exception {
        final DatagramChannelConfig config = this.config();
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        final ByteBuf data = config.getAllocator().heapBuffer(allocHandle.guess());
        boolean free = true;
        try {
            this.tmpPacket.setAddress((InetAddress)null);
            this.tmpPacket.setData(data.array(), data.arrayOffset(), data.capacity());
            this.socket.receive(this.tmpPacket);
            final InetSocketAddress remoteAddr = (InetSocketAddress)this.tmpPacket.getSocketAddress();
            allocHandle.lastBytesRead(this.tmpPacket.getLength());
            buf.add(new io.netty.channel.socket.DatagramPacket(data.writerIndex(allocHandle.lastBytesRead()), this.localAddress(), remoteAddr));
            free = false;
            return 1;
        }
        catch (SocketTimeoutException e2) {
            return 0;
        }
        catch (SocketException e) {
            if (!e.getMessage().toLowerCase(Locale.US).contains("socket closed")) {
                throw e;
            }
            return -1;
        }
        catch (Throwable cause) {
            PlatformDependent.throwException(cause);
            return -1;
        }
        finally {
            if (free) {
                data.release();
            }
        }
    }
    
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object o = in.current();
            if (o == null) {
                break;
            }
            SocketAddress remoteAddress;
            ByteBuf data;
            if (o instanceof AddressedEnvelope) {
                final AddressedEnvelope<ByteBuf, SocketAddress> envelope = (AddressedEnvelope<ByteBuf, SocketAddress>)o;
                remoteAddress = envelope.recipient();
                data = envelope.content();
            }
            else {
                data = (ByteBuf)o;
                remoteAddress = null;
            }
            final int length = data.readableBytes();
            try {
                if (remoteAddress != null) {
                    this.tmpPacket.setSocketAddress(remoteAddress);
                }
                else {
                    if (!this.isConnected()) {
                        throw new NotYetConnectedException();
                    }
                    this.tmpPacket.setAddress((InetAddress)null);
                }
                if (data.hasArray()) {
                    this.tmpPacket.setData(data.array(), data.arrayOffset() + data.readerIndex(), length);
                }
                else {
                    final byte[] tmp = new byte[length];
                    data.getBytes(data.readerIndex(), tmp);
                    this.tmpPacket.setData(tmp);
                }
                this.socket.send(this.tmpPacket);
                in.remove();
            }
            catch (Exception e) {
                in.remove((Throwable)e);
            }
        }
    }
    
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof io.netty.channel.socket.DatagramPacket || msg instanceof ByteBuf) {
            return msg;
        }
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
            if (e.content() instanceof ByteBuf) {
                return msg;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + OioDatagramChannel.EXPECTED_TYPES);
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress) {
        return this.joinGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        this.ensureBound();
        try {
            this.socket.joinGroup(multicastAddress);
            promise.setSuccess();
        }
        catch (IOException e) {
            promise.setFailure((Throwable)e);
        }
        return promise;
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.joinGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        this.ensureBound();
        try {
            this.socket.joinGroup((SocketAddress)multicastAddress, networkInterface);
            promise.setSuccess();
        }
        catch (IOException e) {
            promise.setFailure((Throwable)e);
        }
        return promise;
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.newFailedFuture((Throwable)new UnsupportedOperationException());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        promise.setFailure((Throwable)new UnsupportedOperationException());
        return promise;
    }
    
    private void ensureBound() {
        if (!this.isActive()) {
            throw new IllegalStateException(DatagramChannel.class.getName() + " must be bound to join a group.");
        }
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress) {
        return this.leaveGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            this.socket.leaveGroup(multicastAddress);
            promise.setSuccess();
        }
        catch (IOException e) {
            promise.setFailure((Throwable)e);
        }
        return promise;
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.leaveGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        try {
            this.socket.leaveGroup((SocketAddress)multicastAddress, networkInterface);
            promise.setSuccess();
        }
        catch (IOException e) {
            promise.setFailure((Throwable)e);
        }
        return promise;
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.newFailedFuture((Throwable)new UnsupportedOperationException());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        promise.setFailure((Throwable)new UnsupportedOperationException());
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock) {
        return this.newFailedFuture((Throwable)new UnsupportedOperationException());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock, final ChannelPromise promise) {
        promise.setFailure((Throwable)new UnsupportedOperationException());
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock) {
        return this.newFailedFuture((Throwable)new UnsupportedOperationException());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock, final ChannelPromise promise) {
        promise.setFailure((Throwable)new UnsupportedOperationException());
        return promise;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(OioDatagramChannel.class);
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(io.netty.channel.socket.DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(SocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }
}
