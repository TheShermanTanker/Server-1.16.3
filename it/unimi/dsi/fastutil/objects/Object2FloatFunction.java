package it.unimi.dsi.fastutil.objects;

import java.util.function.ToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Object2FloatFunction<K> extends Function<K, Float>, ToDoubleFunction<K> {
    default double applyAsDouble(final K operand) {
        return this.getFloat(operand);
    }
    
    default float put(final K key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float getFloat(final Object object);
    
    default float removeFloat(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final K key, final Float value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        final Object k = key;
        final float v = this.getFloat(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Float.valueOf(this.removeFloat(k)) : null;
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
