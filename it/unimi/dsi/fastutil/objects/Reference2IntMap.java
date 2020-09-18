package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;

public interface Reference2IntMap<K> extends Reference2IntFunction<K>, Map<K, Integer> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final int integer);
    
    int defaultReturnValue();
    
    ObjectSet<Entry<K>> reference2IntEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Integer>> entrySet() {
        return (ObjectSet<Map.Entry<K, Integer>>)this.reference2IntEntrySet();
    }
    
    @Deprecated
    default Integer put(final K key, final Integer value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Integer get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Integer remove(final Object key) {
        return super.remove(key);
    }
    
    ReferenceSet<K> keySet();
    
    IntCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final int integer);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((int)value);
    }
    
    default int getOrDefault(final Object key, final int defaultValue) {
        final int v;
        return ((v = this.getInt(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default int putIfAbsent(final K key, final int value) {
        final int v = this.getInt(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final int value) {
        final int curValue = this.getInt(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeInt(key);
        return true;
    }
    
    default boolean replace(final K key, final int oldValue, final int newValue) {
        final int curValue = this.getInt(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default int replace(final K key, final int value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default int computeIntIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.getInt(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final int newValue = mappingFunction.applyAsInt(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIntIfAbsentPartial(final K key, final Reference2IntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.getInt(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final int newValue = mappingFunction.getInt(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIntIfPresent(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.getInt(key);
        final int drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Integer newValue = (Integer)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeInt(key);
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int computeInt(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.getInt(key);
        final int drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Integer newValue = (Integer)remappingFunction.apply(key, (contained ? Integer.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeInt(key);
            }
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int mergeInt(final K key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.getInt(key);
        final int drv = this.defaultReturnValue();
        int newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Integer mergedValue = (Integer)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeInt(key);
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
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        return (Integer)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Integer putIfAbsent(final K key, final Integer value) {
        return (Integer)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Integer oldValue, final Integer newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Integer replace(final K key, final Integer value) {
        return (Integer)super.replace(key, value);
    }
    
    @Deprecated
    default Integer merge(final K key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return (Integer)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Integer> {
        int getIntValue();
        
        int setValue(final int integer);
        
        @Deprecated
        default Integer getValue() {
            return this.getIntValue();
        }
        
        @Deprecated
        default Integer setValue(final Integer value) {
            return this.setValue((int)value);
        }
    }
}
