package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public class MpscChunkedAtomicArrayQueue<E> extends MpscChunkedAtomicArrayQueueColdProducerFields<E> {
    long p0;
    long p1;
    long p2;
    long p3;
    long p4;
    long p5;
    long p6;
    long p7;
    long p10;
    long p11;
    long p12;
    long p13;
    long p14;
    long p15;
    long p16;
    long p17;
    
    public MpscChunkedAtomicArrayQueue(final int maxCapacity) {
        super(Math.max(2, Math.min(1024, Pow2.roundToPowerOfTwo(maxCapacity / 8))), maxCapacity);
    }
    
    public MpscChunkedAtomicArrayQueue(final int initialCapacity, final int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }
    
    @Override
    protected long availableInQueue(final long pIndex, final long cIndex) {
        return this.maxQueueCapacity - (pIndex - cIndex);
    }
    
    @Override
    public int capacity() {
        return (int)(this.maxQueueCapacity / 2L);
    }
    
    @Override
    protected int getNextBufferSize(final AtomicReferenceArray<E> buffer) {
        return LinkedAtomicArrayQueueUtil.length(buffer);
    }
    
    @Override
    protected long getCurrentBufferCapacity(final long mask) {
        return mask;
    }
}
