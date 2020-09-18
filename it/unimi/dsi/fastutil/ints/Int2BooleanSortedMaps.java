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

public final class Int2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Int2BooleanSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Integer, ?>> entryComparator(final IntComparator comparator) {
        return ((x, y) -> comparator.compare((int)x.getKey(), (int)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Int2BooleanMap.Entry> fastIterator(final Int2BooleanSortedMap map) {
        final ObjectSortedSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
        return (entries instanceof Int2BooleanSortedMap.FastSortedEntrySet) ? ((Int2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Int2BooleanMap.Entry> fastIterable(final Int2BooleanSortedMap map) {
        final ObjectSortedSet<Int2BooleanMap.Entry> entries = map.int2BooleanEntrySet();
        ObjectBidirectionalIterable<Int2BooleanMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Int2BooleanSortedMap.FastSortedEntrySet) {
            final Int2BooleanSortedMap.FastSortedEntrySet set = (Int2BooleanSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Int2BooleanSortedMap singleton(final Integer key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanSortedMap singleton(final Integer key, final Boolean value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2BooleanSortedMap singleton(final int key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanSortedMap singleton(final int key, final boolean value, final IntComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Int2BooleanSortedMap synchronize(final Int2BooleanSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Int2BooleanSortedMap synchronize(final Int2BooleanSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Int2BooleanSortedMap unmodifiable(final Int2BooleanSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Int2BooleanMaps.EmptyMap implements Int2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public IntComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
            return (ObjectSortedSet<Int2BooleanMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Boolean>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public IntSortedSet keySet() {
            return IntSortedSets.EMPTY_SET;
        }
        
        @Override
        public Int2BooleanSortedMap subMap(final int from, final int to) {
            return Int2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2BooleanSortedMap headMap(final int to) {
            return Int2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2BooleanSortedMap tailMap(final int from) {
            return Int2BooleanSortedMaps.EMPTY_MAP;
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
        public Int2BooleanSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap subMap(final Integer ofrom, final Integer oto) {
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
    
    public static class Singleton extends Int2BooleanMaps.Singleton implements Int2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntComparator comparator;
        
        protected Singleton(final int key, final boolean value, final IntComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final int key, final boolean value) {
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
        public ObjectSortedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractInt2BooleanMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractInt2BooleanMap.BasicEntry>)Int2BooleanSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Int2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.singleton(this.key, this.comparator);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2BooleanSortedMap subMap(final int from, final int to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2BooleanSortedMap headMap(final int to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Int2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Int2BooleanSortedMap tailMap(final int from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Int2BooleanSortedMaps.EMPTY_MAP;
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
        public Int2BooleanSortedMap headMap(final Integer oto) {
            return this.headMap((int)oto);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap tailMap(final Integer ofrom) {
            return this.tailMap((int)ofrom);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap subMap(final Integer ofrom, final Integer oto) {
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
    
    public static class SynchronizedSortedMap extends Int2BooleanMaps.SynchronizedMap implements Int2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Int2BooleanSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Int2BooleanSortedMap m) {
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
        public ObjectSortedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2BooleanMap.Entry>synchronize(this.sortedMap.int2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Int2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2BooleanSortedMap subMap(final int from, final int to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Int2BooleanSortedMap headMap(final int to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Int2BooleanSortedMap tailMap(final int from) {
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
        public Int2BooleanSortedMap subMap(final Integer from, final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap headMap(final Integer to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap tailMap(final Integer from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Int2BooleanMaps.UnmodifiableMap implements Int2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Int2BooleanSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public IntComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Int2BooleanMap.Entry> int2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Int2BooleanMap.Entry>unmodifiable(this.sortedMap.int2BooleanEntrySet());
            }
            return (ObjectSortedSet<Int2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Integer, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Integer, Boolean>>)this.int2BooleanEntrySet();
        }
        
        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = IntSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (IntSortedSet)this.keys;
        }
        
        @Override
        public Int2BooleanSortedMap subMap(final int from, final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Int2BooleanSortedMap headMap(final int to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Int2BooleanSortedMap tailMap(final int from) {
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
        public Int2BooleanSortedMap subMap(final Integer from, final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap headMap(final Integer to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Int2BooleanSortedMap tailMap(final Integer from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
