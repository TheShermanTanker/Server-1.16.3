package it.unimi.dsi.fastutil.chars;

import java.util.SortedMap;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Objects;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Map;
import java.util.Comparator;

public final class Char2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> fastIterator(final Char2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Char2ObjectMap.Entry<V>> entries = map.char2ObjectEntrySet();
        return (entries instanceof Char2ObjectSortedMap.FastSortedEntrySet) ? ((Char2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Char2ObjectMap.Entry<V>> fastIterable(final Char2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Char2ObjectMap.Entry<V>> entries = map.char2ObjectEntrySet();
        ObjectBidirectionalIterable<Char2ObjectMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Char2ObjectSortedMap.FastSortedEntrySet) {
            final Char2ObjectSortedMap.FastSortedEntrySet set = (Char2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Char2ObjectSortedMap<V> emptyMap() {
        return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Char2ObjectSortedMap<V> singleton(final Character key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ObjectSortedMap<V> singleton(final Character key, final V value, final CharComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Char2ObjectSortedMap<V> singleton(final char key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ObjectSortedMap<V> singleton(final char key, final V value, final CharComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Char2ObjectSortedMap<V> synchronize(final Char2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Char2ObjectSortedMap<V> synchronize(final Char2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Char2ObjectSortedMap<V> unmodifiable(final Char2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Char2ObjectMaps.EmptyMap<V> implements Char2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
            return (ObjectSortedSet<Char2ObjectMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet keySet() {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Char2ObjectSortedMap<V> subMap(final char from, final char to) {
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ObjectSortedMap<V> headMap(final char to) {
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ObjectSortedMap<V> tailMap(final char from) {
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public char lastCharKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class Singleton<V> extends Char2ObjectMaps.Singleton<V> implements Char2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharComparator comparator;
        
        protected Singleton(final char key, final V value, final CharComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final char key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final char k1, final char k2) {
            return (this.comparator == null) ? Character.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public CharComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2ObjectMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractChar2ObjectMap.BasicEntry<V>>)Char2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ObjectEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ObjectSortedMap<V> subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ObjectSortedMap<V> headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ObjectSortedMap<V> tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Char2ObjectSortedMap<V>)Char2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public char firstCharKey() {
            return this.key;
        }
        
        @Override
        public char lastCharKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> subMap(final Character ofrom, final Character oto) {
            return this.subMap((char)ofrom, (char)oto);
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.firstCharKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.lastCharKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Char2ObjectMaps.SynchronizedMap<V> implements Char2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Char2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2ObjectMap.Entry<V>>synchronize(this.sortedMap.char2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ObjectEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ObjectSortedMap<V> subMap(final char from, final char to) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2ObjectSortedMap<V> headMap(final char to) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2ObjectSortedMap<V> tailMap(final char from) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public char firstCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstCharKey();
            }
        }
        
        @Override
        public char lastCharKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastCharKey();
            }
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> headMap(final Character to) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> tailMap(final Character from) {
            return new SynchronizedSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Char2ObjectMaps.UnmodifiableMap<V> implements Char2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Char2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2ObjectMap.Entry<V>> char2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2ObjectMap.Entry<V>>unmodifiable(this.sortedMap.char2ObjectEntrySet());
            }
            return (ObjectSortedSet<Char2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ObjectEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ObjectSortedMap<V> subMap(final char from, final char to) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2ObjectSortedMap<V> headMap(final char to) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2ObjectSortedMap<V> tailMap(final char from) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public char firstCharKey() {
            return this.sortedMap.firstCharKey();
        }
        
        @Override
        public char lastCharKey() {
            return this.sortedMap.lastCharKey();
        }
        
        @Deprecated
        @Override
        public Character firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Character lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> headMap(final Character to) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2ObjectSortedMap<V> tailMap(final Character from) {
            return new UnmodifiableSortedMap((Char2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
