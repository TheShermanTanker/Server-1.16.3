package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2IntFunction extends Function<Double, Integer>, DoubleToIntFunction {
    default int applyAsInt(final double operand) {
        return this.get(operand);
    }
    
    default int put(final double key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final double double1);
    
    default int remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Double key, final Integer value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
