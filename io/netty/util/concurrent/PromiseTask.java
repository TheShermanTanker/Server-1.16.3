package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

class PromiseTask<V> extends DefaultPromise<V> implements RunnableFuture<V> {
    protected final Callable<V> task;
    
    static <T> Callable<T> toCallable(final Runnable runnable, final T result) {
        return (Callable<T>)new RunnableAdapter(runnable, result);
    }
    
    PromiseTask(final EventExecutor executor, final Runnable runnable, final V result) {
        this(executor, PromiseTask.<V>toCallable(runnable, result));
    }
    
    PromiseTask(final EventExecutor executor, final Callable<V> callable) {
        super(executor);
        this.task = callable;
    }
    
    public final int hashCode() {
        return System.identityHashCode(this);
    }
    
    public final boolean equals(final Object obj) {
        return this == obj;
    }
    
    public void run() {
        try {
            if (this.setUncancellableInternal()) {
                final V result = (V)this.task.call();
                this.setSuccessInternal(result);
            }
        }
        catch (Throwable e) {
            this.setFailureInternal(e);
        }
    }
    
    @Override
    public final Promise<V> setFailure(final Throwable cause) {
        throw new IllegalStateException();
    }
    
    protected final Promise<V> setFailureInternal(final Throwable cause) {
        super.setFailure(cause);
        return this;
    }
    
    @Override
    public final boolean tryFailure(final Throwable cause) {
        return false;
    }
    
    protected final boolean tryFailureInternal(final Throwable cause) {
        return super.tryFailure(cause);
    }
    
    @Override
    public final Promise<V> setSuccess(final V result) {
        throw new IllegalStateException();
    }
    
    protected final Promise<V> setSuccessInternal(final V result) {
        super.setSuccess(result);
        return this;
    }
    
    @Override
    public final boolean trySuccess(final V result) {
        return false;
    }
    
    protected final boolean trySuccessInternal(final V result) {
        return super.trySuccess(result);
    }
    
    @Override
    public final boolean setUncancellable() {
        throw new IllegalStateException();
    }
    
    protected final boolean setUncancellableInternal() {
        return super.setUncancellable();
    }
    
    @Override
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, ',');
        return buf.append(" task: ").append(this.task).append(')');
    }
    
    private static final class RunnableAdapter<T> implements Callable<T> {
        final Runnable task;
        final T result;
        
        RunnableAdapter(final Runnable task, final T result) {
            this.task = task;
            this.result = result;
        }
        
        public T call() {
            this.task.run();
            return this.result;
        }
        
        public String toString() {
            return new StringBuilder().append("Callable(task: ").append(this.task).append(", result: ").append(this.result).append(')').toString();
        }
    }
}
