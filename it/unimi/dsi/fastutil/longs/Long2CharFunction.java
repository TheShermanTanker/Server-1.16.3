package it.unimi.dsi.fastutil.longs;

import java.util.function.LongToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Long2CharFunction extends Function<Long, Character>, LongToIntFunction {
    default int applyAsInt(final long operand) {
        return this.get(operand);
    }
    
    default char put(final long key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char get(final long long1);
    
    default char remove(final long key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final Long key, final Character value) {
        final long k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        final char v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        if (key == null) {
            return null;
        }
        final long k = (long)key;
        return this.containsKey(k) ? Character.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final long key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((long)key);
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
