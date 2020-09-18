package it.unimi.dsi.fastutil.ints;

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

public interface Int2LongSortedMap extends Int2LongMap, SortedMap<Integer, Long> {
    Int2LongSortedMap subMap(final int integer1, final int integer2);
    
    Int2LongSortedMap headMap(final int integer);
    
    Int2LongSortedMap tailMap(final int integer);
    
    int firstIntKey();
    
    int lastIntKey();
    
    @Deprecated
    default Int2LongSortedMap subMap(final Integer from, final Integer to) {
        return this.subMap((int)from, (int)to);
    }
    
    @Deprecated
    default Int2LongSortedMap headMap(final Integer to) {
        return this.headMap((int)to);
    }
    
    @Deprecated
    default Int2LongSortedMap tailMap(final Integer from) {
        return this.tailMap((int)from);
    }
    
    @Deprecated
    default Integer firstKey() {
        return this.firstIntKey();
    }
    
    @Deprecated
    default Integer lastKey() {
        return this.lastIntKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Integer, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Integer, Long>>)this.int2LongEntrySet();
    }
    
    ObjectSortedSet<Entry> int2LongEntrySet();
    
    IntSortedSet keySet();
    
    LongCollection values();
    
    IntComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
