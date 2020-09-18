package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2ByteFunction extends Function<Long, Byte>, LongToIntFunction {
    default int applyAsInt(final long operand) {
        return this.get(operand);
    }
    
    default byte put(final long key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final long long1);
    
    default byte remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Long key, final Byte value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
