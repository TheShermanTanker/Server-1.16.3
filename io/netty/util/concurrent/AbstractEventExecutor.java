package io.netty.util.concurrent;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;
import io.netty.util.internal.logging.InternalLogger;
import java.util.concurrent.AbstractExecutorService;

public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor {
    private static final InternalLogger logger;
    static final long DEFAULT_SHUTDOWN_QUIET_PERIOD = 2L;
    static final long DEFAULT_SHUTDOWN_TIMEOUT = 15L;
    private final EventExecutorGroup parent;
    private final Collection<EventExecutor> selfCollection;
    
    protected AbstractEventExecutor() {
        this(null);
    }
    
    protected AbstractEventExecutor(final EventExecutorGroup parent) {
        this.selfCollection = (Collection<EventExecutor>)Collections.singleton(this);
        this.parent = parent;
    }
    
    public EventExecutorGroup parent() {
        return this.parent;
    }
    
    public EventExecutor next() {
        return this;
    }
    
    public boolean inEventLoop() {
        return this.inEventLoop(Thread.currentThread());
    }
    
    public Iterator<EventExecutor> iterator() {
        return (Iterator<EventExecutor>)this.selfCollection.iterator();
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
    
    public <V> Promise<V> newPromise() {
        return new DefaultPromise<V>(this);
    }
    
    public <V> ProgressivePromise<V> newProgressivePromise() {
        return new DefaultProgressivePromise<V>(this);
    }
    
    public <V> Future<V> newSucceededFuture(final V result) {
        return new SucceededFuture<V>(this, result);
    }
    
    public <V> Future<V> newFailedFuture(final Throwable cause) {
        return new FailedFuture<V>(this, cause);
    }
    
    public Future<?> submit(final Runnable task) {
        return super.submit(task);
    }
    
    public <T> Future<T> submit(final Runnable task, final T result) {
        return (Future<T>)super.submit(task, result);
    }
    
    public <T> Future<T> submit(final Callable<T> task) {
        return (Future<T>)super.submit((Callable)task);
    }
    
    protected final <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return (RunnableFuture<T>)new PromiseTask(this, runnable, value);
    }
    
    protected final <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return (RunnableFuture<T>)new PromiseTask(this, (java.util.concurrent.Callable<Object>)callable);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    protected static void safeExecute(final Runnable task) {
        try {
            task.run();
        }
        catch (Throwable t) {
            AbstractEventExecutor.logger.warn("A task raised an exception. Task: {}", task, t);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(AbstractEventExecutor.class);
    }
}
