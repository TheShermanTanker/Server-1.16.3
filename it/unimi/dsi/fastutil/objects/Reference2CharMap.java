package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.ToIntFunction;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;

public interface Reference2CharMap<K> extends Reference2CharFunction<K>, Map<K, Character> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final char character);
    
    char defaultReturnValue();
    
    ObjectSet<Entry<K>> reference2CharEntrySet();
    
    @Deprecated
    default ObjectSet<Map.Entry<K, Character>> entrySet() {
        return (ObjectSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
    }
    
    @Deprecated
    default Character put(final K key, final Character value) {
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
    
    ReferenceSet<K> keySet();
    
    CharCollection values();
    
    boolean containsKey(final Object object);
    
    boolean containsValue(final char character);
    
    @Deprecated
    default boolean containsValue(final Object value) {
        return value != null && this.containsValue((char)value);
    }
    
    default char getOrDefault(final Object key, final char defaultValue) {
        final char v;
        return ((v = this.getChar(key)) != this.defaultReturnValue() || this.containsKey(key)) ? v : defaultValue;
    }
    
    default char putIfAbsent(final K key, final char value) {
        final char v = this.getChar(key);
        final char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }
    
    default boolean remove(final Object key, final char value) {
        final char curValue = this.getChar(key);
        if (curValue != value || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.removeChar(key);
        return true;
    }
    
    default boolean replace(final K key, final char oldValue, final char newValue) {
        final char curValue = this.getChar(key);
        if (curValue != oldValue || (curValue == this.defaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }
    
    default char replace(final K key, final char value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }
    
    default char computeCharIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final char v = this.getChar(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        final char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }
    
    default char computeCharIfAbsentPartial(final K key, final Reference2CharFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        final char v = this.getChar(key);
        final char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        final char newValue = mappingFunction.getChar(key);
        this.put(key, newValue);
        return newValue;
    }
    
    default char computeCharIfPresent(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.getChar(key);
        final char drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        final Character newValue = (Character)remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeChar(key);
            return drv;
        }
        final char newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default char computeChar(final K key, final BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.getChar(key);
        final char drv = this.defaultReturnValue();
        final boolean contained = oldValue != drv || this.containsKey(key);
        final Character newValue = (Character)remappingFunction.apply(key, (contained ? Character.valueOf(oldValue) : null));
        if (newValue == null) {
            if (contained) {
                this.removeChar(key);
            }
            return drv;
        }
        final char newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }
    
    default char mergeChar(final K key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final char oldValue = this.getChar(key);
        final char drv = this.defaultReturnValue();
        char newValue;
        if (oldValue != drv || this.containsKey(key)) {
            final Character mergedValue = (Character)remappingFunction.apply(oldValue, value);
            if (mergedValue == null) {
                this.removeChar(key);
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
    default Character putIfAbsent(final K key, final Character value) {
        return (Character)super.putIfAbsent(key, value);
    }
    
    @Deprecated
    default boolean remove(final Object key, final Object value) {
        return super.remove(key, value);
    }
    
    @Deprecated
    default boolean replace(final K key, final Character oldValue, final Character newValue) {
        return super.replace(key, oldValue, newValue);
    }
    
    @Deprecated
    default Character replace(final K key, final Character value) {
        return (Character)super.replace(key, value);
    }
    
    @Deprecated
    default Character merge(final K key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return (Character)super.merge(key, value, (BiFunction)remappingFunction);
    }
    
    public interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        ObjectIterator<Entry<K>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K> extends Map.Entry<K, Character> {
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
