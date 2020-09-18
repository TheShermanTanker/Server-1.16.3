package com.google.common.util.concurrent;

import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.FutureTask;

@GwtIncompatible
public class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V> {
    private final ExecutionList executionList;
    
    public static <V> ListenableFutureTask<V> create(final Callable<V> callable) {
        return new ListenableFutureTask<V>(callable);
    }
    
    public static <V> ListenableFutureTask<V> create(final Runnable runnable, @Nullable final V result) {
        return new ListenableFutureTask<V>(runnable, result);
    }
    
    ListenableFutureTask(final Callable<V> callable) {
        super((Callable)callable);
        this.executionList = new ExecutionList();
    }
    
    ListenableFutureTask(final Runnable runnable, @Nullable final V result) {
        super(runnable, result);
        this.executionList = new ExecutionList();
    }
    
    public void addListener(final Runnable listener, final Executor exec) {
        this.executionList.add(listener, exec);
    }
    
    protected void done() {
        this.executionList.execute();
    }
}
