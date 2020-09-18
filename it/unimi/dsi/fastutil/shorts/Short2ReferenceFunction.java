package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2ReferenceFunction<V> extends Function<Short, V>, IntFunction<V> {
    @Deprecated
    default V apply(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default V put(final short key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final short short1);
    
    default V remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Short key, final V value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
