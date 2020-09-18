package it.unimi.dsi.fastutil.ints;

import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2FloatFunction extends Function<Integer, Float>, IntToDoubleFunction {
    default double applyAsDouble(final int operand) {
        return this.get(operand);
    }
    
    default float put(final int key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final int integer);
    
    default float remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Integer key, final Float value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
