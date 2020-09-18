package it.unimi.dsi.fastutil.bytes;

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

public final class Byte2IntSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Byte2IntSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Byte, ?>> entryComparator(final ByteComparator comparator) {
        return ((x, y) -> comparator.compare((byte)x.getKey(), (byte)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Byte2IntMap.Entry> fastIterator(final Byte2IntSortedMap map) {
        final ObjectSortedSet<Byte2IntMap.Entry> entries = map.byte2IntEntrySet();
        return (entries instanceof Byte2IntSortedMap.FastSortedEntrySet) ? ((Byte2IntSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Byte2IntMap.Entry> fastIterable(final Byte2IntSortedMap map) {
        final ObjectSortedSet<Byte2IntMap.Entry> entries = map.byte2IntEntrySet();
        ObjectBidirectionalIterable<Byte2IntMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Byte2IntSortedMap.FastSortedEntrySet) {
            final Byte2IntSortedMap.FastSortedEntrySet set = (Byte2IntSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Byte2IntSortedMap singleton(final Byte key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Byte2IntSortedMap singleton(final Byte key, final Integer value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2IntSortedMap singleton(final byte key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Byte2IntSortedMap singleton(final byte key, final int value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2IntSortedMap synchronize(final Byte2IntSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Byte2IntSortedMap synchronize(final Byte2IntSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Byte2IntSortedMap unmodifiable(final Byte2IntSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Byte2IntMaps.EmptyMap implements Byte2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ByteComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Byte2IntMap.Entry> byte2IntEntrySet() {
            return (ObjectSortedSet<Byte2IntMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Integer>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet keySet() {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public Byte2IntSortedMap subMap(final byte from, final byte to) {
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2IntSortedMap headMap(final byte to) {
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2IntSortedMap tailMap(final byte from) {
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public byte firstByteKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public byte lastByteKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap subMap(final Byte ofrom, final Byte oto) {
            return this.subMap((byte)ofrom, (byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.firstByteKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.lastByteKey();
        }
    }
    
    public static class Singleton extends Byte2IntMaps.Singleton implements Byte2IntSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteComparator comparator;
        
        protected Singleton(final byte key, final int value, final ByteComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final byte key, final int value) {
            this(key, value, null);
        }
        
        final int compare(final byte k1, final byte k2) {
            return (this.comparator == null) ? Byte.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ByteComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Byte2IntMap.Entry> byte2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractByte2IntMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractByte2IntMap.BasicEntry>)Byte2IntSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Byte2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Integer>>)this.byte2IntEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.singleton(this.key, this.comparator);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2IntSortedMap subMap(final byte from, final byte to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2IntSortedMap headMap(final byte to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2IntSortedMap tailMap(final byte from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Byte2IntSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public byte firstByteKey() {
            return this.key;
        }
        
        @Override
        public byte lastByteKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap subMap(final Byte ofrom, final Byte oto) {
            return this.subMap((byte)ofrom, (byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.firstByteKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.lastByteKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Byte2IntMaps.SynchronizedMap implements Byte2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2IntSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Byte2IntSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Byte2IntSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Byte2IntMap.Entry> byte2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2IntMap.Entry>synchronize(this.sortedMap.byte2IntEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Byte2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Integer>>)this.byte2IntEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2IntSortedMap subMap(final byte from, final byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Byte2IntSortedMap headMap(final byte to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Byte2IntSortedMap tailMap(final byte from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public byte firstByteKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstByteKey();
            }
        }
        
        @Override
        public byte lastByteKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastByteKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap subMap(final Byte from, final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap headMap(final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap tailMap(final Byte from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Byte2IntMaps.UnmodifiableMap implements Byte2IntSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2IntSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Byte2IntSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Byte2IntMap.Entry> byte2IntEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2IntMap.Entry>unmodifiable(this.sortedMap.byte2IntEntrySet());
            }
            return (ObjectSortedSet<Byte2IntMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Integer>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Integer>>)this.byte2IntEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2IntSortedMap subMap(final byte from, final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Byte2IntSortedMap headMap(final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Byte2IntSortedMap tailMap(final byte from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public byte firstByteKey() {
            return this.sortedMap.firstByteKey();
        }
        
        @Override
        public byte lastByteKey() {
            return this.sortedMap.lastByteKey();
        }
        
        @Deprecated
        @Override
        public Byte firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Byte lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap subMap(final Byte from, final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap headMap(final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Byte2IntSortedMap tailMap(final Byte from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
