package io.netty.util.internal.shaded.org.jctools.queues;

import java.lang.reflect.Field;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class BaseMpscLinkedArrayQueueProducerFields<E> extends BaseMpscLinkedArrayQueuePad1<E> {
    private static final long P_INDEX_OFFSET;
    protected long producerIndex;
    
    public final long lvProducerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, BaseMpscLinkedArrayQueueProducerFields.P_INDEX_OFFSET);
    }
    
    final void soProducerIndex(final long newValue) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, BaseMpscLinkedArrayQueueProducerFields.P_INDEX_OFFSET, newValue);
    }
    
    final boolean casProducerIndex(final long expect, final long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, BaseMpscLinkedArrayQueueProducerFields.P_INDEX_OFFSET, expect, newValue);
    }
    
    static {
        try {
            final Field iField = BaseMpscLinkedArrayQueueProducerFields.class.getDeclaredField("producerIndex");
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
