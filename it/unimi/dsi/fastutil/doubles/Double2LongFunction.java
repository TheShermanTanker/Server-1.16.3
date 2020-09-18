package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2LongFunction extends Function<Double, Long>, DoubleToLongFunction {
    default long applyAsLong(final double operand) {
        return this.get(operand);
    }
    
    default long put(final double key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final double double1);
    
    default long remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Double key, final Long value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
