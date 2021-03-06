package it.unimi.dsi.fastutil.shorts;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntUnaryOperator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Short2IntMap extends Short2IntFunction, Map<Short, Integer> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final int integer);
    
    int defaultReturnValue();
    
    ObjectSet<Entry> short2IntEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Short, Integer>> entrySet() {
        return (ObjectSet<Map.Entry<Short, Integer>>)this.short2IntEntrySet();
    }
    
    @Deprecated
    default Integer put(final Short key, final Integer value) {
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
    
    ShortSet keySet();
    
    IntCollection values();
    
    boolean containsKey(final short short1);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final int integer);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((int)value);
    }
    
    default int getOrDefault(final short key, final int defaultValue) {
        final int v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default int putIfAbsent(final short key, final int value) {
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final short key, final int value) {
        final int curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final short key, final int oldValue, final int newValue) {
        final int curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default int replace(final short key, final int value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default int computeIfAbsent(final short key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final int newValue = mappingFunction.applyAsInt((int)key);
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIfAbsentNullable(final short key, final IntFunction<? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Integer mappedValue = (Integer)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final int newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIfAbsentPartial(final short key, final Short2IntFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final int v = this.get(key);
        final int drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final int newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default int computeIfPresent(final short key, final BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Integer newValue = (Integer)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int compute(final short key, final BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Integer newValue = (Integer)remappingFunction.apply(key, (contained ? Integer.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final int newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default int merge(final short key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final int oldValue = this.get(key);
        final int drv = this.defaultReturnValue();
        int newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Integer mergedValue = (Integer)remappingFunction.apply(oldValue, value);
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
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        return (Integer)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Integer putIfAbsent(final Short key, final Integer value) {
        return (Integer)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Short key, final Integer oldValue, final Integer newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Integer replace(final Short key, final Integer value) {
        return (Integer)super.replace(key, value);
    }
    
    @Deprecated
    default Integer computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Integer> mappingFunction) {
        return (Integer)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Integer computeIfPresent(final Short key, final BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return (Integer)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Integer compute(final Short key, final BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return (Integer)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Integer merge(final Short key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return (Integer)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Short, Integer> {
        short getShortKey();
        
        @Deprecated
        default Short getKey() {
            return this.getShortKey();
        }
        
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
