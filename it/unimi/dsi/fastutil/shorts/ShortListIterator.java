package it.unimi.dsi.fastutil.shorts;

import java.util.ListIterator;

public interface ShortListIterator extends ShortBidirectionalIterator, ListIterator<Short> {
    default void set(final short k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final short k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Short next() {
        return super.next();
    }
    
    @Deprecated
    default Short previous() {
        return super.previous();
    }
}
