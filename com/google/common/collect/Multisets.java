package com.google.common.collect;

import java.util.NoSuchElementException;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import com.google.common.primitives.Ints;
import java.util.function.Function;
import java.util.Spliterator;
import java.util.Collection;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.math.IntMath;
import java.util.Iterator;
import java.util.Set;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Multisets {
    private static final Ordering<Multiset.Entry<?>> DECREASING_COUNT_ORDERING;
    
    private Multisets() {
    }
    
    public static <E> Multiset<E> unmodifiableMultiset(final Multiset<? extends E> multiset) {
        if (multiset instanceof UnmodifiableMultiset || multiset instanceof ImmutableMultiset) {
            final Multiset<E> result = (Multiset<E>)multiset;
            return result;
        }
        return new UnmodifiableMultiset<E>(Preconditions.<Multiset<? extends E>>checkNotNull(multiset));
    }
    
    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(final ImmutableMultiset<E> multiset) {
        return Preconditions.<ImmutableMultiset<E>>checkNotNull(multiset);
    }
    
    @Beta
    public static <E> SortedMultiset<E> unmodifiableSortedMultiset(final SortedMultiset<E> sortedMultiset) {
        return new UnmodifiableSortedMultiset<E>(Preconditions.<SortedMultiset<E>>checkNotNull(sortedMultiset));
    }
    
    public static <E> Multiset.Entry<E> immutableEntry(@Nullable final E e, final int n) {
        return new ImmutableEntry<E>(e, n);
    }
    
    @Beta
    public static <E> Multiset<E> filter(final Multiset<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredMultiset) {
            final FilteredMultiset<E> filtered = (FilteredMultiset<E>)(FilteredMultiset)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.<E>and(filtered.predicate, predicate);
            return new FilteredMultiset<E>(filtered.unfiltered, combinedPredicate);
        }
        return new FilteredMultiset<E>(unfiltered, predicate);
    }
    
    static int inferDistinctElements(final Iterable<?> elements) {
        if (elements instanceof Multiset) {
            return ((Multiset)elements).elementSet().size();
        }
        return 11;
    }
    
    @Beta
    public static <E> Multiset<E> union(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
        Preconditions.<Multiset<? extends E>>checkNotNull(multiset1);
        Preconditions.<Multiset<? extends E>>checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            @Override
            public boolean contains(@Nullable final Object element) {
                return multiset1.contains(element) || multiset2.contains(element);
            }
            
            @Override
            public boolean isEmpty() {
                return multiset1.isEmpty() && multiset2.isEmpty();
            }
            
            @Override
            public int count(final Object element) {
                return Math.max(multiset1.count(element), multiset2.count(element));
            }
            
            @Override
            Set<E> createElementSet() {
                return Sets.union(multiset1.elementSet(), multiset2.elementSet());
            }
            
            @Override
            Iterator<Multiset.Entry<E>> entryIterator() {
                final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
                final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
                return (Iterator<Multiset.Entry<E>>)new AbstractIterator<Multiset.Entry<E>>() {
                    @Override
                    protected Multiset.Entry<E> computeNext() {
                        if (iterator1.hasNext()) {
                            final Multiset.Entry<? extends E> entry1 = iterator1.next();
                            final E element = (E)entry1.getElement();
                            final int count = Math.max(entry1.getCount(), multiset2.count(element));
                            return Multisets.<E>immutableEntry(element, count);
                        }
                        while (iterator2.hasNext()) {
                            final Multiset.Entry<? extends E> entry2 = iterator2.next();
                            final E element = (E)entry2.getElement();
                            if (!multiset1.contains(element)) {
                                return Multisets.<E>immutableEntry(element, entry2.getCount());
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            int distinctElements() {
                return this.elementSet().size();
            }
        };
    }
    
    public static <E> Multiset<E> intersection(final Multiset<E> multiset1, final Multiset<?> multiset2) {
        Preconditions.<Multiset<E>>checkNotNull(multiset1);
        Preconditions.<Multiset<?>>checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            @Override
            public int count(final Object element) {
                final int count1 = multiset1.count(element);
                return (count1 == 0) ? 0 : Math.min(count1, multiset2.count(element));
            }
            
            @Override
            Set<E> createElementSet() {
                return Sets.intersection(multiset1.elementSet(), multiset2.elementSet());
            }
            
            @Override
            Iterator<Multiset.Entry<E>> entryIterator() {
                final Iterator<Multiset.Entry<E>> iterator1 = (Iterator<Multiset.Entry<E>>)multiset1.entrySet().iterator();
                return (Iterator<Multiset.Entry<E>>)new AbstractIterator<Multiset.Entry<E>>() {
                    @Override
                    protected Multiset.Entry<E> computeNext() {
                        while (iterator1.hasNext()) {
                            final Multiset.Entry<E> entry1 = (Multiset.Entry<E>)iterator1.next();
                            final E element = entry1.getElement();
                            final int count = Math.min(entry1.getCount(), multiset2.count(element));
                            if (count > 0) {
                                return Multisets.<E>immutableEntry(element, count);
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            int distinctElements() {
                return this.elementSet().size();
            }
        };
    }
    
    @Beta
    public static <E> Multiset<E> sum(final Multiset<? extends E> multiset1, final Multiset<? extends E> multiset2) {
        Preconditions.<Multiset<? extends E>>checkNotNull(multiset1);
        Preconditions.<Multiset<? extends E>>checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            @Override
            public boolean contains(@Nullable final Object element) {
                return multiset1.contains(element) || multiset2.contains(element);
            }
            
            @Override
            public boolean isEmpty() {
                return multiset1.isEmpty() && multiset2.isEmpty();
            }
            
            @Override
            public int size() {
                return IntMath.saturatedAdd(multiset1.size(), multiset2.size());
            }
            
            @Override
            public int count(final Object element) {
                return multiset1.count(element) + multiset2.count(element);
            }
            
            @Override
            Set<E> createElementSet() {
                return Sets.union(multiset1.elementSet(), multiset2.elementSet());
            }
            
            @Override
            Iterator<Multiset.Entry<E>> entryIterator() {
                final Iterator<? extends Multiset.Entry<? extends E>> iterator1 = multiset1.entrySet().iterator();
                final Iterator<? extends Multiset.Entry<? extends E>> iterator2 = multiset2.entrySet().iterator();
                return (Iterator<Multiset.Entry<E>>)new AbstractIterator<Multiset.Entry<E>>() {
                    @Override
                    protected Multiset.Entry<E> computeNext() {
                        if (iterator1.hasNext()) {
                            final Multiset.Entry<? extends E> entry1 = iterator1.next();
                            final E element = (E)entry1.getElement();
                            final int count = entry1.getCount() + multiset2.count(element);
                            return Multisets.<E>immutableEntry(element, count);
                        }
                        while (iterator2.hasNext()) {
                            final Multiset.Entry<? extends E> entry2 = iterator2.next();
                            final E element = (E)entry2.getElement();
                            if (!multiset1.contains(element)) {
                                return Multisets.<E>immutableEntry(element, entry2.getCount());
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            int distinctElements() {
                return this.elementSet().size();
            }
        };
    }
    
    @Beta
    public static <E> Multiset<E> difference(final Multiset<E> multiset1, final Multiset<?> multiset2) {
        Preconditions.<Multiset<E>>checkNotNull(multiset1);
        Preconditions.<Multiset<?>>checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            @Override
            public int count(@Nullable final Object element) {
                final int count1 = multiset1.count(element);
                return (count1 == 0) ? 0 : Math.max(0, count1 - multiset2.count(element));
            }
            
            @Override
            Iterator<Multiset.Entry<E>> entryIterator() {
                final Iterator<Multiset.Entry<E>> iterator1 = (Iterator<Multiset.Entry<E>>)multiset1.entrySet().iterator();
                return (Iterator<Multiset.Entry<E>>)new AbstractIterator<Multiset.Entry<E>>() {
                    @Override
                    protected Multiset.Entry<E> computeNext() {
                        while (iterator1.hasNext()) {
                            final Multiset.Entry<E> entry1 = (Multiset.Entry<E>)iterator1.next();
                            final E element = entry1.getElement();
                            final int count = entry1.getCount() - multiset2.count(element);
                            if (count > 0) {
                                return Multisets.<E>immutableEntry(element, count);
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            @Override
            int distinctElements() {
                return Iterators.size(this.entryIterator());
            }
        };
    }
    
    @CanIgnoreReturnValue
    public static boolean containsOccurrences(final Multiset<?> superMultiset, final Multiset<?> subMultiset) {
        Preconditions.<Multiset<?>>checkNotNull(superMultiset);
        Preconditions.<Multiset<?>>checkNotNull(subMultiset);
        for (final Multiset.Entry<?> entry : subMultiset.entrySet()) {
            final int superCount = superMultiset.count(entry.getElement());
            if (superCount < entry.getCount()) {
                return false;
            }
        }
        return true;
    }
    
    @CanIgnoreReturnValue
    public static boolean retainOccurrences(final Multiset<?> multisetToModify, final Multiset<?> multisetToRetain) {
        return Multisets.retainOccurrencesImpl(multisetToModify, multisetToRetain);
    }
    
    private static <E> boolean retainOccurrencesImpl(final Multiset<E> multisetToModify, final Multiset<?> occurrencesToRetain) {
        Preconditions.<Multiset<E>>checkNotNull(multisetToModify);
        Preconditions.<Multiset<?>>checkNotNull(occurrencesToRetain);
        final Iterator<Multiset.Entry<E>> entryIterator = (Iterator<Multiset.Entry<E>>)multisetToModify.entrySet().iterator();
        boolean changed = false;
        while (entryIterator.hasNext()) {
            final Multiset.Entry<E> entry = (Multiset.Entry<E>)entryIterator.next();
            final int retainCount = occurrencesToRetain.count(entry.getElement());
            if (retainCount == 0) {
                entryIterator.remove();
                changed = true;
            }
            else {
                if (retainCount >= entry.getCount()) {
                    continue;
                }
                multisetToModify.setCount(entry.getElement(), retainCount);
                changed = true;
            }
        }
        return changed;
    }
    
    @CanIgnoreReturnValue
    public static boolean removeOccurrences(final Multiset<?> multisetToModify, final Iterable<?> occurrencesToRemove) {
        if (occurrencesToRemove instanceof Multiset) {
            return removeOccurrences(multisetToModify, (Multiset)occurrencesToRemove);
        }
        Preconditions.<Multiset<?>>checkNotNull(multisetToModify);
        Preconditions.<Iterable<?>>checkNotNull(occurrencesToRemove);
        boolean changed = false;
        for (final Object o : occurrencesToRemove) {
            changed |= multisetToModify.remove(o);
        }
        return changed;
    }
    
    @CanIgnoreReturnValue
    public static boolean removeOccurrences(final Multiset<?> multisetToModify, final Multiset<?> occurrencesToRemove) {
        Preconditions.<Multiset<?>>checkNotNull(multisetToModify);
        Preconditions.<Multiset<?>>checkNotNull(occurrencesToRemove);
        boolean changed = false;
        final Iterator<? extends Multiset.Entry<?>> entryIterator = multisetToModify.entrySet().iterator();
        while (entryIterator.hasNext()) {
            final Multiset.Entry<?> entry = entryIterator.next();
            final int removeCount = occurrencesToRemove.count(entry.getElement());
            if (removeCount >= entry.getCount()) {
                entryIterator.remove();
                changed = true;
            }
            else {
                if (removeCount <= 0) {
                    continue;
                }
                multisetToModify.remove(entry.getElement(), removeCount);
                changed = true;
            }
        }
        return changed;
    }
    
    static boolean equalsImpl(final Multiset<?> multiset, @Nullable final Object object) {
        if (object == multiset) {
            return true;
        }
        if (!(object instanceof Multiset)) {
            return false;
        }
        final Multiset<?> that = object;
        if (multiset.size() != that.size() || multiset.entrySet().size() != that.entrySet().size()) {
            return false;
        }
        for (final Multiset.Entry<?> entry : that.entrySet()) {
            if (multiset.count(entry.getElement()) != entry.getCount()) {
                return false;
            }
        }
        return true;
    }
    
    static <E> boolean addAllImpl(final Multiset<E> self, final Collection<? extends E> elements) {
        if (elements.isEmpty()) {
            return false;
        }
        if (elements instanceof Multiset) {
            final Multiset<? extends E> that = Multisets.cast((java.lang.Iterable<? extends E>)elements);
            for (final Multiset.Entry<? extends E> entry : that.entrySet()) {
                self.add((E)entry.getElement(), entry.getCount());
            }
        }
        else {
            Iterators.addAll((java.util.Collection<Object>)self, (java.util.Iterator<?>)elements.iterator());
        }
        return true;
    }
    
    static boolean removeAllImpl(final Multiset<?> self, final Collection<?> elementsToRemove) {
        final Collection<?> collection = (elementsToRemove instanceof Multiset) ? ((Multiset)elementsToRemove).elementSet() : elementsToRemove;
        return self.elementSet().removeAll((Collection)collection);
    }
    
    static boolean retainAllImpl(final Multiset<?> self, final Collection<?> elementsToRetain) {
        Preconditions.<Collection<?>>checkNotNull(elementsToRetain);
        final Collection<?> collection = (elementsToRetain instanceof Multiset) ? ((Multiset)elementsToRetain).elementSet() : elementsToRetain;
        return self.elementSet().retainAll((Collection)collection);
    }
    
    static <E> int setCountImpl(final Multiset<E> self, final E element, final int count) {
        CollectPreconditions.checkNonnegative(count, "count");
        final int oldCount = self.count(element);
        final int delta = count - oldCount;
        if (delta > 0) {
            self.add(element, delta);
        }
        else if (delta < 0) {
            self.remove(element, -delta);
        }
        return oldCount;
    }
    
    static <E> boolean setCountImpl(final Multiset<E> self, final E element, final int oldCount, final int newCount) {
        CollectPreconditions.checkNonnegative(oldCount, "oldCount");
        CollectPreconditions.checkNonnegative(newCount, "newCount");
        if (self.count(element) == oldCount) {
            self.setCount(element, newCount);
            return true;
        }
        return false;
    }
    
    static <E> Iterator<E> iteratorImpl(final Multiset<E> multiset) {
        return (Iterator<E>)new MultisetIteratorImpl((Multiset<Object>)multiset, (java.util.Iterator<Multiset.Entry<Object>>)multiset.entrySet().iterator());
    }
    
    static <E> Spliterator<E> spliteratorImpl(final Multiset<E> multiset) {
        final Spliterator<Multiset.Entry<E>> entrySpliterator = (Spliterator<Multiset.Entry<E>>)multiset.entrySet().spliterator();
        return CollectSpliterators.<Multiset.Entry<E>, E>flatMap(entrySpliterator, (java.util.function.Function<? super Multiset.Entry<E>, java.util.Spliterator<E>>)(entry -> Collections.nCopies(entry.getCount(), entry.getElement()).spliterator()), 0x40 | (entrySpliterator.characteristics() & 0x510), multiset.size());
    }
    
    static int sizeImpl(final Multiset<?> multiset) {
        long size = 0L;
        for (final Multiset.Entry<?> entry : multiset.entrySet()) {
            size += entry.getCount();
        }
        return Ints.saturatedCast(size);
    }
    
    static <T> Multiset<T> cast(final Iterable<T> iterable) {
        return (Multiset<T>)(Multiset)iterable;
    }
    
    @Beta
    public static <E> ImmutableMultiset<E> copyHighestCountFirst(final Multiset<E> multiset) {
        final List<Multiset.Entry<E>> sortedEntries = Multisets.DECREASING_COUNT_ORDERING.immutableSortedCopy((java.lang.Iterable<Object>)multiset.entrySet());
        return ImmutableMultiset.<E>copyFromEntries((java.util.Collection<? extends Multiset.Entry<? extends E>>)sortedEntries);
    }
    
    static {
        DECREASING_COUNT_ORDERING = new Ordering<Multiset.Entry<?>>() {
            @Override
            public int compare(final Multiset.Entry<?> entry1, final Multiset.Entry<?> entry2) {
                return Ints.compare(entry2.getCount(), entry1.getCount());
            }
        };
    }
    
    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableMultiset(final Multiset<? extends E> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        protected Multiset<E> delegate() {
            return (Multiset<E>)this.delegate;
        }
        
        Set<E> createElementSet() {
            return (Set<E>)Collections.unmodifiableSet((Set)this.delegate.elementSet());
        }
        
        @Override
        public Set<E> elementSet() {
            final Set<E> es = this.elementSet;
            return (es == null) ? (this.elementSet = this.createElementSet()) : es;
        }
        
        @Override
        public Set<Multiset.Entry<E>> entrySet() {
            final Set<Multiset.Entry<E>> es = this.entrySet;
            return (es == null) ? (this.entrySet = (Set<Multiset.Entry<E>>)Collections.unmodifiableSet((Set)this.delegate.entrySet())) : es;
        }
        
        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator((java.util.Iterator<?>)this.delegate.iterator());
        }
        
        public boolean add(final E element) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int add(final E element, final int occurences) {
            throw new UnsupportedOperationException();
        }
        
        public boolean addAll(final Collection<? extends E> elementsToAdd) {
            throw new UnsupportedOperationException();
        }
        
        public boolean remove(final Object element) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int remove(final Object element, final int occurrences) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> elementsToRemove) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> elementsToRetain) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int setCount(final E element, final int count) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean setCount(final E element, final int oldCount, final int newCount) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        @Nullable
        private final E element;
        private final int count;
        private static final long serialVersionUID = 0L;
        
        ImmutableEntry(@Nullable final E element, final int count) {
            this.element = element;
            CollectPreconditions.checkNonnegative(this.count = count, "count");
        }
        
        @Nullable
        public final E getElement() {
            return this.element;
        }
        
        public final int getCount() {
            return this.count;
        }
        
        public ImmutableEntry<E> nextInBucket() {
            return null;
        }
    }
    
    private static final class FilteredMultiset<E> extends AbstractMultiset<E> {
        final Multiset<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredMultiset(final Multiset<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = Preconditions.<Multiset<E>>checkNotNull(unfiltered);
            this.predicate = Preconditions.<Predicate<? super E>>checkNotNull(predicate);
        }
        
        public UnmodifiableIterator<E> iterator() {
            return Iterators.<E>filter(this.unfiltered.iterator(), this.predicate);
        }
        
        @Override
        Set<E> createElementSet() {
            return Sets.<E>filter(this.unfiltered.elementSet(), this.predicate);
        }
        
        @Override
        Set<Multiset.Entry<E>> createEntrySet() {
            return Sets.<Multiset.Entry<E>>filter(this.unfiltered.entrySet(), new Predicate<Multiset.Entry<E>>() {
                public boolean apply(final Multiset.Entry<E> entry) {
                    return FilteredMultiset.this.predicate.apply(entry.getElement());
                }
            });
        }
        
        @Override
        Iterator<Multiset.Entry<E>> entryIterator() {
            throw new AssertionError("should never be called");
        }
        
        @Override
        int distinctElements() {
            return this.elementSet().size();
        }
        
        @Override
        public int count(@Nullable final Object element) {
            final int count = this.unfiltered.count(element);
            if (count > 0) {
                final E e = (E)element;
                return this.predicate.apply(e) ? count : 0;
            }
            return 0;
        }
        
        @Override
        public int add(@Nullable final E element, final int occurrences) {
            Preconditions.checkArgument(this.predicate.apply(element), "Element %s does not match predicate %s", element, this.predicate);
            return this.unfiltered.add(element, occurrences);
        }
        
        @Override
        public int remove(@Nullable final Object element, final int occurrences) {
            CollectPreconditions.checkNonnegative(occurrences, "occurrences");
            if (occurrences == 0) {
                return this.count(element);
            }
            return this.contains(element) ? this.unfiltered.remove(element, occurrences) : 0;
        }
        
        @Override
        public void clear() {
            this.elementSet().clear();
        }
    }
    
    abstract static class AbstractEntry<E> implements Multiset.Entry<E> {
        public boolean equals(@Nullable final Object object) {
            if (object instanceof Multiset.Entry) {
                final Multiset.Entry<?> that = object;
                return this.getCount() == that.getCount() && Objects.equal(this.getElement(), that.getElement());
            }
            return false;
        }
        
        public int hashCode() {
            final E e = this.getElement();
            return ((e == null) ? 0 : e.hashCode()) ^ this.getCount();
        }
        
        public String toString() {
            final String text = String.valueOf(this.getElement());
            final int n = this.getCount();
            return (n == 1) ? text : (text + " x " + n);
        }
    }
    
    abstract static class ElementSet<E> extends Sets.ImprovedAbstractSet<E> {
        abstract Multiset<E> multiset();
        
        public void clear() {
            this.multiset().clear();
        }
        
        public boolean contains(final Object o) {
            return this.multiset().contains(o);
        }
        
        public boolean containsAll(final Collection<?> c) {
            return this.multiset().containsAll(c);
        }
        
        public boolean isEmpty() {
            return this.multiset().isEmpty();
        }
        
        public Iterator<E> iterator() {
            return (Iterator<E>)new TransformedIterator<Multiset.Entry<E>, E>(this.multiset().entrySet().iterator()) {
                @Override
                E transform(final Multiset.Entry<E> entry) {
                    return entry.getElement();
                }
            };
        }
        
        public boolean remove(final Object o) {
            return this.multiset().remove(o, Integer.MAX_VALUE) > 0;
        }
        
        public int size() {
            return this.multiset().entrySet().size();
        }
    }
    
    abstract static class EntrySet<E> extends Sets.ImprovedAbstractSet<Multiset.Entry<E>> {
        abstract Multiset<E> multiset();
        
        public boolean contains(@Nullable final Object o) {
            if (!(o instanceof Multiset.Entry)) {
                return false;
            }
            final Multiset.Entry<?> entry = o;
            if (entry.getCount() <= 0) {
                return false;
            }
            final int count = this.multiset().count(entry.getElement());
            return count == entry.getCount();
        }
        
        public boolean remove(final Object object) {
            if (object instanceof Multiset.Entry) {
                final Multiset.Entry<?> entry = object;
                final Object element = entry.getElement();
                final int entryCount = entry.getCount();
                if (entryCount != 0) {
                    final Multiset<Object> multiset = this.multiset();
                    return multiset.setCount(element, entryCount, 0);
                }
            }
            return false;
        }
        
        public void clear() {
            this.multiset().clear();
        }
    }
    
    static final class MultisetIteratorImpl<E> implements Iterator<E> {
        private final Multiset<E> multiset;
        private final Iterator<Multiset.Entry<E>> entryIterator;
        private Multiset.Entry<E> currentEntry;
        private int laterCount;
        private int totalCount;
        private boolean canRemove;
        
        MultisetIteratorImpl(final Multiset<E> multiset, final Iterator<Multiset.Entry<E>> entryIterator) {
            this.multiset = multiset;
            this.entryIterator = entryIterator;
        }
        
        public boolean hasNext() {
            return this.laterCount > 0 || this.entryIterator.hasNext();
        }
        
        public E next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.laterCount == 0) {
                this.currentEntry = (Multiset.Entry<E>)this.entryIterator.next();
                final int count = this.currentEntry.getCount();
                this.laterCount = count;
                this.totalCount = count;
            }
            --this.laterCount;
            this.canRemove = true;
            return this.currentEntry.getElement();
        }
        
        public void remove() {
            CollectPreconditions.checkRemove(this.canRemove);
            if (this.totalCount == 1) {
                this.entryIterator.remove();
            }
            else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            --this.totalCount;
            this.canRemove = false;
        }
    }
}
