package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.floats.FloatCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatSets;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Reference2FloatMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Reference2FloatMaps() {
    }
    
    public static <K> ObjectIterator<Reference2FloatMap.Entry<K>> fastIterator(final Reference2FloatMap<K> map) {
        final ObjectSet<Reference2FloatMap.Entry<K>> entries = map.reference2FloatEntrySet();
        return (entries instanceof Reference2FloatMap.FastEntrySet) ? ((Reference2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Reference2FloatMap<K> map, final Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
        final ObjectSet<Reference2FloatMap.Entry<K>> entries = map.reference2FloatEntrySet();
        if (entries instanceof Reference2FloatMap.FastEntrySet) {
            ((Reference2FloatMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Reference2FloatMap.Entry<K>> fastIterable(final Reference2FloatMap<K> map) {
        final ObjectSet<Reference2FloatMap.Entry<K>> entries = map.reference2FloatEntrySet();
        return (entries instanceof Reference2FloatMap.FastEntrySet) ? new ObjectIterable<Reference2FloatMap.Entry<K>>() {
            public ObjectIterator<Reference2FloatMap.Entry<K>> iterator() {
                return ((Reference2FloatMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
                ((Reference2FloatMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Reference2FloatMap<K> emptyMap() {
        return (Reference2FloatMap<K>)Reference2FloatMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2FloatMap<K> singleton(final K key, final float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2FloatMap<K> singleton(final K key, final Float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2FloatMap<K> synchronize(final Reference2FloatMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Reference2FloatMap<K> synchronize(final Reference2FloatMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Reference2FloatMap<K> unmodifiable(final Reference2FloatMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Reference2FloatFunctions.EmptyFunction<K> implements Reference2FloatMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final float v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2FloatEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            return (ReferenceSet<K>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public FloatCollection values() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Reference2FloatMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Reference2FloatFunctions.Singleton<K> implements Reference2FloatMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient FloatCollection values;
        
        protected Singleton(final K key, final float value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final float v) {
            return Float.floatToIntBits(this.value) == Float.floatToIntBits(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return Float.floatToIntBits((float)ov) == Float.floatToIntBits(this.value);
        }
        
        public void putAll(final Map<? extends K, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2FloatMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Float>> entrySet() {
            return (ObjectSet<Map.Entry<K, Float>>)this.reference2FloatEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                this.values = FloatSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ HashCommon.float2int(this.value);
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
    
    public static class SynchronizedMap<K> extends Reference2FloatFunctions.SynchronizedFunction<K> implements Reference2FloatMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2FloatMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient FloatCollection values;
        
        protected SynchronizedMap(final Reference2FloatMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Reference2FloatMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
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
        
        public void putAll(final Map<? extends K, ? extends Float> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2FloatEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.reference2FloatEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Float>> entrySet() {
            return (ObjectSet<Map.Entry<K, Float>>)this.reference2FloatEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ReferenceSets.<K>synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public FloatCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return FloatCollections.synchronize(this.map.values(), this.sync);
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
        public float getOrDefault(final Object key, final float defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Float> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Float, ? extends Float> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public float putIfAbsent(final K key, final float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final float value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public float replace(final K key, final float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final float oldValue, final float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public float computeFloatIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeFloatIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public float computeFloatIfAbsentPartial(final K key, final Reference2FloatFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeFloatIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public float computeFloatIfPresent(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeFloatIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public float computeFloat(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeFloat(key, remappingFunction);
            }
        }
        
        @Override
        public float mergeFloat(final K key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeFloat(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
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
        public Float replace(final K key, final Float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Float oldValue, final Float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final K key, final Float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Float computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Float> mappingFunction) {
            synchronized (this.sync) {
                return (Float)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Float computeIfPresent(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return (Float)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Float compute(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return (Float)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float merge(final K key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Reference2FloatFunctions.UnmodifiableFunction<K> implements Reference2FloatMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2FloatMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient FloatCollection values;
        
        protected UnmodifiableMap(final Reference2FloatMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.reference2FloatEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Float>> entrySet() {
            return (ObjectSet<Map.Entry<K, Float>>)this.reference2FloatEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                return FloatCollections.unmodifiable(this.map.values());
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
        public float getOrDefault(final Object key, final float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Float> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Float, ? extends Float> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float putIfAbsent(final K key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float replace(final K key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final float oldValue, final float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeFloatIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeFloatIfAbsentPartial(final K key, final Reference2FloatFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeFloatIfPresent(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeFloat(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float mergeFloat(final K key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float replace(final K key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Float oldValue, final Float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final K key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        public Float computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Float> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Float computeIfPresent(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Float compute(final K key, final BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float merge(final K key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
