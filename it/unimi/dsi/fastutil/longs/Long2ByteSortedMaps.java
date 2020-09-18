package it.unimi.dsi.fastutil.longs;

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

public final class Long2ByteSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Long2ByteSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
        return ((x, y) -> comparator.compare((long)x.getKey(), (long)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Long2ByteMap.Entry> fastIterator(final Long2ByteSortedMap map) {
        final ObjectSortedSet<Long2ByteMap.Entry> entries = map.long2ByteEntrySet();
        return (entries instanceof Long2ByteSortedMap.FastSortedEntrySet) ? ((Long2ByteSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Long2ByteMap.Entry> fastIterable(final Long2ByteSortedMap map) {
        final ObjectSortedSet<Long2ByteMap.Entry> entries = map.long2ByteEntrySet();
        ObjectBidirectionalIterable<Long2ByteMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Long2ByteSortedMap.FastSortedEntrySet) {
            final Long2ByteSortedMap.FastSortedEntrySet set = (Long2ByteSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Long2ByteSortedMap singleton(final Long key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Long2ByteSortedMap singleton(final Long key, final Byte value, final LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Long2ByteSortedMap singleton(final long key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Long2ByteSortedMap singleton(final long key, final byte value, final LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Long2ByteSortedMap synchronize(final Long2ByteSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Long2ByteSortedMap synchronize(final Long2ByteSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Long2ByteSortedMap unmodifiable(final Long2ByteSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Long2ByteMaps.EmptyMap implements Long2ByteSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2ByteMap.Entry> long2ByteEntrySet() {
            return (ObjectSortedSet<Long2ByteMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Byte>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet keySet() {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public Long2ByteSortedMap subMap(final long from, final long to) {
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ByteSortedMap headMap(final long to) {
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ByteSortedMap tailMap(final long from) {
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public long firstLongKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public long lastLongKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap subMap(final Long ofrom, final Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            return this.firstLongKey();
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            return this.lastLongKey();
        }
    }
    
    public static class Singleton extends Long2ByteMaps.Singleton implements Long2ByteSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;
        
        protected Singleton(final long key, final byte value, final LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final long key, final byte value) {
            this(key, value, null);
        }
        
        final int compare(final long k1, final long k2) {
            return (this.comparator == null) ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public LongComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Long2ByteMap.Entry> long2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractLong2ByteMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractLong2ByteMap.BasicEntry>)Long2ByteSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Long2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ByteSortedMap subMap(final long from, final long to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ByteSortedMap headMap(final long to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ByteSortedMap tailMap(final long from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Long2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public long firstLongKey() {
            return this.key;
        }
        
        @Override
        public long lastLongKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap subMap(final Long ofrom, final Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            return this.firstLongKey();
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            return this.lastLongKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Long2ByteMaps.SynchronizedMap implements Long2ByteSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ByteSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Long2ByteSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Long2ByteSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Long2ByteMap.Entry> long2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Long2ByteMap.Entry>synchronize(this.sortedMap.long2ByteEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Long2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ByteSortedMap subMap(final long from, final long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2ByteSortedMap headMap(final long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2ByteSortedMap tailMap(final long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public long firstLongKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstLongKey();
            }
        }
        
        @Override
        public long lastLongKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastLongKey();
            }
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap subMap(final Long from, final Long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap headMap(final Long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap tailMap(final Long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Long2ByteMaps.UnmodifiableMap implements Long2ByteSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ByteSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Long2ByteSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Long2ByteMap.Entry> long2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Long2ByteMap.Entry>unmodifiable(this.sortedMap.long2ByteEntrySet());
            }
            return (ObjectSortedSet<Long2ByteMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ByteSortedMap subMap(final long from, final long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2ByteSortedMap headMap(final long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2ByteSortedMap tailMap(final long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public long firstLongKey() {
            return this.sortedMap.firstLongKey();
        }
        
        @Override
        public long lastLongKey() {
            return this.sortedMap.lastLongKey();
        }
        
        @Deprecated
        @Override
        public Long firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Long lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap subMap(final Long from, final Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap headMap(final Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Long2ByteSortedMap tailMap(final Long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
