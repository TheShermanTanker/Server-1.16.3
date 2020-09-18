package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import java.util.concurrent.atomic.AtomicReferenceArray;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators;
import io.netty.util.internal.shaded.org.jctools.queues.IndexedQueueSizeUtil;
import java.util.AbstractQueue;

abstract class AtomicReferenceArrayQueue<E> extends AbstractQueue<E> implements IndexedQueueSizeUtil.IndexedQueue, QueueProgressIndicators, MessagePassingQueue<E> {
    protected final AtomicReferenceArray<E> buffer;
    protected final int mask;
    
    public AtomicReferenceArrayQueue(final int capacity) {
        final int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = actualCapacity - 1;
        this.buffer = (AtomicReferenceArray<E>)new AtomicReferenceArray(actualCapacity);
    }
    
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    public String toString() {
        return this.getClass().getName();
    }
    
    public void clear() {
        while (this.poll() != null) {}
    }
    
    protected final int calcElementOffset(final long index, final int mask) {
        return (int)index & mask;
    }
    
    protected final int calcElementOffset(final long index) {
        return (int)index & this.mask;
    }
    
    public static <E> E lvElement(final AtomicReferenceArray<E> buffer, final int offset) {
        return (E)buffer.get(offset);
    }
    
    public static <E> E lpElement(final AtomicReferenceArray<E> buffer, final int offset) {
        return (E)buffer.get(offset);
    }
    
    protected final E lpElement(final int offset) {
        return (E)this.buffer.get(offset);
    }
    
    public static <E> void spElement(final AtomicReferenceArray<E> buffer, final int offset, final E value) {
        buffer.lazySet(offset, value);
    }
    
    protected final void spElement(final int offset, final E value) {
        this.buffer.lazySet(offset, value);
    }
    
    public static <E> void soElement(final AtomicReferenceArray<E> buffer, final int offset, final E value) {
        buffer.lazySet(offset, value);
    }
    
    protected final void soElement(final int offset, final E value) {
        this.buffer.lazySet(offset, value);
    }
    
    public static <E> void svElement(final AtomicReferenceArray<E> buffer, final int offset, final E value) {
        buffer.set(offset, value);
    }
    
    protected final E lvElement(final int offset) {
        return AtomicReferenceArrayQueue.<E>lvElement(this.buffer, offset);
    }
    
    public final int capacity() {
        return this.mask + 1;
    }
    
    public final int size() {
        return IndexedQueueSizeUtil.size(this);
    }
    
    public final boolean isEmpty() {
        return IndexedQueueSizeUtil.isEmpty(this);
    }
    
    public final long currentProducerIndex() {
        return this.lvProducerIndex();
    }
    
    public final long currentConsumerIndex() {
        return this.lvConsumerIndex();
    }
}
