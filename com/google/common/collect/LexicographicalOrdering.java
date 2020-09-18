package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class LexicographicalOrdering<T> extends Ordering<Iterable<T>> implements Serializable {
    final Comparator<? super T> elementOrder;
    private static final long serialVersionUID = 0L;
    
    LexicographicalOrdering(final Comparator<? super T> elementOrder) {
        this.elementOrder = elementOrder;
    }
    
    @Override
    public int compare(final Iterable<T> leftIterable, final Iterable<T> rightIterable) {
        final Iterator<T> left = (Iterator<T>)leftIterable.iterator();
        final Iterator<T> right = (Iterator<T>)rightIterable.iterator();
        while (left.hasNext()) {
            if (!right.hasNext()) {
                return 1;
            }
            final int result = this.elementOrder.compare(left.next(), right.next());
            if (result != 0) {
                return result;
            }
        }
        if (right.hasNext()) {
            return -1;
        }
        return 0;
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LexicographicalOrdering) {
            final LexicographicalOrdering<?> that = object;
            return this.elementOrder.equals(that.elementOrder);
        }
        return false;
    }
    
    public int hashCode() {
        return this.elementOrder.hashCode() ^ 0x7BB78CF5;
    }
    
    public String toString() {
        return new StringBuilder().append(this.elementOrder).append(".lexicographical()").toString();
    }
}
