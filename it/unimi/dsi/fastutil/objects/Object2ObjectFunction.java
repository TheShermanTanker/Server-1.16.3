package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Object2ObjectFunction<K, V> extends Function<K, V> {
    default V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final Object object);
    
    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
