package it.unimi.dsi.fastutil.ints;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.floats.FloatCollections;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatSets;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Int2FloatMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Int2FloatMaps() {
    }
    
    public static ObjectIterator<Int2FloatMap.Entry> fastIterator(final Int2FloatMap map) {
        final ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
        return (entries instanceof Int2FloatMap.FastEntrySet) ? ((Int2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Int2FloatMap map, final Consumer<? super Int2FloatMap.Entry> consumer) {
        final ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
        if (entries instanceof Int2FloatMap.FastEntrySet) {
            ((Int2FloatMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Int2FloatMap.Entry> fastIterable(final Int2FloatMap map) {
        final ObjectSet<Int2FloatMap.Entry> entries = map.int2FloatEntrySet();
        return (entries instanceof Int2FloatMap.FastEntrySet) ? new ObjectIterable<Int2FloatMap.Entry>() {
            public ObjectIterator<Int2FloatMap.Entry> iterator() {
                return ((Int2FloatMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Int2FloatMap.Entry> consumer) {
                ((Int2FloatMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Int2FloatMap singleton(final int key, final float value) {
        return new Singleton(key, value);
    }
    
    public static Int2FloatMap singleton(final Integer key, final Float value) {
        return new Singleton(key, value);
    }
    
    public static Int2FloatMap synchronize(final Int2FloatMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Int2FloatMap synchronize(final Int2FloatMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Int2FloatMap unmodifiable(final Int2FloatMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Int2FloatFunctions.EmptyFunction implements Int2FloatMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final float v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Integer, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2FloatEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public FloatCollection values() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Int2FloatMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Int2FloatFunctions.Singleton implements Int2FloatMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient FloatCollection values;
        
        protected Singleton(final int key, final float value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final float v) {
            return Float.floatToIntBits(this.value) == Float.floatToIntBits(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return Float.floatToIntBits((float)ov) == Float.floatToIntBits(this.value);
        }
        
        public void putAll(final Map<? extends Integer, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2FloatMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Float>>)this.int2FloatEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                this.values = FloatSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return this.key ^ HashCommon.float2int(this.value);
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
    
    public static class SynchronizedMap extends Int2FloatFunctions.SynchronizedFunction implements Int2FloatMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2FloatMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient FloatCollection values;
        
        protected SynchronizedMap(final Int2FloatMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Int2FloatMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Float> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> int2FloatEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.int2FloatEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Float>>)this.int2FloatEntrySet();
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
        public FloatCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return FloatCollections.synchronize(this.map.values(), this.sync);
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
        public float getOrDefault(final int key, final float defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Float> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Float, ? extends Float> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public float putIfAbsent(final int key, final float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final int key, final float value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public float replace(final int key, final float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final float oldValue, final float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public float computeIfAbsent(final int key, final IntToDoubleFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfAbsentNullable(final int key, final IntFunction<? extends Float> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfAbsentPartial(final int key, final Int2FloatFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public float computeIfPresent(final int key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public float compute(final int key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public float merge(final int key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
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
        public Float replace(final Integer key, final Float value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Float oldValue, final Float newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final Integer key, final Float value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Float computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Float> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float compute(final Integer key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Float merge(final Integer key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Int2FloatFunctions.UnmodifiableFunction implements Int2FloatMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2FloatMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient FloatCollection values;
        
        protected UnmodifiableMap(final Int2FloatMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final float v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Integer, ? extends Float> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.int2FloatEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Float>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Float>>)this.int2FloatEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public FloatCollection values() {
            if (this.values == null) {
                return FloatCollections.unmodifiable(this.map.values());
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
        public float getOrDefault(final int key, final float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Float> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Float, ? extends Float> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float putIfAbsent(final int key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final int key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float replace(final int key, final float value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final float oldValue, final float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsent(final int key, final IntToDoubleFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsentNullable(final int key, final IntFunction<? extends Float> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfAbsentPartial(final int key, final Int2FloatFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float computeIfPresent(final int key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float compute(final int key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public float merge(final int key, final float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float getOrDefault(final Object key, final Float defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float replace(final Integer key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Float oldValue, final Float newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float putIfAbsent(final Integer key, final Float value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Float> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float compute(final Integer key, final BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Float merge(final Integer key, final Float value, final BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
