package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class AbstractReferenceCounted implements ReferenceCounted {
    private static final AtomicIntegerFieldUpdater<AbstractReferenceCounted> refCntUpdater;
    private volatile int refCnt;
    
    public AbstractReferenceCounted() {
        this.refCnt = 1;
    }
    
    public final int refCnt() {
        return this.refCnt;
    }
    
    protected final void setRefCnt(final int refCnt) {
        AbstractReferenceCounted.refCntUpdater.set(this, refCnt);
    }
    
    public ReferenceCounted retain() {
        return this.retain0(1);
    }
    
    public ReferenceCounted retain(final int increment) {
        return this.retain0(ObjectUtil.checkPositive(increment, "increment"));
    }
    
    private ReferenceCounted retain0(final int increment) {
        final int oldRef = AbstractReferenceCounted.refCntUpdater.getAndAdd(this, increment);
        if (oldRef <= 0 || oldRef + increment < oldRef) {
            AbstractReferenceCounted.refCntUpdater.getAndAdd(this, -increment);
            throw new IllegalReferenceCountException(oldRef, increment);
        }
        return this;
    }
    
    public ReferenceCounted touch() {
        return this.touch(null);
    }
    
    public boolean release() {
        return this.release0(1);
    }
    
    public boolean release(final int decrement) {
        return this.release0(ObjectUtil.checkPositive(decrement, "decrement"));
    }
    
    private boolean release0(final int decrement) {
        final int oldRef = AbstractReferenceCounted.refCntUpdater.getAndAdd(this, -decrement);
        if (oldRef == decrement) {
            this.deallocate();
            return true;
        }
        if (oldRef < decrement || oldRef - decrement > oldRef) {
            AbstractReferenceCounted.refCntUpdater.getAndAdd(this, decrement);
            throw new IllegalReferenceCountException(oldRef, -decrement);
        }
        return false;
    }
    
    protected abstract void deallocate();
    
    static {
        refCntUpdater = AtomicIntegerFieldUpdater.newUpdater((Class)AbstractReferenceCounted.class, "refCnt");
    }
}
