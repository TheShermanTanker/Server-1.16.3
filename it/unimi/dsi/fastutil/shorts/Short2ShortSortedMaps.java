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

public final class Short2ShortSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Short2ShortSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Short, ?>> entryComparator(final ShortComparator comparator) {
        return ((x, y) -> comparator.compare((short)x.getKey(), (short)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Short2ShortMap.Entry> fastIterator(final Short2ShortSortedMap map) {
        final ObjectSortedSet<Short2ShortMap.Entry> entries = map.short2ShortEntrySet();
        return (entries instanceof Short2ShortSortedMap.FastSortedEntrySet) ? ((Short2ShortSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Short2ShortMap.Entry> fastIterable(final Short2ShortSortedMap map) {
        final ObjectSortedSet<Short2ShortMap.Entry> entries = map.short2ShortEntrySet();
        ObjectBidirectionalIterable<Short2ShortMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Short2ShortSortedMap.FastSortedEntrySet) {
            final Short2ShortSortedMap.FastSortedEntrySet set = (Short2ShortSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Short2ShortSortedMap singleton(final Short key, final Short value) {
        return new Singleton(key, value);
    }
    
    public static Short2ShortSortedMap singleton(final Short key, final Short value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2ShortSortedMap singleton(final short key, final short value) {
        return new Singleton(key, value);
    }
    
    public static Short2ShortSortedMap singleton(final short key, final short value, final ShortComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Short2ShortSortedMap synchronize(final Short2ShortSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Short2ShortSortedMap synchronize(final Short2ShortSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Short2ShortSortedMap unmodifiable(final Short2ShortSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Short2ShortMaps.EmptyMap implements Short2ShortSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ShortComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
            return (ObjectSortedSet<Short2ShortMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Short>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ShortSortedSet keySet() {
            return ShortSortedSets.EMPTY_SET;
        }
        
        @Override
        public Short2ShortSortedMap subMap(final short from, final short to) {
            return Short2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ShortSortedMap headMap(final short to) {
            return Short2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ShortSortedMap tailMap(final short from) {
            return Short2ShortSortedMaps.EMPTY_MAP;
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
        public Short2ShortSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap subMap(final Short ofrom, final Short oto) {
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
    
    public static class Singleton extends Short2ShortMaps.Singleton implements Short2ShortSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortComparator comparator;
        
        protected Singleton(final short key, final short value, final ShortComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final short key, final short value) {
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
        public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractShort2ShortMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractShort2ShortMap.BasicEntry>)Short2ShortSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Short2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Short>>)this.short2ShortEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.singleton(this.key, this.comparator);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ShortSortedMap subMap(final short from, final short to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ShortSortedMap headMap(final short to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Short2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Short2ShortSortedMap tailMap(final short from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Short2ShortSortedMaps.EMPTY_MAP;
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
        public Short2ShortSortedMap headMap(final Short oto) {
            return this.headMap((short)oto);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap tailMap(final Short ofrom) {
            return this.tailMap((short)ofrom);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap subMap(final Short ofrom, final Short oto) {
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
    
    public static class SynchronizedSortedMap extends Short2ShortMaps.SynchronizedMap implements Short2ShortSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ShortSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Short2ShortSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Short2ShortSortedMap m) {
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
        public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2ShortMap.Entry>synchronize(this.sortedMap.short2ShortEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Short2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Short>>)this.short2ShortEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ShortSortedMap subMap(final short from, final short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Short2ShortSortedMap headMap(final short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Short2ShortSortedMap tailMap(final short from) {
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
        public Short2ShortSortedMap subMap(final Short from, final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap headMap(final Short to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap tailMap(final Short from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Short2ShortMaps.UnmodifiableMap implements Short2ShortSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ShortSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Short2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ShortComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Short2ShortMap.Entry>unmodifiable(this.sortedMap.short2ShortEntrySet());
            }
            return (ObjectSortedSet<Short2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Short, Short>>)this.short2ShortEntrySet();
        }
        
        @Override
        public ShortSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ShortSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ShortSortedSet)this.keys;
        }
        
        @Override
        public Short2ShortSortedMap subMap(final short from, final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Short2ShortSortedMap headMap(final short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Short2ShortSortedMap tailMap(final short from) {
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
        public Short2ShortSortedMap subMap(final Short from, final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap headMap(final Short to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Short2ShortSortedMap tailMap(final Short from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
