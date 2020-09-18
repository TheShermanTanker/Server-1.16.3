package it.unimi.dsi.fastutil.booleans;

import java.util.ListIterator;

public interface BooleanListIterator extends BooleanBidirectionalIterator, ListIterator<Boolean> {
    default void set(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Boolean next() {
        return super.next();
    }
    
    @Deprecated
    default Boolean previous() {
        return super.previous();
    }
}
