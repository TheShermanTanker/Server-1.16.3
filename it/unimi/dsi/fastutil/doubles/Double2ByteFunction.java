package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2ByteFunction extends Function<Double, Byte>, DoubleToIntFunction {
    default int applyAsInt(final double operand) {
        return this.get(operand);
    }
    
    default byte put(final double key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final double double1);
    
    default byte remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Double key, final Byte value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
