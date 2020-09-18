package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface CharBigListIterator extends CharBidirectionalIterator, BigListIterator<Character> {
    default void set(final char k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Character k) {
        this.set((char)k);
    }
    
    @Deprecated
    default void add(final Character k) {
        this.add((char)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
