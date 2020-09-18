package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface DoubleBigListIterator extends DoubleBidirectionalIterator, BigListIterator<Double> {
    default void set(final double k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final double k) {
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
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextDouble();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousDouble();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
