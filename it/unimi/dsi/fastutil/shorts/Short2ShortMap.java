package it.unimi.dsi.fastutil.shorts;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2ShortMap extends Short2ShortFunction, Map<Short, Short> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final short short1);
    
    short defaultReturnValue();
    
    ObjectSet<Entry> short2ShortEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Short, Short>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Short>>)this.short2ShortEntrySet();
    }
    
    @Deprecated
    default Short put(final Short key, final Short value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Short get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Short remove(final Object key) {
        return super.remove(key);
    }
    
    ShortSet keySet();
    
    ShortCollection values();
    
    boolean containsKey(final short short1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final short short1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((short)value);
    }
    
    default short getOrDefault(final short key, final short defaultValue) {
        final short v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default short putIfAbsent(final short key, final short value) {
        final short v = this.get(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final short key, final short value) {
        final short curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final short key, final short oldValue, final short newValue) {
        final short curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default short replace(final short key, final short value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default short computeIfAbsent(final short key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt((int)key));
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeIfAbsentNullable(final short key, final IntFunction<? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.get(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Short mappedValue = (Short)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final short newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeIfAbsentPartial(final short key, final Short2ShortFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.get(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final short newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeIfPresent(final short key, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.get(key);
        final short drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Short newValue = (Short)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final short newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default short compute(final short key, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.get(key);
        final short drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Short newValue = (Short)remappingFunction.apply(key, (contained ? Short.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final short newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default short merge(final short key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final short oldValue = this.get(key);
        final short drv = this.defaultReturnValue();
        short newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Short mergedValue = (Short)remappingFunction.apply(oldValue, value);
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
    default Short getOrDefault(final Object key, final Short defaultValue) {
        return (Short)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Short putIfAbsent(final Short key, final Short value) {
        return (Short)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Short key, final Short oldValue, final Short newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Short replace(final Short key, final Short value) {
        return (Short)super.replace(key, value);
    }
    
    @Deprecated
    default Short computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Short> mappingFunction) {
        return (Short)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Short computeIfPresent(final Short key, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Short compute(final Short key, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Short merge(final Short key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Short, Short> {
        short getShortKey();
        
        @Deprecated
        default Short getKey() {
            return this.getShortKey();
        }
        
        short getShortValue();
        
        short setValue(final short short1);
        
        @Deprecated
        default Short getValue() {
            return this.getShortValue();
        }
        
        @Deprecated
        default Short setValue(final Short value) {
            return this.setValue((short)value);
        }
    }
}
