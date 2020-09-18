package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2CharFunction extends Function<Character, Character>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default char put(final char key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final char character);
    
    default char remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Character key, final Character value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
