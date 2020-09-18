package it.unimi.dsi.fastutil.longs;

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

public interface Long2ReferenceSortedMap<V> extends Long2ReferenceMap<V>, SortedMap<Long, V> {
    Long2ReferenceSortedMap<V> subMap(final long long1, final long long2);
    
    Long2ReferenceSortedMap<V> headMap(final long long1);
    
    Long2ReferenceSortedMap<V> tailMap(final long long1);
    
    long firstLongKey();
    
    long lastLongKey();
    
    @Deprecated
    default Long2ReferenceSortedMap<V> subMap(final Long from, final Long to) {
        return this.subMap((long)from, (long)to);
    }
    
    @Deprecated
    default Long2ReferenceSortedMap<V> headMap(final Long to) {
        return this.headMap((long)to);
    }
    
    @Deprecated
    default Long2ReferenceSortedMap<V> tailMap(final Long from) {
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
    default ObjectSortedSet<Map.Entry<Long, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<Long, V>>)this.long2ReferenceEntrySet();
    }
    
    ObjectSortedSet<Entry<V>> long2ReferenceEntrySet();
    
    LongSortedSet keySet();
    
    ReferenceCollection<V> values();
    
    LongComparator comparator();
    
    public interface FastSortedEntrySet<V> extends ObjectSortedSet<Entry<V>>, FastEntrySet<V> {
        ObjectBidirectionalIterator<Entry<V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<V>> fastIterator(final Entry<V> entry);
    }
}
