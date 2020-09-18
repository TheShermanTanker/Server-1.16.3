package io.netty.util.concurrent;

import java.util.concurrent.Delayed;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.Collections;
import java.util.concurrent.ThreadFactory;
import java.util.Set;
import io.netty.util.internal.logging.InternalLogger;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class UnorderedThreadPoolEventExecutor extends ScheduledThreadPoolExecutor implements EventExecutor {
    private static final InternalLogger logger;
    private final Promise<?> terminationFuture;
    private final Set<EventExecutor> executorSet;
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize) {
        this(corePoolSize, (ThreadFactory)new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class));
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = (Set<EventExecutor>)Collections.singleton(this);
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final RejectedExecutionHandler handler) {
        this(corePoolSize, (ThreadFactory)new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class), handler);
    }
    
    public UnorderedThreadPoolEventExecutor(final int corePoolSize, final ThreadFactory threadFactory, final RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
        this.terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
        this.executorSet = (Set<EventExecutor>)Collections.singleton(this);
    }
    
    public EventExecutor next() {
        return this;
    }
    
    public EventExecutorGroup parent() {
        return this;
    }
    
    public boolean inEventLoop() {
        return false;
    }
    
    public boolean inEventLoop(final Thread thread) {
        return false;
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
    
    public boolean isShuttingDown() {
        return this.isShutdown();
    }
    
    public List<Runnable> shutdownNow() {
        final List<Runnable> tasks = (List<Runnable>)super.shutdownNow();
        this.terminationFuture.trySuccess(null);
        return tasks;
    }
    
    public void shutdown() {
        super.shutdown();
        this.terminationFuture.trySuccess(null);
    }
    
    public Future<?> shutdownGracefully() {
        return this.shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
    }
    
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        this.shutdown();
        return this.terminationFuture();
    }
    
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    public Iterator<EventExecutor> iterator() {
        return (Iterator<EventExecutor>)this.executorSet.iterator();
    }
    
    protected <V> RunnableScheduledFuture<V> decorateTask(final Runnable runnable, final RunnableScheduledFuture<V> task) {
        return (RunnableScheduledFuture<V>)((runnable instanceof NonNotifyRunnable) ? task : new RunnableScheduledFutureTask(this, runnable, (java.util.concurrent.RunnableScheduledFuture<Object>)task));
    }
    
    protected <V> RunnableScheduledFuture<V> decorateTask(final Callable<V> callable, final RunnableScheduledFuture<V> task) {
        return (RunnableScheduledFuture<V>)new RunnableScheduledFutureTask(this, (java.util.concurrent.Callable<Object>)callable, (java.util.concurrent.RunnableScheduledFuture<Object>)task);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return super.schedule(command, delay, unit);
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return (ScheduledFuture<V>)super.schedule((Callable)callable, delay, unit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return super.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
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
    
    public void execute(final Runnable command) {
        super.schedule((Runnable)new NonNotifyRunnable(command), 0L, TimeUnit.NANOSECONDS);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(UnorderedThreadPoolEventExecutor.class);
    }
    
    private static final class RunnableScheduledFutureTask<V> extends PromiseTask<V> implements RunnableScheduledFuture<V>, ScheduledFuture<V> {
        private final RunnableScheduledFuture<V> future;
        
        RunnableScheduledFutureTask(final EventExecutor executor, final Runnable runnable, final RunnableScheduledFuture<V> future) {
            super(executor, runnable, null);
            this.future = future;
        }
        
        RunnableScheduledFutureTask(final EventExecutor executor, final Callable<V> callable, final RunnableScheduledFuture<V> future) {
            super(executor, callable);
            this.future = future;
        }
        
        @Override
        public void run() {
            if (!this.isPeriodic()) {
                super.run();
            }
            else if (!this.isDone()) {
                try {
                    this.task.call();
                }
                catch (Throwable cause) {
                    if (!this.tryFailureInternal(cause)) {
                        UnorderedThreadPoolEventExecutor.logger.warn("Failure during execution of task", cause);
                    }
                }
            }
        }
        
        public boolean isPeriodic() {
            return this.future.isPeriodic();
        }
        
        public long getDelay(final TimeUnit unit) {
            return this.future.getDelay(unit);
        }
        
        public int compareTo(final Delayed o) {
            return this.future.compareTo(o);
        }
    }
    
    private static final class NonNotifyRunnable implements Runnable {
        private final Runnable task;
        
        NonNotifyRunnable(final Runnable task) {
            this.task = task;
        }
        
        public void run() {
            this.task.run();
        }
    }
}
