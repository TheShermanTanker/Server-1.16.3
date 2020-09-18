package it.unimi.dsi.fastutil.bytes;

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

public final class Byte2ByteMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2ByteMaps() {
    }
    
    public static ObjectIterator<Byte2ByteMap.Entry> fastIterator(final Byte2ByteMap map) {
        final ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
        return (entries instanceof Byte2ByteMap.FastEntrySet) ? ((Byte2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Byte2ByteMap map, final Consumer<? super Byte2ByteMap.Entry> consumer) {
        final ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
        if (entries instanceof Byte2ByteMap.FastEntrySet) {
            ((Byte2ByteMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Byte2ByteMap.Entry> fastIterable(final Byte2ByteMap map) {
        final ObjectSet<Byte2ByteMap.Entry> entries = map.byte2ByteEntrySet();
        return (entries instanceof Byte2ByteMap.FastEntrySet) ? new ObjectIterable<Byte2ByteMap.Entry>() {
            public ObjectIterator<Byte2ByteMap.Entry> iterator() {
                return ((Byte2ByteMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Byte2ByteMap.Entry> consumer) {
                ((Byte2ByteMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Byte2ByteMap singleton(final byte key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Byte2ByteMap singleton(final Byte key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Byte2ByteMap synchronize(final Byte2ByteMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Byte2ByteMap synchronize(final Byte2ByteMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Byte2ByteMap unmodifiable(final Byte2ByteMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Byte2ByteFunctions.EmptyFunction implements Byte2ByteMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Byte, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2ByteEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public ByteCollection values() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2ByteMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Byte2ByteFunctions.Singleton implements Byte2ByteMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient ByteCollection values;
        
        protected Singleton(final byte key, final byte value) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2ByteMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
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
            return new StringBuilder().append("{").append((int)this.key).append("=>").append((int)this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Byte2ByteFunctions.SynchronizedFunction implements Byte2ByteMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient ByteCollection values;
        
        protected SynchronizedMap(final Byte2ByteMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2ByteMap m) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Byte> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> byte2ByteEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.byte2ByteEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
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
        public byte getOrDefault(final byte key, final byte defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Byte> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Byte, ? extends Byte> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public byte putIfAbsent(final byte key, final byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final byte value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public byte replace(final byte key, final byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final byte oldValue, final byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public byte computeIfAbsent(final byte key, final IntUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfAbsentNullable(final byte key, final IntFunction<? extends Byte> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfAbsentPartial(final byte key, final Byte2ByteFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public byte compute(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public byte merge(final byte key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
        public Byte replace(final Byte key, final Byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Byte oldValue, final Byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final Byte key, final Byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Byte computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Byte> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte compute(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte merge(final Byte key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Byte2ByteFunctions.UnmodifiableFunction implements Byte2ByteMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ByteMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient ByteCollection values;
        
        protected UnmodifiableMap(final Byte2ByteMap m) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.byte2ByteEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Byte>>)this.byte2ByteEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
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
        public byte getOrDefault(final byte key, final byte defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Byte> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Byte, ? extends Byte> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte putIfAbsent(final byte key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte replace(final byte key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final byte oldValue, final byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsent(final byte key, final IntUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsentNullable(final byte key, final IntFunction<? extends Byte> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfAbsentPartial(final byte key, final Byte2ByteFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte compute(final byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte merge(final byte key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
        public Byte replace(final Byte key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Byte oldValue, final Byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final Byte key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Byte> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte compute(final Byte key, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte merge(final Byte key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
