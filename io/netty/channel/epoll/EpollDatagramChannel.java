package io.netty.channel.epoll;

import io.netty.channel.unix.DatagramSocketAddress;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelConfig;
import io.netty.util.internal.StringUtil;
import io.netty.channel.DefaultAddressedEnvelope;
import io.netty.channel.unix.UnixChannelUtil;
import io.netty.channel.socket.DatagramPacket;
import java.nio.ByteBuffer;
import io.netty.channel.unix.IovArray;
import io.netty.buffer.ByteBuf;
import io.netty.channel.AddressedEnvelope;
import java.io.IOException;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.NetworkInterface;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.socket.DatagramChannel;

public final class EpollDatagramChannel extends AbstractEpollChannel implements DatagramChannel {
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private final EpollDatagramChannelConfig config;
    private volatile boolean connected;
    
    public EpollDatagramChannel() {
        super(LinuxSocket.newSocketDgram(), Native.EPOLLIN);
        this.config = new EpollDatagramChannelConfig(this);
    }
    
    public EpollDatagramChannel(final int fd) {
        this(new LinuxSocket(fd));
    }
    
    EpollDatagramChannel(final LinuxSocket fd) {
        super(null, fd, Native.EPOLLIN, true);
        this.config = new EpollDatagramChannelConfig(this);
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return EpollDatagramChannel.METADATA;
    }
    
    @Override
    public boolean isActive() {
        return this.socket.isOpen() && ((this.config.getActiveOnOpen() && this.isRegistered()) || this.active);
    }
    
    @Override
    public boolean isConnected() {
        return this.connected;
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress) {
        return this.joinGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.joinGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.joinGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.joinGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.joinGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture joinGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress) {
        return this.leaveGroup(multicastAddress, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final ChannelPromise promise) {
        try {
            return this.leaveGroup(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), null, promise);
        }
        catch (SocketException e) {
            promise.setFailure((Throwable)e);
            return promise;
        }
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface) {
        return this.leaveGroup(multicastAddress, networkInterface, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetSocketAddress multicastAddress, final NetworkInterface networkInterface, final ChannelPromise promise) {
        return this.leaveGroup(multicastAddress.getAddress(), networkInterface, null, promise);
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source) {
        return this.leaveGroup(multicastAddress, networkInterface, source, this.newPromise());
    }
    
    @Override
    public ChannelFuture leaveGroup(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress source, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, networkInterface, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final NetworkInterface networkInterface, final InetAddress sourceToBlock, final ChannelPromise promise) {
        if (multicastAddress == null) {
            throw new NullPointerException("multicastAddress");
        }
        if (sourceToBlock == null) {
            throw new NullPointerException("sourceToBlock");
        }
        if (networkInterface == null) {
            throw new NullPointerException("networkInterface");
        }
        promise.setFailure((Throwable)new UnsupportedOperationException("Multicast not supported"));
        return promise;
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock) {
        return this.block(multicastAddress, sourceToBlock, this.newPromise());
    }
    
    @Override
    public ChannelFuture block(final InetAddress multicastAddress, final InetAddress sourceToBlock, final ChannelPromise promise) {
        try {
            return this.block(multicastAddress, NetworkInterface.getByInetAddress(this.localAddress().getAddress()), sourceToBlock, promise);
        }
        catch (Throwable e) {
            promise.setFailure(e);
            return promise;
        }
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollDatagramChannelUnsafe();
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        super.doBind(localAddress);
        this.active = true;
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        while (true) {
            final Object msg = in.current();
            if (msg == null) {
                this.clearFlag(Native.EPOLLOUT);
                break;
            }
            try {
                if (Native.IS_SUPPORTING_SENDMMSG && in.size() > 1) {
                    final NativeDatagramPacketArray array = NativeDatagramPacketArray.getInstance(in);
                    int cnt = array.count();
                    if (cnt >= 1) {
                        int offset = 0;
                        final NativeDatagramPacketArray.NativeDatagramPacket[] packets = array.packets();
                        while (cnt > 0) {
                            final int send = Native.sendmmsg(this.socket.intValue(), packets, offset, cnt);
                            if (send == 0) {
                                this.setFlag(Native.EPOLLOUT);
                                return;
                            }
                            for (int i = 0; i < send; ++i) {
                                in.remove();
                            }
                            cnt -= send;
                            offset += send;
                        }
                        continue;
                    }
                }
                boolean done = false;
                for (int j = this.config().getWriteSpinCount(); j > 0; --j) {
                    if (this.doWriteMessage(msg)) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    this.setFlag(Native.EPOLLOUT);
                    break;
                }
                in.remove();
            }
            catch (IOException e) {
                in.remove((Throwable)e);
            }
        }
    }
    
    private boolean doWriteMessage(final Object msg) throws Exception {
        ByteBuf data;
        InetSocketAddress remoteAddress;
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<ByteBuf, InetSocketAddress> envelope = (AddressedEnvelope<ByteBuf, InetSocketAddress>)msg;
            data = envelope.content();
            remoteAddress = envelope.recipient();
        }
        else {
            data = (ByteBuf)msg;
            remoteAddress = null;
        }
        final int dataLen = data.readableBytes();
        if (dataLen == 0) {
            return true;
        }
        long writtenBytes;
        if (data.hasMemoryAddress()) {
            final long memoryAddress = data.memoryAddress();
            if (remoteAddress == null) {
                writtenBytes = this.socket.writeAddress(memoryAddress, data.readerIndex(), data.writerIndex());
            }
            else {
                writtenBytes = this.socket.sendToAddress(memoryAddress, data.readerIndex(), data.writerIndex(), remoteAddress.getAddress(), remoteAddress.getPort());
            }
        }
        else if (data.nioBufferCount() > 1) {
            final IovArray array = ((EpollEventLoop)this.eventLoop()).cleanArray();
            array.add(data);
            final int cnt = array.count();
            assert cnt != 0;
            if (remoteAddress == null) {
                writtenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
            }
            else {
                writtenBytes = this.socket.sendToAddresses(array.memoryAddress(0), cnt, remoteAddress.getAddress(), remoteAddress.getPort());
            }
        }
        else {
            final ByteBuffer nioData = data.internalNioBuffer(data.readerIndex(), data.readableBytes());
            if (remoteAddress == null) {
                writtenBytes = this.socket.write(nioData, nioData.position(), nioData.limit());
            }
            else {
                writtenBytes = this.socket.sendTo(nioData, nioData.position(), nioData.limit(), remoteAddress.getAddress(), remoteAddress.getPort());
            }
        }
        return writtenBytes > 0L;
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) {
        if (msg instanceof DatagramPacket) {
            final DatagramPacket packet = (DatagramPacket)msg;
            final ByteBuf content = ((DefaultAddressedEnvelope<ByteBuf, A>)packet).content();
            return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DatagramPacket(this.newDirectBuffer(packet, content), ((DefaultAddressedEnvelope<M, InetSocketAddress>)packet).recipient()) : msg;
        }
        if (msg instanceof ByteBuf) {
            final ByteBuf buf = (ByteBuf)msg;
            return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? this.newDirectBuffer(buf) : buf;
        }
        if (msg instanceof AddressedEnvelope) {
            final AddressedEnvelope<Object, SocketAddress> e = (AddressedEnvelope<Object, SocketAddress>)msg;
            if (e.content() instanceof ByteBuf && (e.recipient() == null || e.recipient() instanceof InetSocketAddress)) {
                final ByteBuf content = e.content();
                return UnixChannelUtil.isBufferCopyNeededForWrite(content) ? new DefaultAddressedEnvelope<Object, SocketAddress>(this.newDirectBuffer(e, content), e.recipient()) : e;
            }
        }
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + EpollDatagramChannel.EXPECTED_TYPES);
    }
    
    @Override
    public EpollDatagramChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.socket.disconnect();
        final boolean b = false;
        this.active = b;
        this.connected = b;
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        return super.doConnect(remoteAddress, localAddress) && (this.connected = true);
    }
    
    @Override
    protected void doClose() throws Exception {
        super.doClose();
        this.connected = false;
    }
    
    static {
        METADATA = new ChannelMetadata(true);
        EXPECTED_TYPES = " (expected: " + StringUtil.simpleClassName(DatagramPacket.class) + ", " + StringUtil.simpleClassName(AddressedEnvelope.class) + '<' + StringUtil.simpleClassName(ByteBuf.class) + ", " + StringUtil.simpleClassName(InetSocketAddress.class) + ">, " + StringUtil.simpleClassName(ByteBuf.class) + ')';
    }
    
    final class EpollDatagramChannelUnsafe extends AbstractEpollUnsafe {
        @Override
        void epollInReady() {
            assert EpollDatagramChannel.this.eventLoop().inEventLoop();
            final DatagramChannelConfig config = EpollDatagramChannel.this.config();
            if (EpollDatagramChannel.this.shouldBreakEpollInReady(config)) {
                this.clearEpollIn0();
                return;
            }
            final EpollRecvByteAllocatorHandle allocHandle = this.recvBufAllocHandle();
            allocHandle.edgeTriggered(EpollDatagramChannel.this.isFlagSet(Native.EPOLLET));
            final ChannelPipeline pipeline = EpollDatagramChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            this.epollInBefore();
            Throwable exception = null;
            try {
                ByteBuf data = null;
                try {
                    do {
                        data = allocHandle.allocate(allocator);
                        allocHandle.attemptedBytesRead(data.writableBytes());
                        DatagramSocketAddress remoteAddress;
                        if (data.hasMemoryAddress()) {
                            remoteAddress = EpollDatagramChannel.this.socket.recvFromAddress(data.memoryAddress(), data.writerIndex(), data.capacity());
                        }
                        else {
                            final ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
                            remoteAddress = EpollDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
                        }
                        if (remoteAddress == null) {
                            allocHandle.lastBytesRead(-1);
                            data.release();
                            data = null;
                            break;
                        }
                        InetSocketAddress localAddress = remoteAddress.localAddress();
                        if (localAddress == null) {
                            localAddress = (InetSocketAddress)this.localAddress();
                        }
                        allocHandle.incMessagesRead(1);
                        allocHandle.lastBytesRead(remoteAddress.receivedAmount());
                        data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
                        this.readPending = false;
                        pipeline.fireChannelRead(new DatagramPacket(data, localAddress, remoteAddress));
                        data = null;
                    } while (allocHandle.continueReading());
                }
                catch (Throwable t) {
                    if (data != null) {
                        data.release();
                    }
                    exception = t;
                }
                allocHandle.readComplete();
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    pipeline.fireExceptionCaught(exception);
                }
            }
            finally {
                this.epollInFinally(config);
            }
        }
    }
}
