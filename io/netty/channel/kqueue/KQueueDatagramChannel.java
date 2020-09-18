package io.netty.channel.kqueue;

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

public final class KQueueDatagramChannel extends AbstractKQueueChannel implements DatagramChannel {
    private static final ChannelMetadata METADATA;
    private static final String EXPECTED_TYPES;
    private volatile boolean connected;
    private final KQueueDatagramChannelConfig config;
    
    public KQueueDatagramChannel() {
        super(null, BsdSocket.newSocketDgram(), false);
        this.config = new KQueueDatagramChannelConfig(this);
    }
    
    public KQueueDatagramChannel(final int fd) {
        this(new BsdSocket(fd), true);
    }
    
    KQueueDatagramChannel(final BsdSocket socket, final boolean active) {
        super(null, socket, active);
        this.config = new KQueueDatagramChannelConfig(this);
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
        return KQueueDatagramChannel.METADATA;
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
    protected AbstractKQueueUnsafe newUnsafe() {
        return new KQueueDatagramChannelUnsafe();
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
                this.writeFilter(false);
                break;
            }
            try {
                boolean done = false;
                for (int i = this.config().getWriteSpinCount(); i > 0; --i) {
                    if (this.doWriteMessage(msg)) {
                        done = true;
                        break;
                    }
                }
                if (!done) {
                    this.writeFilter(true);
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
            final IovArray array = ((KQueueEventLoop)this.eventLoop()).cleanArray();
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
        throw new UnsupportedOperationException("unsupported message type: " + StringUtil.simpleClassName(msg) + KQueueDatagramChannel.EXPECTED_TYPES);
    }
    
    @Override
    public KQueueDatagramChannelConfig config() {
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
    
    final class KQueueDatagramChannelUnsafe extends AbstractKQueueUnsafe {
        @Override
        void readReady(final KQueueRecvByteAllocatorHandle allocHandle) {
            assert KQueueDatagramChannel.this.eventLoop().inEventLoop();
            final DatagramChannelConfig config = KQueueDatagramChannel.this.config();
            if (KQueueDatagramChannel.this.shouldBreakReadReady(config)) {
                this.clearReadFilter0();
                return;
            }
            final ChannelPipeline pipeline = KQueueDatagramChannel.this.pipeline();
            final ByteBufAllocator allocator = config.getAllocator();
            allocHandle.reset(config);
            this.readReadyBefore();
            Throwable exception = null;
            try {
                ByteBuf data = null;
                try {
                    do {
                        data = allocHandle.allocate(allocator);
                        allocHandle.attemptedBytesRead(data.writableBytes());
                        DatagramSocketAddress remoteAddress;
                        if (data.hasMemoryAddress()) {
                            remoteAddress = KQueueDatagramChannel.this.socket.recvFromAddress(data.memoryAddress(), data.writerIndex(), data.capacity());
                        }
                        else {
                            final ByteBuffer nioData = data.internalNioBuffer(data.writerIndex(), data.writableBytes());
                            remoteAddress = KQueueDatagramChannel.this.socket.recvFrom(nioData, nioData.position(), nioData.limit());
                        }
                        if (remoteAddress == null) {
                            allocHandle.lastBytesRead(-1);
                            data.release();
                            data = null;
                            break;
                        }
                        allocHandle.incMessagesRead(1);
                        allocHandle.lastBytesRead(remoteAddress.receivedAmount());
                        data.writerIndex(data.writerIndex() + allocHandle.lastBytesRead());
                        this.readPending = false;
                        pipeline.fireChannelRead(new DatagramPacket(data, (InetSocketAddress)this.localAddress(), remoteAddress));
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
                this.readReadyFinally(config);
            }
        }
    }
}
