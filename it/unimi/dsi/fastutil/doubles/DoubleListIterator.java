package it.unimi.dsi.fastutil.doubles;

import java.util.ListIterator;

public interface DoubleListIterator extends DoubleBidirectionalIterator, ListIterator<Double> {
    default void set(final double k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final double k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Double k) {
        this.set((double)k);
    }
    
    @Deprecated
    default void add(final Double k) {
        this.add((double)k);
    }
    
    @Deprecated
    default Double next() {
        return super.next();
    }
    
    @Deprecated
    default Double previous() {
        return super.previous();
    }
}
