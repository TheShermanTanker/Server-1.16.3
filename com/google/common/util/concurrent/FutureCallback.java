package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface FutureCallback<V> {
    void onSuccess(@Nullable final V object);
    
    void onFailure(final Throwable throwable);
}
