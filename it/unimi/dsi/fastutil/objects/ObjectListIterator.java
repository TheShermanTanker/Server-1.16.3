package it.unimi.dsi.fastutil.objects;

import java.util.ListIterator;

public interface ObjectListIterator<K> extends ObjectBidirectionalIterator<K>, ListIterator<K> {
    default void set(final K k) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final K k) {
        throw new UnsupportedOperationException();
    }
    
    default void remove() {
        throw new UnsupportedOperationException();
    }
}
