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

public final class Double2DoubleSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Double2DoubleSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Double, ?>> entryComparator(final DoubleComparator comparator) {
        return ((x, y) -> comparator.compare((double)x.getKey(), (double)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Double2DoubleMap.Entry> fastIterator(final Double2DoubleSortedMap map) {
        final ObjectSortedSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
        return (entries instanceof Double2DoubleSortedMap.FastSortedEntrySet) ? ((Double2DoubleSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Double2DoubleMap.Entry> fastIterable(final Double2DoubleSortedMap map) {
        final ObjectSortedSet<Double2DoubleMap.Entry> entries = map.double2DoubleEntrySet();
        ObjectBidirectionalIterable<Double2DoubleMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Double2DoubleSortedMap.FastSortedEntrySet) {
            final Double2DoubleSortedMap.FastSortedEntrySet set = (Double2DoubleSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Double2DoubleSortedMap singleton(final Double key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleSortedMap singleton(final Double key, final Double value, final DoubleComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Double2DoubleSortedMap singleton(final double key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleSortedMap singleton(final double key, final double value, final DoubleComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Double2DoubleSortedMap synchronize(final Double2DoubleSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Double2DoubleSortedMap synchronize(final Double2DoubleSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Double2DoubleSortedMap unmodifiable(final Double2DoubleSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Double2DoubleMaps.EmptyMap implements Double2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
            return (ObjectSortedSet<Double2DoubleMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Double>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet keySet() {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public Double2DoubleSortedMap subMap(final double from, final double to) {
            return Double2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2DoubleSortedMap headMap(final double to) {
            return Double2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2DoubleSortedMap tailMap(final double from) {
            return Double2DoubleSortedMaps.EMPTY_MAP;
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
        public Double2DoubleSortedMap headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap subMap(final Double ofrom, final Double oto) {
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
    
    public static class Singleton extends Double2DoubleMaps.Singleton implements Double2DoubleSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleComparator comparator;
        
        protected Singleton(final double key, final double value, final DoubleComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final double key, final double value) {
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
        public ObjectSortedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractDouble2DoubleMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractDouble2DoubleMap.BasicEntry>)Double2DoubleSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Double2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.singleton(this.key, this.comparator);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2DoubleSortedMap subMap(final double from, final double to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Double2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2DoubleSortedMap headMap(final double to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Double2DoubleSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2DoubleSortedMap tailMap(final double from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Double2DoubleSortedMaps.EMPTY_MAP;
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
        public Double2DoubleSortedMap headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap subMap(final Double ofrom, final Double oto) {
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
    
    public static class SynchronizedSortedMap extends Double2DoubleMaps.SynchronizedMap implements Double2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Double2DoubleSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Double2DoubleSortedMap m) {
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
        public ObjectSortedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2DoubleMap.Entry>synchronize(this.sortedMap.double2DoubleEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Double2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2DoubleSortedMap subMap(final double from, final double to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Double2DoubleSortedMap headMap(final double to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Double2DoubleSortedMap tailMap(final double from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
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
        public Double2DoubleSortedMap subMap(final Double from, final Double to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap headMap(final Double to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap tailMap(final Double from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Double2DoubleMaps.UnmodifiableMap implements Double2DoubleSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Double2DoubleSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Double2DoubleMap.Entry> double2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2DoubleMap.Entry>unmodifiable(this.sortedMap.double2DoubleEntrySet());
            }
            return (ObjectSortedSet<Double2DoubleMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2DoubleSortedMap subMap(final double from, final double to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Double2DoubleSortedMap headMap(final double to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Double2DoubleSortedMap tailMap(final double from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
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
        public Double2DoubleSortedMap subMap(final Double from, final Double to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap headMap(final Double to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Double2DoubleSortedMap tailMap(final Double from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
