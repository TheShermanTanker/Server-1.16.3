package com.google.common.collect;

import java.util.Iterator;
import com.google.common.annotations.GwtIncompatible;
import java.util.NavigableSet;
import java.util.Comparator;
import com.google.j2objc.annotations.Weak;
import java.util.SortedSet;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class SortedMultisets {
    private SortedMultisets() {
    }
    
    private static <E> E getElementOrThrow(final Multiset.Entry<E> entry) {
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.getElement();
    }
    
    private static <E> E getElementOrNull(@Nullable final Multiset.Entry<E> entry) {
        return (entry == null) ? null : entry.getElement();
    }
    
    static class ElementSet<E> extends Multisets.ElementSet<E> implements SortedSet<E> {
        @Weak
        private final SortedMultiset<E> multiset;
        
        ElementSet(final SortedMultiset<E> multiset) {
            this.multiset = multiset;
        }
        
        @Override
        final SortedMultiset<E> multiset() {
            return this.multiset;
        }
        
        public Comparator<? super E> comparator() {
            return this.multiset().comparator();
        }
        
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return (SortedSet<E>)this.multiset().subMultiset(fromElement, BoundType.CLOSED, toElement, BoundType.OPEN).elementSet();
        }
        
        public SortedSet<E> headSet(final E toElement) {
            return (SortedSet<E>)this.multiset().headMultiset(toElement, BoundType.OPEN).elementSet();
        }
        
        public SortedSet<E> tailSet(final E fromElement) {
            return (SortedSet<E>)this.multiset().tailMultiset(fromElement, BoundType.CLOSED).elementSet();
        }
        
        public E first() {
            return (E)SortedMultisets.getElementOrThrow((Multiset.Entry<Object>)this.multiset().firstEntry());
        }
        
        public E last() {
            return (E)SortedMultisets.getElementOrThrow((Multiset.Entry<Object>)this.multiset().lastEntry());
        }
    }
    
    @GwtIncompatible
    static class NavigableElementSet<E> extends ElementSet<E> implements NavigableSet<E> {
        NavigableElementSet(final SortedMultiset<E> multiset) {
            super(multiset);
        }
        
        public E lower(final E e) {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().headMultiset(e, BoundType.OPEN).lastEntry());
        }
        
        public E floor(final E e) {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().headMultiset(e, BoundType.CLOSED).lastEntry());
        }
        
        public E ceiling(final E e) {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().tailMultiset(e, BoundType.CLOSED).firstEntry());
        }
        
        public E higher(final E e) {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().tailMultiset(e, BoundType.OPEN).firstEntry());
        }
        
        public NavigableSet<E> descendingSet() {
            return (NavigableSet<E>)new NavigableElementSet((SortedMultiset<Object>)this.multiset().descendingMultiset());
        }
        
        public Iterator<E> descendingIterator() {
            return (Iterator<E>)this.descendingSet().iterator();
        }
        
        public E pollFirst() {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().pollFirstEntry());
        }
        
        public E pollLast() {
            return (E)SortedMultisets.getElementOrNull((Multiset.Entry<Object>)this.multiset().pollLastEntry());
        }
        
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return (NavigableSet<E>)new NavigableElementSet((SortedMultiset<Object>)this.multiset().subMultiset(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
        }
        
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return (NavigableSet<E>)new NavigableElementSet((SortedMultiset<Object>)this.multiset().headMultiset(toElement, BoundType.forBoolean(inclusive)));
        }
        
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return (NavigableSet<E>)new NavigableElementSet((SortedMultiset<Object>)this.multiset().tailMultiset(fromElement, BoundType.forBoolean(inclusive)));
        }
    }
}
