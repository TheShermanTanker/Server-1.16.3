package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
@GwtCompatible
public interface CheckedFuture<V, X extends Exception> extends ListenableFuture<V> {
    V checkedGet() throws X, Exception;
    
    V checkedGet(final long long1, final TimeUnit timeUnit) throws TimeoutException, X, Exception;
}
