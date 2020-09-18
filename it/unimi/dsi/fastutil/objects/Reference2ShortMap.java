package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;

public interface Reference2ShortMap<K> extends Reference2ShortFunction<K>, Map<K, Short> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final short short1);
    
    short defaultReturnValue();
    
    ObjectSet<Entry<K>> reference2ShortEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Short>> entrySet() {
        return (ObjectSet<Map.Entry<K, Short>>)this.reference2ShortEntrySet();
    }
    
    @Deprecated
    default Short put(final K key, final Short value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Short get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Short remove(final Object key) {
        return super.remove(key);
    }
    
    ReferenceSet<K> keySet();
    
    ShortCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final short short1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((short)value);
    }
    
    default short getOrDefault(final Object key, final short defaultValue) {
        final short v;
        return ((v = this.getShort(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default short putIfAbsent(final K key, final short value) {
        final short v = this.getShort(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final short value) {
        final short curValue = this.getShort(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeShort(key);
        return true;
    }
    
    default boolean replace(final K key, final short oldValue, final short newValue) {
        final short curValue = this.getShort(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default short replace(final K key, final short value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default short computeShortIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.getShort(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeShortIfAbsentPartial(final K key, final Reference2ShortFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.getShort(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final short newValue = mappingFunction.getShort(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeShortIfPresent(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.getShort(key);
        final short drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Short newValue = (Short)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeShort(key);
            return drv;
        }
        final short newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default short computeShort(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.getShort(key);
        final short drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Short newValue = (Short)remappingFunction.apply(key, (contained ? Short.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeShort(key);
            }
            return drv;
        }
        final short newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default short mergeShort(final K key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.getShort(key);
        final short drv = this.defaultReturnValue();
        short newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Short mergedValue = (Short)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeShort(key);
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
    default Short getOrDefault(final Object key, final Short defaultValue) {
        return (Short)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Short putIfAbsent(final K key, final Short value) {
        return (Short)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Short oldValue, final Short newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Short replace(final K key, final Short value) {
        return (Short)super.replace(key, value);
    }
    
    @Deprecated
    default Short merge(final K key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Short> {
        short getShortValue();
        
        short setValue(final short short1);
        
        @Deprecated
        default Short getValue() {
            return this.getShortValue();
        }
        
        @Deprecated
        default Short setValue(final Short value) {
            return this.setValue((short)value);
        }
    }
}
