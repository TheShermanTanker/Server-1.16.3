package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpscArrayQueueProducerIndexField<E> extends MpscArrayQueueL1Pad<E> {
    private static final long P_INDEX_OFFSET;
    private volatile long producerIndex;
    
    public MpscArrayQueueProducerIndexField(final int capacity) {
        super(capacity);
    }
    
    public final long lvProducerIndex() {
        return this.producerIndex;
    }
    
    protected final boolean casProducerIndex(final long expect, final long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, MpscArrayQueueProducerIndexField.P_INDEX_OFFSET, expect, newValue);
    }
    
    static {
        try {
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueProducerIndexField.class.getDeclaredField("producerIndex"));
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
