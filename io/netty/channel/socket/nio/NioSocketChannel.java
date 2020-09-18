package io.netty.channel.socket.nio;

import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.concurrent.Executor;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelConfig;
import io.netty.channel.nio.AbstractNioChannel;
import java.nio.ByteBuffer;
import io.netty.channel.ChannelOutboundBuffer;
import java.nio.channels.WritableByteChannel;
import io.netty.channel.FileRegion;
import java.nio.channels.GatheringByteChannel;
import io.netty.channel.RecvByteBufAllocator;
import java.nio.channels.ScatteringByteChannel;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.SocketUtils;
import java.net.SocketAddress;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.util.internal.PlatformDependent;
import java.net.InetSocketAddress;
import java.net.Socket;
import io.netty.channel.socket.ServerSocketChannel;
import java.nio.channels.SelectableChannel;
import io.netty.channel.Channel;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.socket.SocketChannelConfig;
import java.nio.channels.spi.SelectorProvider;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.AbstractNioByteChannel;

public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel {
    private static final InternalLogger logger;
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER;
    private final SocketChannelConfig config;
    
    private static java.nio.channels.SocketChannel newSocket(final SelectorProvider provider) {
        try {
            return provider.openSocketChannel();
        }
        catch (IOException e) {
            throw new ChannelException("Failed to open a socket.", (Throwable)e);
        }
    }
    
    public NioSocketChannel() {
        this(NioSocketChannel.DEFAULT_SELECTOR_PROVIDER);
    }
    
    public NioSocketChannel(final SelectorProvider provider) {
        this(newSocket(provider));
    }
    
    public NioSocketChannel(final java.nio.channels.SocketChannel socket) {
        this(null, socket);
    }
    
    public NioSocketChannel(final Channel parent, final java.nio.channels.SocketChannel socket) {
        super(parent, (SelectableChannel)socket);
        this.config = new NioSocketChannelConfig(this, socket.socket());
    }
    
    @Override
    public ServerSocketChannel parent() {
        return (ServerSocketChannel)super.parent();
    }
    
    @Override
    public SocketChannelConfig config() {
        return this.config;
    }
    
    protected java.nio.channels.SocketChannel javaChannel() {
        return (java.nio.channels.SocketChannel)super.javaChannel();
    }
    
    public boolean isActive() {
        final java.nio.channels.SocketChannel ch = this.javaChannel();
        return ch.isOpen() && ch.isConnected();
    }
    
    public boolean isOutputShutdown() {
        return this.javaChannel().socket().isOutputShutdown() || !this.isActive();
    }
    
    public boolean isInputShutdown() {
        return this.javaChannel().socket().isInputShutdown() || !this.isActive();
    }
    
    public boolean isShutdown() {
        final Socket socket = this.javaChannel().socket();
        return (socket.isInputShutdown() && socket.isOutputShutdown()) || !this.isActive();
    }
    
    @Override
    public InetSocketAddress localAddress() {
        return (InetSocketAddress)super.localAddress();
    }
    
    @Override
    public InetSocketAddress remoteAddress() {
        return (InetSocketAddress)super.remoteAddress();
    }
    
    protected final void doShutdownOutput() throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            this.javaChannel().shutdownOutput();
        }
        else {
            this.javaChannel().socket().shutdownOutput();
        }
    }
    
    public ChannelFuture shutdownOutput() {
        return this.shutdownOutput(this.newPromise());
    }
    
    public ChannelFuture shutdownOutput(final ChannelPromise promise) {
        final EventLoop loop = this.eventLoop();
        if (loop.inEventLoop()) {
            ((AbstractUnsafe)this.unsafe()).shutdownOutput(promise);
        }
        else {
            loop.execute((Runnable)new Runnable() {
                public void run() {
                    ((AbstractUnsafe)NioSocketChannel.this.unsafe()).shutdownOutput(promise);
                }
            });
        }
        return promise;
    }
    
    public ChannelFuture shutdownInput() {
        return this.shutdownInput(this.newPromise());
    }
    
    @Override
    protected boolean isInputShutdown0() {
        return this.isInputShutdown();
    }
    
    public ChannelFuture shutdownInput(final ChannelPromise promise) {
        final EventLoop loop = this.eventLoop();
        if (loop.inEventLoop()) {
            this.shutdownInput0(promise);
        }
        else {
            loop.execute((Runnable)new Runnable() {
                public void run() {
                    NioSocketChannel.this.shutdownInput0(promise);
                }
            });
        }
        return promise;
    }
    
    public ChannelFuture shutdown() {
        return this.shutdown(this.newPromise());
    }
    
    public ChannelFuture shutdown(final ChannelPromise promise) {
        final ChannelFuture shutdownOutputFuture = this.shutdownOutput();
        if (shutdownOutputFuture.isDone()) {
            this.shutdownOutputDone(shutdownOutputFuture, promise);
        }
        else {
            shutdownOutputFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(final ChannelFuture shutdownOutputFuture) throws Exception {
                    NioSocketChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
                }
            });
        }
        return promise;
    }
    
    private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
        final ChannelFuture shutdownInputFuture = this.shutdownInput();
        if (shutdownInputFuture.isDone()) {
            shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
        }
        else {
            shutdownInputFuture.addListener(new ChannelFutureListener() {
                public void operationComplete(final ChannelFuture shutdownInputFuture) throws Exception {
                    shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
                }
            });
        }
    }
    
    private static void shutdownDone(final ChannelFuture shutdownOutputFuture, final ChannelFuture shutdownInputFuture, final ChannelPromise promise) {
        final Throwable shutdownOutputCause = shutdownOutputFuture.cause();
        final Throwable shutdownInputCause = shutdownInputFuture.cause();
        if (shutdownOutputCause != null) {
            if (shutdownInputCause != null) {
                NioSocketChannel.logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
            }
            promise.setFailure(shutdownOutputCause);
        }
        else if (shutdownInputCause != null) {
            promise.setFailure(shutdownInputCause);
        }
        else {
            promise.setSuccess();
        }
    }
    
    private void shutdownInput0(final ChannelPromise promise) {
        try {
            this.shutdownInput0();
            promise.setSuccess();
        }
        catch (Throwable t) {
            promise.setFailure(t);
        }
    }
    
    private void shutdownInput0() throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            this.javaChannel().shutdownInput();
        }
        else {
            this.javaChannel().socket().shutdownInput();
        }
    }
    
    protected SocketAddress localAddress0() {
        return this.javaChannel().socket().getLocalSocketAddress();
    }
    
    protected SocketAddress remoteAddress0() {
        return this.javaChannel().socket().getRemoteSocketAddress();
    }
    
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.doBind0(localAddress);
    }
    
    private void doBind0(final SocketAddress localAddress) throws Exception {
        if (PlatformDependent.javaVersion() >= 7) {
            SocketUtils.bind(this.javaChannel(), localAddress);
        }
        else {
            SocketUtils.bind(this.javaChannel().socket(), localAddress);
        }
    }
    
    @Override
    protected boolean doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) throws Exception {
        if (localAddress != null) {
            this.doBind0(localAddress);
        }
        boolean success = false;
        try {
            final boolean connected = SocketUtils.connect(this.javaChannel(), remoteAddress);
            if (!connected) {
                this.selectionKey().interestOps(8);
            }
            success = true;
            return connected;
        }
        finally {
            if (!success) {
                this.doClose();
            }
        }
    }
    
    @Override
    protected void doFinishConnect() throws Exception {
        if (!this.javaChannel().finishConnect()) {
            throw new Error();
        }
    }
    
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        super.doClose();
        this.javaChannel().close();
    }
    
    @Override
    protected int doReadBytes(final ByteBuf byteBuf) throws Exception {
        final RecvByteBufAllocator.Handle allocHandle = this.unsafe().recvBufAllocHandle();
        allocHandle.attemptedBytesRead(byteBuf.writableBytes());
        return byteBuf.writeBytes((ScatteringByteChannel)this.javaChannel(), allocHandle.attemptedBytesRead());
    }
    
    @Override
    protected int doWriteBytes(final ByteBuf buf) throws Exception {
        final int expectedWrittenBytes = buf.readableBytes();
        return buf.readBytes((GatheringByteChannel)this.javaChannel(), expectedWrittenBytes);
    }
    
    @Override
    protected long doWriteFileRegion(final FileRegion region) throws Exception {
        final long position = region.transferred();
        return region.transferTo((WritableByteChannel)this.javaChannel(), position);
    }
    
    private void adjustMaxBytesPerGatheringWrite(final int attempted, final int written, final int oldMaxBytesPerGatheringWrite) {
        if (attempted == written) {
            if (attempted << 1 > oldMaxBytesPerGatheringWrite) {
                ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted << 1);
            }
        }
        else if (attempted > 4096 && written < attempted >>> 1) {
            ((NioSocketChannelConfig)this.config).setMaxBytesPerGatheringWrite(attempted >>> 1);
        }
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        final java.nio.channels.SocketChannel ch = this.javaChannel();
        int writeSpinCount = this.config().getWriteSpinCount();
        while (!in.isEmpty()) {
            final int maxBytesPerGatheringWrite = ((NioSocketChannelConfig)this.config).getMaxBytesPerGatheringWrite();
            final ByteBuffer[] nioBuffers = in.nioBuffers(1024, maxBytesPerGatheringWrite);
            final int nioBufferCnt = in.nioBufferCount();
            switch (nioBufferCnt) {
                case 0: {
                    writeSpinCount -= this.doWrite0(in);
                    break;
                }
                case 1: {
                    final ByteBuffer buffer = nioBuffers[0];
                    final int attemptedBytes = buffer.remaining();
                    final int localWrittenBytes = ch.write(buffer);
                    if (localWrittenBytes <= 0) {
                        this.incompleteWrite(true);
                        return;
                    }
                    this.adjustMaxBytesPerGatheringWrite(attemptedBytes, localWrittenBytes, maxBytesPerGatheringWrite);
                    in.removeBytes(localWrittenBytes);
                    --writeSpinCount;
                    break;
                }
                default: {
                    final long attemptedBytes2 = in.nioBufferSize();
                    final long localWrittenBytes2 = ch.write(nioBuffers, 0, nioBufferCnt);
                    if (localWrittenBytes2 <= 0L) {
                        this.incompleteWrite(true);
                        return;
                    }
                    this.adjustMaxBytesPerGatheringWrite((int)attemptedBytes2, (int)localWrittenBytes2, maxBytesPerGatheringWrite);
                    in.removeBytes(localWrittenBytes2);
                    --writeSpinCount;
                    break;
                }
            }
            if (writeSpinCount <= 0) {
                this.incompleteWrite(writeSpinCount < 0);
                return;
            }
        }
        this.clearOpWrite();
    }
    
    @Override
    protected AbstractNioUnsafe newUnsafe() {
        return new NioSocketChannelUnsafe();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioSocketChannel.class);
        DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();
    }
    
    private final class NioSocketChannelUnsafe extends NioByteUnsafe {
        @Override
        protected Executor prepareToClose() {
            try {
                if (NioSocketChannel.this.javaChannel().isOpen() && NioSocketChannel.this.config().getSoLinger() > 0) {
                    AbstractNioChannel.this.doDeregister();
                    return (Executor)GlobalEventExecutor.INSTANCE;
                }
            }
            catch (Throwable t) {}
            return null;
        }
    }
    
    private final class NioSocketChannelConfig extends DefaultSocketChannelConfig {
        private volatile int maxBytesPerGatheringWrite;
        
        private NioSocketChannelConfig(final NioSocketChannel channel, final Socket javaSocket) {
            super(channel, javaSocket);
            this.maxBytesPerGatheringWrite = Integer.MAX_VALUE;
            this.calculateMaxBytesPerGatheringWrite();
        }
        
        @Override
        protected void autoReadCleared() {
            AbstractNioChannel.this.clearReadPending();
        }
        
        @Override
        public NioSocketChannelConfig setSendBufferSize(final int sendBufferSize) {
            super.setSendBufferSize(sendBufferSize);
            this.calculateMaxBytesPerGatheringWrite();
            return this;
        }
        
        void setMaxBytesPerGatheringWrite(final int maxBytesPerGatheringWrite) {
            this.maxBytesPerGatheringWrite = maxBytesPerGatheringWrite;
        }
        
        int getMaxBytesPerGatheringWrite() {
            return this.maxBytesPerGatheringWrite;
        }
        
        private void calculateMaxBytesPerGatheringWrite() {
            final int newSendBufferSize = this.getSendBufferSize() << 1;
            if (newSendBufferSize > 0) {
                this.setMaxBytesPerGatheringWrite(this.getSendBufferSize() << 1);
            }
        }
    }
}
