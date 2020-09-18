package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2ByteFunction extends Function<Short, Byte>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default byte put(final short key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final short short1);
    
    default byte remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Short key, final Byte value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
