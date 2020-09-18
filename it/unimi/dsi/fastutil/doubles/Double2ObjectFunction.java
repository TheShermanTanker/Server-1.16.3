package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2ObjectFunction<V> extends Function<Double, V>, DoubleFunction<V> {
    default V apply(final double operand) {
        return this.get(operand);
    }
    
    default V put(final double key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final double double1);
    
    default V remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Double key, final V value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
