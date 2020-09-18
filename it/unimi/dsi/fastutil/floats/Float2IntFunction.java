package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2IntFunction extends Function<Float, Integer>, DoubleToIntFunction {
    @Deprecated
    default int applyAsInt(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default int put(final float key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final float float1);
    
    default int remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Float key, final Integer value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
