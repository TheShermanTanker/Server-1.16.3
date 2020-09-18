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

public final class Double2CharSortedMaps {
    public static final EmptySortedMap EMPTY_MAP;
    
    private Double2CharSortedMaps() {
    }
    
    public static Comparator<? super Map.Entry<Double, ?>> entryComparator(final DoubleComparator comparator) {
        return ((x, y) -> comparator.compare((double)x.getKey(), (double)y.getKey()));
    }
    
    public static ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator(final Double2CharSortedMap map) {
        final ObjectSortedSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
        return (entries instanceof Double2CharSortedMap.FastSortedEntrySet) ? ((Double2CharSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }
    
    public static ObjectBidirectionalIterable<Double2CharMap.Entry> fastIterable(final Double2CharSortedMap map) {
        final ObjectSortedSet<Double2CharMap.Entry> entries = map.double2CharEntrySet();
        ObjectBidirectionalIterable<Double2CharMap.Entry> objectBidirectionalIterable;
        if (entries instanceof Double2CharSortedMap.FastSortedEntrySet) {
            final Double2CharSortedMap.FastSortedEntrySet set = (Double2CharSortedMap.FastSortedEntrySet)entries;
            Objects.requireNonNull(set);
            objectBidirectionalIterable = set::fastIterator;
        }
        else {
            objectBidirectionalIterable = entries;
        }
        return objectBidirectionalIterable;
    }
    
    public static Double2CharSortedMap singleton(final Double key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Double2CharSortedMap singleton(final Double key, final Character value, final DoubleComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Double2CharSortedMap singleton(final double key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Double2CharSortedMap singleton(final double key, final char value, final DoubleComparator comparator) {
        return new Singleton(key, value, comparator);
    }
    
    public static Double2CharSortedMap synchronize(final Double2CharSortedMap m) {
        return new SynchronizedSortedMap(m);
    }
    
    public static Double2CharSortedMap synchronize(final Double2CharSortedMap m, final Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }
    
    public static Double2CharSortedMap unmodifiable(final Double2CharSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }
    
    static {
        EMPTY_MAP = new EmptySortedMap();
    }
    
    public static class EmptySortedMap extends Double2CharMaps.EmptyMap implements Double2CharSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySortedMap() {
        }
        
        @Override
        public DoubleComparator comparator() {
            return null;
        }
        
        @Override
        public ObjectSortedSet<Double2CharMap.Entry> double2CharEntrySet() {
            return (ObjectSortedSet<Double2CharMap.Entry>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Character>>)ObjectSortedSets.EMPTY_SET;
        }
        
        @Override
        public DoubleSortedSet keySet() {
            return DoubleSortedSets.EMPTY_SET;
        }
        
        @Override
        public Double2CharSortedMap subMap(final double from, final double to) {
            return Double2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2CharSortedMap headMap(final double to) {
            return Double2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2CharSortedMap tailMap(final double from) {
            return Double2CharSortedMaps.EMPTY_MAP;
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
        public Double2CharSortedMap headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap subMap(final Double ofrom, final Double oto) {
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
    
    public static class Singleton extends Double2CharMaps.Singleton implements Double2CharSortedMap, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleComparator comparator;
        
        protected Singleton(final double key, final char value, final DoubleComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }
        
        protected Singleton(final double key, final char value) {
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
        public ObjectSortedSet<Double2CharMap.Entry> double2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractDouble2CharMap.BasicEntry(this.key, this.value), (java.util.Comparator<? super AbstractDouble2CharMap.BasicEntry>)Double2CharSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet<Double2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.singleton(this.key, this.comparator);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2CharSortedMap subMap(final double from, final double to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return Double2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2CharSortedMap headMap(final double to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return Double2CharSortedMaps.EMPTY_MAP;
        }
        
        @Override
        public Double2CharSortedMap tailMap(final double from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return Double2CharSortedMaps.EMPTY_MAP;
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
        public Double2CharSortedMap headMap(final Double oto) {
            return this.headMap((double)oto);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap tailMap(final Double ofrom) {
            return this.tailMap((double)ofrom);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap subMap(final Double ofrom, final Double oto) {
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
    
    public static class SynchronizedSortedMap extends Double2CharMaps.SynchronizedMap implements Double2CharSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2CharSortedMap sortedMap;
        
        protected SynchronizedSortedMap(final Double2CharSortedMap m, final Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }
        
        protected SynchronizedSortedMap(final Double2CharSortedMap m) {
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
        public ObjectSortedSet<Double2CharMap.Entry> double2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2CharMap.Entry>synchronize(this.sortedMap.double2CharEntrySet(), this.sync);
            }
            return (ObjectSortedSet<Double2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2CharSortedMap subMap(final double from, final double to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Override
        public Double2CharSortedMap headMap(final double to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Override
        public Double2CharSortedMap tailMap(final double from) {
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
        public Double2CharSortedMap subMap(final Double from, final Double to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap headMap(final Double to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap tailMap(final Double from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }
    
    public static class UnmodifiableSortedMap extends Double2CharMaps.UnmodifiableMap implements Double2CharSortedMap, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2CharSortedMap sortedMap;
        
        protected UnmodifiableSortedMap(final Double2CharSortedMap m) {
            super(m);
            this.sortedMap = m;
        }
        
        @Override
        public DoubleComparator comparator() {
            return this.sortedMap.comparator();
        }
        
        @Override
        public ObjectSortedSet<Double2CharMap.Entry> double2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.<Double2CharMap.Entry>unmodifiable(this.sortedMap.double2CharEntrySet());
            }
            return (ObjectSortedSet<Double2CharMap.Entry>)(ObjectSortedSet)this.entries;
        }
        
        @Deprecated
        @Override
        public ObjectSortedSet<Map.Entry<Double, Character>> entrySet() {
            return (ObjectSortedSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
        }
        
        @Override
        public DoubleSortedSet keySet() {
            if (this.keys == null) {
                this.keys = DoubleSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (DoubleSortedSet)this.keys;
        }
        
        @Override
        public Double2CharSortedMap subMap(final double from, final double to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Override
        public Double2CharSortedMap headMap(final double to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Override
        public Double2CharSortedMap tailMap(final double from) {
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
        public Double2CharSortedMap subMap(final Double from, final Double to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap headMap(final Double to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }
        
        @Deprecated
        @Override
        public Double2CharSortedMap tailMap(final Double from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }
}
