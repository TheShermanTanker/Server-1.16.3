package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2DoubleFunction extends Function<Double, Double>, DoubleUnaryOperator {
    default double applyAsDouble(final double operand) {
        return this.get(operand);
    }
    
    default double put(final double key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final double double1);
    
    default double remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Double key, final Double value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
