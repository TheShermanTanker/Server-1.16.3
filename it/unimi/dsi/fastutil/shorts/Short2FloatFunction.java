package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2FloatFunction extends Function<Short, Float>, IntToDoubleFunction {
    @Deprecated
    default double applyAsDouble(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default float put(final short key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final short short1);
    
    default float remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Short key, final Float value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
