package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2LongFunction extends Function<Float, Long>, DoubleToLongFunction {
    @Deprecated
    default long applyAsLong(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default long put(final float key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final float float1);
    
    default long remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Float key, final Long value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
