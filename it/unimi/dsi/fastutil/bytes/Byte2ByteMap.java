package it.unimi.dsi.fastutil.bytes;

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
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2ByteMap extends Byte2ByteFunction, Map<Byte, Byte> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final byte byte1);
    
    byte defaultReturnValue();
    
    ObjectSet<Entry> byte2ByteEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Byte, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
    }
    
    @Deprecated
    default Byte put(final Byte key, final Byte value) {
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
    
    ByteSet keySet();
    
    ByteCollection values();
    
    boolean containsKey(final byte byte1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final byte byte1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((byte)value);
    }
    
    default byte getOrDefault(final byte key, final byte defaultValue) {
        final byte v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default byte putIfAbsent(final byte key, final byte value) {
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final byte key, final byte value) {
        final byte curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final byte key, final byte oldValue, final byte newValue) {
        final byte curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default byte replace(final byte key, final byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default byte computeIfAbsent(final byte key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt((int)key));
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfAbsentNullable(final byte key, final IntFunction<? extends Byte> mappingFunction) {
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
    
    default byte computeIfAbsentPartial(final byte key, final Byte2ByteFunction mappingFunction) {
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
    
    default byte computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
    
    default byte compute(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
    
    default byte merge(final byte key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
    default Byte putIfAbsent(final Byte key, final Byte value) {
        return (Byte)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Byte key, final Byte oldValue, final Byte newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Byte replace(final Byte key, final Byte value) {
        return (Byte)super.replace(key, value);
    }
    
    @Deprecated
    default Byte computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Byte> mappingFunction) {
        return (Byte)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Byte computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte compute(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte merge(final Byte key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Byte, Byte> {
        byte getByteKey();
        
        @Deprecated
        default Byte getKey() {
            return this.getByteKey();
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
