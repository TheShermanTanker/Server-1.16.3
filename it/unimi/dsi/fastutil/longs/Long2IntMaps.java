package it.unimi.dsi.fastutil.longs;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Long2IntMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Long2IntMaps() {
    }
    
    public static ObjectIterator<Long2IntMap.Entry> fastIterator(final Long2IntMap map) {
        final ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
        return (entries instanceof Long2IntMap.FastEntrySet) ? ((Long2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Long2IntMap map, final Consumer<? super Long2IntMap.Entry> consumer) {
        final ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
        if (entries instanceof Long2IntMap.FastEntrySet) {
            ((Long2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Long2IntMap.Entry> fastIterable(final Long2IntMap map) {
        final ObjectSet<Long2IntMap.Entry> entries = map.long2IntEntrySet();
        return (entries instanceof Long2IntMap.FastEntrySet) ? new ObjectIterable<Long2IntMap.Entry>() {
            public ObjectIterator<Long2IntMap.Entry> iterator() {
                return ((Long2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Long2IntMap.Entry> consumer) {
                ((Long2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Long2IntMap singleton(final long key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Long2IntMap singleton(final Long key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Long2IntMap synchronize(final Long2IntMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Long2IntMap synchronize(final Long2IntMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Long2IntMap unmodifiable(final Long2IntMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Long2IntFunctions.EmptyFunction implements Long2IntMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Long, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> long2IntEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public LongSet keySet() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Long2IntMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Long2IntFunctions.Singleton implements Long2IntMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient LongSet keys;
        protected transient IntCollection values;
        
        protected Singleton(final long key, final int value) {
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
        
        public void putAll(final Map<? extends Long, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> long2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractLong2IntMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Long, Integer>>)this.long2IntEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            if (this.keys == null) {
                this.keys = LongSets.singleton(this.key);
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
            return HashCommon.long2int(this.key) ^ this.value;
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
    
    public static class SynchronizedMap extends Long2IntFunctions.SynchronizedFunction implements Long2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient LongSet keys;
        protected transient IntCollection values;
        
        protected SynchronizedMap(final Long2IntMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Long2IntMap m) {
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
        
        public void putAll(final Map<? extends Long, ? extends Integer> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> long2IntEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.long2IntEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Long, Integer>>)this.long2IntEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = LongSets.synchronize(this.map.keySet(), this.sync);
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
        public int getOrDefault(final long key, final int defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Long, ? super Integer> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super Integer, ? extends Integer> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public int putIfAbsent(final long key, final int value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final long key, final int value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public int replace(final long key, final int value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final long key, final int oldValue, final int newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public int computeIfAbsent(final long key, final LongToIntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentNullable(final long key, final LongFunction<? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentPartial(final long key, final Long2IntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfPresent(final long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public int compute(final long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public int merge(final long key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
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
        public Integer replace(final Long key, final Integer value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final Integer oldValue, final Integer newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Long key, final Integer value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer compute(final Long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer merge(final Long key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Long2IntFunctions.UnmodifiableFunction implements Long2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient LongSet keys;
        protected transient IntCollection values;
        
        protected UnmodifiableMap(final Long2IntMap m) {
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
        
        public void putAll(final Map<? extends Long, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> long2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.long2IntEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Long, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Long, Integer>>)this.long2IntEntrySet();
        }
        
        @Override
        public LongSet keySet() {
            if (this.keys == null) {
                this.keys = LongSets.unmodifiable(this.map.keySet());
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
        public int getOrDefault(final long key, final int defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Long, ? super Integer> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Long, ? super Integer, ? extends Integer> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int putIfAbsent(final long key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final long key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int replace(final long key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final long key, final int oldValue, final int newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsent(final long key, final LongToIntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentNullable(final long key, final LongFunction<? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentPartial(final long key, final Long2IntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfPresent(final long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compute(final long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int merge(final long key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        public Integer replace(final Long key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Long key, final Integer oldValue, final Integer newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Long key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Long key, final java.util.function.Function<? super Long, ? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer compute(final Long key, final BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer merge(final Long key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
