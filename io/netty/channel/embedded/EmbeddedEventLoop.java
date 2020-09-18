package io.netty.channel.embedded;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import io.netty.channel.EventLoopGroup;
import java.util.ArrayDeque;
import java.util.Queue;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.AbstractScheduledEventExecutor;

final class EmbeddedEventLoop extends AbstractScheduledEventExecutor implements EventLoop {
    private final Queue<Runnable> tasks;
    
    EmbeddedEventLoop() {
        this.tasks = (Queue<Runnable>)new ArrayDeque(2);
    }
    
    @Override
    public EventLoopGroup parent() {
        return (EventLoopGroup)super.parent();
    }
    
    @Override
    public EventLoop next() {
        return (EventLoop)super.next();
    }
    
    public void execute(final Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        this.tasks.add(command);
    }
    
    void runTasks() {
        while (true) {
            final Runnable task = (Runnable)this.tasks.poll();
            if (task == null) {
                break;
            }
            task.run();
        }
    }
    
    long runScheduledTasks() {
        final long time = AbstractScheduledEventExecutor.nanoTime();
        while (true) {
            final Runnable task = this.pollScheduledTask(time);
            if (task == null) {
                break;
            }
            task.run();
        }
        return this.nextScheduledTaskNano();
    }
    
    long nextScheduledTask() {
        return this.nextScheduledTaskNano();
    }
    
    @Override
    protected void cancelScheduledTasks() {
        super.cancelScheduledTasks();
    }
    
    public Future<?> shutdownGracefully(final long quietPeriod, final long timeout, final TimeUnit unit) {
        throw new UnsupportedOperationException();
    }
    
    public Future<?> terminationFuture() {
        throw new UnsupportedOperationException();
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
        channel.unsafe().register(this, promise);
        return promise;
    }
    
    @Override
    public boolean inEventLoop() {
        return true;
    }
    
    public boolean inEventLoop(final Thread thread) {
        return true;
    }
}
