package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface ShortBigListIterator extends ShortBidirectionalIterator, BigListIterator<Short> {
    default void set(final short k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final short k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Short k) {
        this.set((short)k);
    }
    
    @Deprecated
    default void add(final Short k) {
        this.add((short)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextShort();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousShort();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
