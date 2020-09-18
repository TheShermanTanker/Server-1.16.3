package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2FloatFunction extends Function<Double, Float>, DoubleUnaryOperator {
    default double applyAsDouble(final double operand) {
        return this.get(operand);
    }
    
    default float put(final double key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final double double1);
    
    default float remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Double key, final Float value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
