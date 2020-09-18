package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoublePredicate;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2BooleanFunction extends Function<Double, Boolean>, DoublePredicate {
    default boolean test(final double operand) {
        return this.get(operand);
    }
    
    default boolean put(final double key, final boolean value) {
        throw new UnsupportedOperationException();
    }
    
    boolean get(final double double1);
    
    default boolean remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Boolean put(final Double key, final Boolean value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final boolean v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Boolean.valueOf(v) : null;
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final boolean rv) {
        throw new UnsupportedOperationException();
    }
    
    default boolean defaultReturnValue() {
        return false;
    }
}
