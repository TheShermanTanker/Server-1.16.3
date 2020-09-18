package io.netty.util.concurrent;

import io.netty.util.internal.DefaultPriorityQueue;
import java.util.Queue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.util.internal.PriorityQueueNode;

final class ScheduledFutureTask<V> extends PromiseTask<V> implements ScheduledFuture<V>, PriorityQueueNode {
    private static final AtomicLong nextTaskId;
    private static final long START_TIME;
    private final long id;
    private long deadlineNanos;
    private final long periodNanos;
    private int queueIndex;
    
    static long nanoTime() {
        return System.nanoTime() - ScheduledFutureTask.START_TIME;
    }
    
    static long deadlineNanos(final long delay) {
        return nanoTime() + delay;
    }
    
    ScheduledFutureTask(final AbstractScheduledEventExecutor executor, final Runnable runnable, final V result, final long nanoTime) {
        this(executor, PromiseTask.<V>toCallable(runnable, result), nanoTime);
    }
    
    ScheduledFutureTask(final AbstractScheduledEventExecutor executor, final Callable<V> callable, final long nanoTime, final long period) {
        super(executor, callable);
        this.id = ScheduledFutureTask.nextTaskId.getAndIncrement();
        this.queueIndex = -1;
        if (period == 0L) {
            throw new IllegalArgumentException("period: 0 (expected: != 0)");
        }
        this.deadlineNanos = nanoTime;
        this.periodNanos = period;
    }
    
    ScheduledFutureTask(final AbstractScheduledEventExecutor executor, final Callable<V> callable, final long nanoTime) {
        super(executor, callable);
        this.id = ScheduledFutureTask.nextTaskId.getAndIncrement();
        this.queueIndex = -1;
        this.deadlineNanos = nanoTime;
        this.periodNanos = 0L;
    }
    
    @Override
    protected EventExecutor executor() {
        return super.executor();
    }
    
    public long deadlineNanos() {
        return this.deadlineNanos;
    }
    
    public long delayNanos() {
        return Math.max(0L, this.deadlineNanos() - nanoTime());
    }
    
    public long delayNanos(final long currentTimeNanos) {
        return Math.max(0L, this.deadlineNanos() - (currentTimeNanos - ScheduledFutureTask.START_TIME));
    }
    
    public long getDelay(final TimeUnit unit) {
        return unit.convert(this.delayNanos(), TimeUnit.NANOSECONDS);
    }
    
    public int compareTo(final Delayed o) {
        if (this == o) {
            return 0;
        }
        final ScheduledFutureTask<?> that = o;
        final long d = this.deadlineNanos() - that.deadlineNanos();
        if (d < 0L) {
            return -1;
        }
        if (d > 0L) {
            return 1;
        }
        if (this.id < that.id) {
            return -1;
        }
        if (this.id == that.id) {
            throw new Error();
        }
        return 1;
    }
    
    @Override
    public void run() {
        assert this.executor().inEventLoop();
        try {
            if (this.periodNanos == 0L) {
                if (this.setUncancellableInternal()) {
                    final V result = (V)this.task.call();
                    this.setSuccessInternal(result);
                }
            }
            else if (!this.isCancelled()) {
                this.task.call();
                if (!this.executor().isShutdown()) {
                    final long p = this.periodNanos;
                    if (p > 0L) {
                        this.deadlineNanos += p;
                    }
                    else {
                        this.deadlineNanos = nanoTime() - p;
                    }
                    if (!this.isCancelled()) {
                        final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)((AbstractScheduledEventExecutor)this.executor()).scheduledTaskQueue;
                        assert scheduledTaskQueue != null;
                        scheduledTaskQueue.add(this);
                    }
                }
            }
        }
        catch (Throwable cause) {
            this.setFailureInternal(cause);
        }
    }
    
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        final boolean canceled = super.cancel(mayInterruptIfRunning);
        if (canceled) {
            ((AbstractScheduledEventExecutor)this.executor()).removeScheduled(this);
        }
        return canceled;
    }
    
    boolean cancelWithoutRemove(final boolean mayInterruptIfRunning) {
        return super.cancel(mayInterruptIfRunning);
    }
    
    @Override
    protected StringBuilder toStringBuilder() {
        final StringBuilder buf = super.toStringBuilder();
        buf.setCharAt(buf.length() - 1, ',');
        return buf.append(" id: ").append(this.id).append(", deadline: ").append(this.deadlineNanos).append(", period: ").append(this.periodNanos).append(')');
    }
    
    @Override
    public int priorityQueueIndex(final DefaultPriorityQueue<?> queue) {
        return this.queueIndex;
    }
    
    @Override
    public void priorityQueueIndex(final DefaultPriorityQueue<?> queue, final int i) {
        this.queueIndex = i;
    }
    
    static {
        nextTaskId = new AtomicLong();
        START_TIME = System.nanoTime();
    }
}
