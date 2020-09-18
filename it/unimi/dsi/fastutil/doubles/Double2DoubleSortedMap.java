package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2DoubleSortedMap extends Double2DoubleMap, SortedMap<Double, Double> {
    Double2DoubleSortedMap subMap(final double double1, final double double2);
    
    Double2DoubleSortedMap headMap(final double double1);
    
    Double2DoubleSortedMap tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2DoubleSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2DoubleSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2DoubleSortedMap tailMap(final Double from) {
        return this.tailMap((double)from);
    }
    
    @Deprecated
    default Double firstKey() {
        return this.firstDoubleKey();
    }
    
    @Deprecated
    default Double lastKey() {
        return this.lastDoubleKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Double, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Double>>)this.double2DoubleEntrySet();
    }
    
    ObjectSortedSet<Entry> double2DoubleEntrySet();
    
    DoubleSortedSet keySet();
    
    DoubleCollection values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
