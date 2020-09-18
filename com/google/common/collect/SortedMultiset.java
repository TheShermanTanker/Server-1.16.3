package com.google.common.collect;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Set;
import java.util.NavigableSet;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public interface SortedMultiset<E> extends SortedMultisetBridge<E>, SortedIterable<E> {
    Comparator<? super E> comparator();
    
    Multiset.Entry<E> firstEntry();
    
    Multiset.Entry<E> lastEntry();
    
    Multiset.Entry<E> pollFirstEntry();
    
    Multiset.Entry<E> pollLastEntry();
    
    NavigableSet<E> elementSet();
    
    Set<Multiset.Entry<E>> entrySet();
    
    Iterator<E> iterator();
    
    SortedMultiset<E> descendingMultiset();
    
    SortedMultiset<E> headMultiset(final E object, final BoundType boundType);
    
    SortedMultiset<E> subMultiset(final E object1, final BoundType boundType2, final E object3, final BoundType boundType4);
    
    SortedMultiset<E> tailMultiset(final E object, final BoundType boundType);
}
