package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
@GwtIncompatible
public final class FakeTimeLimiter implements TimeLimiter {
    public <T> T newProxy(final T target, final Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
        Preconditions.<T>checkNotNull(target);
        Preconditions.<Class<T>>checkNotNull(interfaceType);
        Preconditions.<TimeUnit>checkNotNull(timeoutUnit);
        return target;
    }
    
    public <T> T callWithTimeout(final Callable<T> callable, final long timeoutDuration, final TimeUnit timeoutUnit, final boolean amInterruptible) throws Exception {
        Preconditions.<TimeUnit>checkNotNull(timeoutUnit);
        return (T)callable.call();
    }
}
