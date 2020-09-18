package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToIntFunction;
import it.unimi.dsi.fastutil.Function;

@FunctionalInterface
public interface Float2ByteFunction extends Function<Float, Byte>, DoubleToIntFunction {
    @Deprecated
    default int applyAsInt(final double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }
    
    default byte put(final float key, final byte value) {
        throw new UnsupportedOperationException();
    }
    
    byte get(final float float1);
    
    default byte remove(final float key) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    default Byte put(final Float key, final Byte value) {
        final float k = key;
        final boolean containsKey = this.containsKey(k);
        final byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte get(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        final byte v = this.get(k);
        return (v != this.defaultReturnValue() || this.containsKey(k)) ? Byte.valueOf(v) : null;
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        if (key == null) {
            return null;
        }
        final float k = (float)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }
    
    default boolean containsKey(final float key) {
        return true;
    }
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return key != null && this.containsKey((float)key);
    }
    
    default void defaultReturnValue(final byte rv) {
        throw new UnsupportedOperationException();
    }
    
    default byte defaultReturnValue() {
        return 0;
    }
}
