package com.google.common.collect;

import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ReverseNaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final ReverseNaturalOrdering INSTANCE;
    private static final long serialVersionUID = 0L;
    
    @Override
    public int compare(final Comparable left, final Comparable right) {
        Preconditions.<Comparable>checkNotNull(left);
        if (left == right) {
            return 0;
        }
        return right.compareTo(left);
    }
    
    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return Ordering.<S>natural();
    }
    
    public <E extends Comparable> E min(final E a, final E b) {
        return NaturalOrdering.INSTANCE.<E>max(a, b);
    }
    
    public <E extends Comparable> E min(final E a, final E b, final E c, final E... rest) {
        return NaturalOrdering.INSTANCE.<E>max(a, b, c, rest);
    }
    
    @Override
    public <E extends Comparable> E min(final Iterator<E> iterator) {
        return NaturalOrdering.INSTANCE.<E>max(iterator);
    }
    
    @Override
    public <E extends Comparable> E min(final Iterable<E> iterable) {
        return NaturalOrdering.INSTANCE.<E>max(iterable);
    }
    
    public <E extends Comparable> E max(final E a, final E b) {
        return NaturalOrdering.INSTANCE.<E>min(a, b);
    }
    
    public <E extends Comparable> E max(final E a, final E b, final E c, final E... rest) {
        return NaturalOrdering.INSTANCE.<E>min(a, b, c, rest);
    }
    
    @Override
    public <E extends Comparable> E max(final Iterator<E> iterator) {
        return NaturalOrdering.INSTANCE.<E>min(iterator);
    }
    
    @Override
    public <E extends Comparable> E max(final Iterable<E> iterable) {
        return NaturalOrdering.INSTANCE.<E>min(iterable);
    }
    
    private Object readResolve() {
        return ReverseNaturalOrdering.INSTANCE;
    }
    
    public String toString() {
        return "Ordering.natural().reverse()";
    }
    
    private ReverseNaturalOrdering() {
    }
    
    static {
        INSTANCE = new ReverseNaturalOrdering();
    }
}
