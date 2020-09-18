package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2LongFunction extends Function<Byte, Long>, IntToLongFunction {
    @Deprecated
    default long applyAsLong(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default long put(final byte key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final byte byte1);
    
    default long remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Byte key, final Long value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
