package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public abstract class CompleteFuture<V> extends AbstractFuture<V> {
    private final EventExecutor executor;
    
    protected CompleteFuture(final EventExecutor executor) {
        this.executor = executor;
    }
    
    protected EventExecutor executor() {
        return this.executor;
    }
    
    public Future<V> addListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        DefaultPromise.notifyListener(this.executor(), this, listener);
        return this;
    }
    
    public Future<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        if (listeners == null) {
            throw new NullPointerException("listeners");
        }
        for (final GenericFutureListener<? extends Future<? super V>> l : listeners) {
            if (l == null) {
                break;
            }
            DefaultPromise.notifyListener(this.executor(), this, l);
        }
        return this;
    }
    
    public Future<V> removeListener(final GenericFutureListener<? extends Future<? super V>> listener) {
        return this;
    }
    
    public Future<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... listeners) {
        return this;
    }
    
    public Future<V> await() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }
    
    public boolean await(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }
    
    public Future<V> sync() throws InterruptedException {
        return this;
    }
    
    public Future<V> syncUninterruptibly() {
        return this;
    }
    
    public boolean await(final long timeoutMillis) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return true;
    }
    
    public Future<V> awaitUninterruptibly() {
        return this;
    }
    
    public boolean awaitUninterruptibly(final long timeout, final TimeUnit unit) {
        return true;
    }
    
    public boolean awaitUninterruptibly(final long timeoutMillis) {
        return true;
    }
    
    public boolean isDone() {
        return true;
    }
    
    public boolean isCancellable() {
        return false;
    }
    
    public boolean isCancelled() {
        return false;
    }
    
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }
}
