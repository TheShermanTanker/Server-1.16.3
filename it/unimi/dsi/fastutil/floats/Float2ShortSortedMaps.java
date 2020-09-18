package it.unimi.dsi.fastutil.floats;

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

public final class Float2ShortSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Float2ShortSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(final FloatComparator comparator) {
        return ((x, y) -> comparator.compare((float)x.getKey(), (float)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Float2ShortMap.Entry> fastIterator(final Float2ShortSortedMap map) {
        final ObjectSortedSet<Float2ShortMap.Entry> entries = map.float2ShortEntrySet();
        return (entries instanceof Float2ShortSortedMap.FastSortedEntrySet) ? ((Float2ShortSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Float2ShortMap.Entry> fastIterable(final Float2ShortSortedMap map) {
        final ObjectSortedSet<Float2ShortMap.Entry> entries = map.float2ShortEntrySet();
        ObjectBidirectionalIterable<Float2ShortMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Float2ShortSortedMap.FastSortedEntrySet) {
            final Float2ShortSortedMap.FastSortedEntrySet set = (Float2ShortSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Float2ShortSortedMap singleton(final Float key, final Short value) {
        return new Singleton(key, value);
    }
    
    public static Float2ShortSortedMap singleton(final Float key, final Short value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2ShortSortedMap singleton(final float key, final short value) {
        return new Singleton(key, value);
    }
    
    public static Float2ShortSortedMap singleton(final float key, final short value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2ShortSortedMap synchronize(final Float2ShortSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Float2ShortSortedMap synchronize(final Float2ShortSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Float2ShortSortedMap unmodifiable(final Float2ShortSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Float2ShortMaps.EmptyMap implements Float2ShortSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            return (ObjectSortedSet<Float2ShortMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Short>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float2ShortSortedMap subMap(final float from, final float to) {
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ShortSortedMap headMap(final float to) {
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ShortSortedMap tailMap(final float from) {
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public float firstFloatKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public float lastFloatKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap subMap(final Float ofrom, final Float oto) {
            return this.subMap((float)ofrom, (float)oto);
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            return this.firstFloatKey();
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            return this.lastFloatKey();
        }
    }
    
    public static class Singleton extends Float2ShortMaps.Singleton implements Float2ShortSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;
        
        protected Singleton(final float key, final short value, final FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final float key, final short value) {
            this(key, value, null);
        }
        
        final int compare(final float k1, final float k2) {
            return (this.comparator == null) ? Float.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public FloatComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2ShortMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractFloat2ShortMap.BasicEntry>)Float2ShortSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Float2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Short>>)this.float2ShortEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ShortSortedMap subMap(final float from, final float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ShortSortedMap headMap(final float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2ShortSortedMap tailMap(final float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Float2ShortSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public float firstFloatKey() {
            return this.key;
        }
        
        @Override
        public float lastFloatKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap subMap(final Float ofrom, final Float oto) {
            return this.subMap((float)ofrom, (float)oto);
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            return this.firstFloatKey();
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            return this.lastFloatKey();
        }
    }
    
    public static class SynchronizedSortedMap extends Float2ShortMaps.SynchronizedMap implements Float2ShortSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ShortSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Float2ShortSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Float2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2ShortMap.Entry>synchronize(this.sortedMap.float2ShortEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Float2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Short>>)this.float2ShortEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ShortSortedMap subMap(final float from, final float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2ShortSortedMap headMap(final float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2ShortSortedMap tailMap(final float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public float firstFloatKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstFloatKey();
            }
        }
        
        @Override
        public float lastFloatKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastFloatKey();
            }
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap subMap(final Float from, final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap headMap(final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap tailMap(final Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Float2ShortMaps.UnmodifiableMap implements Float2ShortSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ShortSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Float2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2ShortMap.Entry>unmodifiable(this.sortedMap.float2ShortEntrySet());
            }
            return (ObjectSortedSet<Float2ShortMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Short>>)this.float2ShortEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2ShortSortedMap subMap(final float from, final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2ShortSortedMap headMap(final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2ShortSortedMap tailMap(final float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
        
        @Override
        public float firstFloatKey() {
            return this.sortedMap.firstFloatKey();
        }
        
        @Override
        public float lastFloatKey() {
            return this.sortedMap.lastFloatKey();
        }
        
        @Deprecated
        @Override
        public Float firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Float lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap subMap(final Float from, final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap headMap(final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Float2ShortSortedMap tailMap(final Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
