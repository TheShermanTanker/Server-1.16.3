package com.google.common.collect;

import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
    final ImmutableList<Comparator<? super T>> comparators;
    private static final long serialVersionUID = 0L;
    
    CompoundOrdering(final Comparator<? super T> primary, final Comparator<? super T> secondary) {
        this.comparators = ImmutableList.<Comparator<? super T>>of(primary, secondary);
    }
    
    CompoundOrdering(final Iterable<? extends Comparator<? super T>> comparators) {
        this.comparators = ImmutableList.<Comparator<? super T>>copyOf(comparators);
    }
    
    @Override
    public int compare(final T left, final T right) {
        for (int size = this.comparators.size(), i = 0; i < size; ++i) {
            final int result = ((Comparator)this.comparators.get(i)).compare(left, right);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
    
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof CompoundOrdering) {
            final CompoundOrdering<?> that = object;
            return this.comparators.equals(that.comparators);
        }
        return false;
    }
    
    public int hashCode() {
        return this.comparators.hashCode();
    }
    
    public String toString() {
        return new StringBuilder().append("Ordering.compound(").append(this.comparators).append(")").toString();
    }
}
