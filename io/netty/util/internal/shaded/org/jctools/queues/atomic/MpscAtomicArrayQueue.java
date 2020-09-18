package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MpscAtomicArrayQueue<E> extends MpscAtomicArrayQueueL3Pad<E> {
    public MpscAtomicArrayQueue(final int capacity) {
        super(capacity);
    }
    
    public boolean offerIfBelowThreshold(final E e, final int threshold) {
        if (null == e) {
            throw new NullPointerException();
        }
        final int mask = this.mask;
        final long capacity = mask + 1;
        long producerLimit = this.lvProducerLimit();
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            final long available = producerLimit - pIndex;
            long size = capacity - available;
            if (size >= threshold) {
                final long cIndex = this.lvConsumerIndex();
                size = pIndex - cIndex;
                if (size >= threshold) {
                    return false;
                }
                producerLimit = cIndex + capacity;
                this.soProducerLimit(producerLimit);
            }
        } while (!this.casProducerIndex(pIndex, pIndex + 1L));
        final int offset = this.calcElementOffset(pIndex, mask);
        AtomicReferenceArrayQueue.<E>soElement(this.buffer, offset, e);
        return true;
    }
    
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final int mask = this.mask;
        long producerLimit = this.lvProducerLimit();
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            if (pIndex >= producerLimit) {
                final long cIndex = this.lvConsumerIndex();
                producerLimit = cIndex + mask + 1L;
                if (pIndex >= producerLimit) {
                    return false;
                }
                this.soProducerLimit(producerLimit);
            }
        } while (!this.casProducerIndex(pIndex, pIndex + 1L));
        final int offset = this.calcElementOffset(pIndex, mask);
        AtomicReferenceArrayQueue.<E>soElement(this.buffer, offset, e);
        return true;
    }
    
    public final int failFastOffer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        final int mask = this.mask;
        final long capacity = mask + 1;
        final long pIndex = this.lvProducerIndex();
        long producerLimit = this.lvProducerLimit();
        if (pIndex >= producerLimit) {
            final long cIndex = this.lvConsumerIndex();
            producerLimit = cIndex + capacity;
            if (pIndex >= producerLimit) {
                return 1;
            }
            this.soProducerLimit(producerLimit);
        }
        if (!this.casProducerIndex(pIndex, pIndex + 1L)) {
            return -1;
        }
        final int offset = this.calcElementOffset(pIndex, mask);
        AtomicReferenceArrayQueue.<E>soElement(this.buffer, offset, e);
        return 0;
    }
    
    public E poll() {
        final long cIndex = this.lpConsumerIndex();
        final int offset = this.calcElementOffset(cIndex);
        final AtomicReferenceArray<E> buffer = this.buffer;
        E e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
        if (null == e) {
            if (cIndex == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
            } while (e == null);
        }
        AtomicReferenceArrayQueue.<E>spElement(buffer, offset, (E)null);
        this.soConsumerIndex(cIndex + 1L);
        return e;
    }
    
    public E peek() {
        final AtomicReferenceArray<E> buffer = this.buffer;
        final long cIndex = this.lpConsumerIndex();
        final int offset = this.calcElementOffset(cIndex);
        E e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
        if (null == e) {
            if (cIndex == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
            } while (e == null);
        }
        return e;
    }
    
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
    
    public E relaxedPoll() {
        final AtomicReferenceArray<E> buffer = this.buffer;
        final long cIndex = this.lpConsumerIndex();
        final int offset = this.calcElementOffset(cIndex);
        final E e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
        if (null == e) {
            return null;
        }
        AtomicReferenceArrayQueue.<E>spElement(buffer, offset, (E)null);
        this.soConsumerIndex(cIndex + 1L);
        return e;
    }
    
    public E relaxedPeek() {
        final AtomicReferenceArray<E> buffer = this.buffer;
        final int mask = this.mask;
        final long cIndex = this.lpConsumerIndex();
        return AtomicReferenceArrayQueue.<E>lvElement(buffer, this.calcElementOffset(cIndex, mask));
    }
    
    public int drain(final MessagePassingQueue.Consumer<E> c) {
        return this.drain(c, this.capacity());
    }
    
    public int fill(final MessagePassingQueue.Supplier<E> s) {
        long result = 0L;
        final int capacity = this.capacity();
        do {
            final int filled = this.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int)result;
            }
            result += filled;
        } while (result <= capacity);
        return (int)result;
    }
    
    public int drain(final MessagePassingQueue.Consumer<E> c, final int limit) {
        final AtomicReferenceArray<E> buffer = this.buffer;
        final int mask = this.mask;
        final long cIndex = this.lpConsumerIndex();
        for (int i = 0; i < limit; ++i) {
            final long index = cIndex + i;
            final int offset = this.calcElementOffset(index, mask);
            final E e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
            if (null == e) {
                return i;
            }
            AtomicReferenceArrayQueue.<E>spElement(buffer, offset, (E)null);
            this.soConsumerIndex(index + 1L);
            c.accept(e);
        }
        return limit;
    }
    
    public int fill(final MessagePassingQueue.Supplier<E> s, final int limit) {
        final int mask = this.mask;
        final long capacity = mask + 1;
        long producerLimit = this.lvProducerLimit();
        int actualLimit = 0;
        long pIndex;
        do {
            pIndex = this.lvProducerIndex();
            long available = producerLimit - pIndex;
            if (available <= 0L) {
                final long cIndex = this.lvConsumerIndex();
                producerLimit = cIndex + capacity;
                available = producerLimit - pIndex;
                if (available <= 0L) {
                    return 0;
                }
                this.soProducerLimit(producerLimit);
            }
            actualLimit = Math.min((int)available, limit);
        } while (!this.casProducerIndex(pIndex, pIndex + actualLimit));
        final AtomicReferenceArray<E> buffer = this.buffer;
        for (int i = 0; i < actualLimit; ++i) {
            final int offset = this.calcElementOffset(pIndex + i, mask);
            AtomicReferenceArrayQueue.<E>soElement(buffer, offset, s.get());
        }
        return actualLimit;
    }
    
    public void drain(final MessagePassingQueue.Consumer<E> c, final MessagePassingQueue.WaitStrategy w, final MessagePassingQueue.ExitCondition exit) {
        final AtomicReferenceArray<E> buffer = this.buffer;
        final int mask = this.mask;
        long cIndex = this.lpConsumerIndex();
        int counter = 0;
        while (exit.keepRunning()) {
            for (int i = 0; i < 4096; ++i) {
                final int offset = this.calcElementOffset(cIndex, mask);
                final E e = AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
                if (null == e) {
                    counter = w.idle(counter);
                }
                else {
                    ++cIndex;
                    counter = 0;
                    AtomicReferenceArrayQueue.<E>spElement(buffer, offset, (E)null);
                    this.soConsumerIndex(cIndex);
                    c.accept(e);
                }
            }
        }
    }
    
    public void fill(final MessagePassingQueue.Supplier<E> s, final MessagePassingQueue.WaitStrategy w, final MessagePassingQueue.ExitCondition exit) {
        int idleCounter = 0;
        while (exit.keepRunning()) {
            if (this.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
                idleCounter = w.idle(idleCounter);
            }
            else {
                idleCounter = 0;
            }
        }
    }
    
    @Deprecated
    public int weakOffer(final E e) {
        return this.failFastOffer(e);
    }
}
