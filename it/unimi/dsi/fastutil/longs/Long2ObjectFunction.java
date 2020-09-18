package it.unimi.dsi.fastutil.longs;

import java.util.function.LongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2ObjectFunction<V> extends Function<Long, V>, LongFunction<V> {
    default V apply(final long operand) {
        return this.get(operand);
    }
    
    default V put(final long key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final long long1);
    
    default V remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Long key, final V value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
