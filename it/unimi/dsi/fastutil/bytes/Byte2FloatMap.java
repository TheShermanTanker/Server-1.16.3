package it.unimi.dsi.fastutil.bytes;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntToDoubleFunction;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Byte2FloatMap extends Byte2FloatFunction, Map<Byte, Float> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final float float1);
    
    float defaultReturnValue();
    
    ObjectSet<Entry> byte2FloatEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Byte, Float>> entrySet() {
        return (ObjectSet<Map.Entry<Byte, Float>>)this.byte2FloatEntrySet();
    }
    
    @Deprecated
    default Float put(final Byte key, final Float value) {
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
    
    ByteSet keySet();
    
    FloatCollection values();
    
    boolean containsKey(final byte byte1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final float float1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((float)value);
    }
    
    default float getOrDefault(final byte key, final float defaultValue) {
        final float v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default float putIfAbsent(final byte key, final float value) {
        final float v = this.get(key);
        final float drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final byte key, final float value) {
        final float curValue = this.get(key);
        if (Float.floatToIntBits(curValue) != Float.floatToIntBits(value) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final byte key, final float oldValue, final float newValue) {
        final float curValue = this.get(key);
        if (Float.floatToIntBits(curValue) != Float.floatToIntBits(oldValue) || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default float replace(final byte key, final float value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default float computeIfAbsent(final byte key, final IntToDoubleFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final float v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble((int)key));
        this.put(key, newValue);
        return newValue;
    }
    
    default float computeIfAbsentNullable(final byte key, final IntFunction<? extends Float> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final float v = this.get(key);
        final float drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Float mappedValue = (Float)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final float newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default float computeIfAbsentPartial(final byte key, final Byte2FloatFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final float v = this.get(key);
        final float drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final float newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default float computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.get(key);
        final float drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Float newValue = (Float)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final float newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default float compute(final byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.get(key);
        final float drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Float newValue = (Float)remappingFunction.apply(key, (contained ? Float.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final float newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default float merge(final byte key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final float oldValue = this.get(key);
        final float drv = this.defaultReturnValue();
        float newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Float mergedValue = (Float)remappingFunction.apply(oldValue, value);
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
    default Float getOrDefault(final Object key, final Float defaultValue) {
        return (Float)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Float putIfAbsent(final Byte key, final Float value) {
        return (Float)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Byte key, final Float oldValue, final Float newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Float replace(final Byte key, final Float value) {
        return (Float)super.replace(key, value);
    }
    
    @Deprecated
    default Float computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Float> mappingFunction) {
        return (Float)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Float computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        return (Float)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Float compute(final Byte key, final BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        return (Float)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Float merge(final Byte key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return (Float)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Byte, Float> {
        byte getByteKey();
        
        @Deprecated
        default Byte getKey() {
            return this.getByteKey();
        }
        
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
