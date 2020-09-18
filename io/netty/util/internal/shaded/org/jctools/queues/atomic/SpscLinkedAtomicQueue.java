package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;

public class SpscLinkedAtomicQueue<E> extends BaseLinkedAtomicQueue<E> {
    public SpscLinkedAtomicQueue() {
        final LinkedQueueAtomicNode<E> node = this.newNode();
        this.spProducerNode(node);
        this.spConsumerNode(node);
        node.soNext(null);
    }
    
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final LinkedQueueAtomicNode<E> nextNode = this.newNode(e);
        this.lpProducerNode().soNext(nextNode);
        this.spProducerNode(nextNode);
        return true;
    }
    
    public E poll() {
        return (E)this.relaxedPoll();
    }
    
    public E peek() {
        return (E)this.relaxedPeek();
    }
    
    public int fill(final MessagePassingQueue.Supplier<E> s) {
        long result = 0L;
        do {
            this.fill(s, 4096);
            result += 4096L;
        } while (result <= 2147479551L);
        return (int)result;
    }
    
    public int fill(final MessagePassingQueue.Supplier<E> s, final int limit) {
        if (limit == 0) {
            return 0;
        }
        final LinkedQueueAtomicNode<E> head;
        LinkedQueueAtomicNode<E> tail = head = this.newNode(s.get());
        for (int i = 1; i < limit; ++i) {
            final LinkedQueueAtomicNode<E> temp = this.newNode(s.get());
            tail.soNext(temp);
            tail = temp;
        }
        final LinkedQueueAtomicNode<E> oldPNode = this.lpProducerNode();
        oldPNode.soNext(head);
        this.spProducerNode(tail);
        return limit;
    }
    
    public void fill(final MessagePassingQueue.Supplier<E> s, final MessagePassingQueue.WaitStrategy wait, final MessagePassingQueue.ExitCondition exit) {
        LinkedQueueAtomicNode<E> chaserNode = this.producerNode;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final LinkedQueueAtomicNode<E> nextNode = this.newNode(s.get());
                chaserNode.soNext(nextNode);
                chaserNode = nextNode;
                this.producerNode = chaserNode;
            }
        }
    }
}
