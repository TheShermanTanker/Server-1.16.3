package com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutorService;

@CanIgnoreReturnValue
@GwtIncompatible
public interface ListeningExecutorService extends ExecutorService {
     <T> ListenableFuture<T> submit(final Callable<T> callable);
    
    ListenableFuture<?> submit(final Runnable runnable);
    
     <T> ListenableFuture<T> submit(final Runnable runnable, final T object);
    
     <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> collection) throws InterruptedException;
    
     <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> collection, final long long2, final TimeUnit timeUnit) throws InterruptedException;
}
