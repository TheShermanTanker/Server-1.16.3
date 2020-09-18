package it.unimi.dsi.fastutil.bytes;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2DoubleMap extends Byte2DoubleFunction, Map<Byte, Double> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final double double1);
    
    double defaultReturnValue();
    
    ObjectSet<Entry> byte2DoubleEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Byte, Double>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, Double>>)this.byte2DoubleEntrySet();
    }
    
    @Deprecated
    default Double put(final Byte key, final Double value) {
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
    
    ByteSet keySet();
    
    DoubleCollection values();
    
    boolean containsKey(final byte byte1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final double double1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((double)value);
    }
    
    default double getOrDefault(final byte key, final double defaultValue) {
        final double v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default double putIfAbsent(final byte key, final double value) {
        final double v = this.get(key);
        final double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final byte key, final double value) {
        final double curValue = this.get(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final byte key, final double oldValue, final double newValue) {
        final double curValue = this.get(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default double replace(final byte key, final double value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default double computeIfAbsent(final byte key, final IntToDoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final double v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final double newValue = mappingFunction.applyAsDouble((int)key);
        this.put(key, newValue);
        return newValue;
    }
    
    default double computeIfAbsentNullable(final byte key, final IntFunction<? extends Double> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final double v = this.get(key);
        final double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Double mappedValue = (Double)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final double newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default double computeIfAbsentPartial(final byte key, final Byte2DoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final double v = this.get(key);
        final double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final double newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default double computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.get(key);
        final double drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Double newValue = (Double)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default double compute(final byte key, final BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.get(key);
        final double drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Double newValue = (Double)remappingFunction.apply(key, (contained ? Double.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default double merge(final byte key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final double oldValue = this.get(key);
        final double drv = this.defaultReturnValue();
        double newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Double mergedValue = (Double)remappingFunction.apply(oldValue, value);
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
    default Double getOrDefault(final Object key, final Double defaultValue) {
        return (Double)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Double putIfAbsent(final Byte key, final Double value) {
        return (Double)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Byte key, final Double oldValue, final Double newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Double replace(final Byte key, final Double value) {
        return (Double)super.replace(key, value);
    }
    
    @Deprecated
    default Double computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Double> mappingFunction) {
        return (Double)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Double computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return (Double)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Double compute(final Byte key, final BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return (Double)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Double merge(final Byte key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return (Double)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Byte, Double> {
        byte getByteKey();
        
        @Deprecated
        default Byte getKey() {
            return this.getByteKey();
        }
        
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
