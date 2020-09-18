package io.netty.util.concurrent;

import java.util.Queue;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import io.netty.util.internal.logging.InternalLogger;

public final class GlobalEventExecutor extends AbstractScheduledEventExecutor {
    private static final InternalLogger logger;
    private static final long SCHEDULE_QUIET_PERIOD_INTERVAL;
    public static final GlobalEventExecutor INSTANCE;
    final BlockingQueue<Runnable> taskQueue;
    final ScheduledFutureTask<Void> quietPeriodTask;
    final ThreadFactory threadFactory;
    private final TaskRunner taskRunner;
    private final AtomicBoolean started;
    volatile Thread thread;
    private final Future<?> terminationFuture;
    
    private GlobalEventExecutor() {
        this.taskQueue = (BlockingQueue<Runnable>)new LinkedBlockingQueue();
        this.quietPeriodTask = new ScheduledFutureTask<Void>(this, (java.util.concurrent.Callable<Void>)Executors.callable((Runnable)new Runnable() {
            public void run() {
            }
        }, null), ScheduledFutureTask.deadlineNanos(GlobalEventExecutor.SCHEDULE_QUIET_PERIOD_INTERVAL), -GlobalEventExecutor.SCHEDULE_QUIET_PERIOD_INTERVAL);
        this.threadFactory = (ThreadFactory)new DefaultThreadFactory(DefaultThreadFactory.toPoolName(this.getClass()), false, 5, null);
        this.taskRunner = new TaskRunner();
        this.started = new AtomicBoolean();
        this.terminationFuture = new FailedFuture<>(this, (Throwable)new UnsupportedOperationException());
        this.scheduledTaskQueue().add(this.quietPeriodTask);
    }
    
    Runnable takeTask() {
        final BlockingQueue<Runnable> taskQueue = this.taskQueue;
        while (true) {
            final ScheduledFutureTask<?> scheduledTask = this.peekScheduledTask();
            if (scheduledTask == null) {
                Runnable task = null;
                try {
                    task = (Runnable)taskQueue.take();
                }
                catch (InterruptedException ex) {}
                return task;
            }
            final long delayNanos = scheduledTask.delayNanos();
            Runnable task2 = null;
            Label_0077: {
                if (delayNanos > 0L) {
                    try {
                        task2 = (Runnable)taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                        break Label_0077;
                    }
                    catch (InterruptedException e) {
                        return null;
                    }
                }
                task2 = (Runnable)taskQueue.poll();
            }
            if (task2 == null) {
                this.fetchFromScheduledTaskQueue();
                task2 = (Runnable)taskQueue.poll();
            }
            if (task2 != null) {
                return task2;
            }
        }
    }
    
    private void fetchFromScheduledTaskQueue() {
        final long nanoTime = AbstractScheduledEventExecutor.nanoTime();
        for (Runnable scheduledTask = this.pollScheduledTask(nanoTime); scheduledTask != null; scheduledTask = this.pollScheduledTask(nanoTime)) {
            this.taskQueue.add(scheduledTask);
        }
    }
    
    public int pendingTasks() {
        return this.taskQueue.size();
    }
    
    private void addTask(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.taskQueue.add(task);
    }
    
    public boolean inEventLoop(final Thread thread) {
        return thread == this.thread;
    }
    
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        return this.terminationFuture();
    }
    
    public Future<?> terminationFuture() {
        return this.terminationFuture;
    }
    
    @Deprecated
    @Override
    public void shutdown() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isShuttingDown() {
        return false;
    }
    
    public boolean isShutdown() {
        return false;
    }
    
    public boolean isTerminated() {
        return false;
    }
    
    public boolean awaitTermination(final long timeout, final TimeUnit unit) {
        return false;
    }
    
    public boolean awaitInactivity(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        final Thread thread = this.thread;
        if (thread == null) {
            throw new IllegalStateException("thread was not started");
        }
        thread.join(unit.toMillis(timeout));
        return !thread.isAlive();
    }
    
    public void execute(final Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        this.addTask(task);
        if (!this.inEventLoop()) {
            this.startThread();
        }
    }
    
    private void startThread() {
        if (this.started.compareAndSet(false, true)) {
            final Thread t = this.threadFactory.newThread((Runnable)this.taskRunner);
            AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Void>() {
                public Void run() {
                    t.setContextClassLoader((ClassLoader)null);
                    return null;
                }
            });
            (this.thread = t).start();
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
        SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
        INSTANCE = new GlobalEventExecutor();
    }
    
    final class TaskRunner implements Runnable {
        public void run() {
            while (true) {
                final Runnable task = GlobalEventExecutor.this.takeTask();
                if (task != null) {
                    try {
                        task.run();
                    }
                    catch (Throwable t) {
                        GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
                    }
                    if (task != GlobalEventExecutor.this.quietPeriodTask) {
                        continue;
                    }
                }
                final Queue<ScheduledFutureTask<?>> scheduledTaskQueue = (Queue<ScheduledFutureTask<?>>)GlobalEventExecutor.this.scheduledTaskQueue;
                if (GlobalEventExecutor.this.taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1)) {
                    final boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
                    assert stopped;
                    if (GlobalEventExecutor.this.taskQueue.isEmpty()) {
                        if (scheduledTaskQueue == null) {
                            break;
                        }
                        if (scheduledTaskQueue.size() == 1) {
                            break;
                        }
                    }
                    if (!GlobalEventExecutor.this.started.compareAndSet(false, true)) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
}
