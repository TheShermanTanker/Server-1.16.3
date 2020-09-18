package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToDoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;

public interface Object2FloatMap<K> extends Object2FloatFunction<K>, Map<K, Float> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final float float1);
    
    float defaultReturnValue();
    
    ObjectSet<Entry<K>> object2FloatEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Float>> entrySet() {
        return (ObjectSet<Map.Entry<K, Float>>)this.object2FloatEntrySet();
    }
    
    @Deprecated
    default Float put(final K key, final Float value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Float get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Float remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    FloatCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final float float1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((float)value);
    }
    
    default float getOrDefault(final Object key, final float defaultValue) {
        final float v;
        return ((v = this.getFloat(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default float putIfAbsent(final K key, final float value) {
        final float v = this.getFloat(key);
        final float drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final float value) {
        final float curValue = this.getFloat(key);
        if (Float.floatToIntBits(curValue) != Float.floatToIntBits(value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeFloat(key);
        return true;
    }
    
    default boolean replace(final K key, final float oldValue, final float newValue) {
        final float curValue = this.getFloat(key);
        if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default float replace(final K key, final float value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default float computeFloatIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final float v = this.getFloat(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default float computeFloatIfAbsentPartial(final K key, final Object2FloatFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final float v = this.getFloat(key);
        final float drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final float newValue = mappingFunction.getFloat(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default float computeFloatIfPresent(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.getFloat(key);
        final float drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Float newValue = (Float)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeFloat(key);
            return drv;
        }
        final float newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default float computeFloat(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.getFloat(key);
        final float drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Float newValue = (Float)remappingFunction.apply(key, (contained ? Float.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeFloat(key);
            }
            return drv;
        }
        final float newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default float mergeFloat(final K key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.getFloat(key);
        final float drv = this.defaultReturnValue();
        float newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Float mergedValue = (Float)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeFloat(key);
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
    default Float getOrDefault(final Object key, final Float defaultValue) {
        return (Float)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Float putIfAbsent(final K key, final Float value) {
        return (Float)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Float oldValue, final Float newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Float replace(final K key, final Float value) {
        return (Float)super.replace(key, value);
    }
    
    @Deprecated
    default Float merge(final K key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return (Float)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Float> {
        float getFloatValue();
        
        float setValue(final float float1);
        
        @Deprecated
        default Float getValue() {
            return this.getFloatValue();
        }
        
        @Deprecated
        default Float setValue(final Float value) {
            return this.setValue((float)value);
        }
    }
}
