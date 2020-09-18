package io.netty.channel;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;

public final class DelegatingChannelPromiseNotifier implements ChannelPromise, ChannelFutureListener {
    private static final InternalLogger logger;
    private final ChannelPromise delegate;
    private final boolean logNotifyFailure;
    
    public DelegatingChannelPromiseNotifier(final ChannelPromise delegate) {
        this(delegate, !(delegate instanceof VoidChannelPromise));
    }
    
    public DelegatingChannelPromiseNotifier(final ChannelPromise delegate, final boolean logNotifyFailure) {
        this.delegate = ObjectUtil.<ChannelPromise>checkNotNull(delegate, "delegate");
        this.logNotifyFailure = logNotifyFailure;
    }
    
    public void operationComplete(final ChannelFuture future) throws Exception {
        final InternalLogger internalLogger = this.logNotifyFailure ? DelegatingChannelPromiseNotifier.logger : null;
        if (future.isSuccess()) {
            final Void result = (Void)future.get();
            PromiseNotificationUtil.<Void>trySuccess(this.delegate, result, internalLogger);
        }
        else if (future.isCancelled()) {
            PromiseNotificationUtil.tryCancel(this.delegate, internalLogger);
        }
        else {
            final Throwable cause = future.cause();
            PromiseNotificationUtil.tryFailure(this.delegate, cause, internalLogger);
        }
    }
    
    public Channel channel() {
        return this.delegate.channel();
    }
    
    public ChannelPromise setSuccess(final Void result) {
        this.delegate.setSuccess(result);
        return this;
    }
    
    public ChannelPromise setSuccess() {
        this.delegate.setSuccess();
        return this;
    }
    
    public boolean trySuccess() {
        return this.delegate.trySuccess();
    }
    
    public boolean trySuccess(final Void result) {
        return this.delegate.trySuccess(result);
    }
    
    public ChannelPromise setFailure(final Throwable cause) {
        this.delegate.setFailure(cause);
        return this;
    }
    
    public ChannelPromise addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        this.delegate.addListener(listener);
        return this;
    }
    
    public ChannelPromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        this.delegate.addListeners(listeners);
        return this;
    }
    
    public ChannelPromise removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        this.delegate.removeListener(listener);
        return this;
    }
    
    public ChannelPromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        this.delegate.removeListeners(listeners);
        return this;
    }
    
    public boolean tryFailure(final Throwable cause) {
        return this.delegate.tryFailure(cause);
    }
    
    public boolean setUncancellable() {
        return this.delegate.setUncancellable();
    }
    
    public ChannelPromise await() throws InterruptedException {
        this.delegate.await();
        return this;
    }
    
    public ChannelPromise awaitUninterruptibly() {
        this.delegate.awaitUninterruptibly();
        return this;
    }
    
    public boolean isVoid() {
        return this.delegate.isVoid();
    }
    
    public ChannelPromise unvoid() {
        return this.isVoid() ? new DelegatingChannelPromiseNotifier(this.delegate.unvoid()) : this;
    }
    
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate.await(timeout, unit);
    }
    
    public boolean await(final long timeoutMillis) throws InterruptedException {
        return this.delegate.await(timeoutMillis);
    }
    
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        return this.delegate.awaitUninterruptibly(timeout, unit);
    }
    
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        return this.delegate.awaitUninterruptibly(timeoutMillis);
    }
    
    public Void getNow() {
        return this.delegate.getNow();
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.delegate.cancel(mayInterruptIfRunning);
    }
    
    public boolean isCancelled() {
        return this.delegate.isCancelled();
    }
    
    public boolean isDone() {
        return this.delegate.isDone();
    }
    
    public Void get() throws InterruptedException, ExecutionException {
        return (Void)this.delegate.get();
    }
    
    public Void get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (Void)this.delegate.get(timeout, unit);
    }
    
    public ChannelPromise sync() throws InterruptedException {
        this.delegate.sync();
        return this;
    }
    
    public ChannelPromise syncUninterruptibly() {
        this.delegate.syncUninterruptibly();
        return this;
    }
    
    public boolean isSuccess() {
        return this.delegate.isSuccess();
    }
    
    public boolean isCancellable() {
        return this.delegate.isCancellable();
    }
    
    public Throwable cause() {
        return this.delegate.cause();
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DelegatingChannelPromiseNotifier.class);
    }
}
