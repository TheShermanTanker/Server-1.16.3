package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2FloatFunction extends Function<Byte, Float>, IntToDoubleFunction {
    @Deprecated
    default double applyAsDouble(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default float put(final byte key, final float value) {
        throw new UnsupportedOperationException();
    }
    
    float get(final byte byte1);
    
    default float remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Float put(final Byte key, final Float value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final float v = this.put(k, (float)value);
        return containsKey ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final float v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Float.valueOf(v) : null;
    }
    
    @Deprecated
    default Float remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Float.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final float rv) {
        throw new UnsupportedOperationException();
    }
    
    default float defaultReturnValue() {
        return 0.0f;
    }
}
