package it.unimi.dsi.fastutil.bytes;

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

public final class Byte2LongMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2LongMaps() {
    }
    
    public static ObjectIterator<Byte2LongMap.Entry> fastIterator(final Byte2LongMap map) {
        final ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
        return (entries instanceof Byte2LongMap.FastEntrySet) ? ((Byte2LongMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Byte2LongMap map, final Consumer<? super Byte2LongMap.Entry> consumer) {
        final ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
        if (entries instanceof Byte2LongMap.FastEntrySet) {
            ((Byte2LongMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Byte2LongMap.Entry> fastIterable(final Byte2LongMap map) {
        final ObjectSet<Byte2LongMap.Entry> entries = map.byte2LongEntrySet();
        return (entries instanceof Byte2LongMap.FastEntrySet) ? new ObjectIterable<Byte2LongMap.Entry>() {
            public ObjectIterator<Byte2LongMap.Entry> iterator() {
                return ((Byte2LongMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Byte2LongMap.Entry> consumer) {
                ((Byte2LongMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Byte2LongMap singleton(final byte key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Byte2LongMap singleton(final Byte key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Byte2LongMap synchronize(final Byte2LongMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Byte2LongMap synchronize(final Byte2LongMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Byte2LongMap unmodifiable(final Byte2LongMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Byte2LongFunctions.EmptyFunction implements Byte2LongMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Byte, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2LongEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public LongCollection values() {
            return LongSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2LongMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Byte2LongFunctions.Singleton implements Byte2LongMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient LongCollection values;
        
        protected Singleton(final byte key, final long value) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2LongMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Long>>)this.byte2LongEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
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
            return new StringBuilder().append("{").append((int)this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Byte2LongFunctions.SynchronizedFunction implements Byte2LongMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2LongMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient LongCollection values;
        
        protected SynchronizedMap(final Byte2LongMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2LongMap m) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Long> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> byte2LongEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.byte2LongEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Long>>)this.byte2LongEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ByteSets.synchronize(this.map.keySet(), this.sync);
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
        public long getOrDefault(final byte key, final long defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Long> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Long, ? extends Long> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public long putIfAbsent(final byte key, final long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final long value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public long replace(final byte key, final long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final long oldValue, final long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public long computeIfAbsent(final byte key, final IntToLongFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfAbsentNullable(final byte key, final IntFunction<? extends Long> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfAbsentPartial(final byte key, final Byte2LongFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public long computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public long compute(final byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public long merge(final byte key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
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
        public Long replace(final Byte key, final Long value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Long oldValue, final Long newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final Byte key, final Long value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Long computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Long> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long compute(final Byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Long merge(final Byte key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Byte2LongFunctions.UnmodifiableFunction implements Byte2LongMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2LongMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient LongCollection values;
        
        protected UnmodifiableMap(final Byte2LongMap m) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Long> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.byte2LongEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Long>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Long>>)this.byte2LongEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
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
        public long getOrDefault(final byte key, final long defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Long> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Long, ? extends Long> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long putIfAbsent(final byte key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long replace(final byte key, final long value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final long oldValue, final long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsent(final byte key, final IntToLongFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsentNullable(final byte key, final IntFunction<? extends Long> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfAbsentPartial(final byte key, final Byte2LongFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long compute(final byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long merge(final byte key, final long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
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
        public Long replace(final Byte key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Long oldValue, final Long newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long putIfAbsent(final Byte key, final Long value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Long> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long compute(final Byte key, final BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long merge(final Byte key, final Long value, final BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
