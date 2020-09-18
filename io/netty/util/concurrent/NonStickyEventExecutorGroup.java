package io.netty.util.concurrent;

import java.util.concurrent.RejectedExecutionException;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Queue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import io.netty.util.internal.ObjectUtil;

public final class NonStickyEventExecutorGroup implements EventExecutorGroup {
    private final EventExecutorGroup group;
    private final int maxTaskExecutePerRun;
    
    public NonStickyEventExecutorGroup(final EventExecutorGroup group) {
        this(group, 1024);
    }
    
    public NonStickyEventExecutorGroup(final EventExecutorGroup group, final int maxTaskExecutePerRun) {
        this.group = verify(group);
        this.maxTaskExecutePerRun = ObjectUtil.checkPositive(maxTaskExecutePerRun, "maxTaskExecutePerRun");
    }
    
    private static EventExecutorGroup verify(final EventExecutorGroup group) {
        for (final EventExecutor executor : ObjectUtil.<EventExecutorGroup>checkNotNull(group, "group")) {
            if (executor instanceof OrderedEventExecutor) {
                throw new IllegalArgumentException(new StringBuilder().append("EventExecutorGroup ").append(group).append(" contains OrderedEventExecutors: ").append(executor).toString());
            }
        }
        return group;
    }
    
    private NonStickyOrderedEventExecutor newExecutor(final EventExecutor executor) {
        return new NonStickyOrderedEventExecutor(executor, this.maxTaskExecutePerRun);
    }
    
    public boolean isShuttingDown() {
        return this.group.isShuttingDown();
    }
    
    public Future<?> shutdownGracefully() {
        return this.group.shutdownGracefully();
    }
    
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.group.shutdownGracefully(quietPeriod, timeout, unit);
    }
    
    public Future<?> terminationFuture() {
        return this.group.terminationFuture();
    }
    
    public void shutdown() {
        this.group.shutdown();
    }
    
    public List<Runnable> shutdownNow() {
        return this.group.shutdownNow();
    }
    
    public EventExecutor next() {
        return this.newExecutor(this.group.next());
    }
    
    public Iterator<EventExecutor> iterator() {
        final Iterator<EventExecutor> itr = this.group.iterator();
        return (Iterator<EventExecutor>)new Iterator<EventExecutor>() {
            public boolean hasNext() {
                return itr.hasNext();
            }
            
            public EventExecutor next() {
                return NonStickyEventExecutorGroup.this.newExecutor((EventExecutor)itr.next());
            }
            
            public void remove() {
                itr.remove();
            }
        };
    }
    
    public Future<?> submit(final Runnable task) {
        return this.group.submit(task);
    }
    
    public <T> Future<T> submit(final Runnable task, final T result) {
        return this.group.<T>submit(task, result);
    }
    
    public <T> Future<T> submit(final Callable<T> task) {
        return this.group.<T>submit(task);
    }
    
    public ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.group.schedule(command, delay, unit);
    }
    
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
        return this.group.<V>schedule(callable, delay, unit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.group.scheduleAtFixedRate(command, initialDelay, period, unit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.group.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
    
    public boolean isShutdown() {
        return this.group.isShutdown();
    }
    
    public boolean isTerminated() {
        return this.group.isTerminated();
    }
    
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.group.awaitTermination(timeout, unit);
    }
    
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return (List<java.util.concurrent.Future<T>>)this.group.invokeAll((Collection)tasks);
    }
    
    public <T> List<java.util.concurrent.Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return (List<java.util.concurrent.Future<T>>)this.group.invokeAll((Collection)tasks, timeout, unit);
    }
    
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return (T)this.group.invokeAny((Collection)tasks);
    }
    
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T)this.group.invokeAny((Collection)tasks, timeout, unit);
    }
    
    public void execute(final Runnable command) {
        this.group.execute(command);
    }
    
    private static final class NonStickyOrderedEventExecutor extends AbstractEventExecutor implements Runnable, OrderedEventExecutor {
        private final EventExecutor executor;
        private final Queue<Runnable> tasks;
        private static final int NONE = 0;
        private static final int SUBMITTED = 1;
        private static final int RUNNING = 2;
        private final AtomicInteger state;
        private final int maxTaskExecutePerRun;
        
        NonStickyOrderedEventExecutor(final EventExecutor executor, final int maxTaskExecutePerRun) {
            super(executor);
            this.tasks = PlatformDependent.<Runnable>newMpscQueue();
            this.state = new AtomicInteger();
            this.executor = executor;
            this.maxTaskExecutePerRun = maxTaskExecutePerRun;
        }
        
        public void run() {
            if (!this.state.compareAndSet(1, 2)) {
                return;
            }
            while (true) {
                int i = 0;
                try {
                    while (i < this.maxTaskExecutePerRun) {
                        final Runnable task = (Runnable)this.tasks.poll();
                        if (task == null) {
                            break;
                        }
                        AbstractEventExecutor.safeExecute(task);
                        ++i;
                    }
                    if (i == this.maxTaskExecutePerRun) {
                        try {
                            this.state.set(1);
                            this.executor.execute((Runnable)this);
                            return;
                        }
                        catch (Throwable ignore) {
                            this.state.set(2);
                            continue;
                        }
                    }
                    this.state.set(0);
                }
                finally {
                    if (i == this.maxTaskExecutePerRun) {
                        try {
                            this.state.set(1);
                            this.executor.execute((Runnable)this);
                            return;
                        }
                        catch (Throwable ignore2) {
                            this.state.set(2);
                        }
                    }
                    this.state.set(0);
                }
            }
        }
        
        public boolean inEventLoop(final Thread thread) {
            return false;
        }
        
        @Override
        public boolean inEventLoop() {
            return false;
        }
        
        public boolean isShuttingDown() {
            return this.executor.isShutdown();
        }
        
        public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
            return this.executor.shutdownGracefully(quietPeriod, timeout, unit);
        }
        
        public Future<?> terminationFuture() {
            return this.executor.terminationFuture();
        }
        
        @Override
        public void shutdown() {
            this.executor.shutdown();
        }
        
        public boolean isShutdown() {
            return this.executor.isShutdown();
        }
        
        public boolean isTerminated() {
            return this.executor.isTerminated();
        }
        
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.executor.awaitTermination(timeout, unit);
        }
        
        public void execute(final Runnable command) {
            if (!this.tasks.offer(command)) {
                throw new RejectedExecutionException();
            }
            if (this.state.compareAndSet(0, 1)) {
                try {
                    this.executor.execute((Runnable)this);
                }
                catch (Throwable e) {
                    this.tasks.remove(command);
                    PlatformDependent.throwException(e);
                }
            }
        }
    }
}
