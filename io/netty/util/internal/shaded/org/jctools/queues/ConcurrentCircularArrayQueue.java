package io.netty.util.internal.shaded.org.jctools.queues;

import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public abstract class ConcurrentCircularArrayQueue<E> extends ConcurrentCircularArrayQueueL0Pad<E> {
    protected final long mask;
    protected final E[] buffer;
    
    public ConcurrentCircularArrayQueue(final int capacity) {
        final int actualCapacity = Pow2.roundToPowerOfTwo(capacity);
        this.mask = actualCapacity - 1;
        this.buffer = CircularArrayOffsetCalculator.<E>allocate(actualCapacity);
    }
    
    protected static long calcElementOffset(final long index, final long mask) {
        return CircularArrayOffsetCalculator.calcElementOffset(index, mask);
    }
    
    protected final long calcElementOffset(final long index) {
        return calcElementOffset(index, this.mask);
    }
    
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    public final int size() {
        return IndexedQueueSizeUtil.size(this);
    }
    
    public final boolean isEmpty() {
        return IndexedQueueSizeUtil.isEmpty(this);
    }
    
    public String toString() {
        return this.getClass().getName();
    }
    
    public void clear() {
        while (this.poll() != null) {}
    }
    
    public int capacity() {
        return (int)(this.mask + 1L);
    }
    
    public final long currentProducerIndex() {
        return this.lvProducerIndex();
    }
    
    public final long currentConsumerIndex() {
        return this.lvConsumerIndex();
    }
}
