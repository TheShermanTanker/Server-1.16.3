package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntPredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Byte2BooleanFunction extends Function<Byte, Boolean>, IntPredicate {
    @Deprecated
    default boolean test(final int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }
    
    default boolean put(final byte key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final byte byte1);
    
    default boolean remove(final byte key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Byte key, final Boolean value) {
        final byte k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final byte k = (byte)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final byte key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((byte)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
