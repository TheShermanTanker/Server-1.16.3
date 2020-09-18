package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.SortedSet;

public interface ReferenceSortedSet<K> extends ReferenceSet<K>, SortedSet<K>, ObjectBidirectionalIterable<K> {
    ObjectBidirectionalIterator<K> iterator(final K object);
    
    ObjectBidirectionalIterator<K> iterator();
    
    ReferenceSortedSet<K> subSet(final K object1, final K object2);
    
    ReferenceSortedSet<K> headSet(final K object);
    
    ReferenceSortedSet<K> tailSet(final K object);
}
