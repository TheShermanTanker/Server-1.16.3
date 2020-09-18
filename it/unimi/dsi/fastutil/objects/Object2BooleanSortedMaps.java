package it.unimi.dsi.fastutil.objects;

import java.util.SortedMap;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Map;
import java.util.Comparator;

public final class Object2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Object2BooleanSortedMaps() {
    }
    
    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(final Comparator<? super K> comparator) {
        return ((x, y) -> comparator.compare(x.getKey(), y.getKey()));
    }
    
    public static <K> ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> fastIterator(final Object2BooleanSortedMap<K> map) {
        final ObjectSortedSet<Object2BooleanMap.Entry<K>> entries = map.object2BooleanEntrySet();
        return (entries instanceof Object2BooleanSortedMap.FastSortedEntrySet) ? ((Object2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <K> ObjectBidirectionalIterable<Object2BooleanMap.Entry<K>> fastIterable(final Object2BooleanSortedMap<K> map) {
        final ObjectSortedSet<Object2BooleanMap.Entry<K>> entries = map.object2BooleanEntrySet();
        ObjectBidirectionalIterable<Object2BooleanMap.Entry<K>> objectBidirectionalIterable;
        if (entries instanceof Object2BooleanSortedMap.FastSortedEntrySet) {
            final Object2BooleanSortedMap.FastSortedEntrySet set = (Object2BooleanSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <K> Object2BooleanSortedMap<K> emptyMap() {
        return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
    }
    
    public static <K> Object2BooleanSortedMap<K> singleton(final K key, final Boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2BooleanSortedMap<K> singleton(final K key, final Boolean value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2BooleanSortedMap<K> singleton(final K key, final boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2BooleanSortedMap<K> singleton(final K key, final boolean value, final Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }
    
    public static <K> Object2BooleanSortedMap<K> synchronize(final Object2BooleanSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }
    
    public static <K> Object2BooleanSortedMap<K> synchronize(final Object2BooleanSortedMap<K> m, final Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }
    
    public static <K> Object2BooleanSortedMap<K> unmodifiable(final Object2BooleanSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<K> extends Object2BooleanMaps.EmptyMap<K> implements Object2BooleanSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Object2BooleanMap.Entry<K>> object2BooleanEntrySet() {
            return (ObjectSortedSet<Object2BooleanMap.Entry<K>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            return (ObjectSortedSet<K>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public Object2BooleanSortedMap<K> subMap(final K from, final K to) {
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2BooleanSortedMap<K> headMap(final K to) {
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2BooleanSortedMap<K> tailMap(final K from) {
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            throw new NoSuchElementException();
        }
        
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
    
    public static class Singleton<K> extends Object2BooleanMaps.Singleton<K> implements Object2BooleanSortedMap<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;
        
        protected Singleton(final K key, final boolean value, final Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final K key, final boolean value) {
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
        public ObjectSortedSet<Object2BooleanMap.Entry<K>> object2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractObject2BooleanMap.BasicEntry<K>(this.key, this.value), (java.util.Comparator<? super AbstractObject2BooleanMap.BasicEntry<K>>)Object2BooleanSortedMaps.entryComparator((java.util.Comparator<? super Object>)this.comparator));
            }
            return (ObjectSortedSet<Object2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>singleton(this.key, this.comparator);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2BooleanSortedMap<K> subMap(final K from, final K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2BooleanSortedMap<K> headMap(final K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Object2BooleanSortedMap<K> tailMap(final K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Object2BooleanSortedMap<K>)Object2BooleanSortedMaps.EMPTY_MAP;
        }
        
        public K firstKey() {
            return this.key;
        }
        
        public K lastKey() {
            return this.key;
        }
    }
    
    public static class SynchronizedSortedMap<K> extends Object2BooleanMaps.SynchronizedMap<K> implements Object2BooleanSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2BooleanSortedMap<K> sortedMap;
        
        protected SynchronizedSortedMap(final Object2BooleanSortedMap<K> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Object2BooleanSortedMap<K> m) {
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
        public ObjectSortedSet<Object2BooleanMap.Entry<K>> object2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Object2BooleanMap.Entry<K>>synchronize(this.sortedMap.object2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Object2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2BooleanSortedMap<K> subMap(final K from, final K to) {
            return new SynchronizedSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Object2BooleanSortedMap<K> headMap(final K to) {
            return new SynchronizedSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Object2BooleanSortedMap<K> tailMap(final K from) {
            return new SynchronizedSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
    
    public static class UnmodifiableSortedMap<K> extends Object2BooleanMaps.UnmodifiableMap<K> implements Object2BooleanSortedMap<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2BooleanSortedMap<K> sortedMap;
        
        protected UnmodifiableSortedMap(final Object2BooleanSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Object2BooleanMap.Entry<K>> object2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Object2BooleanMap.Entry<K>>unmodifiable(this.sortedMap.object2BooleanEntrySet());
            }
            return (ObjectSortedSet<Object2BooleanMap.Entry<K>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
        }
        
        @Override
        public ObjectSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ObjectSortedSets.<K>unmodifiable(this.sortedMap.keySet());
            }
            return (ObjectSortedSet<K>)(ObjectSortedSet)this.keys;
        }
        
        @Override
        public Object2BooleanSortedMap<K> subMap(final K from, final K to) {
            return new UnmodifiableSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Object2BooleanSortedMap<K> headMap(final K to) {
            return new UnmodifiableSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Object2BooleanSortedMap<K> tailMap(final K from) {
            return new UnmodifiableSortedMap((Object2BooleanSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        public K firstKey() {
            return (K)this.sortedMap.firstKey();
        }
        
        public K lastKey() {
            return (K)this.sortedMap.lastKey();
        }
    }
}
