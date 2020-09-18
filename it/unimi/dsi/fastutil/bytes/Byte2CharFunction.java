package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2CharFunction extends Function<Byte, Character>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default char put(final byte key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final byte byte1);
    
    default char remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Byte key, final Character value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
