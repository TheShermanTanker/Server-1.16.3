package io.netty.util.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import io.netty.util.internal.DefaultPriorityQueue;
import io.netty.util.internal.PriorityQueue;
import java.util.Comparator;

public abstract class AbstractScheduledEventExecutor extends AbstractEventExecutor {
    private static final Comparator<ScheduledFutureTask<?>> SCHEDULED_FUTURE_TASK_COMPARATOR;
    PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue;
    
    protected AbstractScheduledEventExecutor() {
    }
    
    protected AbstractScheduledEventExecutor(final EventExecutorGroup parent) {
        super(parent);
    }
    
    protected static long nanoTime() {
        return ScheduledFutureTask.nanoTime();
    }
    
    PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue() {
        if (this.scheduledTaskQueue == null) {
            this.scheduledTaskQueue = new DefaultPriorityQueue<ScheduledFutureTask<?>>(AbstractScheduledEventExecutor.SCHEDULED_FUTURE_TASK_COMPARATOR, 11);
        }
        return this.scheduledTaskQueue;
    }
    
    private static boolean isNullOrEmpty(final Queue<ScheduledFutureTask<?>> queue) {
        return queue == null || queue.isEmpty();
    }
    
    protected void cancelScheduledTasks() {
        assert this.inEventLoop();
        final PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
        if (isNullOrEmpty((Queue<ScheduledFutureTask<?>>)scheduledTaskQueue)) {
            return;
        }
        final ScheduledFutureTask[] array;
        final ScheduledFutureTask<?>[] scheduledTasks = (array = (ScheduledFutureTask[])scheduledTaskQueue.toArray((Object[])new ScheduledFutureTask[scheduledTaskQueue.size()]));
        for (final ScheduledFutureTask<?> task : array) {
            task.cancelWithoutRemove(false);
        }
        scheduledTaskQueue.clearIgnoringIndexes();
    }
    
    protected final Runnable pollScheduledTask() {
        return this.pollScheduledTask(nanoTime());
    }
    
    protected final Runnable pollScheduledTask(final long nanoTime) {
        assert this.inEventLoop();
        final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)this.scheduledTaskQueue;
        final ScheduledFutureTask<?> scheduledTask = ((scheduledTaskQueue == null) ? null : ((ScheduledFutureTask)scheduledTaskQueue.peek()));
        if (scheduledTask == null) {
            return null;
        }
        if (scheduledTask.deadlineNanos() <= nanoTime) {
            scheduledTaskQueue.remove();
            return (Runnable)scheduledTask;
        }
        return null;
    }
    
    protected final long nextScheduledTaskNano() {
        final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)this.scheduledTaskQueue;
        final ScheduledFutureTask<?> scheduledTask = ((scheduledTaskQueue == null) ? null : ((ScheduledFutureTask)scheduledTaskQueue.peek()));
        if (scheduledTask == null) {
            return -1L;
        }
        return Math.max(0L, scheduledTask.deadlineNanos() - nanoTime());
    }
    
    final ScheduledFutureTask<?> peekScheduledTask() {
        final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)this.scheduledTaskQueue;
        if (scheduledTaskQueue == null) {
            return null;
        }
        return scheduledTaskQueue.peek();
    }
    
    protected final boolean hasScheduledTasks() {
        final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)this.scheduledTaskQueue;
        final ScheduledFutureTask<?> scheduledTask = ((scheduledTaskQueue == null) ? null : ((ScheduledFutureTask)scheduledTaskQueue.peek()));
        return scheduledTask != null && scheduledTask.deadlineNanos() <= nanoTime();
    }
    
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, long delay, final TimeUnit unit) {
        ObjectUtil.<Runnable>checkNotNull(command, "command");
        ObjectUtil.<TimeUnit>checkNotNull(unit, "unit");
        if (delay < 0L) {
            delay = 0L;
        }
        this.validateScheduled(delay, unit);
        return this.schedule(new ScheduledFutureTask<>(this, command, null, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }
    
    @Override
    public <V> ScheduledFuture<V> schedule(final Callable<V> callable, long delay, final TimeUnit unit) {
        ObjectUtil.<Callable<V>>checkNotNull(callable, "callable");
        ObjectUtil.<TimeUnit>checkNotNull(unit, "unit");
        if (delay < 0L) {
            delay = 0L;
        }
        this.validateScheduled(delay, unit);
        return this.<V>schedule(new ScheduledFutureTask<V>(this, callable, ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
    }
    
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        ObjectUtil.<Runnable>checkNotNull(command, "command");
        ObjectUtil.<TimeUnit>checkNotNull(unit, "unit");
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { initialDelay }));
        }
        if (period <= 0L) {
            throw new IllegalArgumentException(String.format("period: %d (expected: > 0)", new Object[] { period }));
        }
        this.validateScheduled(initialDelay, unit);
        this.validateScheduled(period, unit);
        return this.schedule(new ScheduledFutureTask<>(this, (java.util.concurrent.Callable<?>)Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
    }
    
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        ObjectUtil.<Runnable>checkNotNull(command, "command");
        ObjectUtil.<TimeUnit>checkNotNull(unit, "unit");
        if (initialDelay < 0L) {
            throw new IllegalArgumentException(String.format("initialDelay: %d (expected: >= 0)", new Object[] { initialDelay }));
        }
        if (delay <= 0L) {
            throw new IllegalArgumentException(String.format("delay: %d (expected: > 0)", new Object[] { delay }));
        }
        this.validateScheduled(initialDelay, unit);
        this.validateScheduled(delay, unit);
        return this.schedule(new ScheduledFutureTask<>(this, (java.util.concurrent.Callable<?>)Executors.callable(command, null), ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
    }
    
    protected void validateScheduled(final long amount, final TimeUnit unit) {
    }
    
     <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task) {
        if (this.inEventLoop()) {
            this.scheduledTaskQueue().add(task);
        }
        else {
            this.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractScheduledEventExecutor.this.scheduledTaskQueue().add(task);
                }
            });
        }
        return task;
    }
    
    final void removeScheduled(final ScheduledFutureTask<?> task) {
        if (this.inEventLoop()) {
            this.scheduledTaskQueue().removeTyped(task);
        }
        else {
            this.execute((Runnable)new Runnable() {
                public void run() {
                    AbstractScheduledEventExecutor.this.removeScheduled(task);
                }
            });
        }
    }
    
    static {
        SCHEDULED_FUTURE_TASK_COMPARATOR = (Comparator)new Comparator<ScheduledFutureTask<?>>() {
            public int compare(final ScheduledFutureTask<?> o1, final ScheduledFutureTask<?> o2) {
                return o1.compareTo((Delayed)o2);
            }
        };
    }
}
