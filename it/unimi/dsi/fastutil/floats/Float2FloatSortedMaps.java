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

public final class Float2FloatSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Float2FloatSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(final FloatComparator comparator) {
        return ((x, y) -> comparator.compare((float)x.getKey(), (float)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator(final Float2FloatSortedMap map) {
        final ObjectSortedSet<Float2FloatMap.Entry> entries = map.float2FloatEntrySet();
        return (entries instanceof Float2FloatSortedMap.FastSortedEntrySet) ? ((Float2FloatSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Float2FloatMap.Entry> fastIterable(final Float2FloatSortedMap map) {
        final ObjectSortedSet<Float2FloatMap.Entry> entries = map.float2FloatEntrySet();
        ObjectBidirectionalIterable<Float2FloatMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Float2FloatSortedMap.FastSortedEntrySet) {
            final Float2FloatSortedMap.FastSortedEntrySet set = (Float2FloatSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Float2FloatSortedMap singleton(final Float key, final Float value) {
        return new Singleton(key, value);
    }
    
    public static Float2FloatSortedMap singleton(final Float key, final Float value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2FloatSortedMap singleton(final float key, final float value) {
        return new Singleton(key, value);
    }
    
    public static Float2FloatSortedMap singleton(final float key, final float value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2FloatSortedMap synchronize(final Float2FloatSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Float2FloatSortedMap synchronize(final Float2FloatSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Float2FloatSortedMap unmodifiable(final Float2FloatSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Float2FloatMaps.EmptyMap implements Float2FloatSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
            return (ObjectSortedSet<Float2FloatMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Float>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float2FloatSortedMap subMap(final float from, final float to) {
            return Float2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2FloatSortedMap headMap(final float to) {
            return Float2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2FloatSortedMap tailMap(final float from) {
            return Float2FloatSortedMaps.EMPTY_MAP;
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
        public Float2FloatSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class Singleton extends Float2FloatMaps.Singleton implements Float2FloatSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;
        
        protected Singleton(final float key, final float value, final FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final float key, final float value) {
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
        public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2FloatMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractFloat2FloatMap.BasicEntry>)Float2FloatSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Float2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Float>>)this.float2FloatEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2FloatSortedMap subMap(final float from, final float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2FloatSortedMap headMap(final float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2FloatSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2FloatSortedMap tailMap(final float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Float2FloatSortedMaps.EMPTY_MAP;
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
        public Float2FloatSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class SynchronizedSortedMap extends Float2FloatMaps.SynchronizedMap implements Float2FloatSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2FloatSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Float2FloatSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Float2FloatSortedMap m) {
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
        public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2FloatMap.Entry>synchronize(this.sortedMap.float2FloatEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Float2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Float>>)this.float2FloatEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2FloatSortedMap subMap(final float from, final float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2FloatSortedMap headMap(final float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2FloatSortedMap tailMap(final float from) {
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
        public Float2FloatSortedMap subMap(final Float from, final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap headMap(final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap tailMap(final Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Float2FloatMaps.UnmodifiableMap implements Float2FloatSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2FloatSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Float2FloatSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Float2FloatMap.Entry> float2FloatEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2FloatMap.Entry>unmodifiable(this.sortedMap.float2FloatEntrySet());
            }
            return (ObjectSortedSet<Float2FloatMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Float>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Float>>)this.float2FloatEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2FloatSortedMap subMap(final float from, final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2FloatSortedMap headMap(final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2FloatSortedMap tailMap(final float from) {
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
        public Float2FloatSortedMap subMap(final Float from, final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap headMap(final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Float2FloatSortedMap tailMap(final Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
