package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2FloatFunction extends Function<Float, Float>, DoubleUnaryOperator {
    @Deprecated
    default double applyAsDouble(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default float put(final float key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final float float1);
    
    default float remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Float key, final Float value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
