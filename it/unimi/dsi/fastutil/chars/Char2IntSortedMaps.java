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

public final class Char2IntSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2IntSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator(final Char2IntSortedMap map) {
        final ObjectSortedSet<Char2IntMap.Entry> entries = map.char2IntEntrySet();
        return (entries instanceof Char2IntSortedMap.FastSortedEntrySet) ? ((Char2IntSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Char2IntMap.Entry> fastIterable(final Char2IntSortedMap map) {
        final ObjectSortedSet<Char2IntMap.Entry> entries = map.char2IntEntrySet();
        ObjectBidirectionalIterable<Char2IntMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Char2IntSortedMap.FastSortedEntrySet) {
            final Char2IntSortedMap.FastSortedEntrySet set = (Char2IntSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Char2IntSortedMap singleton(final Character key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Char2IntSortedMap singleton(final Character key, final Integer value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2IntSortedMap singleton(final char key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Char2IntSortedMap singleton(final char key, final int value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2IntSortedMap synchronize(final Char2IntSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Char2IntSortedMap synchronize(final Char2IntSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Char2IntSortedMap unmodifiable(final Char2IntSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Char2IntMaps.EmptyMap implements Char2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet() {
            return (ObjectSortedSet<Char2IntMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Integer>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet keySet() {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Char2IntSortedMap subMap(final char from, final char to) {
            return Char2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2IntSortedMap headMap(final char to) {
            return Char2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2IntSortedMap tailMap(final char from) {
            return Char2IntSortedMaps.EMPTY_MAP;
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
        public Char2IntSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap subMap(final Character ofrom, final Character oto) {
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
    
    public static class Singleton extends Char2IntMaps.Singleton implements Char2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharComparator comparator;
        
        protected Singleton(final char key, final int value, final CharComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final char key, final int value) {
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
        public ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2IntMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractChar2IntMap.BasicEntry>)Char2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Integer>>)this.char2IntEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2IntSortedMap subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2IntSortedMap headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2IntSortedMap tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Char2IntSortedMaps.EMPTY_MAP;
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
        public Char2IntSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap subMap(final Character ofrom, final Character oto) {
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
    
    public static class SynchronizedSortedMap extends Char2IntMaps.SynchronizedMap implements Char2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2IntSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Char2IntSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2IntSortedMap m) {
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
        public ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2IntMap.Entry>synchronize(this.sortedMap.char2IntEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Integer>>)this.char2IntEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2IntSortedMap subMap(final char from, final char to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2IntSortedMap headMap(final char to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2IntSortedMap tailMap(final char from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
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
        public Char2IntSortedMap subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap headMap(final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap tailMap(final Character from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Char2IntMaps.UnmodifiableMap implements Char2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2IntSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Char2IntSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2IntMap.Entry>unmodifiable(this.sortedMap.char2IntEntrySet());
            }
            return (ObjectSortedSet<Char2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Integer>>)this.char2IntEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2IntSortedMap subMap(final char from, final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2IntSortedMap headMap(final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2IntSortedMap tailMap(final char from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
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
        public Char2IntSortedMap subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap headMap(final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2IntSortedMap tailMap(final Character from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
