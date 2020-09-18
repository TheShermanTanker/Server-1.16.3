package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2LongFunction extends Function<Character, Long>, IntToLongFunction {
    @Deprecated
    default long applyAsLong(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default long put(final char key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final char character);
    
    default long remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Character key, final Long value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
