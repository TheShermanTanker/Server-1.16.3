package it.unimi.dsi.fastutil.doubles;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.HashCommon;
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

public final class Double2DoubleMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Double2DoubleMaps() {
    }
    
    public static ObjectIterator<Double2DoubleMap.Entry> fastIterator(final Double2DoubleMap map) {
        final ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
        return (entries instanceof Double2DoubleMap.FastEntrySet) ? ((Double2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Double2DoubleMap map, final Consumer<? super Double2DoubleMap.Entry> consumer) {
        final ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
        if (entries instanceof Double2DoubleMap.FastEntrySet) {
            ((Double2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Double2DoubleMap.Entry> fastIterable(final Double2DoubleMap map) {
        final ObjectSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
        return (entries instanceof Double2DoubleMap.FastEntrySet) ? new ObjectIterable<Double2DoubleMap.Entry>() {
            public ObjectIterator<Double2DoubleMap.Entry> iterator() {
                return ((Double2DoubleMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Double2DoubleMap.Entry> consumer) {
                ((Double2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Double2DoubleMap singleton(final double key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleMap singleton(final Double key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleMap synchronize(final Double2DoubleMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Double2DoubleMap synchronize(final Double2DoubleMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Double2DoubleMap unmodifiable(final Double2DoubleMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Double2DoubleFunctions.EmptyFunction implements Double2DoubleMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Double, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2DoubleEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSet keySet() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public DoubleCollection values() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Double2DoubleMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Double2DoubleFunctions.Singleton implements Double2DoubleMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final double key, final double value) {
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
        
        public void putAll(final Map<? extends Double, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractDouble2DoubleMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.singleton(this.key);
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
            return HashCommon.double2int(this.key) ^ HashCommon.double2int(this.value);
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
    
    public static class SynchronizedMap extends Double2DoubleFunctions.SynchronizedFunction implements Double2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient DoubleCollection values;
        
        protected SynchronizedMap(final Double2DoubleMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Double2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Double> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> double2DoubleEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.double2DoubleEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
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
        public double getOrDefault(final double key, final double defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Double> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Double, ? extends Double> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public double putIfAbsent(final double key, final double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final double key, final double value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public double replace(final double key, final double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final double key, final double oldValue, final double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public double computeIfAbsent(final double key, final DoubleUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentNullable(final double key, final DoubleFunction<? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentPartial(final double key, final Double2DoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfPresent(final double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public double compute(final double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public double merge(final double key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
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
        public Double replace(final Double key, final Double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Double oldValue, final Double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Double key, final Double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double compute(final Double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double merge(final Double key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Double2DoubleFunctions.UnmodifiableFunction implements Double2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient DoubleCollection values;
        
        protected UnmodifiableMap(final Double2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Double, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.double2DoubleEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.unmodifiable(this.map.keySet());
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
        public double getOrDefault(final double key, final double defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Double> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Double, ? extends Double> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double putIfAbsent(final double key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final double key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double replace(final double key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final double key, final double oldValue, final double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsent(final double key, final DoubleUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentNullable(final double key, final DoubleFunction<? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentPartial(final double key, final Double2DoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfPresent(final double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double compute(final double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double merge(final double key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
        public Double replace(final Double key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Double oldValue, final Double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Double key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double compute(final Double key, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double merge(final Double key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
