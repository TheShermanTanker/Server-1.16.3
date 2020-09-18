package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

abstract class MpscAtomicArrayQueueProducerLimitField<E> extends MpscAtomicArrayQueueMidPad<E> {
    private static final AtomicLongFieldUpdater<MpscAtomicArrayQueueProducerLimitField> P_LIMIT_UPDATER;
    private volatile long producerLimit;
    
    public MpscAtomicArrayQueueProducerLimitField(final int capacity) {
        super(capacity);
        this.producerLimit = capacity;
    }
    
    protected final long lvProducerLimit() {
        return this.producerLimit;
    }
    
    protected final void soProducerLimit(final long newValue) {
        MpscAtomicArrayQueueProducerLimitField.P_LIMIT_UPDATER.lazySet(this, newValue);
    }
    
    static {
        P_LIMIT_UPDATER = AtomicLongFieldUpdater.newUpdater((Class)MpscAtomicArrayQueueProducerLimitField.class, "producerLimit");
    }
}
