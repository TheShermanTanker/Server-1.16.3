package it.unimi.dsi.fastutil.shorts;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
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

public final class Short2DoubleMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Short2DoubleMaps() {
    }
    
    public static ObjectIterator<Short2DoubleMap.Entry> fastIterator(final Short2DoubleMap map) {
        final ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
        return (entries instanceof Short2DoubleMap.FastEntrySet) ? ((Short2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Short2DoubleMap map, final Consumer<? super Short2DoubleMap.Entry> consumer) {
        final ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
        if (entries instanceof Short2DoubleMap.FastEntrySet) {
            ((Short2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Short2DoubleMap.Entry> fastIterable(final Short2DoubleMap map) {
        final ObjectSet<Short2DoubleMap.Entry> entries = map.short2DoubleEntrySet();
        return (entries instanceof Short2DoubleMap.FastEntrySet) ? new ObjectIterable<Short2DoubleMap.Entry>() {
            public ObjectIterator<Short2DoubleMap.Entry> iterator() {
                return ((Short2DoubleMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Short2DoubleMap.Entry> consumer) {
                ((Short2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Short2DoubleMap singleton(final short key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Short2DoubleMap singleton(final Short key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Short2DoubleMap synchronize(final Short2DoubleMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Short2DoubleMap synchronize(final Short2DoubleMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Short2DoubleMap unmodifiable(final Short2DoubleMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Short2DoubleFunctions.EmptyFunction implements Short2DoubleMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Short, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> short2DoubleEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ShortSet keySet() {
            return ShortSets.EMPTY_SET;
        }
        
        @Override
        public DoubleCollection values() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Short2DoubleMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Short2DoubleFunctions.Singleton implements Short2DoubleMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ShortSet keys;
        protected transient DoubleCollection values;
        
        protected Singleton(final short key, final double value) {
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
        
        public void putAll(final Map<? extends Short, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> short2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractShort2DoubleMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Short, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
        }
        
        @Override
        public ShortSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSets.singleton(this.key);
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
            return this.key ^ HashCommon.double2int(this.value);
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
            return new StringBuilder().append("{").append((int)this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Short2DoubleFunctions.SynchronizedFunction implements Short2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ShortSet keys;
        protected transient DoubleCollection values;
        
        protected SynchronizedMap(final Short2DoubleMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Short2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Short, ? extends Double> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> short2DoubleEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.short2DoubleEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Short, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
        }
        
        @Override
        public ShortSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ShortSets.synchronize(this.map.keySet(), this.sync);
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
        public double getOrDefault(final short key, final double defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Short, ? super Double> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Short, ? super Double, ? extends Double> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public double putIfAbsent(final short key, final double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final short key, final double value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public double replace(final short key, final double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final short key, final double oldValue, final double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public double computeIfAbsent(final short key, final IntToDoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentNullable(final short key, final IntFunction<? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfAbsentPartial(final short key, final Short2DoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public double computeIfPresent(final short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public double compute(final short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public double merge(final short key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
        public Double replace(final Short key, final Double value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Short key, final Double oldValue, final Double newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Short key, final Double value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Double> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double compute(final Short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Double merge(final Short key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Short2DoubleFunctions.UnmodifiableFunction implements Short2DoubleMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2DoubleMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ShortSet keys;
        protected transient DoubleCollection values;
        
        protected UnmodifiableMap(final Short2DoubleMap m) {
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
        
        public void putAll(final Map<? extends Short, ? extends Double> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> short2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.short2DoubleEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Short, Double>> entrySet() {
            return (ObjectSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
        }
        
        @Override
        public ShortSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSets.unmodifiable(this.map.keySet());
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
        public double getOrDefault(final short key, final double defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Short, ? super Double> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Short, ? super Double, ? extends Double> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double putIfAbsent(final short key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final short key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double replace(final short key, final double value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final short key, final double oldValue, final double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsent(final short key, final IntToDoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentNullable(final short key, final IntFunction<? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfAbsentPartial(final short key, final Short2DoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double computeIfPresent(final short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double compute(final short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public double merge(final short key, final double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
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
        public Double replace(final Short key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Short key, final Double oldValue, final Double newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double putIfAbsent(final Short key, final Double value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfAbsent(final Short key, final java.util.function.Function<? super Short, ? extends Double> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double computeIfPresent(final Short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double compute(final Short key, final BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Double merge(final Short key, final Double value, final BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
