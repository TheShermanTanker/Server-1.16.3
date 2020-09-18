package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class MpscArrayQueueConsumerIndexField<E> extends MpscArrayQueueL2Pad<E> {
    private static final long C_INDEX_OFFSET;
    protected long consumerIndex;
    
    public MpscArrayQueueConsumerIndexField(final int capacity) {
        super(capacity);
    }
    
    protected final long lpConsumerIndex() {
        return this.consumerIndex;
    }
    
    public final long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, MpscArrayQueueConsumerIndexField.C_INDEX_OFFSET);
    }
    
    protected void soConsumerIndex(final long newValue) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, MpscArrayQueueConsumerIndexField.C_INDEX_OFFSET, newValue);
    }
    
    static {
        try {
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueConsumerIndexField.class.getDeclaredField("consumerIndex"));
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
