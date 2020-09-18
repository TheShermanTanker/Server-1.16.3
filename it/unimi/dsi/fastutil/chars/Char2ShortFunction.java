package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2ShortFunction extends Function<Character, Short>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default short put(final char key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final char character);
    
    default short remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Character key, final Short value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
