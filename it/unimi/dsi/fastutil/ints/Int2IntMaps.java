package it.unimi.dsi.fastutil.ints;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Int2IntMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Int2IntMaps() {
    }
    
    public static ObjectIterator<Int2IntMap.Entry> fastIterator(final Int2IntMap map) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        return (entries instanceof Int2IntMap.FastEntrySet) ? ((Int2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Int2IntMap map, final Consumer<? super Int2IntMap.Entry> consumer) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        if (entries instanceof Int2IntMap.FastEntrySet) {
            ((Int2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Int2IntMap.Entry> fastIterable(final Int2IntMap map) {
        final ObjectSet<Int2IntMap.Entry> entries = map.int2IntEntrySet();
        return (entries instanceof Int2IntMap.FastEntrySet) ? new ObjectIterable<Int2IntMap.Entry>() {
            public ObjectIterator<Int2IntMap.Entry> iterator() {
                return ((Int2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Int2IntMap.Entry> consumer) {
                ((Int2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Int2IntMap singleton(final int key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntMap singleton(final Integer key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Int2IntMap synchronize(final Int2IntMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Int2IntMap synchronize(final Int2IntMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Int2IntMap unmodifiable(final Int2IntMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Int2IntFunctions.EmptyFunction implements Int2IntMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Int2IntMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Int2IntFunctions.Singleton implements Int2IntMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient IntCollection values;
        
        protected Singleton(final int key, final int value) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2IntMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
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
            return this.key ^ this.value;
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
    
    public static class SynchronizedMap extends Int2IntFunctions.SynchronizedFunction implements Int2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient IntCollection values;
        
        protected SynchronizedMap(final Int2IntMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Int2IntMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.int2IntEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
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
        public int getOrDefault(final int key, final int defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Integer> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public int putIfAbsent(final int key, final int value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final int key, final int value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public int replace(final int key, final int value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final int oldValue, final int newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public int computeIfAbsent(final int key, final IntUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentNullable(final int key, final IntFunction<? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentPartial(final int key, final Int2IntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfPresent(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public int compute(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public int merge(final int key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        public Integer replace(final Integer key, final Integer value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Integer oldValue, final Integer newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Integer key, final Integer value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer compute(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer merge(final Integer key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Int2IntFunctions.UnmodifiableFunction implements Int2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient IntCollection values;
        
        protected UnmodifiableMap(final Int2IntMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.int2IntEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Integer>>)this.int2IntEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet());
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
        public int getOrDefault(final int key, final int defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Integer> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int putIfAbsent(final int key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final int key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int replace(final int key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final int oldValue, final int newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsent(final int key, final IntUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentNullable(final int key, final IntFunction<? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentPartial(final int key, final Int2IntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfPresent(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compute(final int key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int merge(final int key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        public Integer replace(final Integer key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Integer oldValue, final Integer newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Integer key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer compute(final Integer key, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer merge(final Integer key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
