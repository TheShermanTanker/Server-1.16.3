package it.unimi.dsi.fastutil.longs;

import java.util.function.LongUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2LongFunction extends Function<Long, Long>, LongUnaryOperator {
    default long applyAsLong(final long operand) {
        return this.get(operand);
    }
    
    default long put(final long key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long get(final long long1);
    
    default long remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final Long key, final Long value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final long v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Long.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
