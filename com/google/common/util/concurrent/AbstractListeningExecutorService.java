package com.google.common.util.concurrent;

import java.util.concurrent.Future;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import java.util.concurrent.AbstractExecutorService;

@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public abstract class AbstractListeningExecutorService extends AbstractExecutorService implements ListeningExecutorService {
    protected final <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return TrustedListenableFutureTask.create(runnable, value);
    }
    
    protected final <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return TrustedListenableFutureTask.create(callable);
    }
    
    public ListenableFuture<?> submit(final Runnable task) {
        return super.submit(task);
    }
    
    public <T> ListenableFuture<T> submit(final Runnable task, @Nullable final T result) {
        return (ListenableFuture<T>)super.submit(task, result);
    }
    
    public <T> ListenableFuture<T> submit(final Callable<T> task) {
        return (ListenableFuture<T>)super.submit((Callable)task);
    }
}
