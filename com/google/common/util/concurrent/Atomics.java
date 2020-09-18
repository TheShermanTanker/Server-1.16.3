package com.google.common.util.concurrent;

import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public final class Atomics {
    private Atomics() {
    }
    
    public static <V> AtomicReference<V> newReference() {
        return (AtomicReference<V>)new AtomicReference();
    }
    
    public static <V> AtomicReference<V> newReference(@Nullable final V initialValue) {
        return (AtomicReference<V>)new AtomicReference(initialValue);
    }
    
    public static <E> AtomicReferenceArray<E> newReferenceArray(final int length) {
        return (AtomicReferenceArray<E>)new AtomicReferenceArray(length);
    }
    
    public static <E> AtomicReferenceArray<E> newReferenceArray(final E[] array) {
        return (AtomicReferenceArray<E>)new AtomicReferenceArray((Object[])array);
    }
}
