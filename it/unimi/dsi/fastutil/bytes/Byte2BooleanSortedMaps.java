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

public final class Byte2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Byte2BooleanSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Byte, ?>> entryComparator(final ByteComparator comparator) {
        return ((x, y) -> comparator.compare((byte)x.getKey(), (byte)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Byte2BooleanMap.Entry> fastIterator(final Byte2BooleanSortedMap map) {
        final ObjectSortedSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
        return (entries instanceof Byte2BooleanSortedMap.FastSortedEntrySet) ? ((Byte2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Byte2BooleanMap.Entry> fastIterable(final Byte2BooleanSortedMap map) {
        final ObjectSortedSet<Byte2BooleanMap.Entry> entries = map.byte2BooleanEntrySet();
        ObjectBidirectionalIterable<Byte2BooleanMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Byte2BooleanSortedMap.FastSortedEntrySet) {
            final Byte2BooleanSortedMap.FastSortedEntrySet set = (Byte2BooleanSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Byte2BooleanSortedMap singleton(final Byte key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Byte2BooleanSortedMap singleton(final Byte key, final Boolean value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2BooleanSortedMap singleton(final byte key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Byte2BooleanSortedMap singleton(final byte key, final boolean value, final ByteComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Byte2BooleanSortedMap synchronize(final Byte2BooleanSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Byte2BooleanSortedMap synchronize(final Byte2BooleanSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Byte2BooleanSortedMap unmodifiable(final Byte2BooleanSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Byte2BooleanMaps.EmptyMap implements Byte2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ByteComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
            return (ObjectSortedSet<Byte2BooleanMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet keySet() {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public Byte2BooleanSortedMap subMap(final byte from, final byte to) {
            return Byte2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2BooleanSortedMap headMap(final byte to) {
            return Byte2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2BooleanSortedMap tailMap(final byte from) {
            return Byte2BooleanSortedMaps.EMPTY_MAP;
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
        public Byte2BooleanSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap subMap(final Byte ofrom, final Byte oto) {
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
    
    public static class Singleton extends Byte2BooleanMaps.Singleton implements Byte2BooleanSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteComparator comparator;
        
        protected Singleton(final byte key, final boolean value, final ByteComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final byte key, final boolean value) {
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
        public ObjectSortedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractByte2BooleanMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractByte2BooleanMap.BasicEntry>)Byte2BooleanSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Byte2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.singleton(this.key, this.comparator);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2BooleanSortedMap subMap(final byte from, final byte to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2BooleanSortedMap headMap(final byte to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Byte2BooleanSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2BooleanSortedMap tailMap(final byte from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Byte2BooleanSortedMaps.EMPTY_MAP;
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
        public Byte2BooleanSortedMap headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap subMap(final Byte ofrom, final Byte oto) {
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
    
    public static class SynchronizedSortedMap extends Byte2BooleanMaps.SynchronizedMap implements Byte2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2BooleanSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Byte2BooleanSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Byte2BooleanSortedMap m) {
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
        public ObjectSortedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2BooleanMap.Entry>synchronize(this.sortedMap.byte2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Byte2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2BooleanSortedMap subMap(final byte from, final byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Byte2BooleanSortedMap headMap(final byte to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Byte2BooleanSortedMap tailMap(final byte from) {
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
        public Byte2BooleanSortedMap subMap(final Byte from, final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap headMap(final Byte to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap tailMap(final Byte from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Byte2BooleanMaps.UnmodifiableMap implements Byte2BooleanSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2BooleanSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Byte2BooleanSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Byte2BooleanMap.Entry> byte2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2BooleanMap.Entry>unmodifiable(this.sortedMap.byte2BooleanEntrySet());
            }
            return (ObjectSortedSet<Byte2BooleanMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, Boolean>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, Boolean>>)this.byte2BooleanEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2BooleanSortedMap subMap(final byte from, final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Byte2BooleanSortedMap headMap(final byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Byte2BooleanSortedMap tailMap(final byte from) {
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
        public Byte2BooleanSortedMap subMap(final Byte from, final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap headMap(final Byte to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Byte2BooleanSortedMap tailMap(final Byte from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
