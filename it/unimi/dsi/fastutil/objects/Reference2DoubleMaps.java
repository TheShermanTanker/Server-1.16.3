package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.doubles.DoubleCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleSets;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Reference2DoubleMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Reference2DoubleMaps() {
    }
    
    public static <K> ObjectIterator<Reference2DoubleMap.Entry<K>> fastIterator(final Reference2DoubleMap<K> map) {
        final ObjectSet<Reference2DoubleMap.Entry<K>> entries = map.reference2DoubleEntrySet();
        return (entries instanceof Reference2DoubleMap.FastEntrySet) ? ((Reference2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Reference2DoubleMap<K> map, final Consumer<? super Reference2DoubleMap.Entry<K>> consumer) {
        final ObjectSet<Reference2DoubleMap.Entry<K>> entries = map.reference2DoubleEntrySet();
        if (entries instanceof Reference2DoubleMap.FastEntrySet) {
            ((Reference2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Reference2DoubleMap.Entry<K>> fastIterable(final Reference2DoubleMap<K> map) {
        final ObjectSet<Reference2DoubleMap.Entry<K>> entries = map.reference2DoubleEntrySet();
        return (entries instanceof Reference2DoubleMap.FastEntrySet) ? new ObjectIterable<Reference2DoubleMap.Entry<K>>() {
            public ObjectIterator<Reference2DoubleMap.Entry<K>> iterator() {
                return ((Reference2DoubleMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Reference2DoubleMap.Entry<K>> consumer) {
                ((Reference2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Reference2DoubleMap<K> emptyMap() {
        return (Reference2DoubleMap<K>)Reference2DoubleMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2DoubleMap<K> singleton(final K key, final double value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2DoubleMap<K> singleton(final K key, final Double value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2DoubleMap<K> synchronize(final Reference2DoubleMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Reference2DoubleMap<K> synchronize(final Reference2DoubleMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Reference2DoubleMap<K> unmodifiable(final Reference2DoubleMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Reference2DoubleFunctions.EmptyFunction<K> implements Reference2DoubleMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final double v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2DoubleEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            return (ReferenceSet<K>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public DoubleCollection values() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Reference2DoubleMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Reference2DoubleFunctions.Singleton<K> implements Reference2DoubleMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final K key, final double value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final double v) {
            return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return Double.doubleToLongBits((double)ov) == Double.doubleToLongBits(this.value);
        }
        
        public void putAll(final Map<? extends K, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2DoubleMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Double>> entrySet() {
            return (ObjectSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public DoubleCollection values() {
            if (this.values == null) {
                this.values = DoubleSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ HashCommon.double2int(this.value);
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
    
    public static class Singleton<K> extends Reference2DoubleFunctions.Singleton<K> implements Reference2DoubleMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final K key, final double value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final double v) {
            return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return Double.doubleToLongBits((double)ov) == Double.doubleToLongBits(this.value);
        }
        
        public void putAll(final Map<? extends K, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2DoubleMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Double>> entrySet() {
            return (ObjectSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public DoubleCollection values() {
            if (this.values == null) {
                this.values = DoubleSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ HashCommon.double2int(this.value);
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
    
    public static class SynchronizedMap<K> extends Reference2DoubleFunctions.SynchronizedFunction<K> implements Reference2DoubleMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2DoubleMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient DoubleCollection values;
        
        protected SynchronizedMap(final Reference2DoubleMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Reference2DoubleMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final double v) {
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
        
        public void putAll(final Map<? extends K, ? extends Double> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2DoubleEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.reference2DoubleEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Double>> entrySet() {
            return (ObjectSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
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
        public DoubleCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return DoubleCollections.synchronize(this.map.values(), this.sync);
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
        public double getOrDefault(final Object key, final double defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Double> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Double, ? extends Double> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public double putIfAbsent(final K key, final double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final double value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public double replace(final K key, final double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final double oldValue, final double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public double computeDoubleIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeDoubleIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public double computeDoubleIfAbsentPartial(final K key, final Reference2DoubleFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeDoubleIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public double computeDoubleIfPresent(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeDoubleIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public double computeDouble(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeDouble(key, remappingFunction);
            }
        }
        
        @Override
        public double mergeDouble(final K key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeDouble(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double getOrDefault(final Object key, final Double defaultValue) {
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
        public Double replace(final K key, final Double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Double oldValue, final Double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final K key, final Double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Double computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return (Double)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Double computeIfPresent(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return (Double)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Double compute(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return (Double)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double merge(final K key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Reference2DoubleFunctions.UnmodifiableFunction<K> implements Reference2DoubleMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2DoubleMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient DoubleCollection values;
        
        protected UnmodifiableMap(final Reference2DoubleMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final double v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.reference2DoubleEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Double>> entrySet() {
            return (ObjectSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public DoubleCollection values() {
            if (this.values == null) {
                return DoubleCollections.unmodifiable(this.map.values());
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
        public double getOrDefault(final Object key, final double defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Double> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Double, ? extends Double> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double putIfAbsent(final K key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double replace(final K key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final double oldValue, final double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeDoubleIfAbsent(final K key, final ToDoubleFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeDoubleIfAbsentPartial(final K key, final Reference2DoubleFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeDoubleIfPresent(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeDouble(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double mergeDouble(final K key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double getOrDefault(final Object key, final Double defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double replace(final K key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Double oldValue, final Double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final K key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        public Double computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Double computeIfPresent(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Double compute(final K key, final BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double merge(final K key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
