package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCountedByteBuf extends AbstractByteBuf {
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater;
    private volatile int refCnt;
    
    protected AbstractReferenceCountedByteBuf(final int maxCapacity) {
        super(maxCapacity);
        AbstractReferenceCountedByteBuf.refCntUpdater.set(this, 1);
    }
    
    public int refCnt() {
        return this.refCnt;
    }
    
    protected final void setRefCnt(final int refCnt) {
        AbstractReferenceCountedByteBuf.refCntUpdater.set(this, refCnt);
    }
    
    @Override
    public ByteBuf retain() {
        return this.retain0(1);
    }
    
    @Override
    public ByteBuf retain(final int increment) {
        return this.retain0(ObjectUtil.checkPositive(increment, "increment"));
    }
    
    private ByteBuf retain0(final int increment) {
        final int oldRef = AbstractReferenceCountedByteBuf.refCntUpdater.getAndAdd(this, increment);
        if (oldRef <= 0 || oldRef + increment < oldRef) {
            AbstractReferenceCountedByteBuf.refCntUpdater.getAndAdd(this, -increment);
            throw new IllegalReferenceCountException(oldRef, increment);
        }
        return this;
    }
    
    @Override
    public ByteBuf touch() {
        return this;
    }
    
    @Override
    public ByteBuf touch(final Object hint) {
        return this;
    }
    
    public boolean release() {
        return this.release0(1);
    }
    
    public boolean release(final int decrement) {
        return this.release0(ObjectUtil.checkPositive(decrement, "decrement"));
    }
    
    private boolean release0(final int decrement) {
        final int oldRef = AbstractReferenceCountedByteBuf.refCntUpdater.getAndAdd(this, -decrement);
        if (oldRef == decrement) {
            this.deallocate();
            return true;
        }
        if (oldRef < decrement || oldRef - decrement > oldRef) {
            AbstractReferenceCountedByteBuf.refCntUpdater.getAndAdd(this, decrement);
            throw new IllegalReferenceCountException(oldRef, -decrement);
        }
        return false;
    }
    
    protected abstract void deallocate();
    
    static {
        refCntUpdater = AtomicIntegerFieldUpdater.newUpdater((Class)AbstractReferenceCountedByteBuf.class, "refCnt");
    }
}
