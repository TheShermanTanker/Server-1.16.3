package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

abstract class MpscAtomicArrayQueueConsumerIndexField<E> extends MpscAtomicArrayQueueL2Pad<E> {
    private static final AtomicLongFieldUpdater<MpscAtomicArrayQueueConsumerIndexField> C_INDEX_UPDATER;
    protected volatile long consumerIndex;
    
    public MpscAtomicArrayQueueConsumerIndexField(final int capacity) {
        super(capacity);
    }
    
    protected final long lpConsumerIndex() {
        return this.consumerIndex;
    }
    
    public final long lvConsumerIndex() {
        return this.consumerIndex;
    }
    
    protected void soConsumerIndex(final long newValue) {
        MpscAtomicArrayQueueConsumerIndexField.C_INDEX_UPDATER.lazySet(this, newValue);
    }
    
    static {
        C_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater((Class)MpscAtomicArrayQueueConsumerIndexField.class, "consumerIndex");
    }
}
