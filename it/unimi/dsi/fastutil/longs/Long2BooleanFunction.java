package it.unimi.dsi.fastutil.longs;

import java.util.function.LongPredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2BooleanFunction extends Function<Long, Boolean>, LongPredicate {
    default boolean test(final long operand) {
        return this.get(operand);
    }
    
    default boolean put(final long key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final long long1);
    
    default boolean remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Long key, final Boolean value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
