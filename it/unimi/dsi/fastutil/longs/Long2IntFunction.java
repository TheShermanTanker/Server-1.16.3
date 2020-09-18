package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2IntFunction extends Function<Long, Integer>, LongToIntFunction {
    default int applyAsInt(final long operand) {
        return this.get(operand);
    }
    
    default int put(final long key, final int value) {
        throw new UnsupportedOperationException();
    }
    
    int get(final long long1);
    
    default int remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Integer put(final Long key, final Integer value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final int v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Integer.valueOf(v) : null;
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final int rv) {
        throw new UnsupportedOperationException();
    }
    
    default int defaultReturnValue() {
        return 0;
    }
}
