package io.netty.util.concurrent;

public interface EventExecutor extends EventExecutorGroup {
    EventExecutor next();
    
    EventExecutorGroup parent();
    
    boolean inEventLoop();
    
    boolean inEventLoop(final Thread thread);
    
     <V> Promise<V> newPromise();
    
     <V> ProgressivePromise<V> newProgressivePromise();
    
     <V> Future<V> newSucceededFuture(final V object);
    
     <V> Future<V> newFailedFuture(final Throwable throwable);
}
