package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2DoubleFunction extends Function<Byte, Double>, IntToDoubleFunction {
    @Deprecated
    default double applyAsDouble(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default double put(final byte key, final double value) {
        throw new UnsupportedOperationException();
    }
    
    double get(final byte byte1);
    
    default double remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Double put(final Byte key, final Double value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final double v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Double.valueOf(v) : null;
    }
    
    @Deprecated
    default Double remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final double rv) {
        throw new UnsupportedOperationException();
    }
    
    default double defaultReturnValue() {
        return 0.0;
    }
}
