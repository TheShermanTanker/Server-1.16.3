package it.unimi.dsi.fastutil.chars;

import java.util.ListIterator;

public interface CharListIterator extends CharBidirectionalIterator, ListIterator<Character> {
    default void set(final char k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final char k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
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
    
    @Deprecated
    default Character next() {
        return super.next();
    }
    
    @Deprecated
    default Character previous() {
        return super.previous();
    }
}
