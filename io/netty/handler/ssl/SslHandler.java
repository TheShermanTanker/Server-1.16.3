package io.netty.handler.ssl;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.channel.AbstractCoalescingBufferQueue;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelPromiseNotifier;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import javax.net.ssl.SSLSession;
import java.util.List;
import io.netty.buffer.ByteBufUtil;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ByteBufAllocator;
import javax.net.ssl.SSLEngineResult;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.buffer.ByteBuf;
import java.net.SocketAddress;
import io.netty.util.ReferenceCounted;
import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.ImmediateExecutor;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import io.netty.channel.ChannelHandlerContext;
import java.nio.channels.ClosedChannelException;
import javax.net.ssl.SSLException;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SslHandler extends ByteToMessageDecoder implements ChannelOutboundHandler {
    private static final InternalLogger logger;
    private static final Pattern IGNORABLE_CLASS_IN_STACK;
    private static final Pattern IGNORABLE_ERROR_MESSAGE;
    private static final SSLException SSLENGINE_CLOSED;
    private static final SSLException HANDSHAKE_TIMED_OUT;
    private static final ClosedChannelException CHANNEL_CLOSED;
    private static final int MAX_PLAINTEXT_LENGTH = 16384;
    private volatile ChannelHandlerContext ctx;
    private final SSLEngine engine;
    private final SslEngineType engineType;
    private final Executor delegatedTaskExecutor;
    private final boolean jdkCompatibilityMode;
    private final ByteBuffer[] singleBuffer;
    private final boolean startTls;
    private boolean sentFirstMessage;
    private boolean flushedBeforeHandshake;
    private boolean readDuringHandshake;
    private boolean handshakeStarted;
    private SslHandlerCoalescingBufferQueue pendingUnencryptedWrites;
    private Promise<Channel> handshakePromise;
    private final LazyChannelPromise sslClosePromise;
    private boolean needsFlush;
    private boolean outboundClosed;
    private boolean closeNotify;
    private int packetLength;
    private boolean firedChannelRead;
    private volatile long handshakeTimeoutMillis;
    private volatile long closeNotifyFlushTimeoutMillis;
    private volatile long closeNotifyReadTimeoutMillis;
    volatile int wrapDataSize;
    
    public SslHandler(final SSLEngine engine) {
        this(engine, false);
    }
    
    public SslHandler(final SSLEngine engine, final boolean startTls) {
        this(engine, startTls, (Executor)ImmediateExecutor.INSTANCE);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final Executor delegatedTaskExecutor) {
        this(engine, false, delegatedTaskExecutor);
    }
    
    @Deprecated
    public SslHandler(final SSLEngine engine, final boolean startTls, final Executor delegatedTaskExecutor) {
        this.singleBuffer = new ByteBuffer[1];
        this.handshakePromise = new LazyChannelPromise();
        this.sslClosePromise = new LazyChannelPromise();
        this.handshakeTimeoutMillis = 10000L;
        this.closeNotifyFlushTimeoutMillis = 3000L;
        this.wrapDataSize = 16384;
        if (engine == null) {
            throw new NullPointerException("engine");
        }
        if (delegatedTaskExecutor == null) {
            throw new NullPointerException("delegatedTaskExecutor");
        }
        this.engine = engine;
        this.engineType = SslEngineType.forEngine(engine);
        this.delegatedTaskExecutor = delegatedTaskExecutor;
        this.startTls = startTls;
        this.jdkCompatibilityMode = this.engineType.jdkCompatibilityMode(engine);
        this.setCumulator(this.engineType.cumulator);
    }
    
    public long getHandshakeTimeoutMillis() {
        return this.handshakeTimeoutMillis;
    }
    
    public void setHandshakeTimeout(final long handshakeTimeout, final TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.setHandshakeTimeoutMillis(unit.toMillis(handshakeTimeout));
    }
    
    public void setHandshakeTimeoutMillis(final long handshakeTimeoutMillis) {
        if (handshakeTimeoutMillis < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("handshakeTimeoutMillis: ").append(handshakeTimeoutMillis).append(" (expected: >= 0)").toString());
        }
        this.handshakeTimeoutMillis = handshakeTimeoutMillis;
    }
    
    public final void setWrapDataSize(final int wrapDataSize) {
        this.wrapDataSize = wrapDataSize;
    }
    
    @Deprecated
    public long getCloseNotifyTimeoutMillis() {
        return this.getCloseNotifyFlushTimeoutMillis();
    }
    
    @Deprecated
    public void setCloseNotifyTimeout(final long closeNotifyTimeout, final TimeUnit unit) {
        this.setCloseNotifyFlushTimeout(closeNotifyTimeout, unit);
    }
    
    @Deprecated
    public void setCloseNotifyTimeoutMillis(final long closeNotifyFlushTimeoutMillis) {
        this.setCloseNotifyFlushTimeoutMillis(closeNotifyFlushTimeoutMillis);
    }
    
    public final long getCloseNotifyFlushTimeoutMillis() {
        return this.closeNotifyFlushTimeoutMillis;
    }
    
    public final void setCloseNotifyFlushTimeout(final long closeNotifyFlushTimeout, final TimeUnit unit) {
        this.setCloseNotifyFlushTimeoutMillis(unit.toMillis(closeNotifyFlushTimeout));
    }
    
    public final void setCloseNotifyFlushTimeoutMillis(final long closeNotifyFlushTimeoutMillis) {
        if (closeNotifyFlushTimeoutMillis < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("closeNotifyFlushTimeoutMillis: ").append(closeNotifyFlushTimeoutMillis).append(" (expected: >= 0)").toString());
        }
        this.closeNotifyFlushTimeoutMillis = closeNotifyFlushTimeoutMillis;
    }
    
    public final long getCloseNotifyReadTimeoutMillis() {
        return this.closeNotifyReadTimeoutMillis;
    }
    
    public final void setCloseNotifyReadTimeout(final long closeNotifyReadTimeout, final TimeUnit unit) {
        this.setCloseNotifyReadTimeoutMillis(unit.toMillis(closeNotifyReadTimeout));
    }
    
    public final void setCloseNotifyReadTimeoutMillis(final long closeNotifyReadTimeoutMillis) {
        if (closeNotifyReadTimeoutMillis < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("closeNotifyReadTimeoutMillis: ").append(closeNotifyReadTimeoutMillis).append(" (expected: >= 0)").toString());
        }
        this.closeNotifyReadTimeoutMillis = closeNotifyReadTimeoutMillis;
    }
    
    public SSLEngine engine() {
        return this.engine;
    }
    
    public String applicationProtocol() {
        final SSLEngine engine = this.engine();
        if (!(engine instanceof ApplicationProtocolAccessor)) {
            return null;
        }
        return ((ApplicationProtocolAccessor)engine).getNegotiatedApplicationProtocol();
    }
    
    public Future<Channel> handshakeFuture() {
        return this.handshakePromise;
    }
    
    @Deprecated
    public ChannelFuture close() {
        return this.close(this.ctx.newPromise());
    }
    
    @Deprecated
    public ChannelFuture close(final ChannelPromise promise) {
        final ChannelHandlerContext ctx = this.ctx;
        ctx.executor().execute((Runnable)new Runnable() {
            public void run() {
                SslHandler.this.outboundClosed = true;
                SslHandler.this.engine.closeOutbound();
                try {
                    SslHandler.this.flush(ctx, promise);
                }
                catch (Exception e) {
                    if (!promise.tryFailure((Throwable)e)) {
                        SslHandler.logger.warn("{} flush() raised a masked exception.", ctx.channel(), e);
                    }
                }
            }
        });
        return promise;
    }
    
    public Future<Channel> sslCloseFuture() {
        return this.sslClosePromise;
    }
    
    public void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
        if (!this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.releaseAndFailAll(ctx, (Throwable)new ChannelException("Pending write on removal of SslHandler"));
        }
        this.pendingUnencryptedWrites = null;
        if (this.engine instanceof ReferenceCounted) {
            ((ReferenceCounted)this.engine).release();
        }
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.bind(localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        ctx.deregister(promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, true);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.closeOutboundAndChannel(ctx, promise, false);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        if (!this.handshakePromise.isDone()) {
            this.readDuringHandshake = true;
        }
        ctx.read();
    }
    
    private static IllegalStateException newPendingWritesNullException() {
        return new IllegalStateException("pendingUnencryptedWrites is null, handlerRemoved0 called?");
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            final UnsupportedMessageTypeException exception = new UnsupportedMessageTypeException(msg, new Class[] { ByteBuf.class });
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable)exception);
        }
        else if (this.pendingUnencryptedWrites == null) {
            ReferenceCountUtil.safeRelease(msg);
            promise.setFailure((Throwable)newPendingWritesNullException());
        }
        else {
            this.pendingUnencryptedWrites.add((ByteBuf)msg, promise);
        }
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        if (this.startTls && !this.sentFirstMessage) {
            this.sentFirstMessage = true;
            this.pendingUnencryptedWrites.writeAndRemoveAll(ctx);
            this.forceFlush(ctx);
            return;
        }
        try {
            this.wrapAndFlush(ctx);
        }
        catch (Throwable cause) {
            this.setHandshakeFailure(ctx, cause);
            PlatformDependent.throwException(cause);
        }
    }
    
    private void wrapAndFlush(final ChannelHandlerContext ctx) throws SSLException {
        if (this.pendingUnencryptedWrites.isEmpty()) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, ctx.newPromise());
        }
        if (!this.handshakePromise.isDone()) {
            this.flushedBeforeHandshake = true;
        }
        try {
            this.wrap(ctx, false);
        }
        finally {
            this.forceFlush(ctx);
        }
    }
    
    private void wrap(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        ChannelPromise promise = null;
        final ByteBufAllocator alloc = ctx.alloc();
        boolean needUnwrap = false;
        ByteBuf buf = null;
        try {
            final int wrapDataSize = this.wrapDataSize;
            while (!ctx.isRemoved()) {
                promise = ctx.newPromise();
                buf = ((wrapDataSize > 0) ? this.pendingUnencryptedWrites.remove(alloc, wrapDataSize, promise) : this.pendingUnencryptedWrites.removeFirst(promise));
                if (buf == null) {
                    break;
                }
                if (out == null) {
                    out = this.allocateOutNetBuf(ctx, buf.readableBytes(), buf.nioBufferCount());
                }
                final SSLEngineResult result = this.wrap(alloc, this.engine, buf, out);
                if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
                    buf.release();
                    buf = null;
                    promise.tryFailure((Throwable)SslHandler.SSLENGINE_CLOSED);
                    promise = null;
                    this.pendingUnencryptedWrites.releaseAndFailAll(ctx, (Throwable)SslHandler.SSLENGINE_CLOSED);
                    return;
                }
                if (buf.isReadable()) {
                    this.pendingUnencryptedWrites.addFirst(buf, promise);
                    promise = null;
                }
                else {
                    buf.release();
                }
                buf = null;
                switch (result.getHandshakeStatus()) {
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        continue;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                    }
                    case NEED_WRAP: {
                        this.finishWrap(ctx, out, promise, inUnwrap, false);
                        promise = null;
                        out = null;
                        continue;
                    }
                    case NEED_UNWRAP: {
                        needUnwrap = true;
                    }
                    default: {
                        throw new IllegalStateException(new StringBuilder().append("Unknown handshake status: ").append(result.getHandshakeStatus()).toString());
                    }
                }
            }
        }
        finally {
            if (buf != null) {
                buf.release();
            }
            this.finishWrap(ctx, out, promise, inUnwrap, needUnwrap);
        }
    }
    
    private void finishWrap(final ChannelHandlerContext ctx, ByteBuf out, final ChannelPromise promise, final boolean inUnwrap, final boolean needUnwrap) {
        if (out == null) {
            out = Unpooled.EMPTY_BUFFER;
        }
        else if (!out.isReadable()) {
            out.release();
            out = Unpooled.EMPTY_BUFFER;
        }
        if (promise != null) {
            ctx.write(out, promise);
        }
        else {
            ctx.write(out);
        }
        if (inUnwrap) {
            this.needsFlush = true;
        }
        if (needUnwrap) {
            this.readIfNeeded(ctx);
        }
    }
    
    private boolean wrapNonAppData(final ChannelHandlerContext ctx, final boolean inUnwrap) throws SSLException {
        ByteBuf out = null;
        final ByteBufAllocator alloc = ctx.alloc();
        try {
            while (!ctx.isRemoved()) {
                if (out == null) {
                    out = this.allocateOutNetBuf(ctx, 2048, 1);
                }
                final SSLEngineResult result = this.wrap(alloc, this.engine, Unpooled.EMPTY_BUFFER, out);
                if (result.bytesProduced() > 0) {
                    ctx.write(out);
                    if (inUnwrap) {
                        this.needsFlush = true;
                    }
                    out = null;
                }
                switch (result.getHandshakeStatus()) {
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        return false;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case NEED_UNWRAP: {
                        if (inUnwrap) {
                            return false;
                        }
                        this.unwrapNonAppData(ctx);
                        break;
                    }
                    case NEED_WRAP: {
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        this.setHandshakeSuccessIfStillHandshaking();
                        if (!inUnwrap) {
                            this.unwrapNonAppData(ctx);
                        }
                        return true;
                    }
                    default: {
                        throw new IllegalStateException(new StringBuilder().append("Unknown handshake status: ").append(result.getHandshakeStatus()).toString());
                    }
                }
                if (result.bytesProduced() == 0) {
                    break;
                }
                if (result.bytesConsumed() == 0 && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                    break;
                }
            }
        }
        finally {
            if (out != null) {
                out.release();
            }
        }
        return false;
    }
    
    private SSLEngineResult wrap(final ByteBufAllocator alloc, final SSLEngine engine, final ByteBuf in, final ByteBuf out) throws SSLException {
        ByteBuf newDirectIn = null;
        try {
            final int readerIndex = in.readerIndex();
            final int readableBytes = in.readableBytes();
            ByteBuffer[] in2;
            if (in.isDirect() || !this.engineType.wantsDirectBuffer) {
                if (!(in instanceof CompositeByteBuf) && in.nioBufferCount() == 1) {
                    in2 = this.singleBuffer;
                    in2[0] = in.internalNioBuffer(readerIndex, readableBytes);
                }
                else {
                    in2 = in.nioBuffers();
                }
            }
            else {
                newDirectIn = alloc.directBuffer(readableBytes);
                newDirectIn.writeBytes(in, readerIndex, readableBytes);
                in2 = this.singleBuffer;
                in2[0] = newDirectIn.internalNioBuffer(newDirectIn.readerIndex(), readableBytes);
            }
            while (true) {
                final ByteBuffer out2 = out.nioBuffer(out.writerIndex(), out.writableBytes());
                final SSLEngineResult result = engine.wrap(in2, out2);
                in.skipBytes(result.bytesConsumed());
                out.writerIndex(out.writerIndex() + result.bytesProduced());
                switch (result.getStatus()) {
                    case BUFFER_OVERFLOW: {
                        out.ensureWritable(engine.getSession().getPacketBufferSize());
                        continue;
                    }
                    default: {
                        return result;
                    }
                }
            }
        }
        finally {
            this.singleBuffer[0] = null;
            if (newDirectIn != null) {
                newDirectIn.release();
            }
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.setHandshakeFailure(ctx, (Throwable)SslHandler.CHANNEL_CLOSED, !this.outboundClosed, this.handshakeStarted, false);
        this.notifyClosePromise((Throwable)SslHandler.CHANNEL_CLOSED);
        super.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (this.ignoreException(cause)) {
            if (SslHandler.logger.isDebugEnabled()) {
                SslHandler.logger.debug("{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
            }
            if (ctx.channel().isActive()) {
                ctx.close();
            }
        }
        else {
            ctx.fireExceptionCaught(cause);
        }
    }
    
    private boolean ignoreException(final Throwable t) {
        if (!(t instanceof SSLException) && t instanceof IOException && this.sslClosePromise.isDone()) {
            final String message = t.getMessage();
            if (message != null && SslHandler.IGNORABLE_ERROR_MESSAGE.matcher((CharSequence)message).matches()) {
                return true;
            }
            final StackTraceElement[] stackTrace;
            final StackTraceElement[] elements = stackTrace = t.getStackTrace();
            for (final StackTraceElement element : stackTrace) {
                final String classname = element.getClassName();
                final String methodname = element.getMethodName();
                if (!classname.startsWith("io.netty.")) {
                    if ("read".equals(methodname)) {
                        if (SslHandler.IGNORABLE_CLASS_IN_STACK.matcher((CharSequence)classname).matches()) {
                            return true;
                        }
                        try {
                            final Class<?> clazz = PlatformDependent.getClassLoader(this.getClass()).loadClass(classname);
                            if (SocketChannel.class.isAssignableFrom((Class)clazz) || DatagramChannel.class.isAssignableFrom((Class)clazz)) {
                                return true;
                            }
                            if (PlatformDependent.javaVersion() >= 7 && "com.sun.nio.sctp.SctpChannel".equals(clazz.getSuperclass().getName())) {
                                return true;
                            }
                        }
                        catch (Throwable cause) {
                            SslHandler.logger.debug("Unexpected exception while loading class {} classname {}", this.getClass(), classname, cause);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isEncrypted(final ByteBuf buffer) {
        if (buffer.readableBytes() < 5) {
            throw new IllegalArgumentException("buffer must have at least 5 readable bytes");
        }
        return SslUtils.getEncryptedPacketLength(buffer, buffer.readerIndex()) != -2;
    }
    
    private void decodeJdkCompatible(final ChannelHandlerContext ctx, final ByteBuf in) throws NotSslRecordException {
        int packetLength = this.packetLength;
        if (packetLength > 0) {
            if (in.readableBytes() < packetLength) {
                return;
            }
        }
        else {
            final int readableBytes = in.readableBytes();
            if (readableBytes < 5) {
                return;
            }
            packetLength = SslUtils.getEncryptedPacketLength(in, in.readerIndex());
            if (packetLength == -2) {
                final NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
                in.skipBytes(in.readableBytes());
                this.setHandshakeFailure(ctx, (Throwable)e);
                throw e;
            }
            assert packetLength > 0;
            if (packetLength > readableBytes) {
                this.packetLength = packetLength;
                return;
            }
        }
        this.packetLength = 0;
        try {
            final int bytesConsumed = this.unwrap(ctx, in, in.readerIndex(), packetLength);
            assert !(!this.engine.isInboundDone()) : new StringBuilder().append("we feed the SSLEngine a packets worth of data: ").append(packetLength).append(" but it only consumed: ").append(bytesConsumed).toString();
            in.skipBytes(bytesConsumed);
        }
        catch (Throwable cause) {
            this.handleUnwrapThrowable(ctx, cause);
        }
    }
    
    private void decodeNonJdkCompatible(final ChannelHandlerContext ctx, final ByteBuf in) {
        try {
            in.skipBytes(this.unwrap(ctx, in, in.readerIndex(), in.readableBytes()));
        }
        catch (Throwable cause) {
            this.handleUnwrapThrowable(ctx, cause);
        }
    }
    
    private void handleUnwrapThrowable(final ChannelHandlerContext ctx, final Throwable cause) {
        try {
            if (this.handshakePromise.tryFailure(cause)) {
                ctx.fireUserEventTriggered(new SslHandshakeCompletionEvent(cause));
            }
            this.wrapAndFlush(ctx);
        }
        catch (SSLException ex) {
            SslHandler.logger.debug("SSLException during trying to call SSLEngine.wrap(...) because of an previous SSLException, ignoring...", (Throwable)ex);
        }
        finally {
            this.setHandshakeFailure(ctx, cause, true, false, true);
        }
        PlatformDependent.throwException(cause);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws SSLException {
        if (this.jdkCompatibilityMode) {
            this.decodeJdkCompatible(ctx, in);
        }
        else {
            this.decodeNonJdkCompatible(ctx, in);
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.discardSomeReadBytes();
        this.flushIfNeeded(ctx);
        this.readIfNeeded(ctx);
        this.firedChannelRead = false;
        ctx.fireChannelReadComplete();
    }
    
    private void readIfNeeded(final ChannelHandlerContext ctx) {
        if (!ctx.channel().config().isAutoRead() && (!this.firedChannelRead || !this.handshakePromise.isDone())) {
            ctx.read();
        }
    }
    
    private void flushIfNeeded(final ChannelHandlerContext ctx) {
        if (this.needsFlush) {
            this.forceFlush(ctx);
        }
    }
    
    private void unwrapNonAppData(final ChannelHandlerContext ctx) throws SSLException {
        this.unwrap(ctx, Unpooled.EMPTY_BUFFER, 0, 0);
    }
    
    private int unwrap(final ChannelHandlerContext ctx, final ByteBuf packet, int offset, int length) throws SSLException {
        final int originalLength = length;
        boolean wrapLater = false;
        boolean notifyClosure = false;
        int overflowReadableBytes = -1;
        ByteBuf decodeOut = this.allocate(ctx, length);
        try {
        Label_0495:
            while (!ctx.isRemoved()) {
                final SSLEngineResult result = this.engineType.unwrap(this, packet, offset, length, decodeOut);
                final SSLEngineResult.Status status = result.getStatus();
                final SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
                final int produced = result.bytesProduced();
                final int consumed = result.bytesConsumed();
                offset += consumed;
                length -= consumed;
                switch (status) {
                    case BUFFER_OVERFLOW: {
                        final int readableBytes = decodeOut.readableBytes();
                        final int previousOverflowReadableBytes = overflowReadableBytes;
                        overflowReadableBytes = readableBytes;
                        int bufferSize = this.engine.getSession().getApplicationBufferSize() - readableBytes;
                        if (readableBytes > 0) {
                            this.firedChannelRead = true;
                            ctx.fireChannelRead(decodeOut);
                            decodeOut = null;
                            if (bufferSize <= 0) {
                                bufferSize = this.engine.getSession().getApplicationBufferSize();
                            }
                        }
                        else {
                            decodeOut.release();
                            decodeOut = null;
                        }
                        if (readableBytes == 0 && previousOverflowReadableBytes == 0) {
                            throw new IllegalStateException("Two consecutive overflows but no content was consumed. " + SSLSession.class.getSimpleName() + " getApplicationBufferSize: " + this.engine.getSession().getApplicationBufferSize() + " maybe too small.");
                        }
                        decodeOut = this.allocate(ctx, this.engineType.calculatePendingData(this, bufferSize));
                        continue;
                    }
                    case CLOSED: {
                        notifyClosure = true;
                        overflowReadableBytes = -1;
                        break;
                    }
                    default: {
                        overflowReadableBytes = -1;
                        break;
                    }
                }
                switch (handshakeStatus) {
                    case NEED_UNWRAP: {
                        break;
                    }
                    case NEED_WRAP: {
                        if (this.wrapNonAppData(ctx, true) && length == 0) {
                            break Label_0495;
                        }
                        break;
                    }
                    case NEED_TASK: {
                        this.runDelegatedTasks();
                        break;
                    }
                    case FINISHED: {
                        this.setHandshakeSuccess();
                        wrapLater = true;
                        break;
                    }
                    case NOT_HANDSHAKING: {
                        if (this.setHandshakeSuccessIfStillHandshaking()) {
                            wrapLater = true;
                            continue;
                        }
                        if (this.flushedBeforeHandshake) {
                            this.flushedBeforeHandshake = false;
                            wrapLater = true;
                        }
                        if (length == 0) {
                            break Label_0495;
                        }
                        break;
                    }
                    default: {
                        throw new IllegalStateException(new StringBuilder().append("unknown handshake status: ").append(handshakeStatus).toString());
                    }
                }
                if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW || (consumed == 0 && produced == 0)) {
                    if (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                        this.readIfNeeded(ctx);
                        break;
                    }
                    break;
                }
            }
            if (wrapLater) {
                this.wrap(ctx, true);
            }
            if (notifyClosure) {
                this.notifyClosePromise(null);
            }
        }
        finally {
            if (decodeOut != null) {
                if (decodeOut.isReadable()) {
                    this.firedChannelRead = true;
                    ctx.fireChannelRead(decodeOut);
                }
                else {
                    decodeOut.release();
                }
            }
        }
        return originalLength - length;
    }
    
    private static ByteBuffer toByteBuffer(final ByteBuf out, final int index, final int len) {
        return (out.nioBufferCount() == 1) ? out.internalNioBuffer(index, len) : out.nioBuffer(index, len);
    }
    
    private void runDelegatedTasks() {
        if (this.delegatedTaskExecutor == ImmediateExecutor.INSTANCE) {
            while (true) {
                final Runnable task = this.engine.getDelegatedTask();
                if (task == null) {
                    break;
                }
                task.run();
            }
        }
        else {
            final List<Runnable> tasks = (List<Runnable>)new ArrayList(2);
            while (true) {
                final Runnable task2 = this.engine.getDelegatedTask();
                if (task2 == null) {
                    break;
                }
                tasks.add(task2);
            }
            if (tasks.isEmpty()) {
                return;
            }
            final CountDownLatch latch = new CountDownLatch(1);
            this.delegatedTaskExecutor.execute((Runnable)new Runnable() {
                public void run() {
                    try {
                        for (final Runnable task : tasks) {
                            task.run();
                        }
                    }
                    catch (Exception e) {
                        SslHandler.this.ctx.fireExceptionCaught((Throwable)e);
                    }
                    finally {
                        latch.countDown();
                    }
                }
            });
            boolean interrupted = false;
            while (latch.getCount() != 0L) {
                try {
                    latch.await();
                }
                catch (InterruptedException e) {
                    interrupted = true;
                }
            }
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private boolean setHandshakeSuccessIfStillHandshaking() {
        if (!this.handshakePromise.isDone()) {
            this.setHandshakeSuccess();
            return true;
        }
        return false;
    }
    
    private void setHandshakeSuccess() {
        this.handshakePromise.trySuccess(this.ctx.channel());
        if (SslHandler.logger.isDebugEnabled()) {
            SslHandler.logger.debug("{} HANDSHAKEN: {}", this.ctx.channel(), this.engine.getSession().getCipherSuite());
        }
        this.ctx.fireUserEventTriggered(SslHandshakeCompletionEvent.SUCCESS);
        if (this.readDuringHandshake && !this.ctx.channel().config().isAutoRead()) {
            this.readDuringHandshake = false;
            this.ctx.read();
        }
    }
    
    private void setHandshakeFailure(final ChannelHandlerContext ctx, final Throwable cause) {
        this.setHandshakeFailure(ctx, cause, true, true, false);
    }
    
    private void setHandshakeFailure(final ChannelHandlerContext ctx, final Throwable cause, final boolean closeInbound, final boolean notify, final boolean alwaysFlushAndClose) {
        try {
            this.outboundClosed = true;
            this.engine.closeOutbound();
            if (closeInbound) {
                try {
                    this.engine.closeInbound();
                }
                catch (SSLException e) {
                    if (SslHandler.logger.isDebugEnabled()) {
                        final String msg = e.getMessage();
                        if (msg == null || !msg.contains("possible truncation attack")) {
                            SslHandler.logger.debug("{} SSLEngine.closeInbound() raised an exception.", ctx.channel(), e);
                        }
                    }
                }
            }
            if (this.handshakePromise.tryFailure(cause) || alwaysFlushAndClose) {
                SslUtils.handleHandshakeFailure(ctx, cause, notify);
            }
        }
        finally {
            this.releaseAndFailAll(cause);
        }
    }
    
    private void releaseAndFailAll(final Throwable cause) {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.releaseAndFailAll(this.ctx, cause);
        }
    }
    
    private void notifyClosePromise(final Throwable cause) {
        if (cause == null) {
            if (this.sslClosePromise.trySuccess(this.ctx.channel())) {
                this.ctx.fireUserEventTriggered(SslCloseCompletionEvent.SUCCESS);
            }
        }
        else if (this.sslClosePromise.tryFailure(cause)) {
            this.ctx.fireUserEventTriggered(new SslCloseCompletionEvent(cause));
        }
    }
    
    private void closeOutboundAndChannel(final ChannelHandlerContext ctx, final ChannelPromise promise, final boolean disconnect) throws Exception {
        this.outboundClosed = true;
        this.engine.closeOutbound();
        if (!ctx.channel().isActive()) {
            if (disconnect) {
                ctx.disconnect(promise);
            }
            else {
                ctx.close(promise);
            }
            return;
        }
        final ChannelPromise closeNotifyPromise = ctx.newPromise();
        try {
            this.flush(ctx, closeNotifyPromise);
        }
        finally {
            if (!this.closeNotify) {
                this.closeNotify = true;
                this.safeClose(ctx, closeNotifyPromise, ctx.newPromise().addListener(new ChannelPromiseNotifier(false, new ChannelPromise[] { promise })));
            }
            else {
                this.sslClosePromise.addListener(new FutureListener<Channel>() {
                    public void operationComplete(final Future<Channel> future) {
                        promise.setSuccess();
                    }
                });
            }
        }
    }
    
    private void flush(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        if (this.pendingUnencryptedWrites != null) {
            this.pendingUnencryptedWrites.add(Unpooled.EMPTY_BUFFER, promise);
        }
        else {
            promise.setFailure((Throwable)newPendingWritesNullException());
        }
        this.flush(ctx);
    }
    
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        this.pendingUnencryptedWrites = new SslHandlerCoalescingBufferQueue(ctx.channel(), 16);
        if (ctx.channel().isActive()) {
            this.startHandshakeProcessing();
        }
    }
    
    private void startHandshakeProcessing() {
        this.handshakeStarted = true;
        if (this.engine.getUseClientMode()) {
            this.handshake(null);
        }
        else {
            this.applyHandshakeTimeout(null);
        }
    }
    
    public Future<Channel> renegotiate() {
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        return this.renegotiate(ctx.executor().<Channel>newPromise());
    }
    
    public Future<Channel> renegotiate(final Promise<Channel> promise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        final ChannelHandlerContext ctx = this.ctx;
        if (ctx == null) {
            throw new IllegalStateException();
        }
        final EventExecutor executor = ctx.executor();
        if (!executor.inEventLoop()) {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    SslHandler.this.handshake(promise);
                }
            });
            return promise;
        }
        this.handshake(promise);
        return promise;
    }
    
    private void handshake(final Promise<Channel> newHandshakePromise) {
        Promise<Channel> p;
        if (newHandshakePromise != null) {
            final Promise<Channel> oldHandshakePromise = this.handshakePromise;
            if (!oldHandshakePromise.isDone()) {
                oldHandshakePromise.addListener(new FutureListener<Channel>() {
                    public void operationComplete(final Future<Channel> future) throws Exception {
                        if (future.isSuccess()) {
                            newHandshakePromise.setSuccess(future.getNow());
                        }
                        else {
                            newHandshakePromise.setFailure(future.cause());
                        }
                    }
                });
                return;
            }
            p = newHandshakePromise;
            this.handshakePromise = newHandshakePromise;
        }
        else {
            if (this.engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                return;
            }
            p = this.handshakePromise;
            assert !p.isDone();
        }
        final ChannelHandlerContext ctx = this.ctx;
        try {
            this.engine.beginHandshake();
            this.wrapNonAppData(ctx, false);
        }
        catch (Throwable e) {
            this.setHandshakeFailure(ctx, e);
        }
        finally {
            this.forceFlush(ctx);
        }
        this.applyHandshakeTimeout(p);
    }
    
    private void applyHandshakeTimeout(final Promise<Channel> p) {
        final Promise<Channel> promise = (p == null) ? this.handshakePromise : p;
        final long handshakeTimeoutMillis = this.handshakeTimeoutMillis;
        if (handshakeTimeoutMillis <= 0L || promise.isDone()) {
            return;
        }
        final ScheduledFuture<?> timeoutFuture = this.ctx.executor().schedule((Runnable)new Runnable() {
            public void run() {
                if (promise.isDone()) {
                    return;
                }
                try {
                    if (SslHandler.this.handshakePromise.tryFailure((Throwable)SslHandler.HANDSHAKE_TIMED_OUT)) {
                        SslUtils.handleHandshakeFailure(SslHandler.this.ctx, (Throwable)SslHandler.HANDSHAKE_TIMED_OUT, true);
                    }
                }
                finally {
                    SslHandler.this.releaseAndFailAll((Throwable)SslHandler.HANDSHAKE_TIMED_OUT);
                }
            }
        }, handshakeTimeoutMillis, TimeUnit.MILLISECONDS);
        promise.addListener(new FutureListener<Channel>() {
            public void operationComplete(final Future<Channel> f) throws Exception {
                timeoutFuture.cancel(false);
            }
        });
    }
    
    private void forceFlush(final ChannelHandlerContext ctx) {
        this.needsFlush = false;
        ctx.flush();
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        if (!this.startTls) {
            this.startHandshakeProcessing();
        }
        ctx.fireChannelActive();
    }
    
    private void safeClose(final ChannelHandlerContext ctx, final ChannelFuture flushFuture, final ChannelPromise promise) {
        if (!ctx.channel().isActive()) {
            ctx.close(promise);
            return;
        }
        ScheduledFuture<?> timeoutFuture;
        if (!flushFuture.isDone()) {
            final long closeNotifyTimeout = this.closeNotifyFlushTimeoutMillis;
            if (closeNotifyTimeout > 0L) {
                timeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                    public void run() {
                        if (!flushFuture.isDone()) {
                            SslHandler.logger.warn("{} Last write attempt timed out; force-closing the connection.", ctx.channel());
                            addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    }
                }, closeNotifyTimeout, TimeUnit.MILLISECONDS);
            }
            else {
                timeoutFuture = null;
            }
        }
        else {
            timeoutFuture = null;
        }
        flushFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(final ChannelFuture f) throws Exception {
                if (timeoutFuture != null) {
                    timeoutFuture.cancel(false);
                }
                final long closeNotifyReadTimeout = SslHandler.this.closeNotifyReadTimeoutMillis;
                if (closeNotifyReadTimeout <= 0L) {
                    addCloseListener(ctx.close(ctx.newPromise()), promise);
                }
                else {
                    ScheduledFuture<?> closeNotifyReadTimeoutFuture;
                    if (!SslHandler.this.sslClosePromise.isDone()) {
                        closeNotifyReadTimeoutFuture = ctx.executor().schedule((Runnable)new Runnable() {
                            public void run() {
                                if (!SslHandler.this.sslClosePromise.isDone()) {
                                    SslHandler.logger.debug("{} did not receive close_notify in {}ms; force-closing the connection.", ctx.channel(), closeNotifyReadTimeout);
                                    addCloseListener(ctx.close(ctx.newPromise()), promise);
                                }
                            }
                        }, closeNotifyReadTimeout, TimeUnit.MILLISECONDS);
                    }
                    else {
                        closeNotifyReadTimeoutFuture = null;
                    }
                    SslHandler.this.sslClosePromise.addListener(new FutureListener<Channel>() {
                        public void operationComplete(final Future<Channel> future) throws Exception {
                            if (closeNotifyReadTimeoutFuture != null) {
                                closeNotifyReadTimeoutFuture.cancel(false);
                            }
                            addCloseListener(ctx.close(ctx.newPromise()), promise);
                        }
                    });
                }
            }
        });
    }
    
    private static void addCloseListener(final ChannelFuture future, final ChannelPromise promise) {
        future.addListener(new ChannelPromiseNotifier(false, new ChannelPromise[] { promise }));
    }
    
    private ByteBuf allocate(final ChannelHandlerContext ctx, final int capacity) {
        final ByteBufAllocator alloc = ctx.alloc();
        if (this.engineType.wantsDirectBuffer) {
            return alloc.directBuffer(capacity);
        }
        return alloc.buffer(capacity);
    }
    
    private ByteBuf allocateOutNetBuf(final ChannelHandlerContext ctx, final int pendingBytes, final int numComponents) {
        return this.allocate(ctx, this.engineType.calculateWrapBufferCapacity(this, pendingBytes, numComponents));
    }
    
    private static boolean attemptCopyToCumulation(final ByteBuf cumulation, final ByteBuf next, final int wrapDataSize) {
        final int inReadableBytes = next.readableBytes();
        final int cumulationCapacity = cumulation.capacity();
        if (wrapDataSize - cumulation.readableBytes() >= inReadableBytes && ((cumulation.isWritable(inReadableBytes) && cumulationCapacity >= wrapDataSize) || (cumulationCapacity < wrapDataSize && ByteBufUtil.ensureWritableSuccess(cumulation.ensureWritable(inReadableBytes, false))))) {
            cumulation.writeBytes(next);
            next.release();
            return true;
        }
        return false;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SslHandler.class);
        IGNORABLE_CLASS_IN_STACK = Pattern.compile("^.*(?:Socket|Datagram|Sctp|Udt)Channel.*$");
        IGNORABLE_ERROR_MESSAGE = Pattern.compile("^.*(?:connection.*(?:reset|closed|abort|broken)|broken.*pipe).*$", 2);
        SSLENGINE_CLOSED = ThrowableUtil.<SSLException>unknownStackTrace(new SSLException("SSLEngine closed already"), SslHandler.class, "wrap(...)");
        HANDSHAKE_TIMED_OUT = ThrowableUtil.<SSLException>unknownStackTrace(new SSLException("handshake timed out"), SslHandler.class, "handshake(...)");
        CHANNEL_CLOSED = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), SslHandler.class, "channelInactive(...)");
    }
    
    private enum SslEngineType {
        TCNATIVE(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) {
            @Override
            SSLEngineResult unwrap(final SslHandler handler, final ByteBuf in, final int readerIndex, final int len, final ByteBuf out) throws SSLException {
                final int nioBufferCount = in.nioBufferCount();
                final int writerIndex = out.writerIndex();
                SSLEngineResult result;
                if (nioBufferCount > 1) {
                    final ReferenceCountedOpenSslEngine opensslEngine = (ReferenceCountedOpenSslEngine)handler.engine;
                    try {
                        handler.singleBuffer[0] = toByteBuffer(out, writerIndex, out.writableBytes());
                        result = opensslEngine.unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
                    }
                    finally {
                        handler.singleBuffer[0] = null;
                    }
                }
                else {
                    result = handler.engine.unwrap(toByteBuffer(in, readerIndex, len), toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }
            
            @Override
            int getPacketBufferSize(final SslHandler handler) {
                return ((ReferenceCountedOpenSslEngine)handler.engine).maxEncryptedPacketLength0();
            }
            
            @Override
            int calculateWrapBufferCapacity(final SslHandler handler, final int pendingBytes, final int numComponents) {
                return ((ReferenceCountedOpenSslEngine)handler.engine).calculateMaxLengthForWrap(pendingBytes, numComponents);
            }
            
            @Override
            int calculatePendingData(final SslHandler handler, final int guess) {
                final int sslPending = ((ReferenceCountedOpenSslEngine)handler.engine).sslPending();
                return (sslPending > 0) ? sslPending : guess;
            }
            
            @Override
            boolean jdkCompatibilityMode(final SSLEngine engine) {
                return ((ReferenceCountedOpenSslEngine)engine).jdkCompatibilityMode;
            }
        }, 
        CONSCRYPT(true, ByteToMessageDecoder.COMPOSITE_CUMULATOR) {
            @Override
            SSLEngineResult unwrap(final SslHandler handler, final ByteBuf in, final int readerIndex, final int len, final ByteBuf out) throws SSLException {
                final int nioBufferCount = in.nioBufferCount();
                final int writerIndex = out.writerIndex();
                SSLEngineResult result;
                if (nioBufferCount > 1) {
                    try {
                        handler.singleBuffer[0] = toByteBuffer(out, writerIndex, out.writableBytes());
                        result = ((ConscryptAlpnSslEngine)handler.engine).unwrap(in.nioBuffers(readerIndex, len), handler.singleBuffer);
                    }
                    finally {
                        handler.singleBuffer[0] = null;
                    }
                }
                else {
                    result = handler.engine.unwrap(toByteBuffer(in, readerIndex, len), toByteBuffer(out, writerIndex, out.writableBytes()));
                }
                out.writerIndex(writerIndex + result.bytesProduced());
                return result;
            }
            
            @Override
            int calculateWrapBufferCapacity(final SslHandler handler, final int pendingBytes, final int numComponents) {
                return ((ConscryptAlpnSslEngine)handler.engine).calculateOutNetBufSize(pendingBytes, numComponents);
            }
            
            @Override
            int calculatePendingData(final SslHandler handler, final int guess) {
                return guess;
            }
            
            @Override
            boolean jdkCompatibilityMode(final SSLEngine engine) {
                return true;
            }
        }, 
        JDK(false, ByteToMessageDecoder.MERGE_CUMULATOR) {
            @Override
            SSLEngineResult unwrap(final SslHandler handler, final ByteBuf in, final int readerIndex, final int len, final ByteBuf out) throws SSLException {
                final int writerIndex = out.writerIndex();
                final ByteBuffer inNioBuffer = toByteBuffer(in, readerIndex, len);
                final int position = inNioBuffer.position();
                final SSLEngineResult result = handler.engine.unwrap(inNioBuffer, toByteBuffer(out, writerIndex, out.writableBytes()));
                out.writerIndex(writerIndex + result.bytesProduced());
                if (result.bytesConsumed() == 0) {
                    final int consumed = inNioBuffer.position() - position;
                    if (consumed != result.bytesConsumed()) {
                        return new SSLEngineResult(result.getStatus(), result.getHandshakeStatus(), consumed, result.bytesProduced());
                    }
                }
                return result;
            }
            
            @Override
            int calculateWrapBufferCapacity(final SslHandler handler, final int pendingBytes, final int numComponents) {
                return handler.engine.getSession().getPacketBufferSize();
            }
            
            @Override
            int calculatePendingData(final SslHandler handler, final int guess) {
                return guess;
            }
            
            @Override
            boolean jdkCompatibilityMode(final SSLEngine engine) {
                return true;
            }
        };
        
        final boolean wantsDirectBuffer;
        final Cumulator cumulator;
        
        static SslEngineType forEngine(final SSLEngine engine) {
            return (engine instanceof ReferenceCountedOpenSslEngine) ? SslEngineType.TCNATIVE : ((engine instanceof ConscryptAlpnSslEngine) ? SslEngineType.CONSCRYPT : SslEngineType.JDK);
        }
        
        private SslEngineType(final boolean wantsDirectBuffer, final Cumulator cumulator) {
            this.wantsDirectBuffer = wantsDirectBuffer;
            this.cumulator = cumulator;
        }
        
        int getPacketBufferSize(final SslHandler handler) {
            return handler.engine.getSession().getPacketBufferSize();
        }
        
        abstract SSLEngineResult unwrap(final SslHandler sslHandler, final ByteBuf byteBuf2, final int integer3, final int integer4, final ByteBuf byteBuf5) throws SSLException;
        
        abstract int calculateWrapBufferCapacity(final SslHandler sslHandler, final int integer2, final int integer3);
        
        abstract int calculatePendingData(final SslHandler sslHandler, final int integer);
        
        abstract boolean jdkCompatibilityMode(final SSLEngine sSLEngine);
    }
    
    private final class SslHandlerCoalescingBufferQueue extends AbstractCoalescingBufferQueue {
        SslHandlerCoalescingBufferQueue(final Channel channel, final int initSize) {
            super(channel, initSize);
        }
        
        @Override
        protected ByteBuf compose(final ByteBufAllocator alloc, final ByteBuf cumulation, final ByteBuf next) {
            final int wrapDataSize = SslHandler.this.wrapDataSize;
            if (cumulation instanceof CompositeByteBuf) {
                final CompositeByteBuf composite = (CompositeByteBuf)cumulation;
                final int numComponents = composite.numComponents();
                if (numComponents == 0 || !attemptCopyToCumulation(composite.internalComponent(numComponents - 1), next, wrapDataSize)) {
                    composite.addComponent(true, next);
                }
                return composite;
            }
            return attemptCopyToCumulation(cumulation, next, wrapDataSize) ? cumulation : this.copyAndCompose(alloc, cumulation, next);
        }
        
        @Override
        protected ByteBuf composeFirst(final ByteBufAllocator allocator, ByteBuf first) {
            if (first instanceof CompositeByteBuf) {
                final CompositeByteBuf composite = (CompositeByteBuf)first;
                first = allocator.directBuffer(composite.readableBytes());
                try {
                    first.writeBytes(composite);
                }
                catch (Throwable cause) {
                    first.release();
                    PlatformDependent.throwException(cause);
                }
                composite.release();
            }
            return first;
        }
        
        @Override
        protected ByteBuf removeEmptyValue() {
            return null;
        }
    }
    
    private final class LazyChannelPromise extends DefaultPromise<Channel> {
        @Override
        protected EventExecutor executor() {
            if (SslHandler.this.ctx == null) {
                throw new IllegalStateException();
            }
            return SslHandler.this.ctx.executor();
        }
        
        @Override
        protected void checkDeadLock() {
            if (SslHandler.this.ctx == null) {
                return;
            }
            super.checkDeadLock();
        }
    }
}
