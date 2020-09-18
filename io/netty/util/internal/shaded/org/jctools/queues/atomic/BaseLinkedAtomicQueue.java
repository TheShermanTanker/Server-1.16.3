package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import java.util.Iterator;

abstract class BaseLinkedAtomicQueue<E> extends BaseLinkedAtomicQueuePad2<E> {
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    public String toString() {
        return this.getClass().getName();
    }
    
    protected final LinkedQueueAtomicNode<E> newNode() {
        return new LinkedQueueAtomicNode<E>();
    }
    
    protected final LinkedQueueAtomicNode<E> newNode(final E e) {
        return new LinkedQueueAtomicNode<E>(e);
    }
    
    public final int size() {
        LinkedQueueAtomicNode<E> chaserNode;
        LinkedQueueAtomicNode<E> producerNode;
        int size;
        LinkedQueueAtomicNode<E> next;
        for (chaserNode = this.lvConsumerNode(), producerNode = this.lvProducerNode(), size = 0; chaserNode != producerNode && chaserNode != null && size < Integer.MAX_VALUE; chaserNode = next, ++size) {
            next = chaserNode.lvNext();
            if (next == chaserNode) {
                return size;
            }
        }
        return size;
    }
    
    public final boolean isEmpty() {
        return this.lvConsumerNode() == this.lvProducerNode();
    }
    
    protected E getSingleConsumerNodeValue(final LinkedQueueAtomicNode<E> currConsumerNode, final LinkedQueueAtomicNode<E> nextNode) {
        final E nextValue = nextNode.getAndNullValue();
        currConsumerNode.soNext(currConsumerNode);
        this.spConsumerNode(nextNode);
        return nextValue;
    }
    
    public E relaxedPoll() {
        final LinkedQueueAtomicNode<E> currConsumerNode = this.lpConsumerNode();
        final LinkedQueueAtomicNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return this.getSingleConsumerNodeValue(currConsumerNode, nextNode);
        }
        return null;
    }
    
    public E relaxedPeek() {
        final LinkedQueueAtomicNode<E> nextNode = this.lpConsumerNode().lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        return null;
    }
    
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
    
    public int drain(final MessagePassingQueue.Consumer<E> c) {
        long result = 0L;
        int drained;
        do {
            drained = this.drain(c, 4096);
            result += drained;
        } while (drained == 4096 && result <= 2147479551L);
        return (int)result;
    }
    
    public int drain(final MessagePassingQueue.Consumer<E> c, final int limit) {
        LinkedQueueAtomicNode<E> chaserNode = this.consumerNode;
        for (int i = 0; i < limit; ++i) {
            final LinkedQueueAtomicNode<E> nextNode = chaserNode.lvNext();
            if (nextNode == null) {
                return i;
            }
            final E nextValue = this.getSingleConsumerNodeValue(chaserNode, nextNode);
            chaserNode = nextNode;
            c.accept(nextValue);
        }
        return limit;
    }
    
    public void drain(final MessagePassingQueue.Consumer<E> c, final MessagePassingQueue.WaitStrategy wait, final MessagePassingQueue.ExitCondition exit) {
        LinkedQueueAtomicNode<E> chaserNode = this.consumerNode;
        int idleCounter = 0;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final LinkedQueueAtomicNode<E> nextNode = chaserNode.lvNext();
                if (nextNode == null) {
                    idleCounter = wait.idle(idleCounter);
                }
                else {
                    idleCounter = 0;
                    final E nextValue = this.getSingleConsumerNodeValue(chaserNode, nextNode);
                    chaserNode = nextNode;
                    c.accept(nextValue);
                }
            }
        }
    }
    
    public int capacity() {
        return -1;
    }
}
