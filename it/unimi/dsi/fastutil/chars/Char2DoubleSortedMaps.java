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

public final class Char2DoubleSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Char2DoubleSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Character, ?>> entryComparator(final CharComparator comparator) {
        return ((x, y) -> comparator.compare((char)x.getKey(), (char)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Char2DoubleMap.Entry> fastIterator(final Char2DoubleSortedMap map) {
        final ObjectSortedSet<Char2DoubleMap.Entry> entries = map.char2DoubleEntrySet();
        return (entries instanceof Char2DoubleSortedMap.FastSortedEntrySet) ? ((Char2DoubleSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Char2DoubleMap.Entry> fastIterable(final Char2DoubleSortedMap map) {
        final ObjectSortedSet<Char2DoubleMap.Entry> entries = map.char2DoubleEntrySet();
        ObjectBidirectionalIterable<Char2DoubleMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Char2DoubleSortedMap.FastSortedEntrySet) {
            final Char2DoubleSortedMap.FastSortedEntrySet set = (Char2DoubleSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Char2DoubleSortedMap singleton(final Character key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Char2DoubleSortedMap singleton(final Character key, final Double value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2DoubleSortedMap singleton(final char key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Char2DoubleSortedMap singleton(final char key, final double value, final CharComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Char2DoubleSortedMap synchronize(final Char2DoubleSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Char2DoubleSortedMap synchronize(final Char2DoubleSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Char2DoubleSortedMap unmodifiable(final Char2DoubleSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Char2DoubleMaps.EmptyMap implements Char2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public CharComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
            return (ObjectSortedSet<Char2DoubleMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Double>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public CharSortedSet keySet() {
            return CharSortedSets.EMPTY_SET;
        }
        
        @Override
        public Char2DoubleSortedMap subMap(final char from, final char to) {
            return Char2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2DoubleSortedMap headMap(final char to) {
            return Char2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2DoubleSortedMap tailMap(final char from) {
            return Char2DoubleSortedMaps.EMPTY_MAP;
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
        public Char2DoubleSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap subMap(final Character ofrom, final Character oto) {
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
    
    public static class Singleton extends Char2DoubleMaps.Singleton implements Char2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharComparator comparator;
        
        protected Singleton(final char key, final double value, final CharComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final char key, final double value) {
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
        public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractChar2DoubleMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractChar2DoubleMap.BasicEntry>)Char2DoubleSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Char2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.singleton(this.key, this.comparator);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2DoubleSortedMap subMap(final char from, final char to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2DoubleSortedMap headMap(final char to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Char2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Char2DoubleSortedMap tailMap(final char from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Char2DoubleSortedMaps.EMPTY_MAP;
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
        public Char2DoubleSortedMap headMap(final Character oto) {
            return this.headMap((char)oto);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap tailMap(final Character ofrom) {
            return this.tailMap((char)ofrom);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap subMap(final Character ofrom, final Character oto) {
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
    
    public static class SynchronizedSortedMap extends Char2DoubleMaps.SynchronizedMap implements Char2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2DoubleSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Char2DoubleSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Char2DoubleSortedMap m) {
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
        public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2DoubleMap.Entry>synchronize(this.sortedMap.char2DoubleEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Char2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2DoubleSortedMap subMap(final char from, final char to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Char2DoubleSortedMap headMap(final char to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Char2DoubleSortedMap tailMap(final char from) {
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
        public Char2DoubleSortedMap subMap(final Character from, final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap headMap(final Character to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap tailMap(final Character from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Char2DoubleMaps.UnmodifiableMap implements Char2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Char2DoubleSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Char2DoubleSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public CharComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Char2DoubleMap.Entry>unmodifiable(this.sortedMap.char2DoubleEntrySet());
            }
            return (ObjectSortedSet<Char2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Character, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Character, Double>>)this.char2DoubleEntrySet();
        }
        
        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = CharSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (CharSortedSet)this.keys;
        }
        
        @Override
        public Char2DoubleSortedMap subMap(final char from, final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Char2DoubleSortedMap headMap(final char to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Char2DoubleSortedMap tailMap(final char from) {
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
        public Char2DoubleSortedMap subMap(final Character from, final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap headMap(final Character to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Char2DoubleSortedMap tailMap(final Character from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
