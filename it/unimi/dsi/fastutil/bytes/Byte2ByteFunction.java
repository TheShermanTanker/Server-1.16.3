package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2ByteFunction extends Function<Byte, Byte>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default byte put(final byte key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final byte byte1);
    
    default byte remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Byte key, final Byte value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
