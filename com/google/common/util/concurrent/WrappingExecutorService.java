package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.concurrent.Future;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import com.google.common.base.Throwables;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutorService;

@CanIgnoreReturnValue
@GwtIncompatible
abstract class WrappingExecutorService implements ExecutorService {
    private final ExecutorService delegate;
    
    protected WrappingExecutorService(final ExecutorService delegate) {
        this.delegate = Preconditions.<ExecutorService>checkNotNull(delegate);
    }
    
    protected abstract <T> Callable<T> wrapTask(final Callable<T> callable);
    
    protected Runnable wrapTask(final Runnable command) {
        final Callable<Object> wrapped = this.wrapTask((java.util.concurrent.Callable<Object>)Executors.callable(command, null));
        return (Runnable)new Runnable() {
            public void run() {
                try {
                    wrapped.call();
                }
                catch (Exception e) {
                    Throwables.throwIfUnchecked((Throwable)e);
                    throw new RuntimeException((Throwable)e);
                }
            }
        };
    }
    
    private final <T> ImmutableList<Callable<T>> wrapTasks(final Collection<? extends Callable<T>> tasks) {
        final ImmutableList.Builder<Callable<T>> builder = ImmutableList.<Callable<T>>builder();
        for (final Callable<T> task : tasks) {
            builder.add(this.<T>wrapTask(task));
        }
        return builder.build();
    }
    
    public final void execute(final Runnable command) {
        this.delegate.execute(this.wrapTask(command));
    }
    
    public final <T> Future<T> submit(final Callable<T> task) {
        return (Future<T>)this.delegate.submit((Callable)this.<T>wrapTask(Preconditions.<Callable<T>>checkNotNull(task)));
    }
    
    public final Future<?> submit(final Runnable task) {
        return this.delegate.submit(this.wrapTask(task));
    }
    
    public final <T> Future<T> submit(final Runnable task, final T result) {
        return (Future<T>)this.delegate.submit(this.wrapTask(task), result);
    }
    
    public final <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return (List<Future<T>>)this.delegate.invokeAll((Collection)this.wrapTasks((java.util.Collection<? extends java.util.concurrent.Callable<Object>>)tasks));
    }
    
    public final <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
        return (List<Future<T>>)this.delegate.invokeAll((Collection)this.wrapTasks((java.util.Collection<? extends java.util.concurrent.Callable<Object>>)tasks), timeout, unit);
    }
    
    public final <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return (T)this.delegate.invokeAny((Collection)this.wrapTasks((java.util.Collection<? extends java.util.concurrent.Callable<Object>>)tasks));
    }
    
    public final <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T)this.delegate.invokeAny((Collection)this.wrapTasks((java.util.Collection<? extends java.util.concurrent.Callable<Object>>)tasks), timeout, unit);
    }
    
    public final void shutdown() {
        this.delegate.shutdown();
    }
    
    public final List<Runnable> shutdownNow() {
        return (List<Runnable>)this.delegate.shutdownNow();
    }
    
    public final boolean isShutdown() {
        return this.delegate.isShutdown();
    }
    
    public final boolean isTerminated() {
        return this.delegate.isTerminated();
    }
    
    public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate.awaitTermination(timeout, unit);
    }
}
