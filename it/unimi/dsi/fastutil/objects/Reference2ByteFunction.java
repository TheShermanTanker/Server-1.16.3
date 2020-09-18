package it.unimi.dsi.fastutil.objects;

import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Reference2ByteFunction<K> extends Function<K, Byte>, ToIntFunction<K> {
    default int applyAsInt(final K operand) {
        return this.getByte(operand);
    }
    
    default byte put(final K key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte getByte(final Object object);
    
    default byte removeByte(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final K key, final Byte value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        final Object k = key;
        final byte v = this.getByte(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Byte.valueOf(this.removeByte(k)) : null;
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
