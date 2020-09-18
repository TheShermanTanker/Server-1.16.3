package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2CharFunction extends Function<Double, Character>, DoubleToIntFunction {
    default int applyAsInt(final double operand) {
        return this.get(operand);
    }
    
    default char put(final double key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final double double1);
    
    default char remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Double key, final Character value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
