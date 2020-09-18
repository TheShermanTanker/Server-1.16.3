package it.unimi.dsi.fastutil.doubles;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
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

public final class Double2IntMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Double2IntMaps() {
    }
    
    public static ObjectIterator<Double2IntMap.Entry> fastIterator(final Double2IntMap map) {
        final ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
        return (entries instanceof Double2IntMap.FastEntrySet) ? ((Double2IntMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Double2IntMap map, final Consumer<? super Double2IntMap.Entry> consumer) {
        final ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
        if (entries instanceof Double2IntMap.FastEntrySet) {
            ((Double2IntMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Double2IntMap.Entry> fastIterable(final Double2IntMap map) {
        final ObjectSet<Double2IntMap.Entry> entries = map.double2IntEntrySet();
        return (entries instanceof Double2IntMap.FastEntrySet) ? new ObjectIterable<Double2IntMap.Entry>() {
            public ObjectIterator<Double2IntMap.Entry> iterator() {
                return ((Double2IntMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Double2IntMap.Entry> consumer) {
                ((Double2IntMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Double2IntMap singleton(final double key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Double2IntMap singleton(final Double key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Double2IntMap synchronize(final Double2IntMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Double2IntMap synchronize(final Double2IntMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Double2IntMap unmodifiable(final Double2IntMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Double2IntFunctions.EmptyFunction implements Double2IntMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Double, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2IntEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSet keySet() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public IntCollection values() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Double2IntMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Double2IntFunctions.Singleton implements Double2IntMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient IntCollection values;
        
        protected Singleton(final double key, final int value) {
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
        
        public void putAll(final Map<? extends Double, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractDouble2IntMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Integer>>)this.double2IntEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.singleton(this.key);
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
            return HashCommon.double2int(this.key) ^ this.value;
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
    
    public static class SynchronizedMap extends Double2IntFunctions.SynchronizedFunction implements Double2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient IntCollection values;
        
        protected SynchronizedMap(final Double2IntMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Double2IntMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Integer> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> double2IntEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.double2IntEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Integer>>)this.double2IntEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = DoubleSets.synchronize(this.map.keySet(), this.sync);
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
        public int getOrDefault(final double key, final int defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Integer> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Integer, ? extends Integer> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public int putIfAbsent(final double key, final int value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final double key, final int value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public int replace(final double key, final int value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final double key, final int oldValue, final int newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public int computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentNullable(final double key, final DoubleFunction<? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfAbsentPartial(final double key, final Double2IntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public int computeIfPresent(final double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public int compute(final double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public int merge(final double key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        public Integer replace(final Double key, final Integer value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Integer oldValue, final Integer newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Double key, final Integer value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Integer> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer compute(final Double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Integer merge(final Double key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Double2IntFunctions.UnmodifiableFunction implements Double2IntMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2IntMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient IntCollection values;
        
        protected UnmodifiableMap(final Double2IntMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Integer> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.double2IntEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Integer>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Integer>>)this.double2IntEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.unmodifiable(this.map.keySet());
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
        public int getOrDefault(final double key, final int defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Integer> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Integer, ? extends Integer> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int putIfAbsent(final double key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final double key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int replace(final double key, final int value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final double key, final int oldValue, final int newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentNullable(final double key, final DoubleFunction<? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfAbsentPartial(final double key, final Double2IntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int computeIfPresent(final double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int compute(final double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int merge(final double key, final int value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
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
        public Integer replace(final Double key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Integer oldValue, final Integer newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer putIfAbsent(final Double key, final Integer value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Integer> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer computeIfPresent(final Double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer compute(final Double key, final BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer merge(final Double key, final Integer value, final BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
