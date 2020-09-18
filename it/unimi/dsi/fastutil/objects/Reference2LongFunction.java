package it.unimi.dsi.fastutil.objects;

import java.util.function.ToLongFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Reference2LongFunction<K> extends Function<K, Long>, ToLongFunction<K> {
    default long applyAsLong(final K operand) {
        return this.getLong(operand);
    }
    
    default long put(final K key, final long value) {
        throw new UnsupportedOperationException();
    }
    
    long getLong(final Object object);
    
    default long removeLong(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Long put(final K key, final Long value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final long v = this.put(k, (long)value);
        return containsKey ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long get(final Object key) {
        final Object k = key;
        final long v = this.getLong(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Long.valueOf(v) : null;
    }
    
    @Deprecated
    default Long remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Long.valueOf(this.removeLong(k)) : null;
    }
    
    default void defaultReturnValue(final long rv) {
        throw new UnsupportedOperationException();
    }
    
    default long defaultReturnValue() {
        return 0L;
    }
}
