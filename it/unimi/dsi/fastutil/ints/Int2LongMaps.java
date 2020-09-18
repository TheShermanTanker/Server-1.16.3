package it.unimi.dsi.fastutil.ints;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToLongFunction;
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
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Int2LongMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Int2LongMaps() {
    }
    
    public static ObjectIterator<Int2LongMap.Entry> fastIterator(final Int2LongMap map) {
        final ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        return (entries instanceof Int2LongMap.FastEntrySet) ? ((Int2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Int2LongMap map, final Consumer<? super Int2LongMap.Entry> consumer) {
        final ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        if (entries instanceof Int2LongMap.FastEntrySet) {
            ((Int2LongMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Int2LongMap.Entry> fastIterable(final Int2LongMap map) {
        final ObjectSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        return (entries instanceof Int2LongMap.FastEntrySet) ? new ObjectIterable<Int2LongMap.Entry>() {
            public ObjectIterator<Int2LongMap.Entry> iterator() {
                return ((Int2LongMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Int2LongMap.Entry> consumer) {
                ((Int2LongMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Int2LongMap singleton(final int key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Int2LongMap singleton(final Integer key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Int2LongMap synchronize(final Int2LongMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Int2LongMap synchronize(final Int2LongMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Int2LongMap unmodifiable(final Int2LongMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Int2LongFunctions.EmptyFunction implements Int2LongMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Integer, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2LongEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public LongCollection values() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Int2LongMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Int2LongFunctions.Singleton implements Int2LongMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient LongCollection values;
        
        protected Singleton(final int key, final long value) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2LongMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
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
            return this.key ^ HashCommon.long2int(this.value);
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
    
    public static class SynchronizedMap extends Int2LongFunctions.SynchronizedFunction implements Int2LongMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient LongCollection values;
        
        protected SynchronizedMap(final Int2LongMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Int2LongMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Long> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> int2LongEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.int2LongEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
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
        public long getOrDefault(final int key, final long defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Long> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Long, ? extends Long> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public long putIfAbsent(final int key, final long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final int key, final long value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public long replace(final int key, final long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final long oldValue, final long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public long computeIfAbsent(final int key, final IntToLongFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfAbsentNullable(final int key, final IntFunction<? extends Long> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfAbsentPartial(final int key, final Int2LongFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfPresent(final int key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public long compute(final int key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public long merge(final int key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
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
        public Long replace(final Integer key, final Long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Long oldValue, final Long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final Integer key, final Long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Long computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Long> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long compute(final Integer key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long merge(final Integer key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Int2LongFunctions.UnmodifiableFunction implements Int2LongMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient LongCollection values;
        
        protected UnmodifiableMap(final Int2LongMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.int2LongEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet());
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
        public long getOrDefault(final int key, final long defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Long> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Long, ? extends Long> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long putIfAbsent(final int key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final int key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long replace(final int key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final long oldValue, final long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsent(final int key, final IntToLongFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsentNullable(final int key, final IntFunction<? extends Long> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsentPartial(final int key, final Int2LongFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfPresent(final int key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long compute(final int key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long merge(final int key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
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
        public Long replace(final Integer key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Long oldValue, final Long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final Integer key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Long> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long compute(final Integer key, final BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long merge(final Integer key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
