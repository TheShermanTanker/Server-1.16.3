package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.SoftReference;

@GwtIncompatible
public abstract class FinalizableSoftReference<T> extends SoftReference<T> implements FinalizableReference {
    protected FinalizableSoftReference(final T referent, final FinalizableReferenceQueue queue) {
        super(referent, (ReferenceQueue)queue.queue);
        queue.cleanUp();
    }
}
