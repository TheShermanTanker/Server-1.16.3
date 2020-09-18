package it.unimi.dsi.fastutil.longs;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.LongFunction;
import java.util.Objects;
import java.util.function.LongPredicate;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2BooleanMap extends Long2BooleanFunction, Map<Long, Boolean> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final boolean boolean1);
    
    boolean defaultReturnValue();
    
    ObjectSet<Entry> long2BooleanEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Long, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<Long, Boolean>>)this.long2BooleanEntrySet();
    }
    
    @Deprecated
    default Boolean put(final Long key, final Boolean value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Boolean get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Boolean remove(final Object key) {
        return super.remove(key);
    }
    
    LongSet keySet();
    
    BooleanCollection values();
    
    boolean containsKey(final long long1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final boolean boolean1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((boolean)value);
    }
    
    default boolean getOrDefault(final long key, final boolean defaultValue) {
        final boolean v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default boolean putIfAbsent(final long key, final boolean value) {
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final long key, final boolean value) {
        final boolean curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final long key, final boolean oldValue, final boolean newValue) {
        final boolean curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default boolean replace(final long key, final boolean value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default boolean computeIfAbsent(final long key, final LongPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final boolean newValue = mappingFunction.test(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfAbsentNullable(final long key, final LongFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Boolean mappedValue = (Boolean)mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        final boolean newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfAbsentPartial(final long key, final Long2BooleanFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final boolean newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfPresent(final long key, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean compute(final long key, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Boolean newValue = (Boolean)remappingFunction.apply(key, (contained ? Boolean.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean merge(final long key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        boolean newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Boolean mergedValue = (Boolean)remappingFunction.apply(oldValue, value);
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
    default Boolean getOrDefault(final Object key, final Boolean defaultValue) {
        return (Boolean)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Boolean putIfAbsent(final Long key, final Boolean value) {
        return (Boolean)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Long key, final Boolean oldValue, final Boolean newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Boolean replace(final Long key, final Boolean value) {
        return (Boolean)super.replace(key, value);
    }
    
    @Deprecated
    default Boolean computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends Boolean> mappingFunction) {
        return (Boolean)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Boolean computeIfPresent(final Long key, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Boolean compute(final Long key, final BiFunction<? super Long, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Boolean merge(final Long key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Long, Boolean> {
        long getLongKey();
        
        @Deprecated
        default Long getKey() {
            return this.getLongKey();
        }
        
        boolean getBooleanValue();
        
        boolean setValue(final boolean boolean1);
        
        @Deprecated
        default Boolean getValue() {
            return this.getBooleanValue();
        }
        
        @Deprecated
        default Boolean setValue(final Boolean value) {
            return this.setValue((boolean)value);
        }
    }
}
