package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface FloatBigListIterator extends FloatBidirectionalIterator, BigListIterator<Float> {
    default void set(final float k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Float k) {
        this.set((float)k);
    }
    
    @Deprecated
    default void add(final Float k) {
        this.add((float)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextFloat();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousFloat();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
