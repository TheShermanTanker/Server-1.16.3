package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.WeakReference;

@GwtIncompatible
public abstract class FinalizableWeakReference<T> extends WeakReference<T> implements FinalizableReference {
    protected FinalizableWeakReference(final T referent, final FinalizableReferenceQueue queue) {
        super(referent, (ReferenceQueue)queue.queue);
        queue.cleanUp();
    }
}
