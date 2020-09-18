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

public final class Short2IntSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Short2IntSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
        return ((x, y) -> comparator.compare((short)x.getKey(), (short)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Short2IntMap.Entry> fastIterator(final Short2IntSortedMap map) {
        final ObjectSortedSet<Short2IntMap.Entry> entries = map.short2IntEntrySet();
        return (entries instanceof Short2IntSortedMap.FastSortedEntrySet) ? ((Short2IntSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Short2IntMap.Entry> fastIterable(final Short2IntSortedMap map) {
        final ObjectSortedSet<Short2IntMap.Entry> entries = map.short2IntEntrySet();
        ObjectBidirectionalIterable<Short2IntMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Short2IntSortedMap.FastSortedEntrySet) {
            final Short2IntSortedMap.FastSortedEntrySet set = (Short2IntSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Short2IntSortedMap singleton(final Short key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Short2IntSortedMap singleton(final Short key, final Integer value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2IntSortedMap singleton(final short key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Short2IntSortedMap singleton(final short key, final int value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2IntSortedMap synchronize(final Short2IntSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Short2IntSortedMap synchronize(final Short2IntSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Short2IntSortedMap unmodifiable(final Short2IntSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Short2IntMaps.EmptyMap implements Short2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Short2IntMap.Entry> short2IntEntrySet() {
            return (ObjectSortedSet<Short2IntMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Integer>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet keySet() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short2IntSortedMap subMap(final short from, final short to) {
            return Short2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2IntSortedMap headMap(final short to) {
            return Short2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2IntSortedMap tailMap(final short from) {
            return Short2IntSortedMaps.EMPTY_MAP;
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
        public Short2IntSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap subMap(final Short ofrom, final Short oto) {
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
    
    public static class Singleton extends Short2IntMaps.Singleton implements Short2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortComparator comparator;
        
        protected Singleton(final short key, final int value, final ShortComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final short key, final int value) {
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
        public ObjectSortedSet<Short2IntMap.Entry> short2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractShort2IntMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractShort2IntMap.BasicEntry>)Short2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Short2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Integer>>)this.short2IntEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.singleton(this.key, this.comparator);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2IntSortedMap subMap(final short from, final short to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2IntSortedMap headMap(final short to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2IntSortedMap tailMap(final short from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Short2IntSortedMaps.EMPTY_MAP;
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
        public Short2IntSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap subMap(final Short ofrom, final Short oto) {
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
    
    public static class SynchronizedSortedMap extends Short2IntMaps.SynchronizedMap implements Short2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2IntSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Short2IntSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Short2IntSortedMap m) {
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
        public ObjectSortedSet<Short2IntMap.Entry> short2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2IntMap.Entry>synchronize(this.sortedMap.short2IntEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Short2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Integer>>)this.short2IntEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2IntSortedMap subMap(final short from, final short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2IntSortedMap headMap(final short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2IntSortedMap tailMap(final short from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
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
        public Short2IntSortedMap subMap(final Short from, final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap headMap(final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap tailMap(final Short from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Short2IntMaps.UnmodifiableMap implements Short2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2IntSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Short2IntSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Short2IntMap.Entry> short2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2IntMap.Entry>unmodifiable(this.sortedMap.short2IntEntrySet());
            }
            return (ObjectSortedSet<Short2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Integer>>)this.short2IntEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2IntSortedMap subMap(final short from, final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2IntSortedMap headMap(final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2IntSortedMap tailMap(final short from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
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
        public Short2IntSortedMap subMap(final Short from, final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap headMap(final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Short2IntSortedMap tailMap(final Short from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
