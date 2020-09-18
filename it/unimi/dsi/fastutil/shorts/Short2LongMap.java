package it.unimi.dsi.fastutil.shorts;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntToLongFunction;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2LongMap extends Short2LongFunction, Map<Short, Long> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final long long1);
    
    long defaultReturnValue();
    
    ObjectSet<Entry> short2LongEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Short, Long>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Long>>)this.short2LongEntrySet();
    }
    
    @Deprecated
    default Long put(final Short key, final Long value) {
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
    
    ShortSet keySet();
    
    LongCollection values();
    
    boolean containsKey(final short short1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final long long1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((long)value);
    }
    
    default long getOrDefault(final short key, final long defaultValue) {
        final long v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default long putIfAbsent(final short key, final long value) {
        final long v = this.get(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final short key, final long value) {
        final long curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final short key, final long oldValue, final long newValue) {
        final long curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default long replace(final short key, final long value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default long computeIfAbsent(final short key, final IntToLongFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final long newValue = mappingFunction.applyAsLong((int)key);
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeIfAbsentNullable(final short key, final IntFunction<? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.get(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Long mappedValue = (Long)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final long newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeIfAbsentPartial(final short key, final Short2LongFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.get(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final long newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeIfPresent(final short key, final BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.get(key);
        final long drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Long newValue = (Long)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final long newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default long compute(final short key, final BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.get(key);
        final long drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Long newValue = (Long)remappingFunction.apply(key, (contained ? Long.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final long newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default long merge(final short key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final long oldValue = this.get(key);
        final long drv = this.defaultReturnValue();
        long newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Long mergedValue = (Long)remappingFunction.apply(oldValue, value);
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
    default Long getOrDefault(final Object key, final Long defaultValue) {
        return (Long)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Long putIfAbsent(final Short key, final Long value) {
        return (Long)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Short key, final Long oldValue, final Long newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Long replace(final Short key, final Long value) {
        return (Long)super.replace(key, value);
    }
    
    @Deprecated
    default Long computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Long> mappingFunction) {
        return (Long)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Long computeIfPresent(final Short key, final BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Long compute(final Short key, final BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Long merge(final Short key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Short, Long> {
        short getShortKey();
        
        @Deprecated
        default Short getKey() {
            return this.getShortKey();
        }
        
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
