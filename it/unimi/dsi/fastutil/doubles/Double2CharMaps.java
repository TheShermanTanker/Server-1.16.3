package it.unimi.dsi.fastutil.doubles;

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

public final class Double2CharMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Double2CharMaps() {
    }
    
    public static ObjectIterator<Double2CharMap.Entry> fastIterator(final Double2CharMap map) {
        final ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
        return (entries instanceof Double2CharMap.FastEntrySet) ? ((Double2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Double2CharMap map, final Consumer<? super Double2CharMap.Entry> consumer) {
        final ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
        if (entries instanceof Double2CharMap.FastEntrySet) {
            ((Double2CharMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Double2CharMap.Entry> fastIterable(final Double2CharMap map) {
        final ObjectSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
        return (entries instanceof Double2CharMap.FastEntrySet) ? new ObjectIterable<Double2CharMap.Entry>() {
            public ObjectIterator<Double2CharMap.Entry> iterator() {
                return ((Double2CharMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Double2CharMap.Entry> consumer) {
                ((Double2CharMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Double2CharMap singleton(final double key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Double2CharMap singleton(final Double key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Double2CharMap synchronize(final Double2CharMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Double2CharMap synchronize(final Double2CharMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Double2CharMap unmodifiable(final Double2CharMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Double2CharFunctions.EmptyFunction implements Double2CharMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Double, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2CharEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSet keySet() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public CharCollection values() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Double2CharMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Double2CharFunctions.Singleton implements Double2CharMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient CharCollection values;
        
        protected Singleton(final double key, final char value) {
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
        
        public void putAll(final Map<? extends Double, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractDouble2CharMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.singleton(this.key);
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
            return HashCommon.double2int(this.key) ^ this.value;
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
    
    public static class SynchronizedMap extends Double2CharFunctions.SynchronizedFunction implements Double2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient CharCollection values;
        
        protected SynchronizedMap(final Double2CharMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Double2CharMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Character> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> double2CharEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.double2CharEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = DoubleSets.synchronize(this.map.keySet(), this.sync);
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
        public char getOrDefault(final double key, final char defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Character> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Character, ? extends Character> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public char putIfAbsent(final double key, final char value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final double key, final char value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public char replace(final double key, final char value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final double key, final char oldValue, final char newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public char computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentNullable(final double key, final DoubleFunction<? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentPartial(final double key, final Double2CharFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfPresent(final double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public char compute(final double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public char merge(final double key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
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
        public Character replace(final Double key, final Character value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Character oldValue, final Character newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Double key, final Character value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character compute(final Double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character merge(final Double key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Double2CharFunctions.UnmodifiableFunction implements Double2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient CharCollection values;
        
        protected UnmodifiableMap(final Double2CharMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.double2CharEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.unmodifiable(this.map.keySet());
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
        public char getOrDefault(final double key, final char defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Character> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Character, ? extends Character> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char putIfAbsent(final double key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final double key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char replace(final double key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final double key, final char oldValue, final char newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentNullable(final double key, final DoubleFunction<? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentPartial(final double key, final Double2CharFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfPresent(final double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char compute(final double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char merge(final double key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
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
        public Character replace(final Double key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Character oldValue, final Character newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Double key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character compute(final Double key, final BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character merge(final Double key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
