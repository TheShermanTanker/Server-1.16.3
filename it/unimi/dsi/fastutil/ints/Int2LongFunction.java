package it.unimi.dsi.fastutil.ints;

import java.util.function.IntToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2LongFunction extends Function<Integer, Long>, IntToLongFunction {
    default long applyAsLong(final int operand) {
        return this.get(operand);
    }
    
    default long put(final int key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final int integer);
    
    default long remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Integer key, final Long value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
