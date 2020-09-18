package it.unimi.dsi.fastutil.floats;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
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
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Float2DoubleMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Float2DoubleMaps() {
    }
    
    public static ObjectIterator<Float2DoubleMap.Entry> fastIterator(final Float2DoubleMap map) {
        final ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
        return (entries instanceof Float2DoubleMap.FastEntrySet) ? ((Float2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Float2DoubleMap map, final Consumer<? super Float2DoubleMap.Entry> consumer) {
        final ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
        if (entries instanceof Float2DoubleMap.FastEntrySet) {
            ((Float2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Float2DoubleMap.Entry> fastIterable(final Float2DoubleMap map) {
        final ObjectSet<Float2DoubleMap.Entry> entries = map.float2DoubleEntrySet();
        return (entries instanceof Float2DoubleMap.FastEntrySet) ? new ObjectIterable<Float2DoubleMap.Entry>() {
            public ObjectIterator<Float2DoubleMap.Entry> iterator() {
                return ((Float2DoubleMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Float2DoubleMap.Entry> consumer) {
                ((Float2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Float2DoubleMap singleton(final float key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Float2DoubleMap singleton(final Float key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Float2DoubleMap synchronize(final Float2DoubleMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Float2DoubleMap synchronize(final Float2DoubleMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Float2DoubleMap unmodifiable(final Float2DoubleMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Float2DoubleFunctions.EmptyFunction implements Float2DoubleMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Float, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2DoubleEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public FloatSet keySet() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public DoubleCollection values() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Float2DoubleMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Float2DoubleFunctions.Singleton implements Float2DoubleMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final float key, final double value) {
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
        
        public void putAll(final Map<? extends Float, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractFloat2DoubleMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Double>>)this.float2DoubleEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.singleton(this.key);
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
            return HashCommon.float2int(this.key) ^ HashCommon.double2int(this.value);
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
    
    public static class SynchronizedMap extends Float2DoubleFunctions.SynchronizedFunction implements Float2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient DoubleCollection values;
        
        protected SynchronizedMap(final Float2DoubleMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Float2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Float, ? extends Double> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> float2DoubleEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.float2DoubleEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Double>>)this.float2DoubleEntrySet();
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
        public double getOrDefault(final float key, final double defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Float, ? super Double> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super Double, ? extends Double> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public double putIfAbsent(final float key, final double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final float key, final double value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public double replace(final float key, final double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final float key, final double oldValue, final double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public double computeIfAbsent(final float key, final DoubleUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentNullable(final float key, final DoubleFunction<? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentPartial(final float key, final Float2DoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfPresent(final float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public double compute(final float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public double merge(final float key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
        public Double replace(final Float key, final Double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final Double oldValue, final Double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Float key, final Double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double compute(final Float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double merge(final Float key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Float2DoubleFunctions.UnmodifiableFunction implements Float2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient FloatSet keys;
        protected transient DoubleCollection values;
        
        protected UnmodifiableMap(final Float2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Float, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> float2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.float2DoubleEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Float, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Float, Double>>)this.float2DoubleEntrySet();
        }
        
        @Override
        public FloatSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSets.unmodifiable(this.map.keySet());
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
        public double getOrDefault(final float key, final double defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Float, ? super Double> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Float, ? super Double, ? extends Double> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double putIfAbsent(final float key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final float key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double replace(final float key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final float key, final double oldValue, final double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsent(final float key, final DoubleUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentNullable(final float key, final DoubleFunction<? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentPartial(final float key, final Float2DoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfPresent(final float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double compute(final float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double merge(final float key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
        public Double replace(final Float key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Float key, final Double oldValue, final Double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Float key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Float key, final java.util.function.Function<? super Float, ? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double compute(final Float key, final BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double merge(final Float key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
