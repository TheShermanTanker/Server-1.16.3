package it.unimi.dsi.fastutil.ints;

import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2CharFunction extends Function<Integer, Character>, IntUnaryOperator {
    default int applyAsInt(final int operand) {
        return this.get(operand);
    }
    
    default char put(final int key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final int integer);
    
    default char remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Integer key, final Character value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
