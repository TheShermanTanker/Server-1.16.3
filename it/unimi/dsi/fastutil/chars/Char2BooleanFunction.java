package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntPredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2BooleanFunction extends Function<Character, Boolean>, IntPredicate {
    @Deprecated
    default boolean test(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default boolean put(final char key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final char character);
    
    default boolean remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Character key, final Boolean value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
