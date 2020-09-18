package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2DoubleFunction extends Function<Float, Double>, DoubleUnaryOperator {
    @Deprecated
    default double applyAsDouble(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default double put(final float key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final float float1);
    
    default double remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Float key, final Double value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
