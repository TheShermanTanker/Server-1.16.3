package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public abstract class ForwardingIterator<T> extends ForwardingObject implements Iterator<T> {
    protected ForwardingIterator() {
    }
    
    @Override
    protected abstract Iterator<T> delegate();
    
    public boolean hasNext() {
        return this.delegate().hasNext();
    }
    
    @CanIgnoreReturnValue
    public T next() {
        return (T)this.delegate().next();
    }
    
    public void remove() {
        this.delegate().remove();
    }
}
