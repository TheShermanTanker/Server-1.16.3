package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.shorts.ShortCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortSets;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Object2ShortMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ShortMaps() {
    }
    
    public static <K> ObjectIterator<Object2ShortMap.Entry<K>> fastIterator(final Object2ShortMap<K> map) {
        final ObjectSet<Object2ShortMap.Entry<K>> entries = map.object2ShortEntrySet();
        return (entries instanceof Object2ShortMap.FastEntrySet) ? ((Object2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Object2ShortMap<K> map, final Consumer<? super Object2ShortMap.Entry<K>> consumer) {
        final ObjectSet<Object2ShortMap.Entry<K>> entries = map.object2ShortEntrySet();
        if (entries instanceof Object2ShortMap.FastEntrySet) {
            ((Object2ShortMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Object2ShortMap.Entry<K>> fastIterable(final Object2ShortMap<K> map) {
        final ObjectSet<Object2ShortMap.Entry<K>> entries = map.object2ShortEntrySet();
        return (entries instanceof Object2ShortMap.FastEntrySet) ? new ObjectIterable<Object2ShortMap.Entry<K>>() {
            public ObjectIterator<Object2ShortMap.Entry<K>> iterator() {
                return ((Object2ShortMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Object2ShortMap.Entry<K>> consumer) {
                ((Object2ShortMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Object2ShortMap<K> emptyMap() {
        return (Object2ShortMap<K>)Object2ShortMaps.EMPTY_MAP;
    }
    
    public static <K> Object2ShortMap<K> singleton(final K key, final short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortMap<K> singleton(final K key, final Short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortMap<K> synchronize(final Object2ShortMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Object2ShortMap<K> synchronize(final Object2ShortMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Object2ShortMap<K> unmodifiable(final Object2ShortMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2ShortFunctions.EmptyFunction<K> implements Object2ShortMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final short v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ShortCollection values() {
            return ShortSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Object2ShortMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Object2ShortFunctions.Singleton<K> implements Object2ShortMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected Singleton(final K key, final short value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final short v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (short)ov == this.value;
        }
        
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2ShortMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                this.values = ShortSets.singleton(this.value);
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
            return new StringBuilder().append("{").append(this.key).append("=>").append((int)this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap<K> extends Object2ShortFunctions.SynchronizedFunction<K> implements Object2ShortMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected SynchronizedMap(final Object2ShortMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2ShortMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final short v) {
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
        
        public void putAll(final Map<? extends K, ? extends Short> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.object2ShortEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
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
        public ShortCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ShortCollections.synchronize(this.map.values(), this.sync);
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
        public short getOrDefault(final Object key, final short defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Short> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Short, ? extends Short> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public short putIfAbsent(final K key, final short value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final short value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public short replace(final K key, final short value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final short oldValue, final short newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public short computeShortIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeShortIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public short computeShortIfAbsentPartial(final K key, final Object2ShortFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeShortIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public short computeShortIfPresent(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeShortIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public short computeShort(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeShort(key, remappingFunction);
            }
        }
        
        @Override
        public short mergeShort(final K key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeShort(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short getOrDefault(final Object key, final Short defaultValue) {
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
        public Short replace(final K key, final Short value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Short oldValue, final Short newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Short putIfAbsent(final K key, final Short value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Short computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Short> mappingFunction) {
            synchronized (this.sync) {
                return (Short)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Short computeIfPresent(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return (Short)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Short compute(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return (Short)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short merge(final K key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Object2ShortFunctions.UnmodifiableFunction<K> implements Object2ShortMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ShortCollection values;
        
        protected UnmodifiableMap(final Object2ShortMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final short v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.object2ShortEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                return ShortCollections.unmodifiable(this.map.values());
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
        public short getOrDefault(final Object key, final short defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Short> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Short, ? extends Short> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short putIfAbsent(final K key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short replace(final K key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final short oldValue, final short newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeShortIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeShortIfAbsentPartial(final K key, final Object2ShortFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeShortIfPresent(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeShort(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short mergeShort(final K key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short getOrDefault(final Object key, final Short defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short replace(final K key, final Short value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Short oldValue, final Short newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short putIfAbsent(final K key, final Short value) {
            throw new UnsupportedOperationException();
        }
        
        public Short computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Short> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Short computeIfPresent(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Short compute(final K key, final BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short merge(final K key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
