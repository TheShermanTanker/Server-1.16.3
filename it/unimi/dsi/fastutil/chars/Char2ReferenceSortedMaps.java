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

public final class Char2ReferenceSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2ReferenceSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> fastIterator(final Char2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Char2ReferenceMap.Entry<V>> entries = map.char2ReferenceEntrySet();
        return (entries instanceof Char2ReferenceSortedMap.FastSortedEntrySet) ? ((Char2ReferenceSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Char2ReferenceMap.Entry<V>> fastIterable(final Char2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Char2ReferenceMap.Entry<V>> entries = map.char2ReferenceEntrySet();
        ObjectBidirectionalIterable<Char2ReferenceMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Char2ReferenceSortedMap.FastSortedEntrySet) {
            final Char2ReferenceSortedMap.FastSortedEntrySet set = (Char2ReferenceSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Char2ReferenceSortedMap<V> emptyMap() {
        return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Char2ReferenceSortedMap<V> singleton(final Character key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceSortedMap<V> singleton(final Character key, final V value, final CharComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Char2ReferenceSortedMap<V> singleton(final char key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Char2ReferenceSortedMap<V> singleton(final char key, final V value, final CharComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Char2ReferenceSortedMap<V> synchronize(final Char2ReferenceSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Char2ReferenceSortedMap<V> synchronize(final Char2ReferenceSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Char2ReferenceSortedMap<V> unmodifiable(final Char2ReferenceSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Char2ReferenceMaps.EmptyMap<V> implements Char2ReferenceSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
            return (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
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
        public Char2ReferenceSortedMap<V> subMap(final char from, final char to) {
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> headMap(final char to) {
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final char from) {
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
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
        public Char2ReferenceSortedMap<V> headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> subMap(final Character ofrom, final Character oto) {
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
    
    public static class Singleton<V> extends Char2ReferenceMaps.Singleton<V> implements Char2ReferenceSortedMap<V>, Serializable, Cloneable {
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
        public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2ReferenceMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractChar2ReferenceMap.BasicEntry<V>>)Char2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Char2ReferenceSortedMap<V>)Char2ReferenceSortedMaps.EMPTY_MAP;
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
        public Char2ReferenceSortedMap<V> headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> subMap(final Character ofrom, final Character oto) {
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
    
    public static class SynchronizedSortedMap<V> extends Char2ReferenceMaps.SynchronizedMap<V> implements Char2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Char2ReferenceSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2ReferenceSortedMap<V> m) {
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
        public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2ReferenceMap.Entry<V>>synchronize(this.sortedMap.char2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> subMap(final char from, final char to) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2ReferenceSortedMap<V> headMap(final char to) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final char from) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
        public Char2ReferenceSortedMap<V> subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> headMap(final Character to) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final Character from) {
            return new SynchronizedSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Char2ReferenceMaps.UnmodifiableMap<V> implements Char2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2ReferenceSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Char2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2ReferenceMap.Entry<V>> char2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2ReferenceMap.Entry<V>>unmodifiable(this.sortedMap.char2ReferenceEntrySet());
            }
            return (ObjectSortedSet<Char2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, V>>)this.char2ReferenceEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2ReferenceSortedMap<V> subMap(final char from, final char to) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2ReferenceSortedMap<V> headMap(final char to) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final char from) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
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
        public Char2ReferenceSortedMap<V> subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> headMap(final Character to) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2ReferenceSortedMap<V> tailMap(final Character from) {
            return new UnmodifiableSortedMap((Char2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
