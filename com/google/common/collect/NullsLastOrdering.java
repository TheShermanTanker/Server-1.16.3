package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NullsLastOrdering<T> extends Ordering<T> implements Serializable {
    final Ordering<? super T> ordering;
    private static final long serialVersionUID = 0L;
    
    NullsLastOrdering(final Ordering<? super T> ordering) {
        this.ordering = ordering;
    }
    
    @Override
    public int compare(@Nullable final T left, @Nullable final T right) {
        if (left == right) {
            return 0;
        }
        if (left == null) {
            return 1;
        }
        if (right == null) {
            return -1;
        }
        return this.ordering.compare(left, right);
    }
    
    @Override
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().<S>nullsFirst();
    }
    
    @Override
    public <S extends T> Ordering<S> nullsFirst() {
        return this.ordering.<S>nullsFirst();
    }
    
    @Override
    public <S extends T> Ordering<S> nullsLast() {
        return (Ordering<S>)this;
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof NullsLastOrdering) {
            final NullsLastOrdering<?> that = object;
            return this.ordering.equals(that.ordering);
        }
        return false;
    }
    
    public int hashCode() {
        return this.ordering.hashCode() ^ 0xC9177248;
    }
    
    public String toString() {
        return new StringBuilder().append(this.ordering).append(".nullsLast()").toString();
    }
}
