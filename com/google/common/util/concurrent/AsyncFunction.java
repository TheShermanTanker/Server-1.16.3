package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface AsyncFunction<I, O> {
    ListenableFuture<O> apply(@Nullable final I object) throws Exception;
}
