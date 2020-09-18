package it.unimi.dsi.fastutil.ints;

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

public final class Int2LongSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2LongSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return ((x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Int2LongMap.Entry> fastIterator(final Int2LongSortedMap map) {
        final ObjectSortedSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        return (entries instanceof Int2LongSortedMap.FastSortedEntrySet) ? ((Int2LongSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Int2LongMap.Entry> fastIterable(final Int2LongSortedMap map) {
        final ObjectSortedSet<Int2LongMap.Entry> entries = map.int2LongEntrySet();
        ObjectBidirectionalIterable<Int2LongMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Int2LongSortedMap.FastSortedEntrySet) {
            final Int2LongSortedMap.FastSortedEntrySet set = (Int2LongSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Int2LongSortedMap singleton(final Integer key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Int2LongSortedMap singleton(final Integer key, final Long value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2LongSortedMap singleton(final int key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Int2LongSortedMap singleton(final int key, final long value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2LongSortedMap synchronize(final Int2LongSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Int2LongSortedMap synchronize(final Int2LongSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Int2LongSortedMap unmodifiable(final Int2LongSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Int2LongMaps.EmptyMap implements Int2LongSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            return (ObjectSortedSet<Int2LongMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Long>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2LongSortedMap subMap(final int from, final int to) {
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2LongSortedMap headMap(final int to) {
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2LongSortedMap tailMap(final int from) {
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public int lastIntKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class Singleton extends Int2LongMaps.Singleton implements Int2LongSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final long value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final long value) {
            this(key, value, null);
        }
        
        final int compare(final int k1, final int k2) {
            return (this.comparator == null) ? Integer.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public IntComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2LongMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractInt2LongMap.BasicEntry>)Int2LongSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2LongSortedMap subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2LongSortedMap headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2LongSortedMap tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Int2LongSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public int firstIntKey() {
            return this.key;
        }
        
        @Override
        public int lastIntKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap subMap(final Integer ofrom, final Integer oto) {
            return this.subMap((int)ofrom, (int)oto);
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.firstIntKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.lastIntKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Int2LongMaps.SynchronizedMap implements Int2LongSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Int2LongSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Int2LongSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2LongMap.Entry>synchronize(this.sortedMap.int2LongEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Int2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2LongSortedMap subMap(final int from, final int to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2LongSortedMap headMap(final int to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2LongSortedMap tailMap(final int from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public int firstIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstIntKey();
            }
        }
        
        @Override
        public int lastIntKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastIntKey();
            }
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap subMap(final Integer from, final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap headMap(final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap tailMap(final Integer from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Int2LongMaps.UnmodifiableMap implements Int2LongSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2LongSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Int2LongSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Int2LongMap.Entry> int2LongEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2LongMap.Entry>unmodifiable(this.sortedMap.int2LongEntrySet());
            }
            return (ObjectSortedSet<Int2LongMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2LongSortedMap subMap(final int from, final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2LongSortedMap headMap(final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2LongSortedMap tailMap(final int from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public int firstIntKey() {
            return this.sortedMap.firstIntKey();
        }
        
        @Override
        public int lastIntKey() {
            return this.sortedMap.lastIntKey();
        }
        
        @Deprecated
        @Override
        public Integer firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Integer lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap subMap(final Integer from, final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap headMap(final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Int2LongSortedMap tailMap(final Integer from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
