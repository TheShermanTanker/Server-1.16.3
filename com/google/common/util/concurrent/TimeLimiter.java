package com.google.common.util.concurrent;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public interface TimeLimiter {
     <T> T newProxy(final T object, final Class<T> class2, final long long3, final TimeUnit timeUnit);
    
    @CanIgnoreReturnValue
     <T> T callWithTimeout(final Callable<T> callable, final long long2, final TimeUnit timeUnit, final boolean boolean4) throws Exception;
}
