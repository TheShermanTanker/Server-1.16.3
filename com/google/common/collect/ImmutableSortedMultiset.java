package com.google.common.collect;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.function.ToIntFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.Comparator;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
    @LazyInit
    transient ImmutableSortedMultiset<E> descendingMultiset;
    
    @Beta
    public static <E> Collector<E, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(final Comparator<? super E> comparator) {
        return ImmutableSortedMultiset.<E, E>toImmutableSortedMultiset(comparator, (java.util.function.Function<? super E, ? extends E>)Function.identity(), (java.util.function.ToIntFunction<? super E>)(e -> 1));
    }
    
    private static <T, E> Collector<T, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(final Comparator<? super E> comparator, final Function<? super T, ? extends E> elementFunction, final ToIntFunction<? super T> countFunction) {
        Preconditions.<Comparator<? super E>>checkNotNull(comparator);
        Preconditions.<Function<? super T, ? extends E>>checkNotNull(elementFunction);
        Preconditions.<ToIntFunction<? super T>>checkNotNull(countFunction);
        return Collector.of(() -> TreeMultiset.create((java.util.Comparator<? super Object>)comparator), (multiset, t) -> multiset.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (multiset1, multiset2) -> {
            multiset1.addAll((Collection)multiset2);
            return multiset1;
        }, multiset -> ImmutableSortedMultiset.copyOfSortedEntries((java.util.Comparator<? super Object>)comparator, (java.util.Collection<Multiset.Entry<Object>>)multiset.entrySet()), new Collector.Characteristics[0]);
    }
    
    public static <E> ImmutableSortedMultiset<E> of() {
        return (ImmutableSortedMultiset<E>)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E element) {
        final RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)(RegularImmutableSortedSet)ImmutableSortedSet.<E>of(element);
        final long[] cumulativeCounts = { 0L, 1L };
        return new RegularImmutableSortedMultiset<E>(elementSet, cumulativeCounts, 0, 1);
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2) {
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])new Comparable[] { e1, e2 }));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3) {
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])new Comparable[] { e1, e2, e3 }));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4) {
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])new Comparable[] { e1, e2, e3, e4 }));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5) {
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])new Comparable[] { e1, e2, e3, e4, e5 }));
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(final E e1, final E e2, final E e3, final E e4, final E e5, final E e6, final E... remaining) {
        final int size = remaining.length + 6;
        final List<E> all = Lists.newArrayListWithCapacity(size);
        Collections.addAll((Collection)all, (Object[])new Comparable[] { e1, e2, e3, e4, e5, e6 });
        Collections.addAll((Collection)all, (Object[])remaining);
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)all);
    }
    
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(final E[] elements) {
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)Ordering.<Comparable>natural(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])elements));
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Iterable<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.<E>natural();
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Iterator<? extends E> elements) {
        final Ordering<E> naturalOrder = Ordering.<E>natural();
        return ImmutableSortedMultiset.<E>copyOf((java.util.Comparator<? super E>)naturalOrder, elements);
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Comparator<? super E> comparator, final Iterator<? extends E> elements) {
        Preconditions.<Comparator<? super E>>checkNotNull(comparator);
        return new Builder<E>(comparator).addAll(elements).build();
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOf(final Comparator<? super E> comparator, Iterable<? extends E> elements) {
        if (elements instanceof ImmutableSortedMultiset) {
            final ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset<E>)(ImmutableSortedMultiset)elements;
            if (comparator.equals(multiset.comparator())) {
                if (multiset.isPartialView()) {
                    return ImmutableSortedMultiset.<E>copyOfSortedEntries(comparator, (java.util.Collection<Multiset.Entry<E>>)multiset.entrySet().asList());
                }
                return multiset;
            }
        }
        elements = Lists.newArrayList((java.lang.Iterable<?>)elements);
        final TreeMultiset<E> sortedCopy = TreeMultiset.<E>create(Preconditions.<Comparator<? super E>>checkNotNull(comparator));
        Iterables.addAll((java.util.Collection<Object>)sortedCopy, elements);
        return ImmutableSortedMultiset.<E>copyOfSortedEntries(comparator, (java.util.Collection<Multiset.Entry<E>>)sortedCopy.entrySet());
    }
    
    public static <E> ImmutableSortedMultiset<E> copyOfSorted(final SortedMultiset<E> sortedMultiset) {
        return ImmutableSortedMultiset.<E>copyOfSortedEntries(sortedMultiset.comparator(), (java.util.Collection<Multiset.Entry<E>>)Lists.newArrayList((java.lang.Iterable<?>)sortedMultiset.entrySet()));
    }
    
    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(final Comparator<? super E> comparator, final Collection<Multiset.Entry<E>> entries) {
        if (entries.isEmpty()) {
            return ImmutableSortedMultiset.<E>emptyMultiset(comparator);
        }
        final ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<E>(entries.size());
        final long[] cumulativeCounts = new long[entries.size() + 1];
        int i = 0;
        for (final Multiset.Entry<E> entry : entries) {
            elementsBuilder.add(entry.getElement());
            cumulativeCounts[i + 1] = cumulativeCounts[i] + entry.getCount();
            ++i;
        }
        return new RegularImmutableSortedMultiset<E>(new RegularImmutableSortedSet<E>(elementsBuilder.build(), comparator), cumulativeCounts, 0, entries.size());
    }
    
    static <E> ImmutableSortedMultiset<E> emptyMultiset(final Comparator<? super E> comparator) {
        if (Ordering.<Comparable>natural().equals(comparator)) {
            return (ImmutableSortedMultiset<E>)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
        }
        return new RegularImmutableSortedMultiset<E>(comparator);
    }
    
    ImmutableSortedMultiset() {
    }
    
    @Override
    public final Comparator<? super E> comparator() {
        return this.elementSet().comparator();
    }
    
    @Override
    public abstract ImmutableSortedSet<E> elementSet();
    
    @Override
    public ImmutableSortedMultiset<E> descendingMultiset() {
        final ImmutableSortedMultiset<E> result = this.descendingMultiset;
        if (result == null) {
            return this.descendingMultiset = (this.isEmpty() ? ImmutableSortedMultiset.<E>emptyMultiset((java.util.Comparator<? super E>)Ordering.from(this.comparator()).reverse()) : new DescendingImmutableSortedMultiset<E>(this));
        }
        return result;
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public final Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public abstract ImmutableSortedMultiset<E> headMultiset(final E object, final BoundType boundType);
    
    @Override
    public ImmutableSortedMultiset<E> subMultiset(final E lowerBound, final BoundType lowerBoundType, final E upperBound, final BoundType upperBoundType) {
        Preconditions.checkArgument(this.comparator().compare(lowerBound, upperBound) <= 0, "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
        return this.tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
    }
    
    @Override
    public abstract ImmutableSortedMultiset<E> tailMultiset(final E object, final BoundType boundType);
    
    public static <E> Builder<E> orderedBy(final Comparator<E> comparator) {
        return new Builder<E>(comparator);
    }
    
    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder<E>((java.util.Comparator<? super E>)Ordering.<Comparable>natural().reverse());
    }
    
    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder<E>((java.util.Comparator<? super E>)Ordering.<Comparable>natural());
    }
    
    @Override
    Object writeReplace() {
        return new SerializedForm((SortedMultiset<Object>)this);
    }
    
    public static class Builder<E> extends ImmutableMultiset.Builder<E> {
        public Builder(final Comparator<? super E> comparator) {
            super(TreeMultiset.create(Preconditions.<java.util.Comparator<? super Object>>checkNotNull(comparator)));
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E element) {
            super.add(element);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addCopies(final E element, final int occurrences) {
            super.addCopies(element, occurrences);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> setCount(final E element, final int count) {
            super.setCount(element, count);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> add(final E... elements) {
            super.add(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @CanIgnoreReturnValue
        @Override
        public Builder<E> addAll(final Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }
        
        @Override
        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.<E>copyOfSorted((SortedMultiset<E>)(SortedMultiset)this.contents);
        }
    }
    
    private static final class SerializedForm<E> implements Serializable {
        final Comparator<? super E> comparator;
        final E[] elements;
        final int[] counts;
        
        SerializedForm(final SortedMultiset<E> multiset) {
            this.comparator = multiset.comparator();
            final int n = multiset.entrySet().size();
            this.elements = (E[])new Object[n];
            this.counts = new int[n];
            int i = 0;
            for (final Multiset.Entry<E> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                ++i;
            }
        }
        
        Object readResolve() {
            final int n = this.elements.length;
            final Builder<E> builder = new Builder<E>(this.comparator);
            for (int i = 0; i < n; ++i) {
                builder.addCopies(this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }
}
