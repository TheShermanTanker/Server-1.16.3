package it.unimi.dsi.fastutil.floats;

import java.util.ListIterator;

public interface FloatListIterator extends FloatBidirectionalIterator, ListIterator<Float> {
    default void set(final float k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final float k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Float next() {
        return super.next();
    }
    
    @Deprecated
    default Float previous() {
        return super.previous();
    }
}
