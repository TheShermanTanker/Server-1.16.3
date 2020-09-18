package io.netty.util.concurrent;

public interface ProgressivePromise<V> extends Promise<V>, ProgressiveFuture<V> {
    ProgressivePromise<V> setProgress(final long long1, final long long2);
    
    boolean tryProgress(final long long1, final long long2);
    
    ProgressivePromise<V> setSuccess(final V object);
    
    ProgressivePromise<V> setFailure(final Throwable throwable);
    
    ProgressivePromise<V> addListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    ProgressivePromise<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    ProgressivePromise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    ProgressivePromise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    ProgressivePromise<V> await() throws InterruptedException;
    
    ProgressivePromise<V> awaitUninterruptibly();
    
    ProgressivePromise<V> sync() throws InterruptedException;
    
    ProgressivePromise<V> syncUninterruptibly();
}
