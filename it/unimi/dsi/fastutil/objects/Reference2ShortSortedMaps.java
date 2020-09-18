package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Reference2ShortSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2ShortSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K> ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> fastIterator(final Reference2ShortSortedMap<K> map) {
        final ObjectSortedSet<Reference2ShortMap.Entry<K>> entries = map.reference2ShortEntrySet();
        return (entries instanceof Reference2ShortSortedMap.FastSortedEntrySet) ? ((Reference2ShortSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Reference2ShortMap.Entry<K>> fastIterable(final Reference2ShortSortedMap<K> map) {
        final ObjectSortedSet<Reference2ShortMap.Entry<K>> entries = map.reference2ShortEntrySet();
        ObjectBidirectionalIterable<Reference2ShortMap.Entry<K>> objectBidirectionalIterable;
        if (entries instanceof Reference2ShortSortedMap.FastSortedEntrySet) {
            final Reference2ShortSortedMap.FastSortedEntrySet set = (Reference2ShortSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K> Reference2ShortSortedMap<K> emptyMap() {
        return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2ShortSortedMap<K> singleton(final K key, final Short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2ShortSortedMap<K> singleton(final K key, final Short value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2ShortSortedMap<K> singleton(final K key, final short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2ShortSortedMap<K> singleton(final K key, final short value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2ShortSortedMap<K> synchronize(final Reference2ShortSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Reference2ShortSortedMap<K> synchronize(final Reference2ShortSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Reference2ShortSortedMap<K> unmodifiable(final Reference2ShortSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Reference2ShortMaps.EmptyMap<K> implements Reference2ShortSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2ShortMap.Entry<K>> reference2ShortEntrySet() {
            return (ObjectSortedSet<Reference2ShortMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Short>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2ShortSortedMap<K> subMap(final K from, final K to) {
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ShortSortedMap<K> headMap(final K to) {
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ShortSortedMap<K> tailMap(final K from) {
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Reference2ShortMaps.Singleton<K> implements Reference2ShortSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final short value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final short value) {
            this(key, value, null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Reference2ShortMap.Entry<K>> reference2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractReference2ShortMap.BasicEntry<K>(this.key, this.value), (java.util.Comparator<? super AbstractReference2ShortMap.BasicEntry<K>>)Reference2ShortSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Reference2ShortMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Short>>)this.reference2ShortEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ShortSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ShortSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2ShortSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Reference2ShortSortedMap<K>)Reference2ShortSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Reference2ShortMaps.SynchronizedMap<K> implements Reference2ShortSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ShortSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2ShortSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2ShortSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Reference2ShortMap.Entry<K>> reference2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2ShortMap.Entry<K>>synchronize(this.sortedMap.reference2ShortEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Reference2ShortMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Short>>)this.reference2ShortEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ShortSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2ShortSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2ShortSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        public K firstKey() {
            synchronized (this.sync) {
                return (K)this.sortedMap.firstKey();
            }
        }
        
        public K lastKey() {
            synchronized (this.sync) {
                return (K)this.sortedMap.lastKey();
            }
        }
    }
    
    public static class UnmodifiableSortedMap<K> extends Reference2ShortMaps.UnmodifiableMap<K> implements Reference2ShortSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2ShortSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2ShortSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2ShortMap.Entry<K>> reference2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2ShortMap.Entry<K>>unmodifiable(this.sortedMap.reference2ShortEntrySet());
            }
            return (ObjectSortedSet<Reference2ShortMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Short>>)this.reference2ShortEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2ShortSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2ShortSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2ShortSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2ShortSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
