package it.unimi.dsi.fastutil.bytes;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.booleans.BooleanCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanSets;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Byte2BooleanMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2BooleanMaps() {
    }
    
    public static ObjectIterator<Byte2BooleanMap.Entry> fastIterator(final Byte2BooleanMap map) {
        final ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
        return (entries instanceof Byte2BooleanMap.FastEntrySet) ? ((Byte2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Byte2BooleanMap map, final Consumer<? super Byte2BooleanMap.Entry> consumer) {
        final ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
        if (entries instanceof Byte2BooleanMap.FastEntrySet) {
            ((Byte2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Byte2BooleanMap.Entry> fastIterable(final Byte2BooleanMap map) {
        final ObjectSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
        return (entries instanceof Byte2BooleanMap.FastEntrySet) ? new ObjectIterable<Byte2BooleanMap.Entry>() {
            public ObjectIterator<Byte2BooleanMap.Entry> iterator() {
                return ((Byte2BooleanMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Byte2BooleanMap.Entry> consumer) {
                ((Byte2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Byte2BooleanMap singleton(final byte key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Byte2BooleanMap singleton(final Byte key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Byte2BooleanMap synchronize(final Byte2BooleanMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Byte2BooleanMap synchronize(final Byte2BooleanMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Byte2BooleanMap unmodifiable(final Byte2BooleanMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Byte2BooleanFunctions.EmptyFunction implements Byte2BooleanMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Byte, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2BooleanEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public BooleanCollection values() {
            return BooleanSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2BooleanMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Byte2BooleanFunctions.Singleton implements Byte2BooleanMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient BooleanCollection values;
        
        protected Singleton(final byte key, final boolean value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (boolean)ov == this.value;
        }
        
        public void putAll(final Map<? extends Byte, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2BooleanMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public BooleanCollection values() {
            if (this.values == null) {
                this.values = BooleanSets.singleton(this.value);
            }
            return this.values;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public int hashCode() {
            return this.key ^ (this.value ? 1231 : 1237);
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
    
    public static class SynchronizedMap extends Byte2BooleanFunctions.SynchronizedFunction implements Byte2BooleanMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2BooleanMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient BooleanCollection values;
        
        protected SynchronizedMap(final Byte2BooleanMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2BooleanMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final boolean v) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Boolean> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> byte2BooleanEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.byte2BooleanEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
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
        public BooleanCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return BooleanCollections.synchronize(this.map.values(), this.sync);
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
        public boolean getOrDefault(final byte key, final boolean defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Boolean> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public boolean putIfAbsent(final byte key, final boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final boolean value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final boolean oldValue, final boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public boolean computeIfAbsent(final byte key, final IntPredicate mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfAbsentNullable(final byte key, final IntFunction<? extends Boolean> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfAbsentPartial(final byte key, final Byte2BooleanFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public boolean compute(final byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public boolean merge(final byte key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean getOrDefault(final Object key, final Boolean defaultValue) {
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
        public Boolean replace(final Byte key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Boolean oldValue, final Boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final Byte key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Boolean computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Boolean> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean compute(final Byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean merge(final Byte key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Byte2BooleanFunctions.UnmodifiableFunction implements Byte2BooleanMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2BooleanMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient BooleanCollection values;
        
        protected UnmodifiableMap(final Byte2BooleanMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final boolean v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Byte, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.byte2BooleanEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public BooleanCollection values() {
            if (this.values == null) {
                return BooleanCollections.unmodifiable(this.map.values());
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
        public boolean getOrDefault(final byte key, final boolean defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Boolean> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putIfAbsent(final byte key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final boolean oldValue, final boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsent(final byte key, final IntPredicate mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsentNullable(final byte key, final IntFunction<? extends Boolean> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsentPartial(final byte key, final Byte2BooleanFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean compute(final byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean merge(final byte key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean getOrDefault(final Object key, final Boolean defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean replace(final Byte key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Boolean oldValue, final Boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final Byte key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Boolean> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean compute(final Byte key, final BiFunction<? super Byte, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean merge(final Byte key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
