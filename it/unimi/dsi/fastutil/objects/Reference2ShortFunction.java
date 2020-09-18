package it.unimi.dsi.fastutil.objects;

import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Reference2ShortFunction<K> extends Function<K, Short>, ToIntFunction<K> {
    default int applyAsInt(final K operand) {
        return this.getShort(operand);
    }
    
    default short put(final K key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short getShort(final Object object);
    
    default short removeShort(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final K key, final Short value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        final Object k = key;
        final short v = this.getShort(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Short.valueOf(this.removeShort(k)) : null;
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
