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

public final class Long2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Long2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(final LongComparator comparator) {
        return ((x, y) -> comparator.compare((long)x.getKey(), (long)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> fastIterator(final Long2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        return (entries instanceof Long2ObjectSortedMap.FastSortedEntrySet) ? ((Long2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Long2ObjectMap.Entry<V>> fastIterable(final Long2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
        ObjectBidirectionalIterable<Long2ObjectMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Long2ObjectSortedMap.FastSortedEntrySet) {
            final Long2ObjectSortedMap.FastSortedEntrySet set = (Long2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Long2ObjectSortedMap<V> emptyMap() {
        return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Long2ObjectSortedMap<V> singleton(final Long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ObjectSortedMap<V> singleton(final Long key, final V value, final LongComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Long2ObjectSortedMap<V> singleton(final long key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Long2ObjectSortedMap<V> singleton(final long key, final V value, final LongComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Long2ObjectSortedMap<V> synchronize(final Long2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Long2ObjectSortedMap<V> synchronize(final Long2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Long2ObjectSortedMap<V> unmodifiable(final Long2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Long2ObjectMaps.EmptyMap<V> implements Long2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public LongComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            return (ObjectSortedSet<Long2ObjectMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public LongSortedSet keySet() {
            return LongSortedSets.EMPTY_SET;
        }
        
        @Override
        public Long2ObjectSortedMap<V> subMap(final long from, final long to) {
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ObjectSortedMap<V> headMap(final long to) {
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ObjectSortedMap<V> tailMap(final long from) {
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
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
        public Long2ObjectSortedMap<V> headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> subMap(final Long ofrom, final Long oto) {
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
    
    public static class Singleton<V> extends Long2ObjectMaps.Singleton<V> implements Long2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;
        
        protected Singleton(final long key, final V value, final LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final long key, final V value) {
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
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractLong2ObjectMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractLong2ObjectMap.BasicEntry<V>>)Long2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Long2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ObjectSortedMap<V> subMap(final long from, final long to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ObjectSortedMap<V> headMap(final long to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Long2ObjectSortedMap<V> tailMap(final long from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Long2ObjectSortedMap<V>)Long2ObjectSortedMaps.EMPTY_MAP;
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
        public Long2ObjectSortedMap<V> headMap(final Long oto) {
            return this.headMap((long)oto);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> tailMap(final Long ofrom) {
            return this.tailMap((long)ofrom);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> subMap(final Long ofrom, final Long oto) {
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
    
    public static class SynchronizedSortedMap<V> extends Long2ObjectMaps.SynchronizedMap<V> implements Long2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Long2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Long2ObjectSortedMap<V> m) {
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
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Long2ObjectMap.Entry<V>>synchronize(this.sortedMap.long2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Long2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ObjectSortedMap<V> subMap(final long from, final long to) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Long2ObjectSortedMap<V> headMap(final long to) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Long2ObjectSortedMap<V> tailMap(final long from) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
        public Long2ObjectSortedMap<V> subMap(final Long from, final Long to) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> headMap(final Long to) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> tailMap(final Long from) {
            return new SynchronizedSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Long2ObjectMaps.UnmodifiableMap<V> implements Long2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Long2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public LongComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Long2ObjectMap.Entry<V>>unmodifiable(this.sortedMap.long2ObjectEntrySet());
            }
            return (ObjectSortedSet<Long2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ObjectEntrySet();
        }
        
        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet)this.keys;
        }
        
        @Override
        public Long2ObjectSortedMap<V> subMap(final long from, final long to) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Long2ObjectSortedMap<V> headMap(final long to) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Long2ObjectSortedMap<V> tailMap(final long from) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
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
        public Long2ObjectSortedMap<V> subMap(final Long from, final Long to) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> headMap(final Long to) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Long2ObjectSortedMap<V> tailMap(final Long from) {
            return new UnmodifiableSortedMap((Long2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
