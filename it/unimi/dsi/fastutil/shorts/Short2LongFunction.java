package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Short2LongFunction extends Function<Short, Long>, IntToLongFunction {
    @Deprecated
    default long applyAsLong(final int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }
    
    default long put(final short key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final short short1);
    
    default long remove(final short key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Short key, final Long value) {
        final short k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final short k = (short)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final short key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((short)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
