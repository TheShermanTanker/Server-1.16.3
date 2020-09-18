package com.google.common.collect;

import java.util.NavigableSet;
import java.util.SortedSet;
import com.google.common.annotations.GwtIncompatible;
import java.util.Comparator;
import java.util.NoSuchElementException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class ContiguousSet<C extends Comparable> extends ImmutableSortedSet<C> {
    final DiscreteDomain<C> domain;
    
    public static <C extends Comparable> ContiguousSet<C> create(final Range<C> range, final DiscreteDomain<C> domain) {
        Preconditions.<Range<C>>checkNotNull(range);
        Preconditions.<DiscreteDomain<C>>checkNotNull(domain);
        Range<C> effectiveRange = range;
        try {
            if (!range.hasLowerBound()) {
                effectiveRange = effectiveRange.intersection(Range.<C>atLeast(domain.minValue()));
            }
            if (!range.hasUpperBound()) {
                effectiveRange = effectiveRange.intersection(Range.<C>atMost(domain.maxValue()));
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException((Throwable)e);
        }
        final boolean empty = effectiveRange.isEmpty() || Range.compareOrThrow(range.lowerBound.leastValueAbove(domain), range.upperBound.greatestValueBelow(domain)) > 0;
        return (ContiguousSet<C>)(empty ? new EmptyContiguousSet<C>((DiscreteDomain<Comparable>)domain) : new RegularContiguousSet<C>((Range<Comparable>)effectiveRange, (DiscreteDomain<Comparable>)domain));
    }
    
    ContiguousSet(final DiscreteDomain<C> domain) {
        super((Comparator)Ordering.<Comparable>natural());
        this.domain = domain;
    }
    
    @Override
    public ContiguousSet<C> headSet(final C toElement) {
        return this.headSetImpl(Preconditions.<C>checkNotNull(toElement), false);
    }
    
    @GwtIncompatible
    @Override
    public ContiguousSet<C> headSet(final C toElement, final boolean inclusive) {
        return this.headSetImpl(Preconditions.<C>checkNotNull(toElement), inclusive);
    }
    
    @Override
    public ContiguousSet<C> subSet(final C fromElement, final C toElement) {
        Preconditions.<C>checkNotNull(fromElement);
        Preconditions.<C>checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator().compare(fromElement, toElement) <= 0);
        return this.subSetImpl(fromElement, true, toElement, false);
    }
    
    @GwtIncompatible
    @Override
    public ContiguousSet<C> subSet(final C fromElement, final boolean fromInclusive, final C toElement, final boolean toInclusive) {
        Preconditions.<C>checkNotNull(fromElement);
        Preconditions.<C>checkNotNull(toElement);
        Preconditions.checkArgument(this.comparator().compare(fromElement, toElement) <= 0);
        return this.subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
    }
    
    @Override
    public ContiguousSet<C> tailSet(final C fromElement) {
        return this.tailSetImpl(Preconditions.<C>checkNotNull(fromElement), true);
    }
    
    @GwtIncompatible
    @Override
    public ContiguousSet<C> tailSet(final C fromElement, final boolean inclusive) {
        return this.tailSetImpl(Preconditions.<C>checkNotNull(fromElement), inclusive);
    }
    
    @Override
    abstract ContiguousSet<C> headSetImpl(final C comparable, final boolean boolean2);
    
    @Override
    abstract ContiguousSet<C> subSetImpl(final C comparable1, final boolean boolean2, final C comparable3, final boolean boolean4);
    
    @Override
    abstract ContiguousSet<C> tailSetImpl(final C comparable, final boolean boolean2);
    
    public abstract ContiguousSet<C> intersection(final ContiguousSet<C> contiguousSet);
    
    public abstract Range<C> range();
    
    public abstract Range<C> range(final BoundType boundType1, final BoundType boundType2);
    
    public String toString() {
        return this.range().toString();
    }
    
    @Deprecated
    public static <E> Builder<E> builder() {
        throw new UnsupportedOperationException();
    }
}
