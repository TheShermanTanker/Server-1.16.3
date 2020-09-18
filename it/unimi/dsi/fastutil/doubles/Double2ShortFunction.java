package it.unimi.dsi.fastutil.doubles;

import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Double2ShortFunction extends Function<Double, Short>, DoubleToIntFunction {
    default int applyAsInt(final double operand) {
        return this.get(operand);
    }
    
    default short put(final double key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final double double1);
    
    default short remove(final double key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Double key, final Short value) {
        final double k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final double k = (double)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final double key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((double)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
