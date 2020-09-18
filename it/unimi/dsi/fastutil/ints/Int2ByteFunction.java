package it.unimi.dsi.fastutil.ints;

import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2ByteFunction extends Function<Integer, Byte>, IntUnaryOperator {
    default int applyAsInt(final int operand) {
        return this.get(operand);
    }
    
    default byte put(final int key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final int integer);
    
    default byte remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Integer key, final Byte value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
