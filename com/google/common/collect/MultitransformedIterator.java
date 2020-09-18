package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
abstract class MultitransformedIterator<F, T> implements Iterator<T> {
    final Iterator<? extends F> backingIterator;
    private Iterator<? extends T> current;
    private Iterator<? extends T> removeFrom;
    
    MultitransformedIterator(final Iterator<? extends F> backingIterator) {
        this.current = Iterators.emptyIterator();
        this.backingIterator = Preconditions.<Iterator<? extends F>>checkNotNull(backingIterator);
    }
    
    abstract Iterator<? extends T> transform(final F object);
    
    public boolean hasNext() {
        Preconditions.<Iterator<? extends T>>checkNotNull(this.current);
        if (this.current.hasNext()) {
            return true;
        }
        while (this.backingIterator.hasNext()) {
            Preconditions.<Iterator<? extends T>>checkNotNull(this.current = this.transform(this.backingIterator.next()));
            if (this.current.hasNext()) {
                return true;
            }
        }
        return false;
    }
    
    public T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        this.removeFrom = this.current;
        return (T)this.current.next();
    }
    
    public void remove() {
        CollectPreconditions.checkRemove(this.removeFrom != null);
        this.removeFrom.remove();
        this.removeFrom = null;
    }
}
