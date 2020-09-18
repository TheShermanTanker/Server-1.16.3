package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Comparator;
import java.util.Collection;
import java.util.Set;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.SortedMap;

public interface Long2LongSortedMap extends Long2LongMap, SortedMap<Long, Long> {
    Long2LongSortedMap subMap(final long long1, final long long2);
    
    Long2LongSortedMap headMap(final long long1);
    
    Long2LongSortedMap tailMap(final long long1);
    
    long firstLongKey();
    
    long lastLongKey();
    
    @Deprecated
    default Long2LongSortedMap subMap(final Long from, final Long to) {
        return this.subMap((long)from, (long)to);
    }
    
    @Deprecated
    default Long2LongSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    default Long2LongSortedMap tailMap(final Long from) {
        return this.tailMap((long)from);
    }
    
    @Deprecated
    default Long firstKey() {
        return this.firstLongKey();
    }
    
    @Deprecated
    default Long lastKey() {
        return this.lastLongKey();
    }
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<Long, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Long>>)this.long2LongEntrySet();
    }
    
    ObjectSortedSet<Entry> long2LongEntrySet();
    
    LongSortedSet keySet();
    
    LongCollection values();
    
    LongComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
