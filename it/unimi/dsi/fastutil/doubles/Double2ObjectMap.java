package it.unimi.dsi.fastutil.doubles;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.DoubleFunction;
import java.util.Objects;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Double2ObjectMap<V> extends Double2ObjectFunction<V>, Map<Double, V> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final V object);
    
    V defaultReturnValue();
    
    ObjectSet<Entry<V>> double2ObjectEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Double, V>> entrySet() {
        return (ObjectSet<Map.Entry<Double, V>>)this.double2ObjectEntrySet();
    }
    
    @Deprecated
    default V put(final Double key, final V value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default V get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default V remove(final Object key) {
        return super.remove(key);
    }
    
    DoubleSet keySet();
    
    ObjectCollection<V> values();
    
    boolean containsKey(final double double1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    default V getOrDefault(final double key, final V defaultValue) {
        final V v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default V putIfAbsent(final double key, final V value) {
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final double key, final Object value) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final double key, final V oldValue, final V newValue) {
        final V curValue = this.get(key);
        if (!Objects.equals(curValue, oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default V replace(final double key, final V value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default V computeIfAbsent(final double key, final DoubleFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final V newValue = (V)mappingFunction.apply(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default V computeIfAbsentPartial(final double key, final Double2ObjectFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final V v = this.get(key);
        final V drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default V computeIfPresent(final double key, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final V newValue = (V)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V compute(final double key, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final V newValue = (V)remappingFunction.apply(key, (contained ? oldValue : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        this.put(key, newValue);
        return newValue;
    }
    
    default V merge(final double key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        final V oldValue = this.get(key);
        final V drv = this.defaultReturnValue();
        V newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final V mergedValue = (V)remappingFunction.apply(oldValue, value);
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
    default V getOrDefault(final Object key, final V defaultValue) {
        return (V)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default V putIfAbsent(final Double key, final V value) {
        return (V)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Double key, final V oldValue, final V newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default V replace(final Double key, final V value) {
        return (V)super.replace(key, value);
    }
    
    @Deprecated
    default V computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends V> mappingFunction) {
        return (V)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default V computeIfPresent(final Double key, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return (V)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default V compute(final Double key, final BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return (V)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default V merge(final Double key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return (V)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<V> extends ObjectSet<Entry<V>> {
        ObjectIterator<Entry<V>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<V>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<V> extends Map.Entry<Double, V> {
        double getDoubleKey();
        
        @Deprecated
        default Double getKey() {
            return this.getDoubleKey();
        }
    }
}
