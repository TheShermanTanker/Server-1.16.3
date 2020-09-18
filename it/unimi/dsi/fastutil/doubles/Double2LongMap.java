package it.unimi.dsi.fastutil.doubles;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.Objects;
import java.util.function.DoubleToLongFunction;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2LongMap extends Double2LongFunction, Map<Double, Long> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final long long1);
    
    long defaultReturnValue();
    
    ObjectSet<Entry> double2LongEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Double, Long>> entrySet() {
        return (ObjectSet<Map.Entry<Double, Long>>)this.double2LongEntrySet();
    }
    
    @Deprecated
    default Long put(final Double key, final Long value) {
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
    
    DoubleSet keySet();
    
    LongCollection values();
    
    boolean containsKey(final double double1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final long long1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((long)value);
    }
    
    default long getOrDefault(final double key, final long defaultValue) {
        final long v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default long putIfAbsent(final double key, final long value) {
        final long v = this.get(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final double key, final long value) {
        final long curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final double key, final long oldValue, final long newValue) {
        final long curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default long replace(final double key, final long value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default long computeIfAbsent(final double key, final DoubleToLongFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final long newValue = mappingFunction.applyAsLong(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeIfAbsentNullable(final double key, final DoubleFunction<? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final long v = this.get(key);
        final long drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Long mappedValue = (Long)mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        final long newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default long computeIfAbsentPartial(final double key, final Double2LongFunction mappingFunction) {
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
    
    default long computeIfPresent(final double key, final BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
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
    
    default long compute(final double key, final BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
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
    
    default long merge(final double key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
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
    default Long putIfAbsent(final Double key, final Long value) {
        return (Long)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Double key, final Long oldValue, final Long newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Long replace(final Double key, final Long value) {
        return (Long)super.replace(key, value);
    }
    
    @Deprecated
    default Long computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Long> mappingFunction) {
        return (Long)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Long computeIfPresent(final Double key, final BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Long compute(final Double key, final BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Long merge(final Double key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return (Long)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Double, Long> {
        double getDoubleKey();
        
        @Deprecated
        default Double getKey() {
            return this.getDoubleKey();
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
