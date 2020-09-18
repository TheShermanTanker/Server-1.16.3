package it.unimi.dsi.fastutil.bytes;

import java.util.ListIterator;

public interface ByteListIterator extends ByteBidirectionalIterator, ListIterator<Byte> {
    default void set(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Byte next() {
        return super.next();
    }
    
    @Deprecated
    default Byte previous() {
        return super.previous();
    }
}
