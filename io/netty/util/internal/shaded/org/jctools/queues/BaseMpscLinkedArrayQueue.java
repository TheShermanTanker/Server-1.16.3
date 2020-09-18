package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
import java.util.Iterator;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;
import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;

public abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E> implements MessagePassingQueue<E>, QueueProgressIndicators {
    private static final Object JUMP;
    private static final int CONTINUE_TO_P_INDEX_CAS = 0;
    private static final int RETRY = 1;
    private static final int QUEUE_FULL = 2;
    private static final int QUEUE_RESIZE = 3;
    
    public BaseMpscLinkedArrayQueue(final int initialCapacity) {
        RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
        final int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        final long mask = p2capacity - 1 << 1;
        final E[] buffer = CircularArrayOffsetCalculator.<E>allocate(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        this.consumerBuffer = buffer;
        this.soProducerLimit(this.consumerMask = mask);
    }
    
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final int size() {
        long after = this.lvConsumerIndex();
        long before;
        long currentProducerIndex;
        do {
            before = after;
            currentProducerIndex = this.lvProducerIndex();
            after = this.lvConsumerIndex();
        } while (before != after);
        final long size = currentProducerIndex - after >> 1;
        if (size > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)size;
    }
    
    @Override
    public final boolean isEmpty() {
        return this.lvConsumerIndex() == this.lvProducerIndex();
    }
    
    public String toString() {
        return this.getClass().getName();
    }
    
    @Override
    public boolean offer(final E e) {
        if (null == e) {
            throw new NullPointerException();
        }
        while (true) {
            final long producerLimit = this.lvProducerLimit();
            final long pIndex = this.lvProducerIndex();
            if ((pIndex & 0x1L) == 0x1L) {
                continue;
            }
            final long mask = this.producerMask;
            final E[] buffer = this.producerBuffer;
            if (producerLimit <= pIndex) {
                final int result = this.offerSlowPath(mask, pIndex, producerLimit);
                switch (result) {
                    case 1: {
                        continue;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        this.resize(mask, buffer, pIndex, e);
                        return true;
                    }
                }
            }
            if (this.casProducerIndex(pIndex, pIndex + 2L)) {
                final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, mask);
                UnsafeRefArrayAccess.<E>soElement(buffer, offset, e);
                return true;
            }
        }
    }
    
    @Override
    public E poll() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
        if (e == null) {
            if (index == this.lvProducerIndex()) {
                return null;
            }
            do {
                e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
            } while (e == null);
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            final E[] nextBuffer = this.getNextBuffer(buffer, mask);
            return this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.<E>soElement(buffer, offset, (E)null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }
    
    @Override
    public E peek() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
        Object e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
        if (e == null && index != this.lvProducerIndex()) {
            do {
                e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
            } while (e == null);
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            return this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }
    
    private int offerSlowPath(final long mask, final long pIndex, final long producerLimit) {
        final long cIndex = this.lvConsumerIndex();
        final long bufferCapacity = this.getCurrentBufferCapacity(mask);
        if (cIndex + bufferCapacity > pIndex) {
            if (!this.casProducerLimit(producerLimit, cIndex + bufferCapacity)) {
                return 1;
            }
            return 0;
        }
        else {
            if (this.availableInQueue(pIndex, cIndex) <= 0L) {
                return 2;
            }
            if (this.casProducerIndex(pIndex, pIndex + 1L)) {
                return 3;
            }
            return 1;
        }
    }
    
    protected abstract long availableInQueue(final long long1, final long long2);
    
    private E[] getNextBuffer(final E[] buffer, final long mask) {
        final long offset = this.nextArrayOffset(mask);
        final E[] nextBuffer = UnsafeRefArrayAccess.<E[]>lvElement((E[][])buffer, offset);
        UnsafeRefArrayAccess.<E>soElement(buffer, offset, (E)null);
        return nextBuffer;
    }
    
    private long nextArrayOffset(final long mask) {
        return LinkedArrayQueueUtil.modifiedCalcElementOffset(mask + 2L, Long.MAX_VALUE);
    }
    
    private E newBufferPoll(final E[] nextBuffer, final long index) {
        final long offset = this.newBufferAndOffset(nextBuffer, index);
        final E n = UnsafeRefArrayAccess.<E>lvElement(nextBuffer, offset);
        if (n == null) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        UnsafeRefArrayAccess.<E>soElement(nextBuffer, offset, (E)null);
        this.soConsumerIndex(index + 2L);
        return n;
    }
    
    private E newBufferPeek(final E[] nextBuffer, final long index) {
        final long offset = this.newBufferAndOffset(nextBuffer, index);
        final E n = UnsafeRefArrayAccess.<E>lvElement(nextBuffer, offset);
        if (null == n) {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return n;
    }
    
    private long newBufferAndOffset(final E[] nextBuffer, final long index) {
        this.consumerBuffer = nextBuffer;
        this.consumerMask = LinkedArrayQueueUtil.length(nextBuffer) - 2 << 1;
        return LinkedArrayQueueUtil.modifiedCalcElementOffset(index, this.consumerMask);
    }
    
    @Override
    public long currentProducerIndex() {
        return this.lvProducerIndex() / 2L;
    }
    
    @Override
    public long currentConsumerIndex() {
        return this.lvConsumerIndex() / 2L;
    }
    
    @Override
    public abstract int capacity();
    
    @Override
    public boolean relaxedOffer(final E e) {
        return this.offer(e);
    }
    
    @Override
    public E relaxedPoll() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
        final Object e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
        if (e == null) {
            return null;
        }
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            final E[] nextBuffer = this.getNextBuffer(buffer, mask);
            return this.newBufferPoll(nextBuffer, index);
        }
        UnsafeRefArrayAccess.<E>soElement(buffer, offset, (E)null);
        this.soConsumerIndex(index + 2L);
        return (E)e;
    }
    
    @Override
    public E relaxedPeek() {
        final E[] buffer = this.consumerBuffer;
        final long index = this.consumerIndex;
        final long mask = this.consumerMask;
        final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
        final Object e = UnsafeRefArrayAccess.<E>lvElement(buffer, offset);
        if (e == BaseMpscLinkedArrayQueue.JUMP) {
            return this.newBufferPeek(this.getNextBuffer(buffer, mask), index);
        }
        return (E)e;
    }
    
    @Override
    public int fill(final Supplier<E> s) {
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
    
    @Override
    public int fill(final Supplier<E> s, final int batchSize) {
        while (true) {
            final long producerLimit = this.lvProducerLimit();
            final long pIndex = this.lvProducerIndex();
            if ((pIndex & 0x1L) == 0x1L) {
                continue;
            }
            final long mask = this.producerMask;
            final E[] buffer = this.producerBuffer;
            final long batchIndex = Math.min(producerLimit, pIndex + 2 * batchSize);
            if (pIndex >= producerLimit || producerLimit < batchIndex) {
                final int result = this.offerSlowPath(mask, pIndex, producerLimit);
                switch (result) {
                    case 0:
                    case 1: {
                        continue;
                    }
                    case 2: {
                        return 0;
                    }
                    case 3: {
                        this.resize(mask, buffer, pIndex, s.get());
                        return 1;
                    }
                }
            }
            if (this.casProducerIndex(pIndex, batchIndex)) {
                final int claimedSlots = (int)((batchIndex - pIndex) / 2L);
                for (int i = 0; i < claimedSlots; ++i) {
                    final long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex + 2 * i, mask);
                    UnsafeRefArrayAccess.<E>soElement(buffer, offset, s.get());
                }
                return claimedSlots;
            }
        }
    }
    
    @Override
    public void fill(final Supplier<E> s, final WaitStrategy w, final ExitCondition exit) {
        while (exit.keepRunning()) {
            if (this.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
                int idleCounter = 0;
                while (exit.keepRunning() && this.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
                    idleCounter = w.idle(idleCounter);
                }
            }
        }
    }
    
    @Override
    public int drain(final Consumer<E> c) {
        return this.drain(c, this.capacity());
    }
    
    @Override
    public int drain(final Consumer<E> c, final int limit) {
        int i;
        E m;
        for (i = 0; i < limit && (m = this.relaxedPoll()) != null; ++i) {
            c.accept(m);
        }
        return i;
    }
    
    @Override
    public void drain(final Consumer<E> c, final WaitStrategy w, final ExitCondition exit) {
        int idleCounter = 0;
        while (exit.keepRunning()) {
            final E e = this.relaxedPoll();
            if (e == null) {
                idleCounter = w.idle(idleCounter);
            }
            else {
                idleCounter = 0;
                c.accept(e);
            }
        }
    }
    
    private void resize(final long oldMask, final E[] oldBuffer, final long pIndex, final E e) {
        final int newBufferLength = this.getNextBufferSize(oldBuffer);
        final E[] newBuffer = CircularArrayOffsetCalculator.<E>allocate(newBufferLength);
        this.producerBuffer = newBuffer;
        final int newMask = newBufferLength - 2 << 1;
        this.producerMask = newMask;
        final long offsetInOld = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, oldMask);
        final long offsetInNew = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, newMask);
        UnsafeRefArrayAccess.<E>soElement(newBuffer, offsetInNew, e);
        UnsafeRefArrayAccess.soElement(oldBuffer, this.nextArrayOffset(oldMask), newBuffer);
        final long cIndex = this.lvConsumerIndex();
        final long availableInQueue = this.availableInQueue(pIndex, cIndex);
        RangeUtil.checkPositive(availableInQueue, "availableInQueue");
        this.soProducerLimit(pIndex + Math.min((long)newMask, availableInQueue));
        this.soProducerIndex(pIndex + 2L);
        UnsafeRefArrayAccess.soElement(oldBuffer, offsetInOld, BaseMpscLinkedArrayQueue.JUMP);
    }
    
    protected abstract int getNextBufferSize(final E[] arr);
    
    protected abstract long getCurrentBufferCapacity(final long long1);
    
    static {
        JUMP = new Object();
    }
}
