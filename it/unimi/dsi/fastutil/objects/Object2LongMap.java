package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.ToLongFunction;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;

public interface Object2LongMap<K> extends Object2LongFunction<K>, Map<K, Long> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final long long1);
    
    long defaultReturnValue();
    
    ObjectSet<Entry<K>> object2LongEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Long>> entrySet() {
        return (ObjectSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
    }
    
    @Deprecated
    default Long put(final K key, final Long value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Long get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Long remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    LongCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final long long1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((long)value);
    }
    
    default long getOrDefault(final Object key, final long defaultValue) {
        final long v;
        return ((v = this.getLong(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default long putIfAbsent(final K key, final long value) {
        final long v = this.getLong(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final long value) {
        final long curValue = this.getLong(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeLong(key);
        return true;
    }
    
    default boolean replace(final K key, final long oldValue, final long newValue) {
        final long curValue = this.getLong(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default long replace(final K key, final long value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default long computeLongIfAbsent(final K key, final ToLongFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.getLong(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final long newValue = mappingFunction.applyAsLong(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeLongIfAbsentPartial(final K key, final Object2LongFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.getLong(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final long newValue = mappingFunction.getLong(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeLongIfPresent(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.getLong(key);
        final long drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Long newValue = (Long)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeLong(key);
            return drv;
        }
        final long newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default long computeLong(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.getLong(key);
        final long drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Long newValue = (Long)remappingFunction.apply(key, (contained ? Long.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeLong(key);
            }
            return drv;
        }
        final long newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default long mergeLong(final K key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.getLong(key);
        final long drv = this.defaultReturnValue();
        long newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Long mergedValue = (Long)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeLong(key);
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
    default Long getOrDefault(final Object key, final Long defaultValue) {
        return (Long)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Long putIfAbsent(final K key, final Long value) {
        return (Long)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Long oldValue, final Long newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Long replace(final K key, final Long value) {
        return (Long)super.replace(key, value);
    }
    
    @Deprecated
    default Long merge(final K key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Long> {
        long getLongValue();
        
        long setValue(final long long1);
        
        @Deprecated
        default Long getValue() {
            return this.getLongValue();
        }
        
        @Deprecated
        default Long setValue(final Long value) {
            return this.setValue((long)value);
        }
    }
}
