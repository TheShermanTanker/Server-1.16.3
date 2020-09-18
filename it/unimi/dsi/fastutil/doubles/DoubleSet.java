package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.Set;

public interface DoubleSet extends DoubleCollection, Set<Double> {
    DoubleIterator iterator();
    
    boolean remove(final double double1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Double o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final double k) {
        return this.remove(k);
    }
}
