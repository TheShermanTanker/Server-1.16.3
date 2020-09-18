package it.unimi.dsi.fastutil.chars;

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

public interface Char2CharMap extends Char2CharFunction, Map<Character, Character> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final char character);
    
    char defaultReturnValue();
    
    ObjectSet<Entry> char2CharEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<Character, Character>> entrySet() {
        return (ObjectSet<Map.Entry<Character, Character>>)this.char2CharEntrySet();
    }
    
    @Deprecated
    default Character put(final Character key, final Character value) {
        return super.put(key, value);
    }
    
    @Deprecated
    default Character get(final Object key) {
        return super.get(key);
    }
    
    @Deprecated
    default Character remove(final Object key) {
        return super.remove(key);
    }
    
    CharSet keySet();
    
    CharCollection values();
    
    boolean containsKey(final char character);
    
    @Deprecated
    default boolean containsKey(final Object key) {
        return super.containsKey(key);
    }
    
    boolean containsValue(final char character);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((char)value);
    }
    
    default char getOrDefault(final char key, final char defaultValue) {
        final char v;
        return ((v = this.get(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default char putIfAbsent(final char key, final char value) {
        final char v = this.get(key);
        final char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final char key, final char value) {
        final char curValue = this.get(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }
    
    default boolean replace(final char key, final char oldValue, final char newValue) {
        final char curValue = this.get(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default char replace(final char key, final char value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default char computeIfAbsent(final char key, final IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final char v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt((int)key));
        this.put(key, newValue);
        return newValue;
    }
    
    default char computeIfAbsentNullable(final char key, final IntFunction<? extends Character> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final char v = this.get(key);
        final char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        final Character mappedValue = (Character)mappingFunction.apply((int)key);
        if (mappedValue == null) {
            return drv;
        }
        final char newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }
    
    default char computeIfAbsentPartial(final char key, final Char2CharFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final char v = this.get(key);
        final char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final char newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default char computeIfPresent(final char key, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.get(key);
        final char drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Character newValue = (Character)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        final char newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default char compute(final char key, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.get(key);
        final char drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Character newValue = (Character)remappingFunction.apply(key, (contained ? Character.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        final char newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default char merge(final char key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.get(key);
        final char drv = this.defaultReturnValue();
        char newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Character mergedValue = (Character)remappingFunction.apply(oldValue, value);
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
    default Character getOrDefault(final Object key, final Character defaultValue) {
        return (Character)super.getOrDefault(key, defaultValue);
    }
    
    @Deprecated
    default Character putIfAbsent(final Character key, final Character value) {
        return (Character)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final Character key, final Character oldValue, final Character newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Character replace(final Character key, final Character value) {
        return (Character)super.replace(key, value);
    }
    
    @Deprecated
    default Character computeIfAbsent(final Character key, final java.util.function.Function<? super Character, ? extends Character> mappingFunction) {
        return (Character)super.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
    }
    
    @Deprecated
    default Character computeIfPresent(final Character key, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return (Character)super.computeIfPresent(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Character compute(final Character key, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return (Character)super.compute(key, (BiFunction)remappingFunction);
    }
    
    @Deprecated
    default Character merge(final Character key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return (Character)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet extends ObjectSet<Entry> {
        ObjectIterator<Entry> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry extends Map.Entry<Character, Character> {
        char getCharKey();
        
        @Deprecated
        default Character getKey() {
            return this.getCharKey();
        }
        
        char getCharValue();
        
        char setValue(final char character);
        
        @Deprecated
        default Character getValue() {
            return this.getCharValue();
        }
        
        @Deprecated
        default Character setValue(final Character value) {
            return this.setValue((char)value);
        }
    }
}
