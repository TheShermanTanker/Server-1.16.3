package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;
import java.util.ListIterator;

@GwtCompatible
abstract class TransformedListIterator<F, T> extends TransformedIterator<F, T> implements ListIterator<T> {
    TransformedListIterator(final ListIterator<? extends F> backingIterator) {
        super((Iterator)backingIterator);
    }
    
    private ListIterator<? extends F> backingIterator() {
        return Iterators.cast(this.backingIterator);
    }
    
    public final boolean hasPrevious() {
        return this.backingIterator().hasPrevious();
    }
    
    public final T previous() {
        return this.transform((F)this.backingIterator().previous());
    }
    
    public final int nextIndex() {
        return this.backingIterator().nextIndex();
    }
    
    public final int previousIndex() {
        return this.backingIterator().previousIndex();
    }
    
    public void set(final T element) {
        throw new UnsupportedOperationException();
    }
    
    public void add(final T element) {
        throw new UnsupportedOperationException();
    }
}
