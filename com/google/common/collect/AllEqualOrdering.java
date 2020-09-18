package com.google.common.collect;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class AllEqualOrdering extends Ordering<Object> implements Serializable {
    static final AllEqualOrdering INSTANCE;
    private static final long serialVersionUID = 0L;
    
    @Override
    public int compare(@Nullable final Object left, @Nullable final Object right) {
        return 0;
    }
    
    @Override
    public <E> List<E> sortedCopy(final Iterable<E> iterable) {
        return Lists.newArrayList((java.lang.Iterable<?>)iterable);
    }
    
    @Override
    public <E> ImmutableList<E> immutableSortedCopy(final Iterable<E> iterable) {
        return ImmutableList.<E>copyOf((java.lang.Iterable<? extends E>)iterable);
    }
    
    @Override
    public <S> Ordering<S> reverse() {
        return (Ordering<S>)this;
    }
    
    private Object readResolve() {
        return AllEqualOrdering.INSTANCE;
    }
    
    public String toString() {
        return "Ordering.allEqual()";
    }
    
    static {
        INSTANCE = new AllEqualOrdering();
    }
}
