package it.unimi.dsi.fastutil.bytes;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import it.unimi.dsi.fastutil.chars.CharCollections;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharSets;
import it.unimi.dsi.fastutil.chars.CharCollection;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map;
import java.io.Serializable;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public final class Byte2CharMaps {
    public static final EmptyMap EMPTY_MAP;
    
    private Byte2CharMaps() {
    }
    
    public static ObjectIterator<Byte2CharMap.Entry> fastIterator(final Byte2CharMap map) {
        final ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
        return (entries instanceof Byte2CharMap.FastEntrySet) ? ((Byte2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static void fastForEach(final Byte2CharMap map, final Consumer<? super Byte2CharMap.Entry> consumer) {
        final ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
        if (entries instanceof Byte2CharMap.FastEntrySet) {
            ((Byte2CharMap.FastEntrySet)entries).fastForEach(consumer);
        }
        else {
            entries.forEach((Consumer)consumer);
        }
    }
    
    public static ObjectIterable<Byte2CharMap.Entry> fastIterable(final Byte2CharMap map) {
        final ObjectSet<Byte2CharMap.Entry> entries = map.byte2CharEntrySet();
        return (entries instanceof Byte2CharMap.FastEntrySet) ? new ObjectIterable<Byte2CharMap.Entry>() {
            public ObjectIterator<Byte2CharMap.Entry> iterator() {
                return ((Byte2CharMap.FastEntrySet)entries).fastIterator();
            }
            
            public void forEach(final Consumer<? super Byte2CharMap.Entry> consumer) {
                ((Byte2CharMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }
    
    public static Byte2CharMap singleton(final byte key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Byte2CharMap singleton(final Byte key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Byte2CharMap synchronize(final Byte2CharMap m) {
        return new SynchronizedMap(m);
    }
    
    public static Byte2CharMap synchronize(final Byte2CharMap m, final Object sync) {
        return new SynchronizedMap(m, sync);
    }
    
    public static Byte2CharMap unmodifiable(final Byte2CharMap m) {
        return new UnmodifiableMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptyMap();
    }
    
    public static class EmptyMap extends Byte2CharFunctions.EmptyFunction implements Byte2CharMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyMap() {
        }
        
        @Override
        public boolean containsValue(final char v) {
            return false;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return false;
        }
        
        public void putAll(final Map<? extends Byte, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2CharEntrySet() {
            return (ObjectSet<Entry>)ObjectSets.EMPTY_SET;
        }
        
        @Override
        public ByteSet keySet() {
            return ByteSets.EMPTY_SET;
        }
        
        @Override
        public CharCollection values() {
            return CharSets.EMPTY_SET;
        }
        
        @Override
        public Object clone() {
            return Byte2CharMaps.EMPTY_MAP;
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
    
    public static class Singleton extends Byte2CharFunctions.Singleton implements Byte2CharMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient CharCollection values;
        
        protected Singleton(final byte key, final char value) {
            super(key, value);
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.value == v;
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return (char)ov == this.value;
        }
        
        public void putAll(final Map<? extends Byte, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractByte2CharMap.BasicEntry(this.key, this.value));
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Character>>)this.byte2CharEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.singleton(this.key);
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                this.values = CharSets.singleton(this.value);
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
            return new StringBuilder().append("{").append((int)this.key).append("=>").append(this.value).append("}").toString();
        }
    }
    
    public static class SynchronizedMap extends Byte2CharFunctions.SynchronizedFunction implements Byte2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient CharCollection values;
        
        protected SynchronizedMap(final Byte2CharMap m, final Object sync) {
            super(m, sync);
            this.map = m;
        }
        
        protected SynchronizedMap(final Byte2CharMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
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
        
        public void putAll(final Map<? extends Byte, ? extends Character> m) {
            synchronized (this.sync) {
                this.map.putAll((Map)m);
            }
        }
        
        @Override
        public ObjectSet<Entry> byte2CharEntrySet() {
            synchronized (this.sync) {
                if (this.entries == null) {
                    this.entries = ObjectSets.<Entry>synchronize(this.map.byte2CharEntrySet(), this.sync);
                }
                return this.entries;
            }
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Character>>)this.byte2CharEntrySet();
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
        public CharCollection values() {
            synchronized (this.sync) {
                if (this.values == null) {
                    return CharCollections.synchronize(this.map.values(), this.sync);
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
        public char getOrDefault(final byte key, final char defaultValue) {
            synchronized (this.sync) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Character> action) {
            synchronized (this.sync) {
                this.map.forEach((BiConsumer)action);
            }
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Character, ? extends Character> function) {
            synchronized (this.sync) {
                this.map.replaceAll((BiFunction)function);
            }
        }
        
        @Override
        public char putIfAbsent(final byte key, final char value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Override
        public boolean remove(final byte key, final char value) {
            synchronized (this.sync) {
                return this.map.remove(key, value);
            }
        }
        
        @Override
        public char replace(final byte key, final char value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Override
        public boolean replace(final byte key, final char oldValue, final char newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Override
        public char computeIfAbsent(final byte key, final IntUnaryOperator mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentNullable(final byte key, final IntFunction<? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentNullable(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfAbsentPartial(final byte key, final Byte2CharFunction mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsentPartial(key, mappingFunction);
            }
        }
        
        @Override
        public char computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Override
        public char compute(final byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Override
        public char merge(final byte key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
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
        public Character replace(final Byte key, final Character value) {
            synchronized (this.sync) {
                return this.map.replace(key, value);
            }
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Character oldValue, final Character newValue) {
            synchronized (this.sync) {
                return this.map.replace(key, oldValue, newValue);
            }
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Byte key, final Character value) {
            synchronized (this.sync) {
                return this.map.putIfAbsent(key, value);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Character> mappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfAbsent(key, mappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.computeIfPresent(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character compute(final Byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.compute(key, remappingFunction);
            }
        }
        
        @Deprecated
        @Override
        public Character merge(final Byte key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            synchronized (this.sync) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }
    
    public static class UnmodifiableMap extends Byte2CharFunctions.UnmodifiableFunction implements Byte2CharMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2CharMap map;
        protected transient ObjectSet<Entry> entries;
        protected transient ByteSet keys;
        protected transient CharCollection values;
        
        protected UnmodifiableMap(final Byte2CharMap m) {
            super(m);
            this.map = m;
        }
        
        @Override
        public boolean containsValue(final char v) {
            return this.map.containsValue(v);
        }
        
        @Deprecated
        @Override
        public boolean containsValue(final Object ov) {
            return this.map.containsValue(ov);
        }
        
        public void putAll(final Map<? extends Byte, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectSet<Entry> byte2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.<Entry>unmodifiable(this.map.byte2CharEntrySet());
            }
            return this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSet<Map.Entry<Byte, Character>> entrySet() {
            return (ObjectSet<Map.Entry<Byte, Character>>)this.byte2CharEntrySet();
        }
        
        @Override
        public ByteSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }
        
        @Override
        public CharCollection values() {
            if (this.values == null) {
                return CharCollections.unmodifiable(this.map.values());
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
        public char getOrDefault(final byte key, final char defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        public void forEach(final BiConsumer<? super Byte, ? super Character> action) {
            this.map.forEach((BiConsumer)action);
        }
        
        public void replaceAll(final BiFunction<? super Byte, ? super Character, ? extends Character> function) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char putIfAbsent(final byte key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean remove(final byte key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char replace(final byte key, final char value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean replace(final byte key, final char oldValue, final char newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsent(final byte key, final IntUnaryOperator mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentNullable(final byte key, final IntFunction<? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfAbsentPartial(final byte key, final Byte2CharFunction mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char computeIfPresent(final byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char compute(final byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public char merge(final byte key, final char value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character getOrDefault(final Object key, final Character defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }
        
        @Deprecated
        @Override
        public boolean remove(final Object key, final Object value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character replace(final Byte key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean replace(final Byte key, final Character oldValue, final Character newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character putIfAbsent(final Byte key, final Character value) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfAbsent(final Byte key, final java.util.function.Function<? super Byte, ? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character computeIfPresent(final Byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character compute(final Byte key, final BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Character merge(final Byte key, final Character value, final BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }
}
