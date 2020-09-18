package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import it.unimi.dsi.fastutil.BigListIterator;

public interface ByteBigListIterator extends ByteBidirectionalIterator, BigListIterator<Byte> {
    default void set(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default void set(final Byte k) {
        this.set((byte)k);
    }
    
    @Deprecated
    default void add(final Byte k) {
        this.add((byte)k);
    }
    
    default long skip(final long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1L;
    }
    
    default long back(final long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousByte();
        }
        return n - i - 1L;
    }
    
    default int skip(final int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}
