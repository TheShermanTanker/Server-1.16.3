package com.google.common.util.concurrent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import java.util.concurrent.ScheduledExecutorService;

@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public interface ListeningScheduledExecutorService extends ScheduledExecutorService, ListeningExecutorService {
    ListenableScheduledFuture<?> schedule(final Runnable runnable, final long long2, final TimeUnit timeUnit);
    
     <V> ListenableScheduledFuture<V> schedule(final Callable<V> callable, final long long2, final TimeUnit timeUnit);
    
    ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable runnable, final long long2, final long long3, final TimeUnit timeUnit);
    
    ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable runnable, final long long2, final long long3, final TimeUnit timeUnit);
}
