package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2ObjectSortedMap<V> extends Double2ObjectMap<V>, SortedMap<Double, V> {
    Double2ObjectSortedMap<V> subMap(final double double1, final double double2);
    
    Double2ObjectSortedMap<V> headMap(final double double1);
    
    Double2ObjectSortedMap<V> tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2ObjectSortedMap<V> subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2ObjectSortedMap<V> headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2ObjectSortedMap<V> tailMap(final Double from) {
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
        return (ObjectSortedSet<Map.Entry<Double, V>>)this.double2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> double2ObjectEntrySet();
    
    DoubleSortedSet keySet();
    
    ObjectCollection<V> values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}
