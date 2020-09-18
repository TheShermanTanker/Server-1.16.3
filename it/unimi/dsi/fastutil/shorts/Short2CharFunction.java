package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2CharFunction extends Function<Short, Character>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default char put(final short key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final short short1);
    
    default char remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Short key, final Character value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
