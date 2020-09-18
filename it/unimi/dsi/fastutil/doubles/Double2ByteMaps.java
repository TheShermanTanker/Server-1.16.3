package it.unimi.dsi.fastutil.doubles;

import java.util.function.Function;
import java.util.function.DoubleFunction;
import java.util.function.DoubleToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Double2ByteMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Double2ByteMaps() {
    }
    
    public static ObjectIterator<Double2ByteMap.Entry> fastIterator(final Double2ByteMap map) {
        final ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
        return (entries instanceof Double2ByteMap.FastEntrySet) ? ((Double2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Double2ByteMap map, final Consumer<? super Double2ByteMap.Entry> consumer) {
        final ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
        if (entries instanceof Double2ByteMap.FastEntrySet) {
            ((Double2ByteMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Double2ByteMap.Entry> fastIterable(final Double2ByteMap map) {
        final ObjectSet<Double2ByteMap.Entry> entries = map.double2ByteEntrySet();
        return (entries instanceof Double2ByteMap.FastEntrySet) ? new ObjectIterable<Double2ByteMap.Entry>() {
            public ObjectIterator<Double2ByteMap.Entry> iterator() {
                return ((Double2ByteMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Double2ByteMap.Entry> consumer) {
                ((Double2ByteMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Double2ByteMap singleton(final double key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Double2ByteMap singleton(final Double key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Double2ByteMap synchronize(final Double2ByteMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Double2ByteMap synchronize(final Double2ByteMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Double2ByteMap unmodifiable(final Double2ByteMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Double2ByteFunctions.EmptyFunction implements Double2ByteMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Double, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2ByteEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSet keySet() {
            return DoubleSets.EMPTY_SET;
        }
        
        @Override
        public ByteCollection values() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Double2ByteMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Double2ByteFunctions.Singleton implements Double2ByteMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient ByteCollection values;
        
        protected Singleton(final double key, final byte value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (byte)ov == this.value;
        }
        
        public void putAll(final Map<? extends Double, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractDouble2ByteMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                this.values = ByteSets.singleton(this.value);
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
            return new StringBuilder().append("{").append(this.key).append("=>").append((int)this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Double2ByteFunctions.SynchronizedFunction implements Double2ByteMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient ByteCollection values;
        
        protected SynchronizedMap(final Double2ByteMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Double2ByteMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final byte v) {
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
        
        public void putAll(final Map<? extends Double, ? extends Byte> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> double2ByteEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.double2ByteEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
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
        public ByteCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ByteCollections.synchronize(this.map.values(), this.sync);
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
        public byte getOrDefault(final double key, final byte defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Byte> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Byte, ? extends Byte> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public byte putIfAbsent(final double key, final byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final double key, final byte value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public byte replace(final double key, final byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final double key, final byte oldValue, final byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public byte computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfAbsentNullable(final double key, final DoubleFunction<? extends Byte> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfAbsentPartial(final double key, final Double2ByteFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfPresent(final double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public byte compute(final double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public byte merge(final double key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte getOrDefault(final Object key, final Byte defaultValue) {
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
        public Byte replace(final Double key, final Byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Byte oldValue, final Byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final Double key, final Byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Byte computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Byte> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte computeIfPresent(final Double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte compute(final Double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte merge(final Double key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Double2ByteFunctions.UnmodifiableFunction implements Double2ByteMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient DoubleSet keys;
        protected transient ByteCollection values;
        
        protected UnmodifiableMap(final Double2ByteMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final byte v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Double, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> double2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.double2ByteEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Double, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
        }
        
        @Override
        public DoubleSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ByteCollection values() {
            if (this.values == null) {
                return ByteCollections.unmodifiable(this.map.values());
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
        public byte getOrDefault(final double key, final byte defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Double, ? super Byte> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Double, ? super Byte, ? extends Byte> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte putIfAbsent(final double key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final double key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte replace(final double key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final double key, final byte oldValue, final byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsent(final double key, final DoubleToIntFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsentNullable(final double key, final DoubleFunction<? extends Byte> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsentPartial(final double key, final Double2ByteFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfPresent(final double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte compute(final double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte merge(final double key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte getOrDefault(final Object key, final Byte defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte replace(final Double key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Double key, final Byte oldValue, final Byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final Double key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte computeIfAbsent(final Double key, final java.util.function.Function<? super Double, ? extends Byte> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte computeIfPresent(final Double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte compute(final Double key, final BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte merge(final Double key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
