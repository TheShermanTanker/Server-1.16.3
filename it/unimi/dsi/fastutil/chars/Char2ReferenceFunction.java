package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2ReferenceFunction<V> extends Function<Character, V>, IntFunction<V> {
    @Deprecated
    default V apply(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default V put(final char key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    V get(final char character);
    
    default V remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default V put(final Character key, final V value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final V v = this.put(k, value);
        return containsKey ? v : null;
    }
    
    @Deprecated
    default V get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final V v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? v : null;
    }
    
    @Deprecated
    default V remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? this.remove(k) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final V rv) {
        throw new UnsupportedOperationException();
    }
    
    default V defaultReturnValue() {
        return null;
    }
}
