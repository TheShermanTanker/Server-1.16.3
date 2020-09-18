package it.unimi.dsi.fastutil.chars;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.shorts.ShortCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortSets;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Char2ShortMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Char2ShortMaps() {
    }
    
    public static ObjectIterator<Char2ShortMap.Entry> fastIterator(final Char2ShortMap map) {
        final ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
        return (entries instanceof Char2ShortMap.FastEntrySet) ? ((Char2ShortMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Char2ShortMap map, final Consumer<? super Char2ShortMap.Entry> consumer) {
        final ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
        if (entries instanceof Char2ShortMap.FastEntrySet) {
            ((Char2ShortMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Char2ShortMap.Entry> fastIterable(final Char2ShortMap map) {
        final ObjectSet<Char2ShortMap.Entry> entries = map.char2ShortEntrySet();
        return (entries instanceof Char2ShortMap.FastEntrySet) ? new ObjectIterable<Char2ShortMap.Entry>() {
            public ObjectIterator<Char2ShortMap.Entry> iterator() {
                return ((Char2ShortMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Char2ShortMap.Entry> consumer) {
                ((Char2ShortMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Char2ShortMap singleton(final char key, final short value) {
        return new Singleton(key, value);
    }
    
    public static Char2ShortMap singleton(final Character key, final Short value) {
        return new Singleton(key, value);
    }
    
    public static Char2ShortMap synchronize(final Char2ShortMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Char2ShortMap synchronize(final Char2ShortMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Char2ShortMap unmodifiable(final Char2ShortMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Char2ShortFunctions.EmptyFunction implements Char2ShortMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final short v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Character, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2ShortEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public CharSet keySet() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public ShortCollection values() {
            return ShortSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Char2ShortMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Char2ShortFunctions.Singleton implements Char2ShortMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient ShortCollection values;
        
        protected Singleton(final char key, final short value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final short v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (short)ov == this.value;
        }
        
        public void putAll(final Map<? extends Character, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractChar2ShortMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Character, Short>> entrySet() {
            return (ObjectSet<Map.Entry<Character, Short>>)this.char2ShortEntrySet();
        }
        
        @Override
        public CharSet keySet() {
            if (this.keys == null) {
                this.keys = CharSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                this.values = ShortSets.singleton(this.value);
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
            return new StringBuilder().append("{").append(this.key).append("=>").append((int)this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Char2ShortFunctions.SynchronizedFunction implements Char2ShortMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ShortMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient ShortCollection values;
        
        protected SynchronizedMap(final Char2ShortMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Char2ShortMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final short v) {
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
        
        public void putAll(final Map<? extends Character, ? extends Short> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> char2ShortEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.char2ShortEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Character, Short>> entrySet() {
            return (ObjectSet<Map.Entry<Character, Short>>)this.char2ShortEntrySet();
        }
        
        @Override
        public CharSet keySet() {
            synchronized (this.sync) {
                if (this.keys == null) {
                    this.keys = CharSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }
        
        @Override
        public ShortCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return ShortCollections.synchronize(this.map.values(), this.sync);
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
        public short getOrDefault(final char key, final short defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Character, ? super Short> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Character, ? super Short, ? extends Short> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public short putIfAbsent(final char key, final short value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final char key, final short value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public short replace(final char key, final short value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final char key, final short oldValue, final short newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public short computeIfAbsent(final char key, final IntUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public short computeIfAbsentNullable(final char key, final IntFunction<? extends Short> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public short computeIfAbsentPartial(final char key, final Char2ShortFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public short computeIfPresent(final char key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public short compute(final char key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public short merge(final char key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short getOrDefault(final Object key, final Short defaultValue) {
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
        public Short replace(final Character key, final Short value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Character key, final Short oldValue, final Short newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Short putIfAbsent(final Character key, final Short value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Short computeIfAbsent(final Character key, final java.util.function.Function<? super Character, ? extends Short> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short computeIfPresent(final Character key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short compute(final Character key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Short merge(final Character key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Char2ShortFunctions.UnmodifiableFunction implements Char2ShortMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ShortMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient CharSet keys;
        protected transient ShortCollection values;
        
        protected UnmodifiableMap(final Char2ShortMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final short v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Character, ? extends Short> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> char2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.char2ShortEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Character, Short>> entrySet() {
            return (ObjectSet<Map.Entry<Character, Short>>)this.char2ShortEntrySet();
        }
        
        @Override
        public CharSet keySet() {
            if (this.keys == null) {
                this.keys = CharSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public ShortCollection values() {
            if (this.values == null) {
                return ShortCollections.unmodifiable(this.map.values());
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
        public short getOrDefault(final char key, final short defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Character, ? super Short> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Character, ? super Short, ? extends Short> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short putIfAbsent(final char key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final char key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short replace(final char key, final short value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final char key, final short oldValue, final short newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeIfAbsent(final char key, final IntUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeIfAbsentNullable(final char key, final IntFunction<? extends Short> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeIfAbsentPartial(final char key, final Char2ShortFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short computeIfPresent(final char key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short compute(final char key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short merge(final char key, final short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short getOrDefault(final Object key, final Short defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short replace(final Character key, final Short value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Character key, final Short oldValue, final Short newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short putIfAbsent(final Character key, final Short value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short computeIfAbsent(final Character key, final java.util.function.Function<? super Character, ? extends Short> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short computeIfPresent(final Character key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short compute(final Character key, final BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short merge(final Character key, final Short value, final BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
