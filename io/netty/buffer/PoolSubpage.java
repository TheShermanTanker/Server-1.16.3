package io.netty.buffer;

final class PoolSubpage<T> implements PoolSubpageMetric {
    final PoolChunk<T> chunk;
    private final int memoryMapIdx;
    private final int runOffset;
    private final int pageSize;
    private final long[] bitmap;
    PoolSubpage<T> prev;
    PoolSubpage<T> next;
    boolean doNotDestroy;
    int elemSize;
    private int maxNumElems;
    private int bitmapLength;
    private int nextAvail;
    private int numAvail;
    
    PoolSubpage(final int pageSize) {
        this.chunk = null;
        this.memoryMapIdx = -1;
        this.runOffset = -1;
        this.elemSize = -1;
        this.pageSize = pageSize;
        this.bitmap = null;
    }
    
    PoolSubpage(final PoolSubpage<T> head, final PoolChunk<T> chunk, final int memoryMapIdx, final int runOffset, final int pageSize, final int elemSize) {
        this.chunk = chunk;
        this.memoryMapIdx = memoryMapIdx;
        this.runOffset = runOffset;
        this.pageSize = pageSize;
        this.bitmap = new long[pageSize >>> 10];
        this.init(head, elemSize);
    }
    
    void init(final PoolSubpage<T> head, final int elemSize) {
        this.doNotDestroy = true;
        this.elemSize = elemSize;
        if (elemSize != 0) {
            final int n = this.pageSize / elemSize;
            this.numAvail = n;
            this.maxNumElems = n;
            this.nextAvail = 0;
            this.bitmapLength = this.maxNumElems >>> 6;
            if ((this.maxNumElems & 0x3F) != 0x0) {
                ++this.bitmapLength;
            }
            for (int i = 0; i < this.bitmapLength; ++i) {
                this.bitmap[i] = 0L;
            }
        }
        this.addToPool(head);
    }
    
    long allocate() {
        if (this.elemSize == 0) {
            return this.toHandle(0);
        }
        if (this.numAvail == 0 || !this.doNotDestroy) {
            return -1L;
        }
        final int bitmapIdx = this.getNextAvail();
        final int q = bitmapIdx >>> 6;
        final int r = bitmapIdx & 0x3F;
        assert (this.bitmap[q] >>> r & 0x1L) == 0x0L;
        final long[] bitmap = this.bitmap;
        final int n = q;
        bitmap[n] |= 1L << r;
        if (--this.numAvail == 0) {
            this.removeFromPool();
        }
        return this.toHandle(bitmapIdx);
    }
    
    boolean free(final PoolSubpage<T> head, final int bitmapIdx) {
        if (this.elemSize == 0) {
            return true;
        }
        final int q = bitmapIdx >>> 6;
        final int r = bitmapIdx & 0x3F;
        assert (this.bitmap[q] >>> r & 0x1L) != 0x0L;
        final long[] bitmap = this.bitmap;
        final int n = q;
        bitmap[n] ^= 1L << r;
        this.setNextAvail(bitmapIdx);
        if (this.numAvail++ == 0) {
            this.addToPool(head);
            return true;
        }
        if (this.numAvail != this.maxNumElems) {
            return true;
        }
        if (this.prev == this.next) {
            return true;
        }
        this.doNotDestroy = false;
        this.removeFromPool();
        return false;
    }
    
    private void addToPool(final PoolSubpage<T> head) {
        assert this.prev == null && this.next == null;
        this.prev = head;
        this.next = head.next;
        this.next.prev = this;
        head.next = this;
    }
    
    private void removeFromPool() {
        assert this.prev != null && this.next != null;
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
    }
    
    private void setNextAvail(final int bitmapIdx) {
        this.nextAvail = bitmapIdx;
    }
    
    private int getNextAvail() {
        final int nextAvail = this.nextAvail;
        if (nextAvail >= 0) {
            this.nextAvail = -1;
            return nextAvail;
        }
        return this.findNextAvail();
    }
    
    private int findNextAvail() {
        final long[] bitmap = this.bitmap;
        for (int bitmapLength = this.bitmapLength, i = 0; i < bitmapLength; ++i) {
            final long bits = bitmap[i];
            if (~bits != 0x0L) {
                return this.findNextAvail0(i, bits);
            }
        }
        return -1;
    }
    
    private int findNextAvail0(final int i, long bits) {
        final int maxNumElems = this.maxNumElems;
        final int baseVal = i << 6;
        int j = 0;
        while (j < 64) {
            if ((bits & 0x1L) == 0x0L) {
                final int val = baseVal | j;
                if (val < maxNumElems) {
                    return val;
                }
                break;
            }
            else {
                bits >>>= 1;
                ++j;
            }
        }
        return -1;
    }
    
    private long toHandle(final int bitmapIdx) {
        return 0x4000000000000000L | (long)bitmapIdx << 32 | (long)this.memoryMapIdx;
    }
    
    public String toString() {
        boolean doNotDestroy;
        int elemSize;
        int maxNumElems;
        int numAvail;
        synchronized (this.chunk.arena) {
            if (!this.doNotDestroy) {
                doNotDestroy = false;
                numAvail = (maxNumElems = (elemSize = -1));
            }
            else {
                doNotDestroy = true;
                maxNumElems = this.maxNumElems;
                numAvail = this.numAvail;
                elemSize = this.elemSize;
            }
        }
        if (!doNotDestroy) {
            return new StringBuilder().append("(").append(this.memoryMapIdx).append(": not in use)").toString();
        }
        return new StringBuilder().append("(").append(this.memoryMapIdx).append(": ").append(maxNumElems - numAvail).append('/').append(maxNumElems).append(", offset: ").append(this.runOffset).append(", length: ").append(this.pageSize).append(", elemSize: ").append(elemSize).append(')').toString();
    }
    
    public int maxNumElements() {
        synchronized (this.chunk.arena) {
            return this.maxNumElems;
        }
    }
    
    public int numAvailable() {
        synchronized (this.chunk.arena) {
            return this.numAvail;
        }
    }
    
    public int elementSize() {
        synchronized (this.chunk.arena) {
            return this.elemSize;
        }
    }
    
    public int pageSize() {
        return this.pageSize;
    }
    
    void destroy() {
        if (this.chunk != null) {
            this.chunk.destroy();
        }
    }
}
