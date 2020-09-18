package com.google.common.base;

import java.lang.ref.ReferenceQueue;
import com.google.common.annotations.GwtIncompatible;
import java.lang.ref.PhantomReference;

@GwtIncompatible
public abstract class FinalizablePhantomReference<T> extends PhantomReference<T> implements FinalizableReference {
    protected FinalizablePhantomReference(final T referent, final FinalizableReferenceQueue queue) {
        super(referent, (ReferenceQueue)queue.queue);
        queue.cleanUp();
    }
}
