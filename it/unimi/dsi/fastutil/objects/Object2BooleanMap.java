package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.Predicate;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;

public interface Object2BooleanMap<K> extends Object2BooleanFunction<K>, Map<K, Boolean> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final boolean boolean1);
    
    boolean defaultReturnValue();
    
    ObjectSet<Entry<K>> object2BooleanEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
    }
    
    @Deprecated
    default Boolean put(final K key, final Boolean value) {
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
    
    ObjectSet<K> keySet();
    
    BooleanCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final boolean boolean1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((boolean)value);
    }
    
    default boolean getOrDefault(final Object key, final boolean defaultValue) {
        final boolean v;
        return ((v = this.getBoolean(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default boolean putIfAbsent(final K key, final boolean value) {
        final boolean v = this.getBoolean(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final boolean value) {
        final boolean curValue = this.getBoolean(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeBoolean(key);
        return true;
    }
    
    default boolean replace(final K key, final boolean oldValue, final boolean newValue) {
        final boolean curValue = this.getBoolean(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default boolean replace(final K key, final boolean value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default boolean computeBooleanIfAbsent(final K key, final Predicate<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.getBoolean(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final boolean newValue = mappingFunction.test(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeBooleanIfAbsentPartial(final K key, final Object2BooleanFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.getBoolean(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final boolean newValue = mappingFunction.getBoolean(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeBooleanIfPresent(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.getBoolean(key);
        final boolean drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeBoolean(key);
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean computeBoolean(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.getBoolean(key);
        final boolean drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Boolean newValue = (Boolean)remappingFunction.apply(key, (contained ? Boolean.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeBoolean(key);
            }
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean mergeBoolean(final K key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.getBoolean(key);
        final boolean drv = this.defaultReturnValue();
        boolean newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Boolean mergedValue = (Boolean)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeBoolean(key);
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
    default Boolean putIfAbsent(final K key, final Boolean value) {
        return (Boolean)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Boolean oldValue, final Boolean newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Boolean replace(final K key, final Boolean value) {
        return (Boolean)super.replace(key, value);
    }
    
    @Deprecated
    default Boolean merge(final K key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Boolean> {
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
