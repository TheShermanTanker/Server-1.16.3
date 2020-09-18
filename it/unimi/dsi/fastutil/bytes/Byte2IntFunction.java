package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2IntFunction extends Function<Byte, Integer>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default int put(final byte key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final byte byte1);
    
    default int remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Byte key, final Integer value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
