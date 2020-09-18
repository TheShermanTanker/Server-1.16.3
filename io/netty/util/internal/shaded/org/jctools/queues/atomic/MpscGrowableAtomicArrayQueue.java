package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
import java.util.concurrent.atomic.AtomicReferenceArray;
import io.netty.util.internal.shaded.org.jctools.util.Pow2;

public class MpscGrowableAtomicArrayQueue<E> extends MpscChunkedAtomicArrayQueue<E> {
    public MpscGrowableAtomicArrayQueue(final int maxCapacity) {
        super(Math.max(2, Pow2.roundToPowerOfTwo(maxCapacity / 8)), maxCapacity);
    }
    
    public MpscGrowableAtomicArrayQueue(final int initialCapacity, final int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }
    
    @Override
    protected int getNextBufferSize(final AtomicReferenceArray<E> buffer) {
        final long maxSize = this.maxQueueCapacity / 2L;
        RangeUtil.checkLessThanOrEqual(LinkedAtomicArrayQueueUtil.length(buffer), maxSize, "buffer.length");
        final int newSize = 2 * (LinkedAtomicArrayQueueUtil.length(buffer) - 1);
        return newSize + 1;
    }
    
    @Override
    protected long getCurrentBufferCapacity(final long mask) {
        return (mask + 2L == this.maxQueueCapacity) ? this.maxQueueCapacity : mask;
    }
}
