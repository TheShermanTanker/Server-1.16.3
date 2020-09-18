package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
abstract class TransformedIterator<F, T> implements Iterator<T> {
    final Iterator<? extends F> backingIterator;
    
    TransformedIterator(final Iterator<? extends F> backingIterator) {
        this.backingIterator = Preconditions.<Iterator<? extends F>>checkNotNull(backingIterator);
    }
    
    abstract T transform(final F object);
    
    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }
    
    public final T next() {
        return this.transform(this.backingIterator.next());
    }
    
    public final void remove() {
        this.backingIterator.remove();
    }
}
