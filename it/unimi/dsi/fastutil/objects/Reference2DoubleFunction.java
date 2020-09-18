package it.unimi.dsi.fastutil.objects;

import java.util.function.ToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Reference2DoubleFunction<K> extends Function<K, Double>, ToDoubleFunction<K> {
    default double applyAsDouble(final K operand) {
        return this.getDouble(operand);
    }
    
    default double put(final K key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double getDouble(final Object object);
    
    default double removeDouble(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final K key, final Double value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        final Object k = key;
        final double v = this.getDouble(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Double.valueOf(this.removeDouble(k)) : null;
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
