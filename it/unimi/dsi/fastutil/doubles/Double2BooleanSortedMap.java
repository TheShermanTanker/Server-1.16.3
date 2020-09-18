package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2BooleanSortedMap extends Double2BooleanMap, SortedMap<Double, Boolean> {
    Double2BooleanSortedMap subMap(final double double1, final double double2);
    
    Double2BooleanSortedMap headMap(final double double1);
    
    Double2BooleanSortedMap tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2BooleanSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2BooleanSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2BooleanSortedMap tailMap(final Double from) {
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
    default ObjectSortedSet<Map.Entry<Double, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Boolean>>)this.double2BooleanEntrySet();
    }
    
    ObjectSortedSet<Entry> double2BooleanEntrySet();
    
    DoubleSortedSet keySet();
    
    BooleanCollection values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
