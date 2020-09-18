package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface LongBigListIterator extends LongBidirectionalIterator, BigListIterator<Long> {
    default void set(final long k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Long k) {
        this.set((long)k);
    }
    
    @Deprecated
    default void add(final Long k) {
        this.add((long)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextLong();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousLong();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
