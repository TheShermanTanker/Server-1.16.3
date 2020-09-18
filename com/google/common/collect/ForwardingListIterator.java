package com.google.common.collect;

import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@GwtCompatible
public abstract class ForwardingListIterator<E> extends ForwardingIterator<E> implements ListIterator<E> {
    protected ForwardingListIterator() {
    }
    
    protected abstract ListIterator<E> delegate();
    
    public void add(final E element) {
        this.delegate().add(element);
    }
    
    public boolean hasPrevious() {
        return this.delegate().hasPrevious();
    }
    
    public int nextIndex() {
        return this.delegate().nextIndex();
    }
    
    @CanIgnoreReturnValue
    public E previous() {
        return (E)this.delegate().previous();
    }
    
    public int previousIndex() {
        return this.delegate().previousIndex();
    }
    
    public void set(final E element) {
        this.delegate().set(element);
    }
}
