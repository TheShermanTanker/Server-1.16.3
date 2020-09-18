package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;

public final class FailedFuture<V> extends CompleteFuture<V> {
    private final Throwable cause;
    
    public FailedFuture(final EventExecutor executor, final Throwable cause) {
        super(executor);
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }
    
    public Throwable cause() {
        return this.cause;
    }
    
    public boolean isSuccess() {
        return false;
    }
    
    @Override
    public Future<V> sync() {
        PlatformDependent.throwException(this.cause);
        return this;
    }
    
    @Override
    public Future<V> syncUninterruptibly() {
        PlatformDependent.throwException(this.cause);
        return this;
    }
    
    public V getNow() {
        return null;
    }
}
