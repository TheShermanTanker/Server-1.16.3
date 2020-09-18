package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2DoubleFunction extends Function<Long, Double>, LongToDoubleFunction {
    default double applyAsDouble(final long operand) {
        return this.get(operand);
    }
    
    default double put(final long key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final long long1);
    
    default double remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Long key, final Double value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
