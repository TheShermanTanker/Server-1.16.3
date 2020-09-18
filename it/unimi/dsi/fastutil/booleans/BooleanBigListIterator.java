package it.unimi.dsi.fastutil.booleans;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface BooleanBigListIterator extends BooleanBidirectionalIterator, BigListIterator<Boolean> {
    default void set(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Boolean k) {
        this.set((boolean)k);
    }
    
    @Deprecated
    default void add(final Boolean k) {
        this.add((boolean)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousBoolean();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
