package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Char2ByteFunction extends Function<Character, Byte>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToChar(operand));
    }
    
    default byte put(final char key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final char character);
    
    default byte remove(final char key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Character key, final Byte value) {
        final char k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final char k = (char)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final char key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((char)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
