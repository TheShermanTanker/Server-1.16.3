package it.unimi.dsi.fastutil.floats;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Objects;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Float2ObjectMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Float2ObjectMaps() {
    }
    
    public static <V> ObjectIterator<Float2ObjectMap.Entry<V>> fastIterator(final Float2ObjectMap<V> map) {
        final ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
        return (entries instanceof Float2ObjectMap.FastEntrySet) ? ((Float2ObjectMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> void fastForEach(final Float2ObjectMap<V> map, final Consumer<? super Float2ObjectMap.Entry<V>> consumer) {
        final ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
        if (entries instanceof Float2ObjectMap.FastEntrySet) {
            ((Float2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <V> ObjectIterable<Float2ObjectMap.Entry<V>> fastIterable(final Float2ObjectMap<V> map) {
        final ObjectSet<Float2ObjectMap.Entry<V>> entries = map.float2ObjectEntrySet();
        return (entries instanceof Float2ObjectMap.FastEntrySet) ? new ObjectIterable<Float2ObjectMap.Entry<V>>() {
            public ObjectIterator<Float2ObjectMap.Entry<V>> iterator() {
                return ((Float2ObjectMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Float2ObjectMap.Entry<V>> consumer) {
                ((Float2ObjectMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <V> Float2ObjectMap<V> emptyMap() {
        return (Float2ObjectMap<V>)Float2ObjectMaps.EMPTY_MAP;
    }
    
    public static <V> Float2ObjectMap<V> singleton(final float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectMap<V> singleton(final Float key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Float2ObjectMap<V> synchronize(final Float2ObjectMap<V> m) {
        return new SynchronizedMap<V>(m);
    }
    
    public static <V> Float2ObjectMap<V> synchronize(final Float2ObjectMap<V> m, final Object sync) {
        return new SynchronizedMap<V>(m, sync);
    }
    
    public static <V> Float2ObjectMap<V> unmodifiable(final Float2ObjectMap<V> m) {
        return new UnmodifiableMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<V> extends Float2ObjectFunctions.EmptyFunction<V> implements Float2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        public boolean containsValue(final Object v) {
            return false;
        }
        
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            return (ObjectSet<Entry<V>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public FloatSet keySet() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public ObjectCollection<V> values() {
            return (ObjectCollection<V>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Float2ObjectMaps.EMPTY_MAP;
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
    
    public static class Singleton<V> extends Float2ObjectFunctions.Singleton<V> implements Float2ObjectMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected Singleton(final float key, final V value) {
            super(key, value);
        }
        
        public boolean containsValue(final Object v) {
            return Objects.equals(this.value, v);
        }
        
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractFloat2ObjectMap.BasicEntry<V>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.singleton(this.key);
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
            return HashCommon.float2int(this.key) ^ ((this.value == null) ? 0 : this.value.hashCode());
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
    
    public static class SynchronizedMap<V> extends Float2ObjectFunctions.SynchronizedFunction<V> implements Float2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected SynchronizedMap(final Float2ObjectMap<V> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Float2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            synchronized (this.sync) {
                return this.map.containsValue(v);
            }
        }
        
        public void putAll(final Map<? extends Float, ? extends V> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<V>>synchronize(this.map.float2ObjectEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
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
        
        @Override
        public V getOrDefault(final float key, final V defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Float, ? super V> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super V, ? extends V> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public V putIfAbsent(final float key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final float key, final Object value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public V replace(final float key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final float key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public V computeIfAbsent(final float key, final DoubleFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfAbsentPartial(final float key, final Float2ObjectFunction<? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public V computeIfPresent(final float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public V compute(final float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public V merge(final float key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
        public V replace(final Float key, final V value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final V oldValue, final V newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Float key, final V value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends V> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V compute(final Float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public V merge(final Float key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<V> extends Float2ObjectFunctions.UnmodifiableFunction<V> implements Float2ObjectMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ObjectMap<V> map;
        protected transient ObjectSet<Entry<V>> entries;
        protected transient FloatSet keys;
        protected transient ObjectCollection<V> values;
        
        protected UnmodifiableMap(final Float2ObjectMap<V> m) {
            super(m);
            this.map = m;
        }
        
        public boolean containsValue(final Object v) {
            return this.map.containsValue(v);
        }
        
        public void putAll(final Map<? extends Float, ? extends V> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<V>> float2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<V>>unmodifiable(this.map.float2ObjectEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, V>> entrySet() {
            return (ObjectSet<Map.Entry<Float, V>>)this.float2ObjectEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.unmodifiable(this.map.keySet());
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
        
        @Override
        public V getOrDefault(final float key, final V defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Float, ? super V> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super V, ? extends V> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V putIfAbsent(final float key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final float key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V replace(final float key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final float key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsent(final float key, final DoubleFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfAbsentPartial(final float key, final Float2ObjectFunction<? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V computeIfPresent(final float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V compute(final float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V merge(final float key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
        public V replace(final Float key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final V oldValue, final V newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V putIfAbsent(final Float key, final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends V> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V computeIfPresent(final Float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V compute(final Float key, final BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public V merge(final Float key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
