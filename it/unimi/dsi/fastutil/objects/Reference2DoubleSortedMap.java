package it.unimi.dsi.fastutil.objects;

import java.util.Collection;
import java.util.Set;
import java.util.Comparator;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2DoubleSortedMap<K> extends Reference2DoubleMap<K>, SortedMap<K, Double> {
    Reference2DoubleSortedMap<K> subMap(final K object1, final K object2);
    
    Reference2DoubleSortedMap<K> headMap(final K object);
    
    Reference2DoubleSortedMap<K> tailMap(final K object);
    
    @Deprecated
    default ObjectSortedSet<Map.Entry<K, Double>> entrySet() {
        return (ObjectSortedSet<Map.Entry<K, Double>>)this.reference2DoubleEntrySet();
    }
    
    ObjectSortedSet<Entry<K>> reference2DoubleEntrySet();
    
    ReferenceSortedSet<K> keySet();
    
    DoubleCollection values();
    
    Comparator<? super K> comparator();
    
    public interface FastSortedEntrySet<K> extends ObjectSortedSet<Entry<K>>, FastEntrySet<K> {
        ObjectBidirectionalIterator<Entry<K>> fastIterator();
        
        ObjectBidirectionalIterator<Entry<K>> fastIterator(final Entry<K> entry);
    }
}
