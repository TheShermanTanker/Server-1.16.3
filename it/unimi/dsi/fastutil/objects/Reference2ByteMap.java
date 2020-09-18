package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;

public interface Reference2ByteMap<K> extends Reference2ByteFunction<K>, Map<K, Byte> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final byte byte1);
    
    byte defaultReturnValue();
    
    ObjectSet<Entry<K>> reference2ByteEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Byte>> entrySet() {
        return (ObjectSet<Map.Entry<K, Byte>>)this.reference2ByteEntrySet();
    }
    
    @Deprecated
    default Byte put(final K key, final Byte value) {
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
    
    ReferenceSet<K> keySet();
    
    ByteCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final byte byte1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((byte)value);
    }
    
    default byte getOrDefault(final Object key, final byte defaultValue) {
        final byte v;
        return ((v = this.getByte(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default byte putIfAbsent(final K key, final byte value) {
        final byte v = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final byte value) {
        final byte curValue = this.getByte(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeByte(key);
        return true;
    }
    
    default boolean replace(final K key, final byte oldValue, final byte newValue) {
        final byte curValue = this.getByte(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default byte replace(final K key, final byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default byte computeByteIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.getByte(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeByteIfAbsentPartial(final K key, final Reference2ByteFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final byte v = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final byte newValue = mappingFunction.getByte(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default byte computeByteIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Byte newValue = (Byte)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeByte(key);
            return drv;
        }
        final byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default byte computeByte(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Byte newValue = (Byte)remappingFunction.apply(key, (contained ? Byte.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeByte(key);
            }
            return drv;
        }
        final byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default byte mergeByte(final K key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final byte oldValue = this.getByte(key);
        final byte drv = this.defaultReturnValue();
        byte newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Byte mergedValue = (Byte)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeByte(key);
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
    default Byte putIfAbsent(final K key, final Byte value) {
        return (Byte)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Byte oldValue, final Byte newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Byte replace(final K key, final Byte value) {
        return (Byte)super.replace(key, value);
    }
    
    @Deprecated
    default Byte merge(final K key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return (Byte)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Byte> {
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
