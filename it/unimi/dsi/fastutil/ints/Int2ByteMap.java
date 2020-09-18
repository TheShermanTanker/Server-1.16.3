package it.unimi.dsi.fastutil.ints;

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

public interface Int2ByteMap extends Int2ByteFunction, Map<Integer, Byte> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final byte byte1);
    
    byte defaultReturnValue();
    
    ObjectSet<Entry> int2ByteEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Integer, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, Byte>>)this.int2ByteEntrySet();
    }
    
    @Deprecated
    default Byte put(final Integer key, final Byte value) {
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
    
    IntSet keySet();
    
    ByteCollection values();
    
    boolean containsKey(final int integer);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final byte byte1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((byte)value);
    }
    
    default byte getOrDefault(final int key, final byte defaultValue) {
        final byte v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default byte putIfAbsent(final int key, final byte value) {
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final int key, final byte value) {
        final byte curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final int key, final byte oldValue, final byte newValue) {
        final byte curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default byte replace(final int key, final byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default byte computeIfAbsent(final int key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfAbsentNullable(final int key, final IntFunction<? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.get(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Byte mappedValue = (Byte)mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        final byte newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeIfAbsentPartial(final int key, final Int2ByteFunction mappingFunction) {
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
    
    default byte computeIfPresent(final int key, final BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
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
    
    default byte compute(final int key, final BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
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
    
    default byte merge(final int key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
    default Byte putIfAbsent(final Integer key, final Byte value) {
        return (Byte)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Integer key, final Byte oldValue, final Byte newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Byte replace(final Integer key, final Byte value) {
        return (Byte)super.replace(key, value);
    }
    
    @Deprecated
    default Byte computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Byte> mappingFunction) {
        return (Byte)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Byte computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte compute(final Integer key, final BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Byte merge(final Integer key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Integer, Byte> {
        int getIntKey();
        
        @Deprecated
        default Integer getKey() {
            return this.getIntKey();
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
