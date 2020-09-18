package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Reference2CharSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Reference2CharSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K> ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> fastIterator(final Reference2CharSortedMap<K> map) {
        final ObjectSortedSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        return (entries instanceof Reference2CharSortedMap.FastSortedEntrySet) ? ((Reference2CharSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Reference2CharMap.Entry<K>> fastIterable(final Reference2CharSortedMap<K> map) {
        final ObjectSortedSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        ObjectBidirectionalIterable<Reference2CharMap.Entry<K>> objectBidirectionalIterable;
        if (entries instanceof Reference2CharSortedMap.FastSortedEntrySet) {
            final Reference2CharSortedMap.FastSortedEntrySet set = (Reference2CharSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K> Reference2CharSortedMap<K> emptyMap() {
        return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Reference2CharSortedMap<K> singleton(final K key, final Character value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharSortedMap<K> singleton(final K key, final Character value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2CharSortedMap<K> singleton(final K key, final char value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2CharSortedMap<K> singleton(final K key, final char value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Reference2CharSortedMap<K> synchronize(final Reference2CharSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Reference2CharSortedMap<K> synchronize(final Reference2CharSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Reference2CharSortedMap<K> unmodifiable(final Reference2CharSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Reference2CharMaps.EmptyMap<K> implements Reference2CharSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            return (ObjectSortedSet<Reference2CharMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Character>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public Reference2CharSortedMap<K> subMap(final K from, final K to) {
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2CharSortedMap<K> headMap(final K to) {
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2CharSortedMap<K> tailMap(final K from) {
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Reference2CharMaps.Singleton<K> implements Reference2CharSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final char value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final char value) {
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
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractReference2CharMap.BasicEntry<K>(this.key, this.value), (java.util.Comparator<? super AbstractReference2CharMap.BasicEntry<K>>)Reference2CharSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Reference2CharMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2CharSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2CharSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Reference2CharSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Reference2CharSortedMap<K>)Reference2CharSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Reference2CharMaps.SynchronizedMap<K> implements Reference2CharSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Reference2CharSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Reference2CharSortedMap<K> m) {
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
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2CharMap.Entry<K>>synchronize(this.sortedMap.reference2CharEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Reference2CharMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2CharSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Reference2CharSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Reference2CharSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
    
    public static class UnmodifiableSortedMap<K> extends Reference2CharMaps.UnmodifiableMap<K> implements Reference2CharSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Reference2CharSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Reference2CharMap.Entry<K>>unmodifiable(this.sortedMap.reference2CharEntrySet());
            }
            return (ObjectSortedSet<Reference2CharMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Character>>)this.reference2CharEntrySet();
        }
        
        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet<K>)(ReferenceSortedSet)this.keys;
        }
        
        @Override
        public Reference2CharSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Reference2CharSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Reference2CharSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Reference2CharSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
