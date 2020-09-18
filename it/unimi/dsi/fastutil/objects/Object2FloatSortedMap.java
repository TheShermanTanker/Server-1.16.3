package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.floats.FloatCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Object2FloatSortedMap<K> extends Object2FloatMap<K>, SortedMap<K, Float> {
    Object2FloatSortedMap<K> subMap(final K object1, final K object2);
    
    Object2FloatSortedMap<K> headMap(final K object);
    
    Object2FloatSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Float>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Float>>)this.object2FloatEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> object2FloatEntrySet();
    
    ObjectSortedSet<K> keySet();
    
    FloatCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
