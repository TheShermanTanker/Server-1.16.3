package it.unimi.dsi.fastutil.ints;

import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2DoubleFunction extends Function<Integer, Double>, IntToDoubleFunction {
    default double applyAsDouble(final int operand) {
        return this.get(operand);
    }
    
    default double put(final int key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final int integer);
    
    default double remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Integer key, final Double value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
