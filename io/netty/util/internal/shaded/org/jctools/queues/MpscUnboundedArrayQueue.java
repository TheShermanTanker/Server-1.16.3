package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;

public class MpscUnboundedArrayQueue<E> extends BaseMpscLinkedArrayQueue<E> {
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
    
    public MpscUnboundedArrayQueue(final int chunkSize) {
        super(chunkSize);
    }
    
    @Override
    protected long availableInQueue(final long pIndex, final long cIndex) {
        return 2147483647L;
    }
    
    @Override
    public int capacity() {
        return -1;
    }
    
    @Override
    public int drain(final MessagePassingQueue.Consumer<E> c) {
        return this.drain(c, 4096);
    }
    
    @Override
    public int fill(final MessagePassingQueue.Supplier<E> s) {
        long result = 0L;
        final int capacity = 4096;
        do {
            final int filled = this.fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0) {
                return (int)result;
            }
            result += filled;
        } while (result <= 4096L);
        return (int)result;
    }
    
    @Override
    protected int getNextBufferSize(final E[] buffer) {
        return LinkedArrayQueueUtil.length(buffer);
    }
    
    @Override
    protected long getCurrentBufferCapacity(final long mask) {
        return mask;
    }
}
