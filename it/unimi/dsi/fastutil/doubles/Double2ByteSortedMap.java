package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ByteSortedMap extends Double2ByteMap, SortedMap<Double, Byte> {
    Double2ByteSortedMap subMap(final double double1, final double double2);
    
    Double2ByteSortedMap headMap(final double double1);
    
    Double2ByteSortedMap tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2ByteSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2ByteSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2ByteSortedMap tailMap(final Double from) {
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
    default ObjectSortedSet<Map.Entry<Double, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Byte>>)this.double2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> double2ByteEntrySet();
    
    DoubleSortedSet keySet();
    
    ByteCollection values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
