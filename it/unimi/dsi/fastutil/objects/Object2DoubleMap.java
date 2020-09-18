package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.Objects;
import java.util.function.ToDoubleFunction;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;

public interface Object2DoubleMap<K> extends Object2DoubleFunction<K>, Map<K, Double> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final double double1);
    
    double defaultReturnValue();
    
    ObjectSet<Entry<K>> object2DoubleEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Double>> entrySet() {
        return (ObjectSet<Map.Entry<K, Double>>)this.object2DoubleEntrySet();
    }
    
    @Deprecated
    default Double put(final K key, final Double value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Double get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Double remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    DoubleCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final double double1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((double)value);
    }
    
    default double getOrDefault(final Object key, final double defaultValue) {
        final double v;
        return ((v = this.getDouble(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default double putIfAbsent(final K key, final double value) {
        final double v = this.getDouble(key);
        final double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final double value) {
        final double curValue = this.getDouble(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeDouble(key);
        return true;
    }
    
    default boolean replace(final K key, final double oldValue, final double newValue) {
        final double curValue = this.getDouble(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default double replace(final K key, final double value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default double computeDoubleIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final double v = this.getDouble(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final double newValue = mappingFunction.applyAsDouble(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default double computeDoubleIfAbsentPartial(final K key, final Object2DoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final double v = this.getDouble(key);
        final double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final double newValue = mappingFunction.getDouble(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default double computeDoubleIfPresent(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.getDouble(key);
        final double drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Double newValue = (Double)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeDouble(key);
            return drv;
        }
        final double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default double computeDouble(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.getDouble(key);
        final double drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Double newValue = (Double)remappingFunction.apply(key, (contained ? Double.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeDouble(key);
            }
            return drv;
        }
        final double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default double mergeDouble(final K key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.getDouble(key);
        final double drv = this.defaultReturnValue();
        double newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Double mergedValue = (Double)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeDouble(key);
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
    default Double getOrDefault(final Object key, final Double defaultValue) {
        return (Double)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Double putIfAbsent(final K key, final Double value) {
        return (Double)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Double oldValue, final Double newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Double replace(final K key, final Double value) {
        return (Double)super.replace(key, value);
    }
    
    @Deprecated
    default Double merge(final K key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return (Double)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Double> {
        double getDoubleValue();
        
        double setValue(final double double1);
        
        @Deprecated
        default Double getValue() {
            return this.getDoubleValue();
        }
        
        @Deprecated
        default Double setValue(final Double value) {
            return this.setValue((double)value);
        }
    }
}
