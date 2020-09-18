package io.netty.channel;

import io.netty.util.concurrent.Promise;
import java.util.concurrent.TimeUnit;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.AbstractFuture;

public final class VoidChannelPromise extends AbstractFuture<Void> implements ChannelPromise {
    private final Channel channel;
    private final ChannelFutureListener fireExceptionListener;
    
    public VoidChannelPromise(final Channel channel, final boolean fireException) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        if (fireException) {
            this.fireExceptionListener = new ChannelFutureListener() {
                public void operationComplete(final ChannelFuture future) throws Exception {
                    final Throwable cause = future.cause();
                    if (cause != null) {
                        VoidChannelPromise.this.fireException0(cause);
                    }
                }
            };
        }
        else {
            this.fireExceptionListener = null;
        }
    }
    
    @Override
    public VoidChannelPromise addListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        fail();
        return this;
    }
    
    @Override
    public VoidChannelPromise addListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        fail();
        return this;
    }
    
    @Override
    public VoidChannelPromise removeListener(final GenericFutureListener<? extends Future<? super Void>> listener) {
        return this;
    }
    
    @Override
    public VoidChannelPromise removeListeners(final GenericFutureListener<? extends Future<? super Void>>... listeners) {
        return this;
    }
    
    @Override
    public VoidChannelPromise await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }
    
    public boolean await(final long timeout, final TimeUnit unit) {
        fail();
        return false;
    }
    
    public boolean await(final long timeoutMillis) {
        fail();
        return false;
    }
    
    @Override
    public VoidChannelPromise awaitUninterruptibly() {
        fail();
        return this;
    }
    
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        fail();
        return false;
    }
    
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        fail();
        return false;
    }
    
    @Override
    public Channel channel() {
        return this.channel;
    }
    
    public boolean isDone() {
        return false;
    }
    
    public boolean isSuccess() {
        return false;
    }
    
    public boolean setUncancellable() {
        return true;
    }
    
    public boolean isCancellable() {
        return false;
    }
    
    public boolean isCancelled() {
        return false;
    }
    
    public Throwable cause() {
        return null;
    }
    
    @Override
    public VoidChannelPromise sync() {
        fail();
        return this;
    }
    
    @Override
    public VoidChannelPromise syncUninterruptibly() {
        fail();
        return this;
    }
    
    @Override
    public VoidChannelPromise setFailure(final Throwable cause) {
        this.fireException0(cause);
        return this;
    }
    
    @Override
    public VoidChannelPromise setSuccess() {
        return this;
    }
    
    public boolean tryFailure(final Throwable cause) {
        this.fireException0(cause);
        return false;
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    @Override
    public boolean trySuccess() {
        return false;
    }
    
    private static void fail() {
        throw new IllegalStateException("void future");
    }
    
    @Override
    public VoidChannelPromise setSuccess(final Void result) {
        return this;
    }
    
    public boolean trySuccess(final Void result) {
        return false;
    }
    
    public Void getNow() {
        return null;
    }
    
    @Override
    public ChannelPromise unvoid() {
        final ChannelPromise promise = new DefaultChannelPromise(this.channel);
        if (this.fireExceptionListener != null) {
            promise.addListener(this.fireExceptionListener);
        }
        return promise;
    }
    
    public boolean isVoid() {
        return true;
    }
    
    private void fireException0(final Throwable cause) {
        if (this.fireExceptionListener != null && this.channel.isRegistered()) {
            this.channel.pipeline().fireExceptionCaught(cause);
        }
    }
}
