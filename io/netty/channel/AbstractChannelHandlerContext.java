package io.netty.channel;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.Recycler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.ReferenceCountUtil;
import java.net.SocketAddress;
import io.netty.util.internal.ThrowableUtil;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.concurrent.OrderedEventExecutor;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.ResourceLeakHint;
import io.netty.util.DefaultAttributeMap;

abstract class AbstractChannelHandlerContext extends DefaultAttributeMap implements ChannelHandlerContext, ResourceLeakHint {
    private static final InternalLogger logger;
    volatile AbstractChannelHandlerContext next;
    volatile AbstractChannelHandlerContext prev;
    private static final AtomicIntegerFieldUpdater<AbstractChannelHandlerContext> HANDLER_STATE_UPDATER;
    private static final int ADD_PENDING = 1;
    private static final int ADD_COMPLETE = 2;
    private static final int REMOVE_COMPLETE = 3;
    private static final int INIT = 0;
    private final boolean inbound;
    private final boolean outbound;
    private final DefaultChannelPipeline pipeline;
    private final String name;
    private final boolean ordered;
    final EventExecutor executor;
    private ChannelFuture succeededFuture;
    private Runnable invokeChannelReadCompleteTask;
    private Runnable invokeReadTask;
    private Runnable invokeChannelWritableStateChangedTask;
    private Runnable invokeFlushTask;
    private volatile int handlerState;
    
    AbstractChannelHandlerContext(final DefaultChannelPipeline pipeline, final EventExecutor executor, final String name, final boolean inbound, final boolean outbound) {
        this.handlerState = 0;
        this.name = ObjectUtil.<String>checkNotNull(name, "name");
        this.pipeline = pipeline;
        this.executor = executor;
        this.inbound = inbound;
        this.outbound = outbound;
        this.ordered = (executor == null || executor instanceof OrderedEventExecutor);
    }
    
    @Override
    public Channel channel() {
        return this.pipeline.channel();
    }
    
    @Override
    public ChannelPipeline pipeline() {
        return this.pipeline;
    }
    
    @Override
    public ByteBufAllocator alloc() {
        return this.channel().config().getAllocator();
    }
    
    @Override
    public EventExecutor executor() {
        if (this.executor == null) {
            return this.channel().eventLoop();
        }
        return this.executor;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public ChannelHandlerContext fireChannelRegistered() {
        invokeChannelRegistered(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelRegistered(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRegistered();
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRegistered();
                }
            });
        }
    }
    
    private void invokeChannelRegistered() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelRegistered(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelRegistered();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelUnregistered() {
        invokeChannelUnregistered(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelUnregistered(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelUnregistered();
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelUnregistered();
                }
            });
        }
    }
    
    private void invokeChannelUnregistered() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelUnregistered(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelUnregistered();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelActive() {
        invokeChannelActive(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelActive(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelActive();
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelActive();
                }
            });
        }
    }
    
    private void invokeChannelActive() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelActive(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelActive();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelInactive() {
        invokeChannelInactive(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelInactive(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelInactive();
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelInactive();
                }
            });
        }
    }
    
    private void invokeChannelInactive() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelInactive(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelInactive();
        }
    }
    
    @Override
    public ChannelHandlerContext fireExceptionCaught(final Throwable cause) {
        invokeExceptionCaught(this.next, cause);
        return this;
    }
    
    static void invokeExceptionCaught(final AbstractChannelHandlerContext next, final Throwable cause) {
        ObjectUtil.<Throwable>checkNotNull(cause, "cause");
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeExceptionCaught(cause);
        }
        else {
            try {
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeExceptionCaught(cause);
                    }
                });
            }
            catch (Throwable t) {
                if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                    AbstractChannelHandlerContext.logger.warn("Failed to submit an exceptionCaught() event.", t);
                    AbstractChannelHandlerContext.logger.warn("The exceptionCaught() event that was failed to submit was:", cause);
                }
            }
        }
    }
    
    private void invokeExceptionCaught(final Throwable cause) {
        if (this.invokeHandler()) {
            try {
                this.handler().exceptionCaught(this, cause);
            }
            catch (Throwable error) {
                if (AbstractChannelHandlerContext.logger.isDebugEnabled()) {
                    AbstractChannelHandlerContext.logger.debug("An exception {}was thrown by a user handler's exceptionCaught() method while handling the following exception:", ThrowableUtil.stackTraceToString(error), cause);
                }
                else if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                    AbstractChannelHandlerContext.logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:", error, cause);
                }
            }
        }
        else {
            this.fireExceptionCaught(cause);
        }
    }
    
    @Override
    public ChannelHandlerContext fireUserEventTriggered(final Object event) {
        invokeUserEventTriggered(this.findContextInbound(), event);
        return this;
    }
    
    static void invokeUserEventTriggered(final AbstractChannelHandlerContext next, final Object event) {
        ObjectUtil.checkNotNull(event, "event");
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeUserEventTriggered(event);
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeUserEventTriggered(event);
                }
            });
        }
    }
    
    private void invokeUserEventTriggered(final Object event) {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).userEventTriggered(this, event);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireUserEventTriggered(event);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        invokeChannelRead(this.findContextInbound(), msg);
        return this;
    }
    
    static void invokeChannelRead(final AbstractChannelHandlerContext next, final Object msg) {
        final Object m = next.pipeline.touch(ObjectUtil.checkNotNull(msg, "msg"), next);
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelRead(m);
        }
        else {
            executor.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeChannelRead(m);
                }
            });
        }
    }
    
    private void invokeChannelRead(final Object msg) {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelRead(this, msg);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelRead(msg);
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelReadComplete() {
        invokeChannelReadComplete(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelReadComplete(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelReadComplete();
        }
        else {
            Runnable task = next.invokeChannelReadCompleteTask;
            if (task == null) {
                task = (next.invokeChannelReadCompleteTask = (Runnable)new Runnable() {
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelReadComplete();
                    }
                });
            }
            executor.execute(task);
        }
    }
    
    private void invokeChannelReadComplete() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelReadComplete(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelReadComplete();
        }
    }
    
    @Override
    public ChannelHandlerContext fireChannelWritabilityChanged() {
        invokeChannelWritabilityChanged(this.findContextInbound());
        return this;
    }
    
    static void invokeChannelWritabilityChanged(final AbstractChannelHandlerContext next) {
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeChannelWritabilityChanged();
        }
        else {
            Runnable task = next.invokeChannelWritableStateChangedTask;
            if (task == null) {
                task = (next.invokeChannelWritableStateChangedTask = (Runnable)new Runnable() {
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeChannelWritabilityChanged();
                    }
                });
            }
            executor.execute(task);
        }
    }
    
    private void invokeChannelWritabilityChanged() {
        if (this.invokeHandler()) {
            try {
                ((ChannelInboundHandler)this.handler()).channelWritabilityChanged(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.fireChannelWritabilityChanged();
        }
    }
    
    public ChannelFuture bind(final SocketAddress localAddress) {
        return this.bind(localAddress, this.newPromise());
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress) {
        return this.connect(remoteAddress, this.newPromise());
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
        return this.connect(remoteAddress, localAddress, this.newPromise());
    }
    
    public ChannelFuture disconnect() {
        return this.disconnect(this.newPromise());
    }
    
    public ChannelFuture close() {
        return this.close(this.newPromise());
    }
    
    public ChannelFuture deregister() {
        return this.deregister(this.newPromise());
    }
    
    public ChannelFuture bind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (localAddress == null) {
            throw new NullPointerException("localAddress");
        }
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeBind(localAddress, promise);
        }
        else {
            safeExecute(executor, (Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeBind(localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeBind(final SocketAddress localAddress, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).bind(this, localAddress, promise);
            }
            catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.bind(localAddress, promise);
        }
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final ChannelPromise promise) {
        return this.connect(remoteAddress, null, promise);
    }
    
    public ChannelFuture connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (remoteAddress == null) {
            throw new NullPointerException("remoteAddress");
        }
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeConnect(remoteAddress, localAddress, promise);
        }
        else {
            safeExecute(executor, (Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeConnect(remoteAddress, localAddress, promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).connect(this, remoteAddress, localAddress, promise);
            }
            catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.connect(remoteAddress, localAddress, promise);
        }
    }
    
    public ChannelFuture disconnect(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (!this.channel().metadata().hasDisconnect()) {
                next.invokeClose(promise);
            }
            else {
                next.invokeDisconnect(promise);
            }
        }
        else {
            safeExecute(executor, (Runnable)new Runnable() {
                public void run() {
                    if (!AbstractChannelHandlerContext.this.channel().metadata().hasDisconnect()) {
                        AbstractChannelHandlerContext.this.invokeClose(promise);
                    }
                    else {
                        AbstractChannelHandlerContext.this.invokeDisconnect(promise);
                    }
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDisconnect(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).disconnect(this, promise);
            }
            catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.disconnect(promise);
        }
    }
    
    public ChannelFuture close(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeClose(promise);
        }
        else {
            safeExecute(executor, (Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeClose(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeClose(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).close(this, promise);
            }
            catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.close(promise);
        }
    }
    
    public ChannelFuture deregister(final ChannelPromise promise) {
        if (this.isNotValidPromise(promise, false)) {
            return promise;
        }
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeDeregister(promise);
        }
        else {
            safeExecute(executor, (Runnable)new Runnable() {
                public void run() {
                    AbstractChannelHandlerContext.this.invokeDeregister(promise);
                }
            }, promise, null);
        }
        return promise;
    }
    
    private void invokeDeregister(final ChannelPromise promise) {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).deregister(this, promise);
            }
            catch (Throwable t) {
                notifyOutboundHandlerException(t, promise);
            }
        }
        else {
            this.deregister(promise);
        }
    }
    
    @Override
    public ChannelHandlerContext read() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeRead();
        }
        else {
            Runnable task = next.invokeReadTask;
            if (task == null) {
                task = (next.invokeReadTask = (Runnable)new Runnable() {
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeRead();
                    }
                });
            }
            executor.execute(task);
        }
        return this;
    }
    
    private void invokeRead() {
        if (this.invokeHandler()) {
            try {
                ((ChannelOutboundHandler)this.handler()).read(this);
            }
            catch (Throwable t) {
                this.notifyHandlerException(t);
            }
        }
        else {
            this.read();
        }
    }
    
    public ChannelFuture write(final Object msg) {
        return this.write(msg, this.newPromise());
    }
    
    public ChannelFuture write(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        try {
            if (this.isNotValidPromise(promise, true)) {
                ReferenceCountUtil.release(msg);
                return promise;
            }
        }
        catch (RuntimeException e) {
            ReferenceCountUtil.release(msg);
            throw e;
        }
        this.write(msg, false, promise);
        return promise;
    }
    
    private void invokeWrite(final Object msg, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            this.invokeWrite0(msg, promise);
        }
        else {
            this.write(msg, promise);
        }
    }
    
    private void invokeWrite0(final Object msg, final ChannelPromise promise) {
        try {
            ((ChannelOutboundHandler)this.handler()).write(this, msg, promise);
        }
        catch (Throwable t) {
            notifyOutboundHandlerException(t, promise);
        }
    }
    
    @Override
    public ChannelHandlerContext flush() {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            next.invokeFlush();
        }
        else {
            Runnable task = next.invokeFlushTask;
            if (task == null) {
                task = (next.invokeFlushTask = (Runnable)new Runnable() {
                    public void run() {
                        AbstractChannelHandlerContext.this.invokeFlush();
                    }
                });
            }
            safeExecute(executor, task, this.channel().voidPromise(), null);
        }
        return this;
    }
    
    private void invokeFlush() {
        if (this.invokeHandler()) {
            this.invokeFlush0();
        }
        else {
            this.flush();
        }
    }
    
    private void invokeFlush0() {
        try {
            ((ChannelOutboundHandler)this.handler()).flush(this);
        }
        catch (Throwable t) {
            this.notifyHandlerException(t);
        }
    }
    
    public ChannelFuture writeAndFlush(final Object msg, final ChannelPromise promise) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (this.isNotValidPromise(promise, true)) {
            ReferenceCountUtil.release(msg);
            return promise;
        }
        this.write(msg, true, promise);
        return promise;
    }
    
    private void invokeWriteAndFlush(final Object msg, final ChannelPromise promise) {
        if (this.invokeHandler()) {
            this.invokeWrite0(msg, promise);
            this.invokeFlush0();
        }
        else {
            this.writeAndFlush(msg, promise);
        }
    }
    
    private void write(final Object msg, final boolean flush, final ChannelPromise promise) {
        final AbstractChannelHandlerContext next = this.findContextOutbound();
        final Object m = this.pipeline.touch(msg, next);
        final EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (flush) {
                next.invokeWriteAndFlush(m, promise);
            }
            else {
                next.invokeWrite(m, promise);
            }
        }
        else {
            AbstractWriteTask task;
            if (flush) {
                task = newInstance(next, m, promise);
            }
            else {
                task = newInstance(next, m, promise);
            }
            safeExecute(executor, (Runnable)task, promise, m);
        }
    }
    
    public ChannelFuture writeAndFlush(final Object msg) {
        return this.writeAndFlush(msg, this.newPromise());
    }
    
    private static void notifyOutboundHandlerException(final Throwable cause, final ChannelPromise promise) {
        PromiseNotificationUtil.tryFailure(promise, cause, (promise instanceof VoidChannelPromise) ? null : AbstractChannelHandlerContext.logger);
    }
    
    private void notifyHandlerException(final Throwable cause) {
        if (inExceptionCaught(cause)) {
            if (AbstractChannelHandlerContext.logger.isWarnEnabled()) {
                AbstractChannelHandlerContext.logger.warn("An exception was thrown by a user handler while handling an exceptionCaught event", cause);
            }
            return;
        }
        this.invokeExceptionCaught(cause);
    }
    
    private static boolean inExceptionCaught(Throwable cause) {
        do {
            final StackTraceElement[] trace = cause.getStackTrace();
            if (trace != null) {
                for (final StackTraceElement t : trace) {
                    if (t == null) {
                        break;
                    }
                    if ("exceptionCaught".equals(t.getMethodName())) {
                        return true;
                    }
                }
            }
            cause = cause.getCause();
        } while (cause != null);
        return false;
    }
    
    public ChannelPromise newPromise() {
        return new DefaultChannelPromise(this.channel(), this.executor());
    }
    
    public ChannelProgressivePromise newProgressivePromise() {
        return new DefaultChannelProgressivePromise(this.channel(), this.executor());
    }
    
    public ChannelFuture newSucceededFuture() {
        ChannelFuture succeededFuture = this.succeededFuture;
        if (succeededFuture == null) {
            succeededFuture = (this.succeededFuture = new SucceededChannelFuture(this.channel(), this.executor()));
        }
        return succeededFuture;
    }
    
    public ChannelFuture newFailedFuture(final Throwable cause) {
        return new FailedChannelFuture(this.channel(), this.executor(), cause);
    }
    
    private boolean isNotValidPromise(final ChannelPromise promise, final boolean allowVoidPromise) {
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        if (promise.isDone()) {
            if (promise.isCancelled()) {
                return true;
            }
            throw new IllegalArgumentException(new StringBuilder().append("promise already done: ").append(promise).toString());
        }
        else {
            if (promise.channel() != this.channel()) {
                throw new IllegalArgumentException(String.format("promise.channel does not match: %s (expected: %s)", new Object[] { promise.channel(), this.channel() }));
            }
            if (promise.getClass() == DefaultChannelPromise.class) {
                return false;
            }
            if (!allowVoidPromise && promise instanceof VoidChannelPromise) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(VoidChannelPromise.class) + " not allowed for this operation");
            }
            if (promise instanceof AbstractChannel.CloseFuture) {
                throw new IllegalArgumentException(StringUtil.simpleClassName(AbstractChannel.CloseFuture.class) + " not allowed in a pipeline");
            }
            return false;
        }
    }
    
    private AbstractChannelHandlerContext findContextInbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.next;
        } while (!ctx.inbound);
        return ctx;
    }
    
    private AbstractChannelHandlerContext findContextOutbound() {
        AbstractChannelHandlerContext ctx = this;
        do {
            ctx = ctx.prev;
        } while (!ctx.outbound);
        return ctx;
    }
    
    public ChannelPromise voidPromise() {
        return this.channel().voidPromise();
    }
    
    final void setRemoved() {
        this.handlerState = 3;
    }
    
    final void setAddComplete() {
        int oldState;
        do {
            oldState = this.handlerState;
        } while (oldState != 3 && !AbstractChannelHandlerContext.HANDLER_STATE_UPDATER.compareAndSet(this, oldState, 2));
    }
    
    final void setAddPending() {
        final boolean updated = AbstractChannelHandlerContext.HANDLER_STATE_UPDATER.compareAndSet(this, 0, 1);
        assert updated;
    }
    
    private boolean invokeHandler() {
        final int handlerState = this.handlerState;
        return handlerState == 2 || (!this.ordered && handlerState == 1);
    }
    
    @Override
    public boolean isRemoved() {
        return this.handlerState == 3;
    }
    
    @Override
    public <T> Attribute<T> attr(final AttributeKey<T> key) {
        return this.channel().<T>attr(key);
    }
    
    @Override
    public <T> boolean hasAttr(final AttributeKey<T> key) {
        return this.channel().<T>hasAttr(key);
    }
    
    private static void safeExecute(final EventExecutor executor, final Runnable runnable, final ChannelPromise promise, final Object msg) {
        try {
            executor.execute(runnable);
        }
        catch (Throwable cause) {
            try {
                promise.setFailure(cause);
            }
            finally {
                if (msg != null) {
                    ReferenceCountUtil.release(msg);
                }
            }
        }
    }
    
    @Override
    public String toHintString() {
        return '\'' + this.name + "' will handle the message from this point.";
    }
    
    public String toString() {
        return StringUtil.simpleClassName(ChannelHandlerContext.class) + '(' + this.name + ", " + this.channel() + ')';
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractChannelHandlerContext.class);
        HANDLER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater((Class)AbstractChannelHandlerContext.class, "handlerState");
    }
    
    abstract static class AbstractWriteTask implements Runnable {
        private static final boolean ESTIMATE_TASK_SIZE_ON_SUBMIT;
        private static final int WRITE_TASK_OVERHEAD;
        private final Recycler.Handle<AbstractWriteTask> handle;
        private AbstractChannelHandlerContext ctx;
        private Object msg;
        private ChannelPromise promise;
        private int size;
        
        private AbstractWriteTask(final Recycler.Handle<? extends AbstractWriteTask> handle) {
            this.handle = (Recycler.Handle<AbstractWriteTask>)handle;
        }
        
        protected static void init(final AbstractWriteTask task, final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            task.ctx = ctx;
            task.msg = msg;
            task.promise = promise;
            if (AbstractWriteTask.ESTIMATE_TASK_SIZE_ON_SUBMIT) {
                task.size = ctx.pipeline.estimatorHandle().size(msg) + AbstractWriteTask.WRITE_TASK_OVERHEAD;
                ctx.pipeline.incrementPendingOutboundBytes(task.size);
            }
            else {
                task.size = 0;
            }
        }
        
        public final void run() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: ifeq            21
            //     6: aload_0         /* this */
            //     7: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.ctx:Lio/netty/channel/AbstractChannelHandlerContext;
            //    10: invokestatic    io/netty/channel/AbstractChannelHandlerContext.access$1800:(Lio/netty/channel/AbstractChannelHandlerContext;)Lio/netty/channel/DefaultChannelPipeline;
            //    13: aload_0         /* this */
            //    14: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.size:I
            //    17: i2l            
            //    18: invokevirtual   io/netty/channel/DefaultChannelPipeline.decrementPendingOutboundBytes:(J)V
            //    21: aload_0         /* this */
            //    22: aload_0         /* this */
            //    23: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.ctx:Lio/netty/channel/AbstractChannelHandlerContext;
            //    26: aload_0         /* this */
            //    27: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.msg:Ljava/lang/Object;
            //    30: aload_0         /* this */
            //    31: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.promise:Lio/netty/channel/ChannelPromise;
            //    34: invokevirtual   io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.write:(Lio/netty/channel/AbstractChannelHandlerContext;Ljava/lang/Object;Lio/netty/channel/ChannelPromise;)V
            //    37: aload_0         /* this */
            //    38: aconst_null    
            //    39: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.ctx:Lio/netty/channel/AbstractChannelHandlerContext;
            //    42: aload_0         /* this */
            //    43: aconst_null    
            //    44: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.msg:Ljava/lang/Object;
            //    47: aload_0         /* this */
            //    48: aconst_null    
            //    49: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.promise:Lio/netty/channel/ChannelPromise;
            //    52: aload_0         /* this */
            //    53: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.handle:Lio/netty/util/Recycler$Handle;
            //    56: aload_0         /* this */
            //    57: invokeinterface io/netty/util/Recycler$Handle.recycle:(Ljava/lang/Object;)V
            //    62: goto            93
            //    65: astore_1       
            //    66: aload_0         /* this */
            //    67: aconst_null    
            //    68: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.ctx:Lio/netty/channel/AbstractChannelHandlerContext;
            //    71: aload_0         /* this */
            //    72: aconst_null    
            //    73: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.msg:Ljava/lang/Object;
            //    76: aload_0         /* this */
            //    77: aconst_null    
            //    78: putfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.promise:Lio/netty/channel/ChannelPromise;
            //    81: aload_0         /* this */
            //    82: getfield        io/netty/channel/AbstractChannelHandlerContext$AbstractWriteTask.handle:Lio/netty/util/Recycler$Handle;
            //    85: aload_0         /* this */
            //    86: invokeinterface io/netty/util/Recycler$Handle.recycle:(Ljava/lang/Object;)V
            //    91: aload_1        
            //    92: athrow         
            //    93: return         
            //    StackMapTable: 00 03 15 6B 07 00 5C 1B
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  0      37     65     93     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
            //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
            //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
            //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
            //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
            //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
            //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
            //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
            //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
            //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
            //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        protected void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            ctx.invokeWrite(msg, promise);
        }
        
        static {
            ESTIMATE_TASK_SIZE_ON_SUBMIT = SystemPropertyUtil.getBoolean("io.netty.transport.estimateSizeOnSubmit", true);
            WRITE_TASK_OVERHEAD = SystemPropertyUtil.getInt("io.netty.transport.writeTaskSizeOverhead", 48);
        }
    }
    
    static final class WriteTask extends AbstractWriteTask implements SingleThreadEventLoop.NonWakeupRunnable {
        private static final Recycler<WriteTask> RECYCLER;
        
        private static WriteTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            final WriteTask task = WriteTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, promise);
            return task;
        }
        
        private WriteTask(final Recycler.Handle<WriteTask> handle) {
            super((Recycler.Handle)handle);
        }
        
        static {
            RECYCLER = new Recycler<WriteTask>() {
                @Override
                protected WriteTask newObject(final Handle<WriteTask> handle) {
                    return new WriteTask((Handle)handle);
                }
            };
        }
    }
    
    static final class WriteAndFlushTask extends AbstractWriteTask {
        private static final Recycler<WriteAndFlushTask> RECYCLER;
        
        private static WriteAndFlushTask newInstance(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            final WriteAndFlushTask task = WriteAndFlushTask.RECYCLER.get();
            AbstractWriteTask.init(task, ctx, msg, promise);
            return task;
        }
        
        private WriteAndFlushTask(final Recycler.Handle<WriteAndFlushTask> handle) {
            super((Recycler.Handle)handle);
        }
        
        public void write(final AbstractChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
            super.write(ctx, msg, promise);
            ctx.invokeFlush();
        }
        
        static {
            RECYCLER = new Recycler<WriteAndFlushTask>() {
                @Override
                protected WriteAndFlushTask newObject(final Handle<WriteAndFlushTask> handle) {
                    return new WriteAndFlushTask((Handle)handle);
                }
            };
        }
    }
}
