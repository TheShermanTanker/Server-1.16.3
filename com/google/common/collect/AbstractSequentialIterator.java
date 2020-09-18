package com.google.common.collect;

import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T> {
    private T nextOrNull;
    
    protected AbstractSequentialIterator(@Nullable final T firstOrNull) {
        this.nextOrNull = firstOrNull;
    }
    
    protected abstract T computeNext(final T object);
    
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }
    
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return this.nextOrNull;
        }
        finally {
            this.nextOrNull = this.computeNext(this.nextOrNull);
        }
    }
}
