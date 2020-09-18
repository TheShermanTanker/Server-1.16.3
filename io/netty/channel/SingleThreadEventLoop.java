package io.netty.channel;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import java.util.concurrent.ThreadFactory;
import java.util.Queue;
import io.netty.util.concurrent.SingleThreadEventExecutor;

public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor implements EventLoop {
    protected static final int DEFAULT_MAX_PENDING_TASKS;
    private final Queue<Runnable> tailTasks;
    
    protected SingleThreadEventLoop(final EventLoopGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp) {
        this(parent, threadFactory, addTaskWakesUp, SingleThreadEventLoop.DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
    }
    
    protected SingleThreadEventLoop(final EventLoopGroup parent, final Executor executor, final boolean addTaskWakesUp) {
        this(parent, executor, addTaskWakesUp, SingleThreadEventLoop.DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
    }
    
    protected SingleThreadEventLoop(final EventLoopGroup parent, final ThreadFactory threadFactory, final boolean addTaskWakesUp, final int maxPendingTasks, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, threadFactory, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
        this.tailTasks = this.newTaskQueue(maxPendingTasks);
    }
    
    protected SingleThreadEventLoop(final EventLoopGroup parent, final Executor executor, final boolean addTaskWakesUp, final int maxPendingTasks, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
        this.tailTasks = this.newTaskQueue(maxPendingTasks);
    }
    
    @Override
    public EventLoopGroup parent() {
        return (EventLoopGroup)super.parent();
    }
    
    public EventLoop next() {
        return (EventLoop)super.next();
    }
    
    public ChannelFuture register(final Channel channel) {
        return this.register(new DefaultChannelPromise(channel, this));
    }
    
    public ChannelFuture register(final ChannelPromise promise) {
        ObjectUtil.<ChannelPromise>checkNotNull(promise, "promise");
        promise.channel().unsafe().register(this, promise);
        return promise;
    }
    
    @Deprecated
    public ChannelFuture register(final Channel channel, final ChannelPromise promise) {
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (promise == null) {
            throw new NullPointerException("promise");
        }
        channel.unsafe().register(this, promise);
        return promise;
    }
    
    public final void executeAfterEventLoopIteration(final Runnable task) {
        ObjectUtil.<Runnable>checkNotNull(task, "task");
        if (this.isShutdown()) {
            reject();
        }
        if (!this.tailTasks.offer(task)) {
            this.reject(task);
        }
        if (this.wakesUpForTask(task)) {
            this.wakeup(this.inEventLoop());
        }
    }
    
    final boolean removeAfterEventLoopIterationTask(final Runnable task) {
        return this.tailTasks.remove(ObjectUtil.<Runnable>checkNotNull(task, "task"));
    }
    
    @Override
    protected boolean wakesUpForTask(final Runnable task) {
        return !(task instanceof NonWakeupRunnable);
    }
    
    @Override
    protected void afterRunningAllTasks() {
        this.runAllTasksFrom(this.tailTasks);
    }
    
    @Override
    protected boolean hasTasks() {
        return super.hasTasks() || !this.tailTasks.isEmpty();
    }
    
    @Override
    public int pendingTasks() {
        return super.pendingTasks() + this.tailTasks.size();
    }
    
    static {
        DEFAULT_MAX_PENDING_TASKS = Math.max(16, SystemPropertyUtil.getInt("io.netty.eventLoop.maxPendingTasks", Integer.MAX_VALUE));
    }
    
    interface NonWakeupRunnable extends Runnable {
    }
}
