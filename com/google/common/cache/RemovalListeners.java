package com.google.common.cache;

import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public final class RemovalListeners {
    private RemovalListeners() {
    }
    
    public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, final Executor executor) {
        Preconditions.<RemovalListener<K, V>>checkNotNull(listener);
        Preconditions.<Executor>checkNotNull(executor);
        return new RemovalListener<K, V>() {
            public void onRemoval(final RemovalNotification<K, V> notification) {
                executor.execute((Runnable)new Runnable() {
                    public void run() {
                        listener.onRemoval(notification);
                    }
                });
            }
        };
    }
}
