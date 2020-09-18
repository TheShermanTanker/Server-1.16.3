package it.unimi.dsi.fastutil.shorts;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2ByteMap extends Short2ByteFunction, Map<Short, Byte> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final byte byte1);
    
    byte defaultReturnValue();
    
    ObjectSet<Entry> short2ByteEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Short, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Byte>>)this.short2ByteEntrySet();
    }
    
    @Deprecated
    default Byte put(final Short key, final Byte value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Byte get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Byte remove(final Object key) {
        return super.remove(key);
    }
    
    ShortSet keySet();
    
    ByteCollection values();
    
    boolean containsKey(final short short1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final byte byte1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((byte)value);
    }
    
    default byte getOrDefault(final short key, final byte defaultValue) {
        final byte v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default byte putIfAbsent(final short key, final byte value) {
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final short key, final byte value) {
        final byte curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final short key, final byte oldValue, final byte newValue) {
        final byte curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default byte replace(final short key, final byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default byte computeIfAbsent(final short key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt((int)key));
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfAbsentNullable(final short key, final IntFunction<? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Byte mappedValue = (Byte)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final byte newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfAbsentPartial(final short key, final Short2ByteFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final byte newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfPresent(final short key, final BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Byte newValue = (Byte)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default byte compute(final short key, final BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.get(key);
        final byte drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Byte newValue = (Byte)remappingFunction.apply(key, (contained ? Byte.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default byte merge(final short key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.get(key);
        final byte drv = this.defaultReturnValue();
        byte newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Byte mergedValue = (Byte)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.remove(key);
                return drv;
            }
            newValue = mergedValue;
        }
        else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    @Deprecated
    default Byte getOrDefault(final Object key, final Byte defaultValue) {
        return (Byte)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Byte putIfAbsent(final Short key, final Byte value) {
        return (Byte)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Short key, final Byte oldValue, final Byte newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Byte replace(final Short key, final Byte value) {
        return (Byte)super.replace(key, value);
    }
    
    @Deprecated
    default Byte computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Byte> mappingFunction) {
        return (Byte)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Byte computeIfPresent(final Short key, final BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte compute(final Short key, final BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte merge(final Short key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Short, Byte> {
        short getShortKey();
        
        @Deprecated
        default Short getKey() {
            return this.getShortKey();
        }
        
        byte getByteValue();
        
        byte setValue(final byte byte1);
        
        @Deprecated
        default Byte getValue() {
            return this.getByteValue();
        }
        
        @Deprecated
        default Byte setValue(final Byte value) {
            return this.setValue((byte)value);
        }
    }
}
