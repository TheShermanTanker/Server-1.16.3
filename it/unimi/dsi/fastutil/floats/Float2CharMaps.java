package it.unimi.dsi.fastutil.floats;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharSets;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Float2CharMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Float2CharMaps() {
    }
    
    public static ObjectIterator<Float2CharMap.Entry> fastIterator(final Float2CharMap map) {
        final ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
        return (entries instanceof Float2CharMap.FastEntrySet) ? ((Float2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Float2CharMap map, final Consumer<? super Float2CharMap.Entry> consumer) {
        final ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
        if (entries instanceof Float2CharMap.FastEntrySet) {
            ((Float2CharMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Float2CharMap.Entry> fastIterable(final Float2CharMap map) {
        final ObjectSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
        return (entries instanceof Float2CharMap.FastEntrySet) ? new ObjectIterable<Float2CharMap.Entry>() {
            public ObjectIterator<Float2CharMap.Entry> iterator() {
                return ((Float2CharMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Float2CharMap.Entry> consumer) {
                ((Float2CharMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Float2CharMap singleton(final float key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Float2CharMap singleton(final Float key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Float2CharMap synchronize(final Float2CharMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Float2CharMap synchronize(final Float2CharMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Float2CharMap unmodifiable(final Float2CharMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Float2CharFunctions.EmptyFunction implements Float2CharMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final char v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Float, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2CharEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public FloatSet keySet() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public CharCollection values() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Float2CharMaps.EMPTY_MAP;
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Map && ((Map)o).isEmpty();
        }
        
        @Override
        public String toString() {
            return "{}";
        }
    }
    
    public static class Singleton extends Float2CharFunctions.Singleton implements Float2CharMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient CharCollection values;
        
        protected Singleton(final float key, final char value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (char)ov == this.value;
        }
        
        public void putAll(final Map<? extends Float, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractFloat2CharMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                this.values = CharSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return HashCommon.float2int(this.key) ^ this.value;
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Map)) {
                return false;
            }
            final Map<?, ?> m = o;
            return m.size() == 1 && ((Map.Entry)m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
        }
        
        public String toString() {
            return new StringBuilder().append("{").append(this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Float2CharFunctions.SynchronizedFunction implements Float2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient CharCollection values;
        
        protected SynchronizedMap(final Float2CharMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Float2CharMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            synchronized (this.sync) {
                return this.map.containsValue(ov);
            }
        }
        
        public void putAll(final Map<? extends Float, ? extends Character> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> float2CharEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.float2CharEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = FloatSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public CharCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return CharCollections.synchronize(this.map.values(), this.sync);
                }
                return this.values;
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.map.isEmpty();
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.map.hashCode();
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.map.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
        
        @Override
        public char getOrDefault(final float key, final char defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Float, ? super Character> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super Character, ? extends Character> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public char putIfAbsent(final float key, final char value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final float key, final char value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public char replace(final float key, final char value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final float key, final char oldValue, final char newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public char computeIfAbsent(final float key, final DoubleToIntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentNullable(final float key, final DoubleFunction<? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentPartial(final float key, final Float2CharFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfPresent(final float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public char compute(final float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public char merge(final float key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Character replace(final Float key, final Character value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final Character oldValue, final Character newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Float key, final Character value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character compute(final Float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character merge(final Float key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Float2CharFunctions.UnmodifiableFunction implements Float2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient CharCollection values;
        
        protected UnmodifiableMap(final Float2CharMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Float, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.float2CharEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                return CharCollections.unmodifiable(this.map.values());
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        @Override
        public int hashCode() {
            return this.map.hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.map.equals(o);
        }
        
        @Override
        public char getOrDefault(final float key, final char defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Float, ? super Character> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super Character, ? extends Character> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char putIfAbsent(final float key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final float key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char replace(final float key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final float key, final char oldValue, final char newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsent(final float key, final DoubleToIntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentNullable(final float key, final DoubleFunction<? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentPartial(final float key, final Float2CharFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfPresent(final float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char compute(final float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char merge(final float key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character replace(final Float key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final Character oldValue, final Character newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Float key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character compute(final Float key, final BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character merge(final Float key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
