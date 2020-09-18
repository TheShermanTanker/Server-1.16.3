package it.unimi.dsi.fastutil.ints;

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

public final class Int2BooleanMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Int2BooleanMaps() {
    }
    
    public static ObjectIterator<Int2BooleanMap.Entry> fastIterator(final Int2BooleanMap map) {
        final ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
        return (entries instanceof Int2BooleanMap.FastEntrySet) ? ((Int2BooleanMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Int2BooleanMap map, final Consumer<? super Int2BooleanMap.Entry> consumer) {
        final ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
        if (entries instanceof Int2BooleanMap.FastEntrySet) {
            ((Int2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Int2BooleanMap.Entry> fastIterable(final Int2BooleanMap map) {
        final ObjectSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
        return (entries instanceof Int2BooleanMap.FastEntrySet) ? new ObjectIterable<Int2BooleanMap.Entry>() {
            public ObjectIterator<Int2BooleanMap.Entry> iterator() {
                return ((Int2BooleanMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Int2BooleanMap.Entry> consumer) {
                ((Int2BooleanMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Int2BooleanMap singleton(final int key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanMap singleton(final Integer key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanMap synchronize(final Int2BooleanMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Int2BooleanMap synchronize(final Int2BooleanMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Int2BooleanMap unmodifiable(final Int2BooleanMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Int2BooleanFunctions.EmptyFunction implements Int2BooleanMap, Serializable, Cloneable {
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
        
        public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2BooleanEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public IntSet keySet() {
            return IntSets.EMPTY_SET;
        }
        
        @Override
        public BooleanCollection values() {
            return BooleanSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Int2BooleanMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Int2BooleanFunctions.Singleton implements Int2BooleanMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient BooleanCollection values;
        
        protected Singleton(final int key, final boolean value) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractInt2BooleanMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.singleton(this.key);
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
            return new StringBuilder().append("{").append(this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Int2BooleanFunctions.SynchronizedFunction implements Int2BooleanMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient BooleanCollection values;
        
        protected SynchronizedMap(final Int2BooleanMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Int2BooleanMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> int2BooleanEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.int2BooleanEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
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
        public boolean getOrDefault(final int key, final boolean defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Boolean> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public boolean putIfAbsent(final int key, final boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final int key, final boolean value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final int key, final boolean oldValue, final boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public boolean computeIfAbsent(final int key, final IntPredicate mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfAbsentNullable(final int key, final IntFunction<? extends Boolean> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfAbsentPartial(final int key, final Int2BooleanFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public boolean computeIfPresent(final int key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public boolean compute(final int key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public boolean merge(final int key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
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
        public Boolean replace(final Integer key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Boolean oldValue, final Boolean newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final Integer key, final Boolean value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Boolean computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Boolean> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean compute(final Integer key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Boolean merge(final Integer key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Int2BooleanFunctions.UnmodifiableFunction implements Int2BooleanMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient IntSet keys;
        protected transient BooleanCollection values;
        
        protected UnmodifiableMap(final Int2BooleanMap m) {
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
        
        public void putAll(final Map<? extends Integer, ? extends Boolean> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> int2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.int2BooleanEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
        }
        
        @Override
        public IntSet keySet() {
            if (this.keys == null) {
                this.keys = IntSets.unmodifiable(this.map.keySet());
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
        public boolean getOrDefault(final int key, final boolean defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Integer, ? super Boolean> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean putIfAbsent(final int key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final int key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final int key, final boolean oldValue, final boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsent(final int key, final IntPredicate mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsentNullable(final int key, final IntFunction<? extends Boolean> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfAbsentPartial(final int key, final Int2BooleanFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean computeIfPresent(final int key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean compute(final int key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean merge(final int key, final boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
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
        public Boolean replace(final Integer key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Integer key, final Boolean oldValue, final Boolean newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean putIfAbsent(final Integer key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean computeIfAbsent(final Integer key, final java.util.function.Function<? super Integer, ? extends Boolean> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean computeIfPresent(final Integer key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean compute(final Integer key, final BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean merge(final Integer key, final Boolean value, final BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
