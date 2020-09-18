package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2LongSortedMap<K> extends Object2LongMap<K>, SortedMap<K, Long> {
    Object2LongSortedMap<K> subMap(final K object1, final K object2);
    
    Object2LongSortedMap<K> headMap(final K object);
    
    Object2LongSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Long>>)this.object2LongEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> object2LongEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    LongCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
