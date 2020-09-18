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

public final class Float2CharSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Float2CharSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(final FloatComparator comparator) {
        return ((x, y) -> comparator.compare((float)x.getKey(), (float)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Float2CharMap.Entry> fastIterator(final Float2CharSortedMap map) {
        final ObjectSortedSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
        return (entries instanceof Float2CharSortedMap.FastSortedEntrySet) ? ((Float2CharSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Float2CharMap.Entry> fastIterable(final Float2CharSortedMap map) {
        final ObjectSortedSet<Float2CharMap.Entry> entries = map.float2CharEntrySet();
        ObjectBidirectionalIterable<Float2CharMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Float2CharSortedMap.FastSortedEntrySet) {
            final Float2CharSortedMap.FastSortedEntrySet set = (Float2CharSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Float2CharSortedMap singleton(final Float key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Float2CharSortedMap singleton(final Float key, final Character value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2CharSortedMap singleton(final float key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Float2CharSortedMap singleton(final float key, final char value, final FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Float2CharSortedMap synchronize(final Float2CharSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Float2CharSortedMap synchronize(final Float2CharSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Float2CharSortedMap unmodifiable(final Float2CharSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Float2CharMaps.EmptyMap implements Float2CharSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public FloatComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Float2CharMap.Entry> float2CharEntrySet() {
            return (ObjectSortedSet<Float2CharMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Character>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }
        
        @Override
        public Float2CharSortedMap subMap(final float from, final float to) {
            return Float2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2CharSortedMap headMap(final float to) {
            return Float2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2CharSortedMap tailMap(final float from) {
            return Float2CharSortedMaps.EMPTY_MAP;
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
        public Float2CharSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class Singleton extends Float2CharMaps.Singleton implements Float2CharSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;
        
        protected Singleton(final float key, final char value, final FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final float key, final char value) {
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
        public ObjectSortedSet<Float2CharMap.Entry> float2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2CharMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractFloat2CharMap.BasicEntry>)Float2CharSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Float2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2CharSortedMap subMap(final float from, final float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2CharSortedMap headMap(final float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Float2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Float2CharSortedMap tailMap(final float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Float2CharSortedMaps.EMPTY_MAP;
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
        public Float2CharSortedMap headMap(final Float oto) {
            return this.headMap((float)oto);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap tailMap(final Float ofrom) {
            return this.tailMap((float)ofrom);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap subMap(final Float ofrom, final Float oto) {
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
    
    public static class SynchronizedSortedMap extends Float2CharMaps.SynchronizedMap implements Float2CharSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2CharSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Float2CharSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Float2CharSortedMap m) {
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
        public ObjectSortedSet<Float2CharMap.Entry> float2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2CharMap.Entry>synchronize(this.sortedMap.float2CharEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Float2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2CharSortedMap subMap(final float from, final float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Float2CharSortedMap headMap(final float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Float2CharSortedMap tailMap(final float from) {
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
        public Float2CharSortedMap subMap(final Float from, final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap headMap(final Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap tailMap(final Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Float2CharMaps.UnmodifiableMap implements Float2CharSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2CharSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Float2CharSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Float2CharMap.Entry> float2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Float2CharMap.Entry>unmodifiable(this.sortedMap.float2CharEntrySet());
            }
            return (ObjectSortedSet<Float2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Float, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Float, Character>>)this.float2CharEntrySet();
        }
        
        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }
        
        @Override
        public Float2CharSortedMap subMap(final float from, final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Float2CharSortedMap headMap(final float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Float2CharSortedMap tailMap(final float from) {
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
        public Float2CharSortedMap subMap(final Float from, final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap headMap(final Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Float2CharSortedMap tailMap(final Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
