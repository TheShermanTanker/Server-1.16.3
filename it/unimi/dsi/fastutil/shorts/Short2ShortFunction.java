package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2ShortFunction extends Function<Short, Short>, IntUnaryOperator {
    @Deprecated
    default int applyAsInt(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default short put(final short key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final short short1);
    
    default short remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Short key, final Short value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
