package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpscArrayQueueProducerLimitField<E> extends MpscArrayQueueMidPad<E> {
    private static final long P_LIMIT_OFFSET;
    private volatile long producerLimit;
    
    public MpscArrayQueueProducerLimitField(final int capacity) {
        super(capacity);
        this.producerLimit = capacity;
    }
    
    protected final long lvProducerLimit() {
        return this.producerLimit;
    }
    
    protected final void soProducerLimit(final long newValue) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, MpscArrayQueueProducerLimitField.P_LIMIT_OFFSET, newValue);
    }
    
    static {
        try {
            P_LIMIT_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueProducerLimitField.class.getDeclaredField("producerLimit"));
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
