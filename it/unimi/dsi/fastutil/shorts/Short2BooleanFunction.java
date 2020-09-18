package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntPredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2BooleanFunction extends Function<Short, Boolean>, IntPredicate {
    @Deprecated
    default boolean test(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default boolean put(final short key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final short short1);
    
    default boolean remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Short key, final Boolean value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
