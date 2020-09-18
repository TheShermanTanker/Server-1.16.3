package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.ints.IntCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Object2IntMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Object2IntMaps() {
    }
    
    public static <K> ObjectIterator<Object2IntMap.Entry<K>> fastIterator(final Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return (entries instanceof Object2IntMap.FastEntrySet) ? ((Object2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Object2IntMap<K> map, final Consumer<? super Object2IntMap.Entry<K>> consumer) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        if (entries instanceof Object2IntMap.FastEntrySet) {
            ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Object2IntMap.Entry<K>> fastIterable(final Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return (entries instanceof Object2IntMap.FastEntrySet) ? new ObjectIterable<Object2IntMap.Entry<K>>() {
            public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
                return ((Object2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Object2IntMap.Entry<K>> consumer) {
                ((Object2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Object2IntMap<K> emptyMap() {
        return (Object2IntMap<K>)Object2IntMaps.EMPTY_MAP;
    }
    
    public static <K> Object2IntMap<K> singleton(final K key, final int value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntMap<K> singleton(final K key, final Integer value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2IntMap<K> synchronize(final Object2IntMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Object2IntMap<K> synchronize(final Object2IntMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Object2IntMap<K> unmodifiable(final Object2IntMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2IntFunctions.EmptyFunction<K> implements Object2IntMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final int v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Object2IntMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Object2IntFunctions.Singleton<K> implements Object2IntMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient IntCollection values;
        
        protected Singleton(final K key, final int value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final int v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (int)ov == this.value;
        }
        
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2IntMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<K, Integer>>)this.object2IntEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public IntCollection values() {
            if (this.values == null) {
                this.values = IntSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
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
    
    public static class SynchronizedMap<K> extends Object2IntFunctions.SynchronizedFunction<K> implements Object2IntMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient IntCollection values;
        
        protected SynchronizedMap(final Object2IntMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2IntMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final int v) {
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
        
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.object2IntEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<K, Integer>>)this.object2IntEntrySet();
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
        public IntCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return IntCollections.synchronize(this.map.values(), this.sync);
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
        public int getOrDefault(final Object key, final int defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Integer> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Integer, ? extends Integer> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public int putIfAbsent(final K key, final int value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final int value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public int replace(final K key, final int value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final int oldValue, final int newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public int computeIntIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIntIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIntIfAbsentPartial(final K key, final Object2IntFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIntIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIntIfPresent(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIntIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public int computeInt(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeInt(key, remappingFunction);
            }
        }
        
        @Override
        public int mergeInt(final K key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeInt(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer getOrDefault(final Object key, final Integer defaultValue) {
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
        public Integer replace(final K key, final Integer value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Integer oldValue, final Integer newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final K key, final Integer value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Integer computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return (Integer)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Integer computeIfPresent(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return (Integer)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Integer compute(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return (Integer)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer merge(final K key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Object2IntFunctions.UnmodifiableFunction<K> implements Object2IntMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2IntMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient IntCollection values;
        
        protected UnmodifiableMap(final Object2IntMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final int v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.object2IntEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<K, Integer>>)this.object2IntEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public IntCollection values() {
            if (this.values == null) {
                return IntCollections.unmodifiable(this.map.values());
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
        public int getOrDefault(final Object key, final int defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Integer> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Integer, ? extends Integer> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int putIfAbsent(final K key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int replace(final K key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final int oldValue, final int newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIntIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIntIfAbsentPartial(final K key, final Object2IntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIntIfPresent(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeInt(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int mergeInt(final K key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer getOrDefault(final Object key, final Integer defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer replace(final K key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Integer oldValue, final Integer newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final K key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        public Integer computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Integer computeIfPresent(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Integer compute(final K key, final BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer merge(final K key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
