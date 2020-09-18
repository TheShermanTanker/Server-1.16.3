package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2ReferenceSortedMap<K, V> extends Reference2ReferenceMap<K, V>, SortedMap<K, V> {
    Reference2ReferenceSortedMap<K, V> subMap(final K object1, final K object2);
    
    Reference2ReferenceSortedMap<K, V> headMap(final K object);
    
    Reference2ReferenceSortedMap<K, V> tailMap(final K object);
    
    default ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, V>>)this.reference2ReferenceEntrySet();
    }
    
    ObjectSortedSet<Entry<K, V>> reference2ReferenceEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    ReferenceCollection<V> values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Entry<K, V>>, FastEntrySet<K, V> {
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator(final Entry<K, V> entry);
    }
}
