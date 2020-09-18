package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Short2DoubleSortedMap extends Short2DoubleMap, SortedMap<Short, Double> {
    Short2DoubleSortedMap subMap(final short short1, final short short2);
    
    Short2DoubleSortedMap headMap(final short short1);
    
    Short2DoubleSortedMap tailMap(final short short1);
    
    short firstShortKey();
    
    short lastShortKey();
    
    @Deprecated
    default Short2DoubleSortedMap subMap(final Short from, final Short to) {
        return this.subMap((short)from, (short)to);
    }
    
    @Deprecated
    default Short2DoubleSortedMap headMap(final Short to) {
        return this.headMap((short)to);
    }
    
    @Deprecated
    default Short2DoubleSortedMap tailMap(final Short from) {
        return this.tailMap((short)from);
    }
    
    @Deprecated
    default Short firstKey() {
        return this.firstShortKey();
    }
    
    @Deprecated
    default Short lastKey() {
        return this.lastShortKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Short, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Short, Double>>)this.short2DoubleEntrySet();
    }
    
    ObjectSortedSet<Entry> short2DoubleEntrySet();
    
    ShortSortedSet keySet();
    
    DoubleCollection values();
    
    ShortComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
