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

public final class Byte2ObjectSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Byte2ObjectSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Byte, ?>> entryComparator(final ByteComparator comparator) {
        return ((x, y) -> comparator.compare((byte)x.getKey(), (byte)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Byte2ObjectMap.Entry<V>> fastIterator(final Byte2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Byte2ObjectMap.Entry<V>> entries = map.byte2ObjectEntrySet();
        return (entries instanceof Byte2ObjectSortedMap.FastSortedEntrySet) ? ((Byte2ObjectSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Byte2ObjectMap.Entry<V>> fastIterable(final Byte2ObjectSortedMap<V> map) {
        final ObjectSortedSet<Byte2ObjectMap.Entry<V>> entries = map.byte2ObjectEntrySet();
        ObjectBidirectionalIterable<Byte2ObjectMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Byte2ObjectSortedMap.FastSortedEntrySet) {
            final Byte2ObjectSortedMap.FastSortedEntrySet set = (Byte2ObjectSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Byte2ObjectSortedMap<V> emptyMap() {
        return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Byte2ObjectSortedMap<V> singleton(final Byte key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Byte2ObjectSortedMap<V> singleton(final Byte key, final V value, final ByteComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Byte2ObjectSortedMap<V> singleton(final byte key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Byte2ObjectSortedMap<V> singleton(final byte key, final V value, final ByteComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Byte2ObjectSortedMap<V> synchronize(final Byte2ObjectSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Byte2ObjectSortedMap<V> synchronize(final Byte2ObjectSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Byte2ObjectSortedMap<V> unmodifiable(final Byte2ObjectSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Byte2ObjectMaps.EmptyMap<V> implements Byte2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public ByteComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
            return (ObjectSortedSet<Byte2ObjectMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public ByteSortedSet keySet() {
            return ByteSortedSets.EMPTY_SET;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> subMap(final byte from, final byte to) {
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> headMap(final byte to) {
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final byte from) {
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
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
        public Byte2ObjectSortedMap<V> headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> subMap(final Byte ofrom, final Byte oto) {
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
    
    public static class Singleton<V> extends Byte2ObjectMaps.Singleton<V> implements Byte2ObjectSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteComparator comparator;
        
        protected Singleton(final byte key, final V value, final ByteComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final byte key, final V value) {
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
        public ObjectSortedSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractByte2ObjectMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractByte2ObjectMap.BasicEntry<V>>)Byte2ObjectSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Byte2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, V>>)this.byte2ObjectEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.singleton(this.key, this.comparator);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> subMap(final byte from, final byte to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> headMap(final byte to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final byte from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Byte2ObjectSortedMap<V>)Byte2ObjectSortedMaps.EMPTY_MAP;
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
        public Byte2ObjectSortedMap<V> headMap(final Byte oto) {
            return this.headMap((byte)oto);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final Byte ofrom) {
            return this.tailMap((byte)ofrom);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> subMap(final Byte ofrom, final Byte oto) {
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
    
    public static class SynchronizedSortedMap<V> extends Byte2ObjectMaps.SynchronizedMap<V> implements Byte2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ObjectSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Byte2ObjectSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Byte2ObjectSortedMap<V> m) {
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
        public ObjectSortedSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2ObjectMap.Entry<V>>synchronize(this.sortedMap.byte2ObjectEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Byte2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, V>>)this.byte2ObjectEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> subMap(final byte from, final byte to) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Byte2ObjectSortedMap<V> headMap(final byte to) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final byte from) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
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
        public Byte2ObjectSortedMap<V> subMap(final Byte from, final Byte to) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> headMap(final Byte to) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final Byte from) {
            return new SynchronizedSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Byte2ObjectMaps.UnmodifiableMap<V> implements Byte2ObjectSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2ObjectSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Byte2ObjectSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public ByteComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Byte2ObjectMap.Entry<V>> byte2ObjectEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Byte2ObjectMap.Entry<V>>unmodifiable(this.sortedMap.byte2ObjectEntrySet());
            }
            return (ObjectSortedSet<Byte2ObjectMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Byte, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Byte, V>>)this.byte2ObjectEntrySet();
        }
        
        @Override
        public ByteSortedSet keySet() {
            if (this.keys == null) {
                this.keys = ByteSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ByteSortedSet)this.keys;
        }
        
        @Override
        public Byte2ObjectSortedMap<V> subMap(final byte from, final byte to) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Byte2ObjectSortedMap<V> headMap(final byte to) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final byte from) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
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
        public Byte2ObjectSortedMap<V> subMap(final Byte from, final Byte to) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> headMap(final Byte to) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Byte2ObjectSortedMap<V> tailMap(final Byte from) {
            return new UnmodifiableSortedMap((Byte2ObjectSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
