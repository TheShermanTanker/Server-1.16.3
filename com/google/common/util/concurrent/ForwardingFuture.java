package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Future;
import com.google.common.collect.ForwardingObject;

@CanIgnoreReturnValue
@GwtCompatible
public abstract class ForwardingFuture<V> extends ForwardingObject implements Future<V> {
    protected ForwardingFuture() {
    }
    
    @Override
    protected abstract Future<? extends V> delegate();
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return this.delegate().cancel(mayInterruptIfRunning);
    }
    
    public boolean isCancelled() {
        return this.delegate().isCancelled();
    }
    
    public boolean isDone() {
        return this.delegate().isDone();
    }
    
    public V get() throws InterruptedException, ExecutionException {
        return (V)this.delegate().get();
    }
    
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (V)this.delegate().get(timeout, unit);
    }
    
    public abstract static class SimpleForwardingFuture<V> extends ForwardingFuture<V> {
        private final Future<V> delegate;
        
        protected SimpleForwardingFuture(final Future<V> delegate) {
            this.delegate = Preconditions.<Future<V>>checkNotNull(delegate);
        }
        
        @Override
        protected final Future<V> delegate() {
            return this.delegate;
        }
    }
}
