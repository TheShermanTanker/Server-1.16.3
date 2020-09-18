package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2DoubleFunction extends Function<Short, Double>, IntToDoubleFunction {
    @Deprecated
    default double applyAsDouble(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default double put(final short key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final short short1);
    
    default double remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Short key, final Double value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
