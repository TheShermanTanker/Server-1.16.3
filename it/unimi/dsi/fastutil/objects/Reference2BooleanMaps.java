package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.booleans.BooleanCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Reference2BooleanMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Reference2BooleanMaps() {
    }
    
    public static <K> ObjectIterator<Reference2BooleanMap.Entry<K>> fastIterator(final Reference2BooleanMap<K> map) {
        final ObjectSet<Reference2BooleanMap.Entry<K>> entries = map.reference2BooleanEntrySet();
        return (entries instanceof Reference2BooleanMap.FastEntrySet) ? ((Reference2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Reference2BooleanMap<K> map, final Consumer<? super Reference2BooleanMap.Entry<K>> consumer) {
        final ObjectSet<Reference2BooleanMap.Entry<K>> entries = map.reference2BooleanEntrySet();
        if (entries instanceof Reference2BooleanMap.FastEntrySet) {
            ((Reference2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Reference2BooleanMap.Entry<K>> fastIterable(final Reference2BooleanMap<K> map) {
        final ObjectSet<Reference2BooleanMap.Entry<K>> entries = map.reference2BooleanEntrySet();
        return (entries instanceof Reference2BooleanMap.FastEntrySet) ? new ObjectIterable<Reference2BooleanMap.Entry<K>>() {
            public ObjectIterator<Reference2BooleanMap.Entry<K>> iterator() {
                return ((Reference2BooleanMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Reference2BooleanMap.Entry<K>> consumer) {
                ((Reference2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Reference2BooleanMap<K> emptyMap() {
        return (Reference2BooleanMap<K>)Reference2BooleanMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2BooleanMap<K> singleton(final K key, final boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanMap<K> singleton(final K key, final Boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanMap<K> synchronize(final Reference2BooleanMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Reference2BooleanMap<K> synchronize(final Reference2BooleanMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Reference2BooleanMap<K> unmodifiable(final Reference2BooleanMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Reference2BooleanFunctions.EmptyFunction<K> implements Reference2BooleanMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends K, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2BooleanEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            return (ReferenceSet<K>)ReferenceSets.EMPTY_SET;
        }
        
        @Override
        public BooleanCollection values() {
            return BooleanSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Reference2BooleanMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Reference2BooleanFunctions.Singleton<K> implements Reference2BooleanMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient BooleanCollection values;
        
        protected Singleton(final K key, final boolean value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (boolean)ov == this.value;
        }
        
        public void putAll(final Map<? extends K, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2BooleanMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public BooleanCollection values() {
            if (this.values == null) {
                this.values = BooleanSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return System.identityHashCode(this.key) ^ (this.value ? 1231 : 1237);
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
    
    public static class SynchronizedMap<K> extends Reference2BooleanFunctions.SynchronizedFunction<K> implements Reference2BooleanMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient BooleanCollection values;
        
        protected SynchronizedMap(final Reference2BooleanMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Reference2BooleanMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final boolean v) {
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
        
        public void putAll(final Map<? extends K, ? extends Boolean> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2BooleanEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.reference2BooleanEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
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
        public BooleanCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return BooleanCollections.synchronize(this.map.values(), this.sync);
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
        public boolean getOrDefault(final Object key, final boolean defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Boolean> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Boolean, ? extends Boolean> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public boolean putIfAbsent(final K key, final boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final boolean value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final boolean oldValue, final boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public boolean computeBooleanIfAbsent(final K key, final Predicate<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeBooleanIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeBooleanIfAbsentPartial(final K key, final Reference2BooleanFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeBooleanIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeBooleanIfPresent(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeBooleanIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public boolean computeBoolean(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeBoolean(key, remappingFunction);
            }
        }
        
        @Override
        public boolean mergeBoolean(final K key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeBoolean(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean getOrDefault(final Object key, final Boolean defaultValue) {
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
        public Boolean replace(final K key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Boolean oldValue, final Boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final K key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Boolean computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Boolean> mappingFunction) {
            synchronized (this.sync) {
                return (Boolean)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Boolean computeIfPresent(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return (Boolean)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Boolean compute(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return (Boolean)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean merge(final K key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Reference2BooleanFunctions.UnmodifiableFunction<K> implements Reference2BooleanMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient BooleanCollection values;
        
        protected UnmodifiableMap(final Reference2BooleanMap<K> m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends K, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> reference2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.reference2BooleanEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<K, Boolean>>)this.reference2BooleanEntrySet();
        }
        
        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.<K>unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public BooleanCollection values() {
            if (this.values == null) {
                return BooleanCollections.unmodifiable(this.map.values());
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
        public boolean getOrDefault(final Object key, final boolean defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Boolean> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Boolean, ? extends Boolean> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putIfAbsent(final K key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final boolean oldValue, final boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeBooleanIfAbsent(final K key, final Predicate<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeBooleanIfAbsentPartial(final K key, final Reference2BooleanFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeBooleanIfPresent(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeBoolean(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean mergeBoolean(final K key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean getOrDefault(final Object key, final Boolean defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean replace(final K key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Boolean oldValue, final Boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final K key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        public Boolean computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Boolean> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Boolean computeIfPresent(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Boolean compute(final K key, final BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean merge(final K key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
