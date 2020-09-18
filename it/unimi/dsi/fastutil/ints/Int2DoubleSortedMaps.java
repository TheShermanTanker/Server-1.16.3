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

public final class Int2DoubleSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2DoubleSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return ((x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Int2DoubleMap.Entry> fastIterator(final Int2DoubleSortedMap map) {
        final ObjectSortedSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
        return (entries instanceof Int2DoubleSortedMap.FastSortedEntrySet) ? ((Int2DoubleSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Int2DoubleMap.Entry> fastIterable(final Int2DoubleSortedMap map) {
        final ObjectSortedSet<Int2DoubleMap.Entry> entries = map.int2DoubleEntrySet();
        ObjectBidirectionalIterable<Int2DoubleMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Int2DoubleSortedMap.FastSortedEntrySet) {
            final Int2DoubleSortedMap.FastSortedEntrySet set = (Int2DoubleSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Int2DoubleSortedMap singleton(final Integer key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Int2DoubleSortedMap singleton(final Integer key, final Double value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2DoubleSortedMap singleton(final int key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Int2DoubleSortedMap singleton(final int key, final double value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2DoubleSortedMap synchronize(final Int2DoubleSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Int2DoubleSortedMap synchronize(final Int2DoubleSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Int2DoubleSortedMap unmodifiable(final Int2DoubleSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Int2DoubleMaps.EmptyMap implements Int2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
            return (ObjectSortedSet<Int2DoubleMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Double>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2DoubleSortedMap subMap(final int from, final int to) {
            return Int2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2DoubleSortedMap headMap(final int to) {
            return Int2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2DoubleSortedMap tailMap(final int from) {
            return Int2DoubleSortedMaps.EMPTY_MAP;
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
        public Int2DoubleSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap subMap(final Integer ofrom, final Integer oto) {
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
    
    public static class Singleton extends Int2DoubleMaps.Singleton implements Int2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final double value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final double value) {
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
        public ObjectSortedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2DoubleMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractInt2DoubleMap.BasicEntry>)Int2DoubleSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Double>>)this.int2DoubleEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2DoubleSortedMap subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2DoubleSortedMap headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2DoubleSortedMap tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Int2DoubleSortedMaps.EMPTY_MAP;
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
        public Int2DoubleSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap subMap(final Integer ofrom, final Integer oto) {
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
    
    public static class SynchronizedSortedMap extends Int2DoubleMaps.SynchronizedMap implements Int2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2DoubleSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Int2DoubleSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Int2DoubleSortedMap m) {
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
        public ObjectSortedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2DoubleMap.Entry>synchronize(this.sortedMap.int2DoubleEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Int2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Double>>)this.int2DoubleEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2DoubleSortedMap subMap(final int from, final int to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2DoubleSortedMap headMap(final int to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2DoubleSortedMap tailMap(final int from) {
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
        public Int2DoubleSortedMap subMap(final Integer from, final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap headMap(final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap tailMap(final Integer from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Int2DoubleMaps.UnmodifiableMap implements Int2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2DoubleSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Int2DoubleSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Int2DoubleMap.Entry> int2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2DoubleMap.Entry>unmodifiable(this.sortedMap.int2DoubleEntrySet());
            }
            return (ObjectSortedSet<Int2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Double>>)this.int2DoubleEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2DoubleSortedMap subMap(final int from, final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2DoubleSortedMap headMap(final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2DoubleSortedMap tailMap(final int from) {
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
        public Int2DoubleSortedMap subMap(final Integer from, final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap headMap(final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Int2DoubleSortedMap tailMap(final Integer from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
