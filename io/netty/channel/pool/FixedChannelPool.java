package io.netty.channel.pool;

import io.netty.util.internal.ThrowableUtil;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.TimeUnit;
import java.util.ArrayDeque;
import io.netty.bootstrap.Bootstrap;
import java.util.Queue;
import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.TimeoutException;

public class FixedChannelPool extends SimpleChannelPool {
    private static final IllegalStateException FULL_EXCEPTION;
    private static final TimeoutException TIMEOUT_EXCEPTION;
    static final IllegalStateException POOL_CLOSED_ON_RELEASE_EXCEPTION;
    static final IllegalStateException POOL_CLOSED_ON_ACQUIRE_EXCEPTION;
    private final EventExecutor executor;
    private final long acquireTimeoutNanos;
    private final Runnable timeoutTask;
    private final Queue<AcquireTask> pendingAcquireQueue;
    private final int maxConnections;
    private final int maxPendingAcquires;
    private int acquiredChannelCount;
    private int pendingAcquireCount;
    private boolean closed;
    
    public FixedChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final int maxConnections) {
        this(bootstrap, handler, maxConnections, Integer.MAX_VALUE);
    }
    
    public FixedChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final int maxConnections, final int maxPendingAcquires) {
        this(bootstrap, handler, ChannelHealthChecker.ACTIVE, null, -1L, maxConnections, maxPendingAcquires);
    }
    
    public FixedChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck, final AcquireTimeoutAction action, final long acquireTimeoutMillis, final int maxConnections, final int maxPendingAcquires) {
        this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, true);
    }
    
    public FixedChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck, final AcquireTimeoutAction action, final long acquireTimeoutMillis, final int maxConnections, final int maxPendingAcquires, final boolean releaseHealthCheck) {
        this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, releaseHealthCheck, true);
    }
    
    public FixedChannelPool(final Bootstrap bootstrap, final ChannelPoolHandler handler, final ChannelHealthChecker healthCheck, final AcquireTimeoutAction action, final long acquireTimeoutMillis, final int maxConnections, final int maxPendingAcquires, final boolean releaseHealthCheck, final boolean lastRecentUsed) {
        super(bootstrap, handler, healthCheck, releaseHealthCheck, lastRecentUsed);
        this.pendingAcquireQueue = (Queue<AcquireTask>)new ArrayDeque();
        if (maxConnections < 1) {
            throw new IllegalArgumentException(new StringBuilder().append("maxConnections: ").append(maxConnections).append(" (expected: >= 1)").toString());
        }
        if (maxPendingAcquires < 1) {
            throw new IllegalArgumentException(new StringBuilder().append("maxPendingAcquires: ").append(maxPendingAcquires).append(" (expected: >= 1)").toString());
        }
        if (action == null && acquireTimeoutMillis == -1L) {
            this.timeoutTask = null;
            this.acquireTimeoutNanos = -1L;
        }
        else {
            if (action == null && acquireTimeoutMillis != -1L) {
                throw new NullPointerException("action");
            }
            if (action != null && acquireTimeoutMillis < 0L) {
                throw new IllegalArgumentException(new StringBuilder().append("acquireTimeoutMillis: ").append(acquireTimeoutMillis).append(" (expected: >= 0)").toString());
            }
            this.acquireTimeoutNanos = TimeUnit.MILLISECONDS.toNanos(acquireTimeoutMillis);
            switch (action) {
                case FAIL: {
                    this.timeoutTask = (Runnable)new TimeoutTask() {
                        @Override
                        public void onTimeout(final AcquireTask task) {
                            task.promise.setFailure((Throwable)FixedChannelPool.TIMEOUT_EXCEPTION);
                        }
                    };
                    break;
                }
                case NEW: {
                    this.timeoutTask = (Runnable)new TimeoutTask() {
                        @Override
                        public void onTimeout(final AcquireTask task) {
                            task.acquired();
                            SimpleChannelPool.this.acquire(task.promise);
                        }
                    };
                    break;
                }
                default: {
                    throw new Error();
                }
            }
        }
        this.executor = bootstrap.config().group().next();
        this.maxConnections = maxConnections;
        this.maxPendingAcquires = maxPendingAcquires;
    }
    
    @Override
    public Future<Channel> acquire(final Promise<Channel> promise) {
        try {
            if (this.executor.inEventLoop()) {
                this.acquire0(promise);
            }
            else {
                this.executor.execute((Runnable)new Runnable() {
                    public void run() {
                        FixedChannelPool.this.acquire0(promise);
                    }
                });
            }
        }
        catch (Throwable cause) {
            promise.setFailure(cause);
        }
        return promise;
    }
    
    private void acquire0(final Promise<Channel> promise) {
        assert this.executor.inEventLoop();
        if (this.closed) {
            promise.setFailure((Throwable)FixedChannelPool.POOL_CLOSED_ON_ACQUIRE_EXCEPTION);
            return;
        }
        if (this.acquiredChannelCount < this.maxConnections) {
            assert this.acquiredChannelCount >= 0;
            final Promise<Channel> p = this.executor.<Channel>newPromise();
            final AcquireListener l = new AcquireListener(promise);
            l.acquired();
            p.addListener(l);
            super.acquire(p);
        }
        else {
            if (this.pendingAcquireCount >= this.maxPendingAcquires) {
                promise.setFailure((Throwable)FixedChannelPool.FULL_EXCEPTION);
            }
            else {
                final AcquireTask task = new AcquireTask(promise);
                if (this.pendingAcquireQueue.offer(task)) {
                    ++this.pendingAcquireCount;
                    if (this.timeoutTask != null) {
                        task.timeoutFuture = this.executor.schedule(this.timeoutTask, this.acquireTimeoutNanos, TimeUnit.NANOSECONDS);
                    }
                }
                else {
                    promise.setFailure((Throwable)FixedChannelPool.FULL_EXCEPTION);
                }
            }
            assert this.pendingAcquireCount > 0;
        }
    }
    
    @Override
    public Future<Void> release(final Channel channel, final Promise<Void> promise) {
        ObjectUtil.<Promise<Void>>checkNotNull(promise, "promise");
        final Promise<Void> p = this.executor.<Void>newPromise();
        super.release(channel, p.addListener(new FutureListener<Void>() {
            public void operationComplete(final Future<Void> future) throws Exception {
                assert FixedChannelPool.this.executor.inEventLoop();
                if (FixedChannelPool.this.closed) {
                    channel.close();
                    promise.setFailure((Throwable)FixedChannelPool.POOL_CLOSED_ON_RELEASE_EXCEPTION);
                    return;
                }
                if (future.isSuccess()) {
                    FixedChannelPool.this.decrementAndRunTaskQueue();
                    promise.setSuccess(null);
                }
                else {
                    final Throwable cause = future.cause();
                    if (!(cause instanceof IllegalArgumentException)) {
                        FixedChannelPool.this.decrementAndRunTaskQueue();
                    }
                    promise.setFailure(future.cause());
                }
            }
        }));
        return promise;
    }
    
    private void decrementAndRunTaskQueue() {
        --this.acquiredChannelCount;
        assert this.acquiredChannelCount >= 0;
        this.runTaskQueue();
    }
    
    private void runTaskQueue() {
        while (this.acquiredChannelCount < this.maxConnections) {
            final AcquireTask task = (AcquireTask)this.pendingAcquireQueue.poll();
            if (task == null) {
                break;
            }
            final ScheduledFuture<?> timeoutFuture = task.timeoutFuture;
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
            }
            --this.pendingAcquireCount;
            task.acquired();
            super.acquire(task.promise);
        }
        assert this.pendingAcquireCount >= 0;
        assert this.acquiredChannelCount >= 0;
    }
    
    @Override
    public void close() {
        this.executor.execute((Runnable)new Runnable() {
            public void run() {
                if (!FixedChannelPool.this.closed) {
                    FixedChannelPool.this.closed = true;
                    while (true) {
                        final AcquireTask task = (AcquireTask)FixedChannelPool.this.pendingAcquireQueue.poll();
                        if (task == null) {
                            break;
                        }
                        final ScheduledFuture<?> f = task.timeoutFuture;
                        if (f != null) {
                            f.cancel(false);
                        }
                        task.promise.setFailure((Throwable)new ClosedChannelException());
                    }
                    FixedChannelPool.this.acquiredChannelCount = 0;
                    FixedChannelPool.this.pendingAcquireCount = 0;
                    SimpleChannelPool.this.close();
                }
            }
        });
    }
    
    static {
        FULL_EXCEPTION = ThrowableUtil.<IllegalStateException>unknownStackTrace(new IllegalStateException("Too many outstanding acquire operations"), FixedChannelPool.class, "acquire0(...)");
        TIMEOUT_EXCEPTION = ThrowableUtil.<TimeoutException>unknownStackTrace(new TimeoutException("Acquire operation took longer then configured maximum time"), FixedChannelPool.class, "<init>(...)");
        POOL_CLOSED_ON_RELEASE_EXCEPTION = ThrowableUtil.<IllegalStateException>unknownStackTrace(new IllegalStateException("FixedChannelPool was closed"), FixedChannelPool.class, "release(...)");
        POOL_CLOSED_ON_ACQUIRE_EXCEPTION = ThrowableUtil.<IllegalStateException>unknownStackTrace(new IllegalStateException("FixedChannelPool was closed"), FixedChannelPool.class, "acquire0(...)");
    }
    
    public enum AcquireTimeoutAction {
        NEW, 
        FAIL;
    }
    
    private final class AcquireTask extends AcquireListener {
        final Promise<Channel> promise;
        final long expireNanoTime;
        ScheduledFuture<?> timeoutFuture;
        
        public AcquireTask(final Promise<Channel> promise) {
            super(promise);
            this.expireNanoTime = System.nanoTime() + FixedChannelPool.this.acquireTimeoutNanos;
            this.promise = FixedChannelPool.this.executor.<Channel>newPromise().addListener(this);
        }
    }
    
    private abstract class TimeoutTask implements Runnable {
        public final void run() {
            assert FixedChannelPool.this.executor.inEventLoop();
            final long nanoTime = System.nanoTime();
            while (true) {
                final AcquireTask task = (AcquireTask)FixedChannelPool.this.pendingAcquireQueue.peek();
                if (task == null || nanoTime - task.expireNanoTime < 0L) {
                    break;
                }
                FixedChannelPool.this.pendingAcquireQueue.remove();
                --FixedChannelPool.this.pendingAcquireCount;
                this.onTimeout(task);
            }
        }
        
        public abstract void onTimeout(final AcquireTask acquireTask);
    }
    
    private class AcquireListener implements FutureListener<Channel> {
        private final Promise<Channel> originalPromise;
        protected boolean acquired;
        
        AcquireListener(final Promise<Channel> originalPromise) {
            this.originalPromise = originalPromise;
        }
        
        public void operationComplete(final Future<Channel> future) throws Exception {
            assert FixedChannelPool.this.executor.inEventLoop();
            if (FixedChannelPool.this.closed) {
                if (future.isSuccess()) {
                    future.getNow().close();
                }
                this.originalPromise.setFailure((Throwable)FixedChannelPool.POOL_CLOSED_ON_ACQUIRE_EXCEPTION);
                return;
            }
            if (future.isSuccess()) {
                this.originalPromise.setSuccess(future.getNow());
            }
            else {
                if (this.acquired) {
                    FixedChannelPool.this.decrementAndRunTaskQueue();
                }
                else {
                    FixedChannelPool.this.runTaskQueue();
                }
                this.originalPromise.setFailure(future.cause());
            }
        }
        
        public void acquired() {
            if (this.acquired) {
                return;
            }
            FixedChannelPool.this.acquiredChannelCount++;
            this.acquired = true;
        }
    }
}
