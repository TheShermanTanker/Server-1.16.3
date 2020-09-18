package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2IntFunction extends Function<Character, Integer>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default int put(final char key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final char character);
    
    default int remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Character key, final Integer value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
