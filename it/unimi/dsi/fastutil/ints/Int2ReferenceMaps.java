package it.unimi.dsi.fastutil.ints;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ReferenceCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Int2ReferenceMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Int2ReferenceMaps() {
    }
    
    public static <V> ObjectIterator<Int2ReferenceMap.Entry<V>> fastIterator(final Int2ReferenceMap<V> map) {
        final ObjectSet<Int2ReferenceMap.Entry<V>> entries = map.int2ReferenceEntrySet();
        return (entries instanceof Int2ReferenceMap.FastEntrySet) ? ((Int2ReferenceMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> void fastForEach(final Int2ReferenceMap<V> map, final Consumer<? super Int2ReferenceMap.Entry<V>> consumer) {
        final ObjectSet<Int2ReferenceMap.Entry<V>> entries = map.int2ReferenceEntrySet();
        if (entries instanceof Int2ReferenceMap.FastEntrySet) {
            ((Int2ReferenceMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <V> ObjectIterable<Int2ReferenceMap.Entry<V>> fastIterable(final Int2ReferenceMap<V> map) {
        final ObjectSet<Int2ReferenceMap.Entry<V>> entries = map.int2ReferenceEntrySet();
        return (entries instanceof Int2ReferenceMap.FastEntrySet) ? new ObjectIterable<Int2ReferenceMap.Entry<V>>() {
            public ObjectIterator<Int2ReferenceMap.Entry<V>> iterator() {
                return ((Int2ReferenceMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Int2ReferenceMap.Entry<V>> consumer) {
                ((Int2ReferenceMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <V> Int2ReferenceMap<V> emptyMap() {
        return (Int2ReferenceMap<V>)Int2ReferenceMaps.EMPTY_MAP;
    }
    
    public static <V> Int2ReferenceMap<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ReferenceMap<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ReferenceMap<V> synchronize(final Int2ReferenceMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Int2ReferenceMap<V> synchronize(final Int2ReferenceMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Int2ReferenceMap<V> unmodifiable(final Int2ReferenceMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Int2ReferenceFunctions.EmptyFunction<V> implements Int2ReferenceMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        public boolean containsValue(final Object v) {
            return false;
        }
        
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ReferenceEntrySet() {
            return (ObjectSet<Entry<V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            return (ReferenceCollection<V>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Int2ReferenceMaps.EMPTY_MAP;
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
    
    public static class Singleton<V> extends Int2ReferenceFunctions.Singleton<V> implements Int2ReferenceMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient IntSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected Singleton(final int key, final V value) {
            super(key, value);
        }
        
        public boolean containsValue(final Object v) {
            return this.value == v;
        }
        
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2ReferenceMap.BasicEntry<V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                this.values = ReferenceSets.<V>singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return this.key ^ ((this.value == null) ? 0 : System.identityHashCode(this.value));
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
    
    public static class SynchronizedMap<V> extends Int2ReferenceFunctions.SynchronizedFunction<V> implements Int2ReferenceMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient IntSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected SynchronizedMap(final Int2ReferenceMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Int2ReferenceMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ReferenceEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<V>>synchronize(this.map.int2ReferenceEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = IntSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ReferenceCollection<V> values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ReferenceCollections.<V>synchronize(this.map.values(), this.sync);
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
        public V getOrDefault(final int key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public V putIfAbsent(final int key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final int key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public V replace(final int key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public V computeIfAbsent(final int key, final IntFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfAbsentPartial(final int key, final Int2ReferenceFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final int key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
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
        public V replace(final Integer key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Integer key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V compute(final Integer key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V merge(final Integer key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<V> extends Int2ReferenceFunctions.UnmodifiableFunction<V> implements Int2ReferenceMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ReferenceMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient IntSet keys;
        protected transient ReferenceCollection<V> values;
        
        protected UnmodifiableMap(final Int2ReferenceMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends Integer, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> int2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<V>>unmodifiable(this.map.int2ReferenceEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, V>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, V>>)this.int2ReferenceEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ReferenceCollection<V> values() {
            if (this.values == null) {
                return ReferenceCollections.<V>unmodifiable(this.map.values());
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
        public V getOrDefault(final int key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V putIfAbsent(final int key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final int key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V replace(final int key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsent(final int key, final IntFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsentPartial(final int key, final Int2ReferenceFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfPresent(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V compute(final int key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V merge(final int key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V getOrDefault(final Object key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V replace(final Integer key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Integer key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V compute(final Integer key, final BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V merge(final Integer key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
