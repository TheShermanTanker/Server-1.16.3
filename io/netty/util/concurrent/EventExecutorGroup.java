package io.netty.util.concurrent;

import java.util.concurrent.Callable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor> {
    boolean isShuttingDown();
    
    Future<?> shutdownGracefully();
    
    Future<?> shutdownGracefully(final long long1, final long long2, final TimeUnit timeUnit);
    
    Future<?> terminationFuture();
    
    @Deprecated
    void shutdown();
    
    @Deprecated
    List<Runnable> shutdownNow();
    
    EventExecutor next();
    
    Iterator<EventExecutor> iterator();
    
    Future<?> submit(final Runnable runnable);
    
     <T> Future<T> submit(final Runnable runnable, final T object);
    
     <T> Future<T> submit(final Callable<T> callable);
    
    ScheduledFuture<?> schedule(final Runnable runnable, final long long2, final TimeUnit timeUnit);
    
     <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long long2, final TimeUnit timeUnit);
    
    ScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable, final long long2, final long long3, final TimeUnit timeUnit);
    
    ScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable, final long long2, final long long3, final TimeUnit timeUnit);
}
