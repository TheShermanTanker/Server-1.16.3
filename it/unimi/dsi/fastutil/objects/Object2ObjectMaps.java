package it.unimi.dsi.fastutil.objects;

import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Object2ObjectMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ObjectMaps() {
    }
    
    public static <K, V> ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator(final Object2ObjectMap<K, V> map) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        return (entries instanceof Object2ObjectMap.FastEntrySet) ? ((Object2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K, V> void fastForEach(final Object2ObjectMap<K, V> map, final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        if (entries instanceof Object2ObjectMap.FastEntrySet) {
            ((Object2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K, V> ObjectIterable<Object2ObjectMap.Entry<K, V>> fastIterable(final Object2ObjectMap<K, V> map) {
        final ObjectSet<Object2ObjectMap.Entry<K, V>> entries = map.object2ObjectEntrySet();
        return (entries instanceof Object2ObjectMap.FastEntrySet) ? new ObjectIterable<Object2ObjectMap.Entry<K, V>>() {
            public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
                return ((Object2ObjectMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
                ((Object2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K, V> Object2ObjectMap<K, V> emptyMap() {
        return (Object2ObjectMap<K, V>)Object2ObjectMaps.EMPTY_MAP;
    }
    
    public static <K, V> Object2ObjectMap<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ObjectMap<K, V> synchronize(final Object2ObjectMap<K, V> m) {
        return new SynchronizedMap<K, V>(m);
    }
    
    public static <K, V> Object2ObjectMap<K, V> synchronize(final Object2ObjectMap<K, V> m, final Object sync) {
        return new SynchronizedMap<K, V>(m, sync);
    }
    
    public static <K, V> Object2ObjectMap<K, V> unmodifiable(final Object2ObjectMap<K, V> m) {
        return new UnmodifiableMap<K, V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K, V> extends Object2ObjectFunctions.EmptyFunction<K, V> implements Object2ObjectMap<K, V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        public boolean containsValue(final Object v) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            return (ObjectSet<Entry<K, V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return (ObjectCollection<V>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Object2ObjectMaps.EMPTY_MAP;
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
    
    public static class Singleton<K, V> extends Object2ObjectFunctions.Singleton<K, V> implements Object2ObjectMap<K, V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final K key, final V value) {
            super(key, value);
        }
        
        public boolean containsValue(final Object v) {
            return Objects.equals(this.value, v);
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2ObjectMap.BasicEntry<K, V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                this.values = ObjectSets.<V>singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
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
    
    public static class SynchronizedMap<K, V> extends Object2ObjectFunctions.SynchronizedFunction<K, V> implements Object2ObjectMap<K, V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ObjectMap<K, V> map;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ObjectCollection<V> values;
        
        protected SynchronizedMap(final Object2ObjectMap<K, V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2ObjectMap<K, V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K, V>>synchronize(this.map.object2ObjectEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ObjectSets.<K>synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ObjectCollection<V> values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ObjectCollections.<V>synchronize(this.map.values(), this.sync);
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
        
        public V getOrDefault(final Object key, final V defaultValue) {
            synchronized (this.sync) {
                return (V)this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        public V putIfAbsent(final K key, final V value) {
            synchronized (this.sync) {
                return (V)this.map.putIfAbsent(key, value);
            }
        }
        
        public boolean remove(final Object key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        public V replace(final K key, final V value) {
            synchronized (this.sync) {
                return (V)this.map.replace(key, value);
            }
        }
        
        public boolean replace(final K key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return (V)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return (V)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return (V)this.map.merge(key, value, (BiFunction)remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K, V> extends Object2ObjectFunctions.UnmodifiableFunction<K, V> implements Object2ObjectMap<K, V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ObjectMap<K, V> map;
        protected transient ObjectSet<Entry<K, V>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ObjectCollection<V> values;
        
        protected UnmodifiableMap(final Object2ObjectMap<K, V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K, V>> object2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K, V>>unmodifiable(this.map.object2ObjectEntrySet());
            }
            return this.entries;
        }
        
        @Override
        public ObjectSet<Map.Entry<K, V>> entrySet() {
            return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ObjectCollection<V> values() {
            if (this.values == null) {
                return ObjectCollections.<V>unmodifiable(this.map.values());
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
        
        public V getOrDefault(final Object key, final V defaultValue) {
            return (V)this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        public V putIfAbsent(final K key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        public V replace(final K key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        public boolean replace(final K key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
