package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ScheduledExecutorService;

@CanIgnoreReturnValue
@GwtIncompatible
abstract class WrappingScheduledExecutorService extends WrappingExecutorService implements ScheduledExecutorService {
    final ScheduledExecutorService delegate;
    
    protected WrappingScheduledExecutorService(final ScheduledExecutorService delegate) {
        super((ExecutorService)delegate);
        this.delegate = delegate;
    }
    
    public final ScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
        return this.delegate.schedule(this.wrapTask(command), delay, unit);
    }
    
    public final <V> ScheduledFuture<V> schedule(final Callable<V> task, final long delay, final TimeUnit unit) {
        return (ScheduledFuture<V>)this.delegate.schedule((Callable)this.<V>wrapTask(task), delay, unit);
    }
    
    public final ScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
        return this.delegate.scheduleAtFixedRate(this.wrapTask(command), initialDelay, period, unit);
    }
    
    public final ScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
        return this.delegate.scheduleWithFixedDelay(this.wrapTask(command), initialDelay, delay, unit);
    }
}
