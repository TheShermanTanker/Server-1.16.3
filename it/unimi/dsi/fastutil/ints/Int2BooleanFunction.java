package it.unimi.dsi.fastutil.ints;

import java.util.function.IntPredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2BooleanFunction extends Function<Integer, Boolean>, IntPredicate {
    default boolean test(final int operand) {
        return this.get(operand);
    }
    
    default boolean put(final int key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final int integer);
    
    default boolean remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Integer key, final Boolean value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
