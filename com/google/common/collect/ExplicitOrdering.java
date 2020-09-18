package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable {
    final ImmutableMap<T, Integer> rankMap;
    private static final long serialVersionUID = 0L;
    
    ExplicitOrdering(final List<T> valuesInOrder) {
        this(Maps.indexMap((java.util.Collection<Object>)valuesInOrder));
    }
    
    ExplicitOrdering(final ImmutableMap<T, Integer> rankMap) {
        this.rankMap = rankMap;
    }
    
    @Override
    public int compare(final T left, final T right) {
        return this.rank(left) - this.rank(right);
    }
    
    private int rank(final T value) {
        final Integer rank = this.rankMap.get(value);
        if (rank == null) {
            throw new IncomparableValueException(value);
        }
        return rank;
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object instanceof ExplicitOrdering) {
            final ExplicitOrdering<?> that = object;
            return this.rankMap.equals(that.rankMap);
        }
        return false;
    }
    
    public int hashCode() {
        return this.rankMap.hashCode();
    }
    
    public String toString() {
        return new StringBuilder().append("Ordering.explicit(").append(this.rankMap.keySet()).append(")").toString();
    }
}
