package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Object2ByteSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Object2ByteSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K> ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> fastIterator(final Object2ByteSortedMap<K> map) {
        final ObjectSortedSet<Object2ByteMap.Entry<K>> entries = map.object2ByteEntrySet();
        return (entries instanceof Object2ByteSortedMap.FastSortedEntrySet) ? ((Object2ByteSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Object2ByteMap.Entry<K>> fastIterable(final Object2ByteSortedMap<K> map) {
        final ObjectSortedSet<Object2ByteMap.Entry<K>> entries = map.object2ByteEntrySet();
        ObjectBidirectionalIterable<Object2ByteMap.Entry<K>> objectBidirectionalIterable;
        if (entries instanceof Object2ByteSortedMap.FastSortedEntrySet) {
            final Object2ByteSortedMap.FastSortedEntrySet set = (Object2ByteSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K> Object2ByteSortedMap<K> emptyMap() {
        return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Object2ByteSortedMap<K> singleton(final K key, final Byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteSortedMap<K> singleton(final K key, final Byte value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2ByteSortedMap<K> singleton(final K key, final byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteSortedMap<K> singleton(final K key, final byte value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2ByteSortedMap<K> synchronize(final Object2ByteSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Object2ByteSortedMap<K> synchronize(final Object2ByteSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Object2ByteSortedMap<K> unmodifiable(final Object2ByteSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Object2ByteMaps.EmptyMap<K> implements Object2ByteSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2ByteMap.Entry<K>> object2ByteEntrySet() {
            return (ObjectSortedSet<Object2ByteMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Byte>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public Object2ByteSortedMap<K> subMap(final K from, final K to) {
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ByteSortedMap<K> headMap(final K to) {
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ByteSortedMap<K> tailMap(final K from) {
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Object2ByteMaps.Singleton<K> implements Object2ByteSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final byte value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final byte value) {
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
        public ObjectSortedSet<Object2ByteMap.Entry<K>> object2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractObject2ByteMap.BasicEntry<K>(this.key, this.value), (java.util.Comparator<? super AbstractObject2ByteMap.BasicEntry<K>>)Object2ByteSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Object2ByteMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2ByteSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ByteSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2ByteSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Object2ByteSortedMap<K>)Object2ByteSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Object2ByteMaps.SynchronizedMap<K> implements Object2ByteSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Object2ByteSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Object2ByteSortedMap<K> m) {
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
        public ObjectSortedSet<Object2ByteMap.Entry<K>> object2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Object2ByteMap.Entry<K>>synchronize(this.sortedMap.object2ByteEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Object2ByteMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2ByteSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Object2ByteSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Object2ByteSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
    
    public static class UnmodifiableSortedMap<K> extends Object2ByteMaps.UnmodifiableMap<K> implements Object2ByteSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Object2ByteSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Object2ByteMap.Entry<K>> object2ByteEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Object2ByteMap.Entry<K>>unmodifiable(this.sortedMap.object2ByteEntrySet());
            }
            return (ObjectSortedSet<Object2ByteMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Byte>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Byte>>)this.object2ByteEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2ByteSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Object2ByteSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Object2ByteSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Object2ByteSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
