package io.netty.util.internal.shaded.org.jctools.queues;

import java.lang.reflect.Field;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class BaseMpscLinkedArrayQueueConsumerFields<E> extends BaseMpscLinkedArrayQueuePad2<E> {
    private static final long C_INDEX_OFFSET;
    protected long consumerMask;
    protected E[] consumerBuffer;
    protected long consumerIndex;
    
    public final long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, BaseMpscLinkedArrayQueueConsumerFields.C_INDEX_OFFSET);
    }
    
    final void soConsumerIndex(final long newValue) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, BaseMpscLinkedArrayQueueConsumerFields.C_INDEX_OFFSET, newValue);
    }
    
    static {
        try {
            final Field iField = BaseMpscLinkedArrayQueueConsumerFields.class.getDeclaredField("consumerIndex");
            C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
