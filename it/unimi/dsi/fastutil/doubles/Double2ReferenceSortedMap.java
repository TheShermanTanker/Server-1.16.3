package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ReferenceSortedMap<V> extends Double2ReferenceMap<V>, SortedMap<Double, V> {
    Double2ReferenceSortedMap<V> subMap(final double double1, final double double2);
    
    Double2ReferenceSortedMap<V> headMap(final double double1);
    
    Double2ReferenceSortedMap<V> tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2ReferenceSortedMap<V> subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2ReferenceSortedMap<V> headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2ReferenceSortedMap<V> tailMap(final Double from) {
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
    default ObjectSortedSet<Map.Entry<Double, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ReferenceEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> double2ReferenceEntrySet();
    
    DoubleSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}
