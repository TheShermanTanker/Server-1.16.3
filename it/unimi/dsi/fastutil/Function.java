package it.unimi.dsi.fastutil;

@FunctionalInterface
public interface Function<K, V> extends java.util.function.Function<K, V> {
    default V apply(final K key) {
        return this.get(key);
    }
    
    default V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final Object object);
    
    default boolean containsKey(final Object key) {
        return true;
    }
    
    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    default int size() {
        return -1;
    }
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
}