package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2CharFunction extends Function<Float, Character>, DoubleToIntFunction {
    @Deprecated
    default int applyAsInt(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default char put(final float key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final float float1);
    
    default char remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Float key, final Character value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
