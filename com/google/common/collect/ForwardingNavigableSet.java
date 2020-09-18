package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import com.google.common.annotations.Beta;
import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;

@GwtIncompatible
public abstract class ForwardingNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E> {
    protected ForwardingNavigableSet() {
    }
    
    protected abstract NavigableSet<E> delegate();
    
    public E lower(final E e) {
        return (E)this.delegate().lower(e);
    }
    
    protected E standardLower(final E e) {
        return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
    }
    
    public E floor(final E e) {
        return (E)this.delegate().floor(e);
    }
    
    protected E standardFloor(final E e) {
        return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
    }
    
    public E ceiling(final E e) {
        return (E)this.delegate().ceiling(e);
    }
    
    protected E standardCeiling(final E e) {
        return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.tailSet(e, true).iterator(), (E)null);
    }
    
    public E higher(final E e) {
        return (E)this.delegate().higher(e);
    }
    
    protected E standardHigher(final E e) {
        return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.tailSet(e, false).iterator(), (E)null);
    }
    
    public E pollFirst() {
        return (E)this.delegate().pollFirst();
    }
    
    protected E standardPollFirst() {
        return Iterators.<E>pollNext(this.iterator());
    }
    
    public E pollLast() {
        return (E)this.delegate().pollLast();
    }
    
    protected E standardPollLast() {
        return Iterators.<E>pollNext(this.descendingIterator());
    }
    
    protected E standardFirst() {
        return (E)this.iterator().next();
    }
    
    protected E standardLast() {
        return (E)this.descendingIterator().next();
    }
    
    public NavigableSet<E> descendingSet() {
        return (NavigableSet<E>)this.delegate().descendingSet();
    }
    
    public Iterator<E> descendingIterator() {
        return (Iterator<E>)this.delegate().descendingIterator();
    }
    
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return (NavigableSet<E>)this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Beta
    protected NavigableSet<E> standardSubSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        return (NavigableSet<E>)this.tailSet(fromElement, fromInclusive).headSet(toElement, toInclusive);
    }
    
    @Override
    protected SortedSet<E> standardSubSet(final E fromElement, final E toElement) {
        return (SortedSet<E>)this.subSet(fromElement, true, toElement, false);
    }
    
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        return (NavigableSet<E>)this.delegate().headSet(toElement, inclusive);
    }
    
    protected SortedSet<E> standardHeadSet(final E toElement) {
        return (SortedSet<E>)this.headSet(toElement, false);
    }
    
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        return (NavigableSet<E>)this.delegate().tailSet(fromElement, inclusive);
    }
    
    protected SortedSet<E> standardTailSet(final E fromElement) {
        return (SortedSet<E>)this.tailSet(fromElement, true);
    }
    
    @Beta
    protected class StandardDescendingSet extends Sets.DescendingSet<E> {
        public StandardDescendingSet() {
            super((NavigableSet)ForwardingNavigableSet.this);
        }
    }
}
