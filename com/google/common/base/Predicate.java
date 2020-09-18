package com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@FunctionalInterface
@GwtCompatible
public interface Predicate<T> extends java.util.function.Predicate<T> {
    @CanIgnoreReturnValue
    boolean apply(@Nullable final T object);
    
    boolean equals(@Nullable final Object object);
    
    default boolean test(@Nullable final T input) {
        return this.apply(input);
    }
}
