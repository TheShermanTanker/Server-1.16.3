package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2LongSortedMap<K> extends Reference2LongMap<K>, SortedMap<K, Long> {
    Reference2LongSortedMap<K> subMap(final K object1, final K object2);
    
    Reference2LongSortedMap<K> headMap(final K object);
    
    Reference2LongSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Long>>)this.reference2LongEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> reference2LongEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    LongCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
