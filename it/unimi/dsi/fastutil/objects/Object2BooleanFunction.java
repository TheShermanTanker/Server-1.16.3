package it.unimi.dsi.fastutil.objects;

import java.util.function.Predicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Object2BooleanFunction<K> extends Function<K, Boolean>, Predicate<K> {
    default boolean test(final K operand) {
        return this.getBoolean(operand);
    }
    
    default boolean put(final K key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean getBoolean(final Object object);
    
    default boolean removeBoolean(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final K key, final Boolean value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        final Object k = key;
        final boolean v = this.getBoolean(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Boolean.valueOf(this.removeBoolean(k)) : null;
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
