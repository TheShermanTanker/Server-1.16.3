package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2IntSortedMap<K> extends Reference2IntMap<K>, SortedMap<K, Integer> {
    Reference2IntSortedMap<K> subMap(final K object1, final K object2);
    
    Reference2IntSortedMap<K> headMap(final K object);
    
    Reference2IntSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Integer>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Integer>>)this.reference2IntEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> reference2IntEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    IntCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
