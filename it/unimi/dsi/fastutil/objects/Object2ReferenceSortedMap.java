package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ReferenceSortedMap<K, V> extends Object2ReferenceMap<K, V>, SortedMap<K, V> {
    Object2ReferenceSortedMap<K, V> subMap(final K object1, final K object2);
    
    Object2ReferenceSortedMap<K, V> headMap(final K object);
    
    Object2ReferenceSortedMap<K, V> tailMap(final K object);
    
    default ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, V>>)this.object2ReferenceEntrySet();
    }
    
    ObjectSortedSet<Entry<K, V>> object2ReferenceEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ReferenceCollection<V> values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Entry<K, V>>, FastEntrySet<K, V> {
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator(final Entry<K, V> entry);
    }
}
