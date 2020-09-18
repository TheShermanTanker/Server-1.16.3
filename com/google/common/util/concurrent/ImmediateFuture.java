package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class ImmediateFuture<V> implements ListenableFuture<V> {
    private static final Logger log;
    
    public void addListener(final Runnable listener, final Executor executor) {
        Preconditions.<Runnable>checkNotNull(listener, "Runnable was null.");
        Preconditions.<Executor>checkNotNull(executor, "Executor was null.");
        try {
            executor.execute(listener);
        }
        catch (RuntimeException e) {
            ImmediateFuture.log.log(Level.SEVERE, new StringBuilder().append("RuntimeException while executing runnable ").append(listener).append(" with executor ").append(executor).toString(), (Throwable)e);
        }
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
    
    public abstract V get() throws ExecutionException;
    
    public V get(final long timeout, final TimeUnit unit) throws ExecutionException {
        Preconditions.<TimeUnit>checkNotNull(unit);
        return this.get();
    }
    
    public boolean isCancelled() {
        return false;
    }
    
    public boolean isDone() {
        return true;
    }
    
    static {
        log = Logger.getLogger(ImmediateFuture.class.getName());
    }
    
    static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
        static final ImmediateSuccessfulFuture<Object> NULL;
        @Nullable
        private final V value;
        
        ImmediateSuccessfulFuture(@Nullable final V value) {
            this.value = value;
        }
        
        @Override
        public V get() {
            return this.value;
        }
        
        static {
            NULL = new ImmediateSuccessfulFuture<>(null);
        }
    }
    
    @GwtIncompatible
    static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
        @Nullable
        private final V value;
        
        ImmediateSuccessfulCheckedFuture(@Nullable final V value) {
            this.value = value;
        }
        
        @Override
        public V get() {
            return this.value;
        }
        
        @Override
        public V checkedGet() {
            return this.value;
        }
        
        @Override
        public V checkedGet(final long timeout, final TimeUnit unit) {
            Preconditions.<TimeUnit>checkNotNull(unit);
            return this.value;
        }
    }
    
    static final class ImmediateFailedFuture<V> extends TrustedFuture<V> {
        ImmediateFailedFuture(final Throwable thrown) {
            this.setException(thrown);
        }
    }
    
    static final class ImmediateCancelledFuture<V> extends TrustedFuture<V> {
        ImmediateCancelledFuture() {
            this.cancel(false);
        }
    }
    
    @GwtIncompatible
    static class ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
        private final X thrown;
        
        ImmediateFailedCheckedFuture(final X thrown) {
            this.thrown = thrown;
        }
        
        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException((Throwable)this.thrown);
        }
        
        @Override
        public V checkedGet() throws X, Exception {
            throw this.thrown;
        }
        
        @Override
        public V checkedGet(final long timeout, final TimeUnit unit) throws X, Exception {
            Preconditions.<TimeUnit>checkNotNull(unit);
            throw this.thrown;
        }
    }
}
