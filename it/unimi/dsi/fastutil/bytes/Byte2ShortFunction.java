package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2ShortFunction extends Function<Byte, Short>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default short put(final byte key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final byte byte1);
    
    default short remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Byte key, final Short value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
