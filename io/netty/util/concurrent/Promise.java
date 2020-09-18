package io.netty.util.concurrent;

public interface Promise<V> extends Future<V> {
    Promise<V> setSuccess(final V object);
    
    boolean trySuccess(final V object);
    
    Promise<V> setFailure(final Throwable throwable);
    
    boolean tryFailure(final Throwable throwable);
    
    boolean setUncancellable();
    
    Promise<V> addListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    Promise<V> addListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    Promise<V> removeListener(final GenericFutureListener<? extends Future<? super V>> genericFutureListener);
    
    Promise<V> removeListeners(final GenericFutureListener<? extends Future<? super V>>... arr);
    
    Promise<V> await() throws InterruptedException;
    
    Promise<V> awaitUninterruptibly();
    
    Promise<V> sync() throws InterruptedException;
    
    Promise<V> syncUninterruptibly();
}
