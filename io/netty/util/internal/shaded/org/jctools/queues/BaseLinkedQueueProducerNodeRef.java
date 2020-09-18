package io.netty.util.internal.shaded.org.jctools.queues;

import java.lang.reflect.Field;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class BaseLinkedQueueProducerNodeRef<E> extends BaseLinkedQueuePad0<E> {
    protected static final long P_NODE_OFFSET;
    protected LinkedQueueNode<E> producerNode;
    
    protected final void spProducerNode(final LinkedQueueNode<E> newValue) {
        this.producerNode = newValue;
    }
    
    protected final LinkedQueueNode<E> lvProducerNode() {
        return (LinkedQueueNode<E>)UnsafeAccess.UNSAFE.getObjectVolatile(this, BaseLinkedQueueProducerNodeRef.P_NODE_OFFSET);
    }
    
    protected final boolean casProducerNode(final LinkedQueueNode<E> expect, final LinkedQueueNode<E> newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapObject(this, BaseLinkedQueueProducerNodeRef.P_NODE_OFFSET, expect, newValue);
    }
    
    protected final LinkedQueueNode<E> lpProducerNode() {
        return this.producerNode;
    }
    
    static {
        try {
            final Field pNodeField = BaseLinkedQueueProducerNodeRef.class.getDeclaredField("producerNode");
            P_NODE_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(pNodeField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
