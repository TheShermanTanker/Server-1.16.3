package it.unimi.dsi.fastutil.longs;

import java.util.ListIterator;

public interface LongListIterator extends LongBidirectionalIterator, ListIterator<Long> {
    default void set(final long k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final long k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Long next() {
        return super.next();
    }
    
    @Deprecated
    default Long previous() {
        return super.previous();
    }
}
