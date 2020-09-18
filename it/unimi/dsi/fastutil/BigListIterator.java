package it.unimi.dsi.fastutil;

public interface BigListIterator<K> extends BidirectionalIterator<K> {
    long nextIndex();
    
    long previousIndex();
    
    default void set(final K e) {
        throw new UnsupportedOperationException();
    }
    
    default void add(final K e) {
        throw new UnsupportedOperationException();
    }
}
