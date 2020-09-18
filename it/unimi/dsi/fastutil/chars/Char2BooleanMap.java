package it.unimi.dsi.fastutil.chars;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.IntPredicate;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Char2BooleanMap extends Char2BooleanFunction, Map<Character, Boolean> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final boolean boolean1);
    
    boolean defaultReturnValue();
    
    ObjectSet<Entry> char2BooleanEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Character, Boolean>> entrySet() {
        return (ObjectSet<Map.Entry<Character, Boolean>>)this.char2BooleanEntrySet();
    }
    
    @Deprecated
    default Boolean put(final Character key, final Boolean value) {
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
    
    CharSet keySet();
    
    BooleanCollection values();
    
    boolean containsKey(final char character);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final boolean boolean1);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((boolean)value);
    }
    
    default boolean getOrDefault(final char key, final boolean defaultValue) {
        final boolean v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default boolean putIfAbsent(final char key, final boolean value) {
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final char key, final boolean value) {
        final boolean curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final char key, final boolean oldValue, final boolean newValue) {
        final boolean curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default boolean replace(final char key, final boolean value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default boolean computeIfAbsent(final char key, final IntPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final boolean newValue = mappingFunction.test((int)key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfAbsentNullable(final char key, final IntFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Boolean mappedValue = (Boolean)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final boolean newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfAbsentPartial(final char key, final Char2BooleanFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final boolean v = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final boolean newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default boolean computeIfPresent(final char key, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Boolean newValue = (Boolean)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean compute(final char key, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Boolean newValue = (Boolean)remappingFunction.apply(key, (contained ? Boolean.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default boolean merge(final char key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final boolean oldValue = this.get(key);
        final boolean drv = this.defaultReturnValue();
        boolean newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Boolean mergedValue = (Boolean)remappingFunction.apply(oldValue, value);
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
    default Boolean getOrDefault(final Object key, final Boolean defaultValue) {
        return (Boolean)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Boolean putIfAbsent(final Character key, final Boolean value) {
        return (Boolean)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Character key, final Boolean oldValue, final Boolean newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Boolean replace(final Character key, final Boolean value) {
        return (Boolean)super.replace(key, value);
    }
    
    @Deprecated
    default Boolean computeIfAbsent(final Character key, final java.util.function.Function<? super Character, ? extends Boolean> mappingFunction) {
        return (Boolean)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Boolean computeIfPresent(final Character key, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Boolean compute(final Character key, final BiFunction<? super Character, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Boolean merge(final Character key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        return (Boolean)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Character, Boolean> {
        char getCharKey();
        
        @Deprecated
        default Character getKey() {
            return this.getCharKey();
        }
        
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
