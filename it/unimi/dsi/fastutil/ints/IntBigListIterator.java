package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface IntBigListIterator extends IntBidirectionalIterator, BigListIterator<Integer> {
    default void set(final int k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final int k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Integer k) {
        this.set((int)k);
    }
    
    @Deprecated
    default void add(final Integer k) {
        this.add((int)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextInt();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousInt();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
