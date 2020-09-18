package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2LongSortedMap extends Double2LongMap, SortedMap<Double, Long> {
    Double2LongSortedMap subMap(final double double1, final double double2);
    
    Double2LongSortedMap headMap(final double double1);
    
    Double2LongSortedMap tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2LongSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2LongSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2LongSortedMap tailMap(final Double from) {
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
    default ObjectSortedSet<Map.Entry<Double, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Long>>)this.double2LongEntrySet();
    }
    
    ObjectSortedSet<Entry> double2LongEntrySet();
    
    DoubleSortedSet keySet();
    
    LongCollection values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
