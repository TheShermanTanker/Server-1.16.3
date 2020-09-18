package io.netty.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

public abstract class AbstractEventExecutorGroup implements EventExecutorGroup {
    public Future<?> submit(final Runnable task) {
        return this.next().submit(task);
    }
    
    public <T> Future<T> submit(final Runnable task, final T result) {
        return this.next().<T>submit(task, result);
    }
    
    public <T> Future<T> submit(final Callable<T> task) {
        return this.next().<T>submit(task);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.next().schedule(command, delay, unit);
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.next().<V>schedule(callable, delay, unit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.next().scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.next().scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    public Future<?> shutdownGracefully() {
        return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }
    
    @Deprecated
    public abstract void shutdown();
    
    @Deprecated
    public List<Runnable> shutdownNow() {
        this.shutdown();
        return (List<Runnable>)Collections.emptyList();
    }
    
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return (List<java.util.concurrent.Future<T>>)this.next().invokeAll((Collection)tasks);
    }
    
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return (List<java.util.concurrent.Future<T>>)this.next().invokeAll((Collection)tasks, timeout, unit);
    }
    
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return (T)this.next().invokeAny((Collection)tasks);
    }
    
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T)this.next().invokeAny((Collection)tasks, timeout, unit);
    }
    
    public void execute(final Runnable command) {
        this.next().execute(command);
    }
}
