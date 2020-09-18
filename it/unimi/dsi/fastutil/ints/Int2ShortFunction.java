package it.unimi.dsi.fastutil.ints;

import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Int2ShortFunction extends Function<Integer, Short>, IntUnaryOperator {
    default int applyAsInt(final int operand) {
        return this.get(operand);
    }
    
    default short put(final int key, final short value) {
        throw new UnsupportedOperationException();
    }
    
    short get(final int integer);
    
    default short remove(final int key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Short put(final Integer key, final Short value) {
        final int k = key;
        final boolean containsKey = this.containsKey(k);
        final short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short get(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        final short v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Short.valueOf(v) : null;
    }
    
    @Deprecated
    default Short remove(final Object key) {
        if (key == null) {
            return null;
        }
        final int k = (int)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final int key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((int)key);
    }
    
    default void defaultReturnValue(final short rv) {
        throw new UnsupportedOperationException();
    }
    
    default short defaultReturnValue() {
        return 0;
    }
}
