package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final NaturalOrdering INSTANCE;
    private transient Ordering<Comparable> nullsFirst;
    private transient Ordering<Comparable> nullsLast;
    private static final long serialVersionUID = 0L;
    
    @Override
    public int compare(final Comparable left, final Comparable right) {
        Preconditions.<Comparable>checkNotNull(left);
        Preconditions.<Comparable>checkNotNull(right);
        return left.compareTo(right);
    }
    
    @Override
    public <S extends Comparable> Ordering<S> nullsFirst() {
        Ordering<Comparable> result = this.nullsFirst;
        if (result == null) {
            final Ordering<Object> nullsFirst = super.nullsFirst();
            this.nullsFirst = (Ordering<Comparable>)nullsFirst;
            result = (Ordering<Comparable>)nullsFirst;
        }
        return (Ordering<S>)result;
    }
    
    @Override
    public <S extends Comparable> Ordering<S> nullsLast() {
        Ordering<Comparable> result = this.nullsLast;
        if (result == null) {
            final Ordering<Object> nullsLast = super.nullsLast();
            this.nullsLast = (Ordering<Comparable>)nullsLast;
            result = (Ordering<Comparable>)nullsLast;
        }
        return (Ordering<S>)result;
    }
    
    @Override
    public <S extends Comparable> Ordering<S> reverse() {
        return (Ordering<S>)ReverseNaturalOrdering.INSTANCE;
    }
    
    private Object readResolve() {
        return NaturalOrdering.INSTANCE;
    }
    
    public String toString() {
        return "Ordering.natural()";
    }
    
    private NaturalOrdering() {
    }
    
    static {
        INSTANCE = new NaturalOrdering();
    }
}
