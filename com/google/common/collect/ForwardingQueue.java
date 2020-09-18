package com.google.common.collect;

import java.util.Collection;
import java.util.NoSuchElementException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E> {
    protected ForwardingQueue() {
    }
    
    protected abstract Queue<E> delegate();
    
    @CanIgnoreReturnValue
    public boolean offer(final E o) {
        return this.delegate().offer(o);
    }
    
    @CanIgnoreReturnValue
    public E poll() {
        return (E)this.delegate().poll();
    }
    
    @CanIgnoreReturnValue
    public E remove() {
        return (E)this.delegate().remove();
    }
    
    public E peek() {
        return (E)this.delegate().peek();
    }
    
    public E element() {
        return (E)this.delegate().element();
    }
    
    protected boolean standardOffer(final E e) {
        try {
            return this.add(e);
        }
        catch (IllegalStateException caught) {
            return false;
        }
    }
    
    protected E standardPeek() {
        try {
            return this.element();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }
    
    protected E standardPoll() {
        try {
            return this.remove();
        }
        catch (NoSuchElementException caught) {
            return null;
        }
    }
}
