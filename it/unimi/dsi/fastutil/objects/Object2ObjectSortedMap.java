package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ObjectSortedMap<K, V> extends Object2ObjectMap<K, V>, SortedMap<K, V> {
    Object2ObjectSortedMap<K, V> subMap(final K object1, final K object2);
    
    Object2ObjectSortedMap<K, V> headMap(final K object);
    
    Object2ObjectSortedMap<K, V> tailMap(final K object);
    
    default ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
    }
    
    ObjectSortedSet<Entry<K, V>> object2ObjectEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ObjectCollection<V> values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K, V> extends ObjectSortedSet<Entry<K, V>>, FastEntrySet<K, V> {
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K, V>> fastIterator(final Entry<K, V> entry);
    }
}
