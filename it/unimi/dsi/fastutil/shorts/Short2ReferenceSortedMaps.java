package it.unimi.dsi.fastutil.shorts;

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

public final class Short2ReferenceSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Short2ReferenceSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
        return ((x, y) -> comparator.compare((short)x.getKey(), (short)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> fastIterator(final Short2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Short2ReferenceMap.Entry<V>> entries = map.short2ReferenceEntrySet();
        return (entries instanceof Short2ReferenceSortedMap.FastSortedEntrySet) ? ((Short2ReferenceSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Short2ReferenceMap.Entry<V>> fastIterable(final Short2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Short2ReferenceMap.Entry<V>> entries = map.short2ReferenceEntrySet();
        ObjectBidirectionalIterable<Short2ReferenceMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Short2ReferenceSortedMap.FastSortedEntrySet) {
            final Short2ReferenceSortedMap.FastSortedEntrySet set = (Short2ReferenceSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Short2ReferenceSortedMap<V> emptyMap() {
        return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Short2ReferenceSortedMap<V> singleton(final Short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ReferenceSortedMap<V> singleton(final Short key, final V value, final ShortComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Short2ReferenceSortedMap<V> singleton(final short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ReferenceSortedMap<V> singleton(final short key, final V value, final ShortComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Short2ReferenceSortedMap<V> synchronize(final Short2ReferenceSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Short2ReferenceSortedMap<V> synchronize(final Short2ReferenceSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Short2ReferenceSortedMap<V> unmodifiable(final Short2ReferenceSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Short2ReferenceMaps.EmptyMap<V> implements Short2ReferenceSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
            return (ObjectSortedSet<Short2ReferenceMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet keySet() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> subMap(final short from, final short to) {
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> headMap(final short to) {
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final short from) {
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public short lastShortKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class Singleton<V> extends Short2ReferenceMaps.Singleton<V> implements Short2ReferenceSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortComparator comparator;
        
        protected Singleton(final short key, final V value, final ShortComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final short key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final short k1, final short k2) {
            return (this.comparator == null) ? Short.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ShortComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractShort2ReferenceMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractShort2ReferenceMap.BasicEntry<V>>)Short2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Short2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ReferenceEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.singleton(this.key, this.comparator);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> subMap(final short from, final short to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> headMap(final short to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final short from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Short2ReferenceSortedMap<V>)Short2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public short firstShortKey() {
            return this.key;
        }
        
        @Override
        public short lastShortKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> subMap(final Short ofrom, final Short oto) {
            return this.subMap((short)ofrom, (short)oto);
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.firstShortKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.lastShortKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Short2ReferenceMaps.SynchronizedMap<V> implements Short2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ReferenceSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Short2ReferenceSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Short2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2ReferenceMap.Entry<V>>synchronize(this.sortedMap.short2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Short2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ReferenceEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> subMap(final short from, final short to) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2ReferenceSortedMap<V> headMap(final short to) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final short from) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public short firstShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstShortKey();
            }
        }
        
        @Override
        public short lastShortKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastShortKey();
            }
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> subMap(final Short from, final Short to) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> headMap(final Short to) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final Short from) {
            return new SynchronizedSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Short2ReferenceMaps.UnmodifiableMap<V> implements Short2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ReferenceSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Short2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Short2ReferenceMap.Entry<V>> short2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2ReferenceMap.Entry<V>>unmodifiable(this.sortedMap.short2ReferenceEntrySet());
            }
            return (ObjectSortedSet<Short2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, V>>)this.short2ReferenceEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ReferenceSortedMap<V> subMap(final short from, final short to) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2ReferenceSortedMap<V> headMap(final short to) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final short from) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public short firstShortKey() {
            return this.sortedMap.firstShortKey();
        }
        
        @Override
        public short lastShortKey() {
            return this.sortedMap.lastShortKey();
        }
        
        @Deprecated
        @Override
        public Short firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Short lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> subMap(final Short from, final Short to) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> headMap(final Short to) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Short2ReferenceSortedMap<V> tailMap(final Short from) {
            return new UnmodifiableSortedMap((Short2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
