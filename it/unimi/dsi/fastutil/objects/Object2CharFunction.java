package it.unimi.dsi.fastutil.objects;

import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Object2CharFunction<K> extends Function<K, Character>, ToIntFunction<K> {
    default int applyAsInt(final K operand) {
        return this.getChar(operand);
    }
    
    default char put(final K key, final char value) {
        throw new UnsupportedOperationException();
    }
    
    char getChar(final Object object);
    
    default char removeChar(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Character put(final K key, final Character value) {
        final K k = key;
        final boolean containsKey = this.containsKey(k);
        final char v = this.put(k, (char)value);
        return containsKey ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character get(final Object key) {
        final Object k = key;
        final char v = this.getChar(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Character.valueOf(v) : null;
    }
    
    @Deprecated
    default Character remove(final Object key) {
        final Object k = key;
        return this.containsKey(k) ? Character.valueOf(this.removeChar(k)) : null;
    }
    
    default void defaultReturnValue(final char rv) {
        throw new UnsupportedOperationException();
    }
    
    default char defaultReturnValue() {
        return '\0';
    }
}
