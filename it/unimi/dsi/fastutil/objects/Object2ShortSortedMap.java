package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ShortSortedMap<K> extends Object2ShortMap<K>, SortedMap<K, Short> {
    Object2ShortSortedMap<K> subMap(final K object1, final K object2);
    
    Object2ShortSortedMap<K> headMap(final K object);
    
    Object2ShortSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Short>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Short>>)this.object2ShortEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> object2ShortEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    ShortCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
