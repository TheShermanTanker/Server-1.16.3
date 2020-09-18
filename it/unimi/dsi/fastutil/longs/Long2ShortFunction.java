package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2ShortFunction extends Function<Long, Short>, LongToIntFunction {
    default int applyAsInt(final long operand) {
        return this.get(operand);
    }
    
    default short put(final long key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final long long1);
    
    default short remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Long key, final Short value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
