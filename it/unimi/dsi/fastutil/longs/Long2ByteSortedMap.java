package it.unimi.dsi.fastutil.longs;

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

public interface Long2ByteSortedMap extends Long2ByteMap, SortedMap<Long, Byte> {
    Long2ByteSortedMap subMap(final long long1, final long long2);
    
    Long2ByteSortedMap headMap(final long long1);
    
    Long2ByteSortedMap tailMap(final long long1);
    
    long firstLongKey();
    
    long lastLongKey();
    
    @Deprecated
    default Long2ByteSortedMap subMap(final Long from, final Long to) {
        return this.subMap((long)from, (long)to);
    }
    
    @Deprecated
    default Long2ByteSortedMap headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    default Long2ByteSortedMap tailMap(final Long from) {
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
    default ObjectSortedSet<Map.Entry<Long, Byte>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, Byte>>)this.long2ByteEntrySet();
    }
    
    ObjectSortedSet<Entry> long2ByteEntrySet();
    
    LongSortedSet keySet();
    
    ByteCollection values();
    
    LongComparator comparator();
    
    public interface FastSortedEntrySet extends ObjectSortedSet<Entry>, FastEntrySet {
        ObjectBidirectionalIterator<Entry> fastIterator();
        
        ObjectBidirectionalIterator<Entry> fastIterator(final Entry entry);
    }
}
