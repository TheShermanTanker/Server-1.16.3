package io.netty.util.concurrent;

public interface ProgressiveFuture<V> extends Future<V> {
    ProgressiveFuture<V> addListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    ProgressiveFuture<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    ProgressiveFuture<V> removeListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    ProgressiveFuture<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    ProgressiveFuture<V> sync() throws InterruptedException;
    
    ProgressiveFuture<V> syncUninterruptibly();
    
    ProgressiveFuture<V> await() throws InterruptedException;
    
    ProgressiveFuture<V> awaitUninterruptibly();
}
