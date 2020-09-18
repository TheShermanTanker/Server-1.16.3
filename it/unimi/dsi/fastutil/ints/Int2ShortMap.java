package it.unimi.dsi.fastutil.ints;

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
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Int2ShortMap extends Int2ShortFunction, Map<Integer, Short> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final short short1);
    
    short defaultReturnValue();
    
    ObjectSet<Entry> int2ShortEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Integer, Short>> entrySet() {
        return (ObjectSet<Map.Entry<Integer, Short>>)this.int2ShortEntrySet();
    }
    
    @Deprecated
    default Short put(final Integer key, final Short value) {
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
    
    IntSet keySet();
    
    ShortCollection values();
    
    boolean containsKey(final int integer);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final short short1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((short)value);
    }
    
    default short getOrDefault(final int key, final short defaultValue) {
        final short v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default short putIfAbsent(final int key, final short value) {
        final short v = this.get(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final int key, final short value) {
        final short curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final int key, final short oldValue, final short newValue) {
        final short curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default short replace(final int key, final short value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default short computeIfAbsent(final int key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeIfAbsentNullable(final int key, final IntFunction<? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final short v = this.get(key);
        final short drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Short mappedValue = (Short)mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        final short newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default short computeIfAbsentPartial(final int key, final Int2ShortFunction mappingFunction) {
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
    
    default short computeIfPresent(final int key, final BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
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
    
    default short compute(final int key, final BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
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
    
    default short merge(final int key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
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
    default Short putIfAbsent(final Integer key, final Short value) {
        return (Short)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Integer key, final Short oldValue, final Short newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Short replace(final Integer key, final Short value) {
        return (Short)super.replace(key, value);
    }
    
    @Deprecated
    default Short computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Short> mappingFunction) {
        return (Short)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Short computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Short compute(final Integer key, final BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Short merge(final Integer key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return (Short)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Integer, Short> {
        int getIntKey();
        
        @Deprecated
        default Integer getKey() {
            return this.getIntKey();
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
