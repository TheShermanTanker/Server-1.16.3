package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.SortedSet;

public interface ObjectSortedSet<K> extends ObjectSet<K>, SortedSet<K>, ObjectBidirectionalIterable<K> {
    ObjectBidirectionalIterator<K> iterator(final K object);
    
    ObjectBidirectionalIterator<K> iterator();
    
    ObjectSortedSet<K> subSet(final K object1, final K object2);
    
    ObjectSortedSet<K> headSet(final K object);
    
    ObjectSortedSet<K> tailSet(final K object);
}
