package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.longs.LongCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Object2LongMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Object2LongMaps() {
    }
    
    public static <K> ObjectIterator<Object2LongMap.Entry<K>> fastIterator(final Object2LongMap<K> map) {
        final ObjectSet<Object2LongMap.Entry<K>> entries = map.object2LongEntrySet();
        return (entries instanceof Object2LongMap.FastEntrySet) ? ((Object2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Object2LongMap<K> map, final Consumer<? super Object2LongMap.Entry<K>> consumer) {
        final ObjectSet<Object2LongMap.Entry<K>> entries = map.object2LongEntrySet();
        if (entries instanceof Object2LongMap.FastEntrySet) {
            ((Object2LongMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Object2LongMap.Entry<K>> fastIterable(final Object2LongMap<K> map) {
        final ObjectSet<Object2LongMap.Entry<K>> entries = map.object2LongEntrySet();
        return (entries instanceof Object2LongMap.FastEntrySet) ? new ObjectIterable<Object2LongMap.Entry<K>>() {
            public ObjectIterator<Object2LongMap.Entry<K>> iterator() {
                return ((Object2LongMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Object2LongMap.Entry<K>> consumer) {
                ((Object2LongMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Object2LongMap<K> emptyMap() {
        return (Object2LongMap<K>)Object2LongMaps.EMPTY_MAP;
    }
    
    public static <K> Object2LongMap<K> singleton(final K key, final long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2LongMap<K> singleton(final K key, final Long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2LongMap<K> synchronize(final Object2LongMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Object2LongMap<K> synchronize(final Object2LongMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Object2LongMap<K> unmodifiable(final Object2LongMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2LongFunctions.EmptyFunction<K> implements Object2LongMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final long v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2LongEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public LongCollection values() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Object2LongMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Object2LongFunctions.Singleton<K> implements Object2LongMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient LongCollection values;
        
        protected Singleton(final K key, final long value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final long v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (long)ov == this.value;
        }
        
        public void putAll(final Map<? extends K, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2LongMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public LongCollection values() {
            if (this.values == null) {
                this.values = LongSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ HashCommon.long2int(this.value);
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
    
    public static class SynchronizedMap<K> extends Object2LongFunctions.SynchronizedFunction<K> implements Object2LongMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2LongMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient LongCollection values;
        
        protected SynchronizedMap(final Object2LongMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2LongMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final long v) {
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
        
        public void putAll(final Map<? extends K, ? extends Long> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> object2LongEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.object2LongEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
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
        public LongCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return LongCollections.synchronize(this.map.values(), this.sync);
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
        public long getOrDefault(final Object key, final long defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Long> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Long, ? extends Long> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public long putIfAbsent(final K key, final long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final long value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public long replace(final K key, final long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final long oldValue, final long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public long computeLongIfAbsent(final K key, final ToLongFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeLongIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public long computeLongIfAbsentPartial(final K key, final Object2LongFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeLongIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public long computeLongIfPresent(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeLongIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public long computeLong(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeLong(key, remappingFunction);
            }
        }
        
        @Override
        public long mergeLong(final K key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeLong(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long getOrDefault(final Object key, final Long defaultValue) {
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
        public Long replace(final K key, final Long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Long oldValue, final Long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final K key, final Long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Long computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Long> mappingFunction) {
            synchronized (this.sync) {
                return (Long)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Long computeIfPresent(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return (Long)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Long compute(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return (Long)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long merge(final K key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Object2LongFunctions.UnmodifiableFunction<K> implements Object2LongMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2LongMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient LongCollection values;
        
        protected UnmodifiableMap(final Object2LongMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final long v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.object2LongEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Long>> entrySet() {
            return (ObjectSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public LongCollection values() {
            if (this.values == null) {
                return LongCollections.unmodifiable(this.map.values());
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
        public long getOrDefault(final Object key, final long defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Long> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Long, ? extends Long> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long putIfAbsent(final K key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long replace(final K key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final long oldValue, final long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeLongIfAbsent(final K key, final ToLongFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeLongIfAbsentPartial(final K key, final Object2LongFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeLongIfPresent(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeLong(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long mergeLong(final K key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long getOrDefault(final Object key, final Long defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long replace(final K key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Long oldValue, final Long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final K key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        public Long computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Long> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Long computeIfPresent(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Long compute(final K key, final BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long merge(final K key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
