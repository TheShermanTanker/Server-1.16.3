package it.unimi.dsi.fastutil.objects;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.bytes.ByteCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteSets;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

public final class Object2ByteMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Object2ByteMaps() {
    }
    
    public static <K> ObjectIterator<Object2ByteMap.Entry<K>> fastIterator(final Object2ByteMap<K> map) {
        final ObjectSet<Object2ByteMap.Entry<K>> entries = map.object2ByteEntrySet();
        return (entries instanceof Object2ByteMap.FastEntrySet) ? ((Object2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> void fastForEach(final Object2ByteMap<K> map, final Consumer<? super Object2ByteMap.Entry<K>> consumer) {
        final ObjectSet<Object2ByteMap.Entry<K>> entries = map.object2ByteEntrySet();
        if (entries instanceof Object2ByteMap.FastEntrySet) {
            ((Object2ByteMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static <K> ObjectIterable<Object2ByteMap.Entry<K>> fastIterable(final Object2ByteMap<K> map) {
        final ObjectSet<Object2ByteMap.Entry<K>> entries = map.object2ByteEntrySet();
        return (entries instanceof Object2ByteMap.FastEntrySet) ? new ObjectIterable<Object2ByteMap.Entry<K>>() {
            public ObjectIterator<Object2ByteMap.Entry<K>> iterator() {
                return ((Object2ByteMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Object2ByteMap.Entry<K>> consumer) {
                ((Object2ByteMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static <K> Object2ByteMap<K> emptyMap() {
        return (Object2ByteMap<K>)Object2ByteMaps.EMPTY_MAP;
    }
    
    public static <K> Object2ByteMap<K> singleton(final K key, final byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteMap<K> singleton(final K key, final Byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteMap<K> synchronize(final Object2ByteMap<K> m) {
        return new SynchronizedMap<K>(m);
    }
    
    public static <K> Object2ByteMap<K> synchronize(final Object2ByteMap<K> m, final Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }
    
    public static <K> Object2ByteMap<K> unmodifiable(final Object2ByteMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap<K> extends Object2ByteFunctions.EmptyFunction<K> implements Object2ByteMap<K>, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ByteEntrySet() {
            return (ObjectSet<Entry<K>>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSet<K> keySet() {
            return (ObjectSet<K>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteCollection values() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Object2ByteMaps.EMPTY_MAP;
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
    
    public static class Singleton<K> extends Object2ByteFunctions.Singleton<K> implements Object2ByteMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ByteCollection values;
        
        protected Singleton(final K key, final byte value) {
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
        
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractObject2ByteMap.BasicEntry<K>(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>singleton(this.key);
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
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ this.value;
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
    
    public static class SynchronizedMap<K> extends Object2ByteFunctions.SynchronizedFunction<K> implements Object2ByteMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ByteCollection values;
        
        protected SynchronizedMap(final Object2ByteMap<K> m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Object2ByteMap<K> m) {
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
        
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ByteEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry<K>>synchronize(this.map.object2ByteEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = ObjectSets.<K>synchronize(this.map.keySet(), this.sync);
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
        public byte getOrDefault(final Object key, final byte defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super K, ? super Byte> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Byte, ? extends Byte> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public byte putIfAbsent(final K key, final byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final Object key, final byte value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public byte replace(final K key, final byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final K key, final byte oldValue, final byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public byte computeByteIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeByteIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeByteIfAbsentPartial(final K key, final Object2ByteFunction<? super K> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeByteIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public byte computeByteIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeByteIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public byte computeByte(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeByte(key, remappingFunction);
            }
        }
        
        @Override
        public byte mergeByte(final K key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.mergeByte(key, value, remappingFunction);
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
        public Byte replace(final K key, final Byte value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Byte oldValue, final Byte newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final K key, final Byte value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        public Byte computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Byte> mappingFunction) {
            synchronized (this.sync) {
                return (Byte)this.map.computeIfAbsent(key, (java.util.function.Function)mappingFunction);
            }
        }
        
        public Byte computeIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return (Byte)this.map.computeIfPresent(key, (BiFunction)remappingFunction);
            }
        }
        
        public Byte compute(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return (Byte)this.map.compute(key, (BiFunction)remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Byte merge(final K key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap<K> extends Object2ByteFunctions.UnmodifiableFunction<K> implements Object2ByteMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteMap<K> map;
        protected transient ObjectSet<Entry<K>> entries;
        protected transient ObjectSet<K> keys;
        protected transient ByteCollection values;
        
        protected UnmodifiableMap(final Object2ByteMap<K> m) {
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
        
        public void putAll(final Map<? extends K, ? extends Byte> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry<K>> object2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry<K>>unmodifiable(this.map.object2ByteEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSets.<K>unmodifiable(this.map.keySet());
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
        public byte getOrDefault(final Object key, final byte defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super K, ? super Byte> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super K, ? super Byte, ? extends Byte> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte putIfAbsent(final K key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final Object key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte replace(final K key, final byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final K key, final byte oldValue, final byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeByteIfAbsent(final K key, final ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeByteIfAbsentPartial(final K key, final Object2ByteFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeByteIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte computeByte(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte mergeByte(final K key, final byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
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
        public Byte replace(final K key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final K key, final Byte oldValue, final Byte newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte putIfAbsent(final K key, final Byte value) {
            throw new UnsupportedOperationException();
        }
        
        public Byte computeIfAbsent(final K key, final java.util.function.Function<? super K, ? extends Byte> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Byte computeIfPresent(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        public Byte compute(final K key, final BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte merge(final K key, final Byte value, final BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
