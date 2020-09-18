package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2ObjectFunction<V> extends Function<Byte, V>, IntFunction<V> {
    @Deprecated
    default V apply(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default V put(final byte key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final byte byte1);
    
    default V remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Byte key, final V value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
