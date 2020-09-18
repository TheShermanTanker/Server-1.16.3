package io.netty.util.internal.shaded.org.jctools.queues;

import java.lang.reflect.Field;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;

abstract class BaseLinkedQueueConsumerNodeRef<E> extends BaseLinkedQueuePad1<E> {
    protected static final long C_NODE_OFFSET;
    protected LinkedQueueNode<E> consumerNode;
    
    protected final void spConsumerNode(final LinkedQueueNode<E> newValue) {
        this.consumerNode = newValue;
    }
    
    protected final LinkedQueueNode<E> lvConsumerNode() {
        return (LinkedQueueNode<E>)UnsafeAccess.UNSAFE.getObjectVolatile(this, BaseLinkedQueueConsumerNodeRef.C_NODE_OFFSET);
    }
    
    protected final LinkedQueueNode<E> lpConsumerNode() {
        return this.consumerNode;
    }
    
    static {
        try {
            final Field cNodeField = BaseLinkedQueueConsumerNodeRef.class.getDeclaredField("consumerNode");
            C_NODE_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(cNodeField);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException((Throwable)e);
        }
    }
}
