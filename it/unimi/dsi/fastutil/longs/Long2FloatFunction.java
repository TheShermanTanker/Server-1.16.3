package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2FloatFunction extends Function<Long, Float>, LongToDoubleFunction {
    default double applyAsDouble(final long operand) {
        return this.get(operand);
    }
    
    default float put(final long key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final long long1);
    
    default float remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Long key, final Float value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
