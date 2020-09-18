package it.unimi.dsi.fastutil.doubles;

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

public final class Double2ReferenceSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Double2ReferenceSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Double, ?>> entryComparator(final DoubleComparator comparator) {
        return ((x, y) -> comparator.compare((double)x.getKey(), (double)y.getKey()));
    }
    
    public static <V> ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> fastIterator(final Double2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Double2ReferenceMap.Entry<V>> entries = map.double2ReferenceEntrySet();
        return (entries instanceof Double2ReferenceSortedMap.FastSortedEntrySet) ? ((Double2ReferenceSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static <V> ObjectBidirectionalIterable<Double2ReferenceMap.Entry<V>> fastIterable(final Double2ReferenceSortedMap<V> map) {
        final ObjectSortedSet<Double2ReferenceMap.Entry<V>> entries = map.double2ReferenceEntrySet();
        ObjectBidirectionalIterable<Double2ReferenceMap.Entry<V>> objectBidirectionalIterable;
        if (entries instanceof Double2ReferenceSortedMap.FastSortedEntrySet) {
            final Double2ReferenceSortedMap.FastSortedEntrySet set = (Double2ReferenceSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static <V> Double2ReferenceSortedMap<V> emptyMap() {
        return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
    }
    
    public static <V> Double2ReferenceSortedMap<V> singleton(final Double key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Double2ReferenceSortedMap<V> singleton(final Double key, final V value, final DoubleComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Double2ReferenceSortedMap<V> singleton(final double key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Double2ReferenceSortedMap<V> singleton(final double key, final V value, final DoubleComparator comparator) {
        return new Singleton<V>(key, value, comparator);
    }
    
    public static <V> Double2ReferenceSortedMap<V> synchronize(final Double2ReferenceSortedMap<V> m) {
        return new SynchronizedSortedMap<V>(m);
    }
    
    public static <V> Double2ReferenceSortedMap<V> synchronize(final Double2ReferenceSortedMap<V> m, final Object sync) {
        return new SynchronizedSortedMap<V>(m, sync);
    }
    
    public static <V> Double2ReferenceSortedMap<V> unmodifiable(final Double2ReferenceSortedMap<V> m) {
        return new UnmodifiableSortedMap<V>(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap<V> extends Double2ReferenceMaps.EmptyMap<V> implements Double2ReferenceSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
            return (ObjectSortedSet<Double2ReferenceMap.Entry<V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, V>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet keySet() {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> headMap(final double to) {
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final double from) {
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public double firstDoubleKey() {
            throw new NoSuchElementException();
        }
        
        @Override
        public double lastDoubleKey() {
            throw new NoSuchElementException();
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> subMap(final Double ofrom, final Double oto) {
            return this.subMap((double)ofrom, (double)oto);
        }
        
        @Deprecated
        @Override
        public Double firstKey() {
            return this.firstDoubleKey();
        }
        
        @Deprecated
        @Override
        public Double lastKey() {
            return this.lastDoubleKey();
        }
    }
    
    public static class Singleton<V> extends Double2ReferenceMaps.Singleton<V> implements Double2ReferenceSortedMap<V>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleComparator comparator;
        
        protected Singleton(final double key, final V value, final DoubleComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final double key, final V value) {
            this(key, value, null);
        }
        
        final int compare(final double k1, final double k2) {
            return (this.comparator == null) ? Double.compare(k1, k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.comparator;
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractDouble2ReferenceMap.BasicEntry<V>(this.key, this.value), (java.util.Comparator<? super AbstractDouble2ReferenceMap.BasicEntry<V>>)Double2ReferenceSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Double2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ReferenceEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.singleton(this.key, this.comparator);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> headMap(final double to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final double from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return (Double2ReferenceSortedMap<V>)Double2ReferenceSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public double firstDoubleKey() {
            return this.key;
        }
        
        @Override
        public double lastDoubleKey() {
            return this.key;
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> subMap(final Double ofrom, final Double oto) {
            return this.subMap((double)ofrom, (double)oto);
        }
        
        @Deprecated
        @Override
        public Double firstKey() {
            return this.firstDoubleKey();
        }
        
        @Deprecated
        @Override
        public Double lastKey() {
            return this.lastDoubleKey();
        }
    }
    
    public static class SynchronizedSortedMap<V> extends Double2ReferenceMaps.SynchronizedMap<V> implements Double2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ReferenceSortedMap<V> sortedMap;
        
        protected SynchronizedSortedMap(final Double2ReferenceSortedMap<V> m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Double2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public DoubleComparator comparator() {
            synchronized (this.sync) {
                return this.sortedMap.comparator();
            }
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2ReferenceMap.Entry<V>>synchronize(this.sortedMap.double2ReferenceEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Double2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ReferenceEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Double2ReferenceSortedMap<V> headMap(final double to) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final double from) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
        
        @Override
        public double firstDoubleKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstDoubleKey();
            }
        }
        
        @Override
        public double lastDoubleKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastDoubleKey();
            }
        }
        
        @Deprecated
        @Override
        public Double firstKey() {
            synchronized (this.sync) {
                return this.sortedMap.firstKey();
            }
        }
        
        @Deprecated
        @Override
        public Double lastKey() {
            synchronized (this.sync) {
                return this.sortedMap.lastKey();
            }
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> subMap(final Double from, final Double to) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> headMap(final Double to) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final Double from) {
            return new SynchronizedSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap<V> extends Double2ReferenceMaps.UnmodifiableMap<V> implements Double2ReferenceSortedMap<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2ReferenceSortedMap<V> sortedMap;
        
        protected UnmodifiableSortedMap(final Double2ReferenceSortedMap<V> m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Double2ReferenceMap.Entry<V>> double2ReferenceEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2ReferenceMap.Entry<V>>unmodifiable(this.sortedMap.double2ReferenceEntrySet());
            }
            return (ObjectSortedSet<Double2ReferenceMap.Entry<V>>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ReferenceEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2ReferenceSortedMap<V> subMap(final double from, final double to) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Double2ReferenceSortedMap<V> headMap(final double to) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final double from) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
        
        @Override
        public double firstDoubleKey() {
            return this.sortedMap.firstDoubleKey();
        }
        
        @Override
        public double lastDoubleKey() {
            return this.sortedMap.lastDoubleKey();
        }
        
        @Deprecated
        @Override
        public Double firstKey() {
            return this.sortedMap.firstKey();
        }
        
        @Deprecated
        @Override
        public Double lastKey() {
            return this.sortedMap.lastKey();
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> subMap(final Double from, final Double to) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> headMap(final Double to) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Double2ReferenceSortedMap<V> tailMap(final Double from) {
            return new UnmodifiableSortedMap((Double2ReferenceSortedMap<Object>)this.sortedMap.tailMap(from));
        }
    }
}
