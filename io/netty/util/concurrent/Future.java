package io.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

public interface Future<V> extends java.util.concurrent.Future<V> {
    boolean isSuccess();
    
    boolean isCancellable();
    
    Throwable cause();
    
    Future<V> addListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    Future<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    Future<V> removeListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    Future<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    Future<V> sync() throws InterruptedException;
    
    Future<V> syncUninterruptibly();
    
    Future<V> await() throws InterruptedException;
    
    Future<V> awaitUninterruptibly();
    
    boolean await(final long long1, final TimeUnit timeUnit) throws InterruptedException;
    
    boolean await(final long long1) throws InterruptedException;
    
    boolean awaitUninterruptibly(final long long1, final TimeUnit timeUnit);
    
    boolean awaitUninterruptibly(final long long1);
    
    V getNow();
    
    boolean cancel(final boolean boolean1);
}
