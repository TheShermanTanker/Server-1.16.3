package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class BaseLinkedAtomicQueueProducerNodeRef<E> extends BaseLinkedAtomicQueuePad0<E> {
    private static final AtomicReferenceFieldUpdater<BaseLinkedAtomicQueueProducerNodeRef, LinkedQueueAtomicNode> P_NODE_UPDATER;
    protected volatile LinkedQueueAtomicNode<E> producerNode;
    
    protected final void spProducerNode(final LinkedQueueAtomicNode<E> newValue) {
        BaseLinkedAtomicQueueProducerNodeRef.P_NODE_UPDATER.lazySet(this, newValue);
    }
    
    protected final LinkedQueueAtomicNode<E> lvProducerNode() {
        return this.producerNode;
    }
    
    protected final boolean casProducerNode(final LinkedQueueAtomicNode<E> expect, final LinkedQueueAtomicNode<E> newValue) {
        return BaseLinkedAtomicQueueProducerNodeRef.P_NODE_UPDATER.compareAndSet(this, expect, newValue);
    }
    
    protected final LinkedQueueAtomicNode<E> lpProducerNode() {
        return this.producerNode;
    }
    
    protected final LinkedQueueAtomicNode<E> xchgProducerNode(final LinkedQueueAtomicNode<E> newValue) {
        return (LinkedQueueAtomicNode<E>)BaseLinkedAtomicQueueProducerNodeRef.P_NODE_UPDATER.getAndSet(this, newValue);
    }
    
    static {
        P_NODE_UPDATER = AtomicReferenceFieldUpdater.newUpdater((Class)BaseLinkedAtomicQueueProducerNodeRef.class, (Class)LinkedQueueAtomicNode.class, "producerNode");
    }
}
