package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2BooleanSortedMap<K> extends Object2BooleanMap<K>, SortedMap<K, Boolean> {
    Object2BooleanSortedMap<K> subMap(final K object1, final K object2);
    
    Object2BooleanSortedMap<K> headMap(final K object);
    
    Object2BooleanSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Boolean>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Boolean>>)this.object2BooleanEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> object2BooleanEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    BooleanCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
