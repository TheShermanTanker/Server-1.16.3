package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2ReferenceFunction<V> extends Function<Float, V>, DoubleFunction<V> {
    @Deprecated
    default V apply(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default V put(final float key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final float float1);
    
    default V remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Float key, final V value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
