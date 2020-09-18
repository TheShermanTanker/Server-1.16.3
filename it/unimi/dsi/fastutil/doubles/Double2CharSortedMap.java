package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.chars.CharCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Double2CharSortedMap extends Double2CharMap, SortedMap<Double, Character> {
    Double2CharSortedMap subMap(final double double1, final double double2);
    
    Double2CharSortedMap headMap(final double double1);
    
    Double2CharSortedMap tailMap(final double double1);
    
    double firstDoubleKey();
    
    double lastDoubleKey();
    
    @Deprecated
    default Double2CharSortedMap subMap(final Double from, final Double to) {
        return this.subMap((double)from, (double)to);
    }
    
    @Deprecated
    default Double2CharSortedMap headMap(final Double to) {
        return this.headMap((double)to);
    }
    
    @Deprecated
    default Double2CharSortedMap tailMap(final Double from) {
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
    default ObjectSortedSet<Map.Entry<Double, Character>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Double, Character>>)this.double2CharEntrySet();
    }
    
    ObjectSortedSet<Entry> double2CharEntrySet();
    
    DoubleSortedSet keySet();
    
    CharCollection values();
    
    DoubleComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
