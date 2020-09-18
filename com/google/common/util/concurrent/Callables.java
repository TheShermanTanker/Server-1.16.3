package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Callables {
    private Callables() {
    }
    
    public static <T> Callable<T> returning(@Nullable final T value) {
        return (Callable<T>)new Callable<T>() {
            public T call() {
                return value;
            }
        };
    }
    
    @Beta
    @GwtIncompatible
    public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, final ListeningExecutorService listeningExecutorService) {
        Preconditions.<Callable<T>>checkNotNull(callable);
        Preconditions.<ListeningExecutorService>checkNotNull(listeningExecutorService);
        return new AsyncCallable<T>() {
            public ListenableFuture<T> call() throws Exception {
                return listeningExecutorService.<T>submit(callable);
            }
        };
    }
    
    @GwtIncompatible
    static <T> Callable<T> threadRenaming(final Callable<T> callable, final Supplier<String> nameSupplier) {
        Preconditions.<Supplier<String>>checkNotNull(nameSupplier);
        Preconditions.<Callable<T>>checkNotNull(callable);
        return (Callable<T>)new Callable<T>() {
            public T call() throws Exception {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    return (T)callable.call();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    @GwtIncompatible
    static Runnable threadRenaming(final Runnable task, final Supplier<String> nameSupplier) {
        Preconditions.<Supplier<String>>checkNotNull(nameSupplier);
        Preconditions.<Runnable>checkNotNull(task);
        return (Runnable)new Runnable() {
            public void run() {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                final boolean restoreName = trySetName(nameSupplier.get(), currentThread);
                try {
                    task.run();
                }
                finally {
                    if (restoreName) {
                        trySetName(oldName, currentThread);
                    }
                }
            }
        };
    }
    
    @GwtIncompatible
    private static boolean trySetName(final String threadName, final Thread currentThread) {
        try {
            currentThread.setName(threadName);
            return true;
        }
        catch (SecurityException e) {
            return false;
        }
    }
}
