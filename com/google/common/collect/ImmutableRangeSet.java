package com.google.common.collect;

import java.util.Collections;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.primitives.Ints;
import java.util.Set;
import javax.annotation.Nullable;
import java.util.NoSuchElementException;
import java.util.Comparator;
import com.google.common.base.Function;
import java.util.List;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@GwtIncompatible
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
    private static final ImmutableRangeSet<Comparable<?>> EMPTY;
    private static final ImmutableRangeSet<Comparable<?>> ALL;
    private final transient ImmutableList<Range<C>> ranges;
    @LazyInit
    private transient ImmutableRangeSet<C> complement;
    
    public static <C extends Comparable> ImmutableRangeSet<C> of() {
        return (ImmutableRangeSet<C>)ImmutableRangeSet.EMPTY;
    }
    
    static <C extends Comparable> ImmutableRangeSet<C> all() {
        return (ImmutableRangeSet<C>)ImmutableRangeSet.ALL;
    }
    
    public static <C extends Comparable> ImmutableRangeSet<C> of(final Range<C> range) {
        Preconditions.<Range<C>>checkNotNull(range);
        if (range.isEmpty()) {
            return ImmutableRangeSet.<C>of();
        }
        if (range.equals(Range.<Comparable>all())) {
            return ImmutableRangeSet.<C>all();
        }
        return new ImmutableRangeSet<C>(ImmutableList.of((Range<C>)range));
    }
    
    public static <C extends Comparable> ImmutableRangeSet<C> copyOf(final RangeSet<C> rangeSet) {
        Preconditions.<RangeSet<C>>checkNotNull(rangeSet);
        if (rangeSet.isEmpty()) {
            return ImmutableRangeSet.<C>of();
        }
        if (rangeSet.encloses(Range.<C>all())) {
            return ImmutableRangeSet.<C>all();
        }
        if (rangeSet instanceof ImmutableRangeSet) {
            final ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet<C>)(ImmutableRangeSet)rangeSet;
            if (!immutableRangeSet.isPartialView()) {
                return immutableRangeSet;
            }
        }
        return new ImmutableRangeSet<C>(ImmutableList.<Range<C>>copyOf((java.util.Collection<? extends Range<C>>)rangeSet.asRanges()));
    }
    
    public static <C extends Comparable<?>> ImmutableRangeSet<C> unionOf(final Iterable<Range<C>> ranges) {
        return ImmutableRangeSet.<C>copyOf(TreeRangeSet.<C>create(ranges));
    }
    
    public static <C extends Comparable<?>> ImmutableRangeSet<C> copyOf(final Iterable<Range<C>> ranges) {
        return new Builder<C>().addAll(ranges).build();
    }
    
    ImmutableRangeSet(final ImmutableList<Range<C>> ranges) {
        this.ranges = ranges;
    }
    
    private ImmutableRangeSet(final ImmutableList<Range<C>> ranges, final ImmutableRangeSet<C> complement) {
        this.ranges = ranges;
        this.complement = complement;
    }
    
    @Override
    public boolean intersects(final Range<C> otherRange) {
        final int ceilingIndex = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), (Cut<Comparable>)otherRange.lowerBound, (java.util.Comparator<? super Cut<Comparable>>)Ordering.<Comparable>natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        return (ceilingIndex < this.ranges.size() && ((Range)this.ranges.get(ceilingIndex)).isConnected(otherRange) && !((Range)this.ranges.get(ceilingIndex)).intersection(otherRange).isEmpty()) || (ceilingIndex > 0 && ((Range)this.ranges.get(ceilingIndex - 1)).isConnected(otherRange) && !((Range)this.ranges.get(ceilingIndex - 1)).intersection(otherRange).isEmpty());
    }
    
    @Override
    public boolean encloses(final Range<C> otherRange) {
        final int index = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), (Cut<Comparable>)otherRange.lowerBound, (java.util.Comparator<? super Cut<Comparable>>)Ordering.<Comparable>natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        return index != -1 && ((Range)this.ranges.get(index)).encloses(otherRange);
    }
    
    @Override
    public Range<C> rangeContaining(final C value) {
        final int index = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), Cut.belowValue(value), (java.util.Comparator<? super Cut<Comparable>>)Ordering.<Comparable>natural(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index != -1) {
            final Range<C> range = (Range<C>)this.ranges.get(index);
            return range.contains(value) ? range : null;
        }
        return null;
    }
    
    public Range<C> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Range.<C>create((Cut<C>)((Range)this.ranges.get(0)).lowerBound, (Cut<C>)((Range)this.ranges.get(this.ranges.size() - 1)).upperBound);
    }
    
    @Override
    public boolean isEmpty() {
        return this.ranges.isEmpty();
    }
    
    @Deprecated
    @Override
    public void add(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void addAll(final RangeSet<C> other) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void addAll(final Iterable<Range<C>> other) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void remove(final Range<C> range) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    @Override
    public void removeAll(final RangeSet<C> other) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void removeAll(final Iterable<Range<C>> other) {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableSet<Range<C>> asRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableSet.<Range<C>>of();
        }
        return new RegularImmutableSortedSet<Range<C>>(this.ranges, (java.util.Comparator<? super Range<C>>)Range.RANGE_LEX_ORDERING);
    }
    
    public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableSet.<Range<C>>of();
        }
        return new RegularImmutableSortedSet<Range<C>>(this.ranges.reverse(), (java.util.Comparator<? super Range<C>>)Range.RANGE_LEX_ORDERING.reverse());
    }
    
    public ImmutableRangeSet<C> complement() {
        ImmutableRangeSet<C> result = this.complement;
        if (result != null) {
            return result;
        }
        if (this.ranges.isEmpty()) {
            return this.complement = ImmutableRangeSet.<C>all();
        }
        if (this.ranges.size() == 1 && ((Range)this.ranges.get(0)).equals(Range.<Comparable>all())) {
            return this.complement = ImmutableRangeSet.<C>of();
        }
        final ImmutableList<Range<C>> complementRanges = new ComplementRanges();
        final ImmutableRangeSet complement = new ImmutableRangeSet((ImmutableList<Range<Comparable>>)complementRanges, (ImmutableRangeSet<Comparable>)this);
        this.complement = complement;
        result = complement;
        return result;
    }
    
    public ImmutableRangeSet<C> union(final RangeSet<C> other) {
        return ImmutableRangeSet.<C>unionOf(Iterables.<Range<C>>concat((java.lang.Iterable<? extends Range<C>>)this.asRanges(), (java.lang.Iterable<? extends Range<C>>)other.asRanges()));
    }
    
    public ImmutableRangeSet<C> intersection(final RangeSet<C> other) {
        final RangeSet<C> copy = TreeRangeSet.create((RangeSet<Comparable>)this);
        copy.removeAll(other.complement());
        return ImmutableRangeSet.<C>copyOf(copy);
    }
    
    public ImmutableRangeSet<C> difference(final RangeSet<C> other) {
        final RangeSet<C> copy = TreeRangeSet.create((RangeSet<Comparable>)this);
        copy.removeAll(other);
        return ImmutableRangeSet.<C>copyOf(copy);
    }
    
    private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
        if (this.ranges.isEmpty() || range.isEmpty()) {
            return ImmutableList.<Range<C>>of();
        }
        if (range.encloses(this.span())) {
            return this.ranges;
        }
        int fromIndex;
        if (range.hasLowerBound()) {
            fromIndex = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>upperBoundFn(), (Cut<Comparable>)range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        }
        else {
            fromIndex = 0;
        }
        int toIndex;
        if (range.hasUpperBound()) {
            toIndex = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), (Cut<Comparable>)range.upperBound, SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        }
        else {
            toIndex = this.ranges.size();
        }
        final int length = toIndex - fromIndex;
        if (length == 0) {
            return ImmutableList.<Range<C>>of();
        }
        return new ImmutableList<Range<C>>() {
            public int size() {
                return length;
            }
            
            public Range<C> get(final int index) {
                Preconditions.checkElementIndex(index, length);
                if (index == 0 || index == length - 1) {
                    return ((Range)ImmutableRangeSet.this.ranges.get(index + fromIndex)).intersection(range);
                }
                return (Range<C>)ImmutableRangeSet.this.ranges.get(index + fromIndex);
            }
            
            @Override
            boolean isPartialView() {
                return true;
            }
        };
    }
    
    public ImmutableRangeSet<C> subRangeSet(final Range<C> range) {
        if (!this.isEmpty()) {
            final Range<C> span = this.span();
            if (range.encloses(span)) {
                return this;
            }
            if (range.isConnected(span)) {
                return new ImmutableRangeSet<C>(this.intersectRanges(range));
            }
        }
        return ImmutableRangeSet.<C>of();
    }
    
    public ImmutableSortedSet<C> asSet(final DiscreteDomain<C> domain) {
        Preconditions.<DiscreteDomain<C>>checkNotNull(domain);
        if (this.isEmpty()) {
            return ImmutableSortedSet.<C>of();
        }
        final Range<C> span = this.span().canonical(domain);
        if (!span.hasLowerBound()) {
            throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
        }
        if (!span.hasUpperBound()) {
            try {
                domain.maxValue();
            }
            catch (NoSuchElementException e) {
                throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
            }
        }
        return new AsSet(domain);
    }
    
    boolean isPartialView() {
        return this.ranges.isPartialView();
    }
    
    public static <C extends Comparable<?>> Builder<C> builder() {
        return new Builder<C>();
    }
    
    Object writeReplace() {
        return new SerializedForm((ImmutableList<Range<Comparable>>)this.ranges);
    }
    
    static {
        EMPTY = new ImmutableRangeSet<Comparable<?>>(ImmutableList.<Range<Comparable<?>>>of());
        ALL = new ImmutableRangeSet<Comparable<?>>(ImmutableList.of(Range.<C>all()));
    }
    
    private final class ComplementRanges extends ImmutableList<Range<C>> {
        private final boolean positiveBoundedBelow;
        private final boolean positiveBoundedAbove;
        private final int size;
        
        ComplementRanges() {
            this.positiveBoundedBelow = ((Range)ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
            this.positiveBoundedAbove = Iterables.<Range>getLast((java.lang.Iterable<Range>)ImmutableRangeSet.this.ranges).hasUpperBound();
            int size = ImmutableRangeSet.this.ranges.size() - 1;
            if (this.positiveBoundedBelow) {
                ++size;
            }
            if (this.positiveBoundedAbove) {
                ++size;
            }
            this.size = size;
        }
        
        public int size() {
            return this.size;
        }
        
        public Range<C> get(final int index) {
            Preconditions.checkElementIndex(index, this.size);
            Cut<C> lowerBound;
            if (this.positiveBoundedBelow) {
                lowerBound = (Cut<C>)((index == 0) ? Cut.<C>belowAll() : ((Range)ImmutableRangeSet.this.ranges.get(index - 1)).upperBound);
            }
            else {
                lowerBound = (Cut<C>)((Range)ImmutableRangeSet.this.ranges.get(index)).upperBound;
            }
            Cut<C> upperBound;
            if (this.positiveBoundedAbove && index == this.size - 1) {
                upperBound = Cut.<C>aboveAll();
            }
            else {
                upperBound = (Cut<C>)((Range)ImmutableRangeSet.this.ranges.get(index + (this.positiveBoundedBelow ? 0 : 1))).lowerBound;
            }
            return Range.<C>create(lowerBound, upperBound);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
    
    private final class AsSet extends ImmutableSortedSet<C> {
        private final DiscreteDomain<C> domain;
        private transient Integer size;
        
        AsSet(final DiscreteDomain<C> domain) {
            super((Comparator)Ordering.<Comparable>natural());
            this.domain = domain;
        }
        
        public int size() {
            Integer result = this.size;
            if (result == null) {
                long total = 0L;
                for (final Range<C> range : ImmutableRangeSet.this.ranges) {
                    total += ContiguousSet.<C>create(range, this.domain).size();
                    if (total >= 2147483647L) {
                        break;
                    }
                }
                final Integer value = Ints.saturatedCast(total);
                this.size = value;
                result = value;
            }
            return result;
        }
        
        @Override
        public UnmodifiableIterator<C> iterator() {
            return new AbstractIterator<C>() {
                final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();
                Iterator<C> elemItr = Iterators.emptyIterator();
                
                @Override
                protected C computeNext() {
                    while (!this.elemItr.hasNext()) {
                        if (!this.rangeItr.hasNext()) {
                            return this.endOfData();
                        }
                        this.elemItr = (Iterator<C>)ContiguousSet.<Comparable>create((Range<Comparable>)this.rangeItr.next(), AsSet.this.domain).iterator();
                    }
                    return (C)this.elemItr.next();
                }
            };
        }
        
        @GwtIncompatible("NavigableSet")
        @Override
        public UnmodifiableIterator<C> descendingIterator() {
            return new AbstractIterator<C>() {
                final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();
                Iterator<C> elemItr = Iterators.emptyIterator();
                
                @Override
                protected C computeNext() {
                    while (!this.elemItr.hasNext()) {
                        if (!this.rangeItr.hasNext()) {
                            return this.endOfData();
                        }
                        this.elemItr = (Iterator<C>)ContiguousSet.<Comparable>create((Range<Comparable>)this.rangeItr.next(), AsSet.this.domain).descendingIterator();
                    }
                    return (C)this.elemItr.next();
                }
            };
        }
        
        ImmutableSortedSet<C> subSet(final Range<C> range) {
            return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
        }
        
        @Override
        ImmutableSortedSet<C> headSetImpl(final C toElement, final boolean inclusive) {
            return this.subSet(Range.<C>upTo(toElement, BoundType.forBoolean(inclusive)));
        }
        
        @Override
        ImmutableSortedSet<C> subSetImpl(final C fromElement, final boolean fromInclusive, final C toElement, final boolean toInclusive) {
            if (!fromInclusive && !toInclusive && Range.compareOrThrow(fromElement, toElement) == 0) {
                return ImmutableSortedSet.<C>of();
            }
            return this.subSet(Range.<C>range(fromElement, BoundType.forBoolean(fromInclusive), toElement, BoundType.forBoolean(toInclusive)));
        }
        
        @Override
        ImmutableSortedSet<C> tailSetImpl(final C fromElement, final boolean inclusive) {
            return this.subSet(Range.<C>downTo(fromElement, BoundType.forBoolean(inclusive)));
        }
        
        public boolean contains(@Nullable final Object o) {
            if (o == null) {
                return false;
            }
            try {
                final C c = (C)o;
                return ImmutableRangeSet.this.contains(c);
            }
            catch (ClassCastException e) {
                return false;
            }
        }
        
        @Override
        int indexOf(final Object target) {
            if (this.contains(target)) {
                final C c = (C)target;
                long total = 0L;
                for (final Range<C> range : ImmutableRangeSet.this.ranges) {
                    if (range.contains(c)) {
                        return Ints.saturatedCast(total + ContiguousSet.<C>create(range, this.domain).indexOf(c));
                    }
                    total += ContiguousSet.<C>create(range, this.domain).size();
                }
                throw new AssertionError("impossible");
            }
            return -1;
        }
        
        boolean isPartialView() {
            return ImmutableRangeSet.this.ranges.isPartialView();
        }
        
        public String toString() {
            return ImmutableRangeSet.this.ranges.toString();
        }
        
        @Override
        Object writeReplace() {
            return new AsSetSerializedForm(ImmutableRangeSet.this.ranges, (DiscreteDomain<Comparable>)this.domain);
        }
    }
    
    private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
        private final ImmutableList<Range<C>> ranges;
        private final DiscreteDomain<C> domain;
        
        AsSetSerializedForm(final ImmutableList<Range<C>> ranges, final DiscreteDomain<C> domain) {
            this.ranges = ranges;
            this.domain = domain;
        }
        
        Object readResolve() {
            return new ImmutableRangeSet<C>(this.ranges).asSet(this.domain);
        }
    }
    
    public static class Builder<C extends Comparable<?>> {
        private final List<Range<C>> ranges;
        
        public Builder() {
            this.ranges = Lists.newArrayList();
        }
        
        @CanIgnoreReturnValue
        public Builder<C> add(final Range<C> range) {
            Preconditions.checkArgument(!range.isEmpty(), "range must not be empty, but was %s", range);
            this.ranges.add(range);
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<C> addAll(final RangeSet<C> ranges) {
            return this.addAll((Iterable<Range<C>>)ranges.asRanges());
        }
        
        @CanIgnoreReturnValue
        public Builder<C> addAll(final Iterable<Range<C>> ranges) {
            for (final Range<C> range : ranges) {
                this.add(range);
            }
            return this;
        }
        
        public ImmutableRangeSet<C> build() {
            final ImmutableList.Builder<Range<C>> mergedRangesBuilder = new ImmutableList.Builder<Range<C>>(this.ranges.size());
            Collections.sort((List)this.ranges, (Comparator)Range.RANGE_LEX_ORDERING);
            final PeekingIterator<Range<C>> peekingItr = Iterators.<Range<C>>peekingIterator((java.util.Iterator<? extends Range<C>>)this.ranges.iterator());
            while (peekingItr.hasNext()) {
                Range<C> range = peekingItr.next();
                while (peekingItr.hasNext()) {
                    final Range<C> nextRange = peekingItr.peek();
                    if (!range.isConnected(nextRange)) {
                        break;
                    }
                    Preconditions.checkArgument(range.intersection(nextRange).isEmpty(), "Overlapping ranges not permitted but found %s overlapping %s", range, nextRange);
                    range = range.span(peekingItr.next());
                }
                mergedRangesBuilder.add(range);
            }
            final ImmutableList<Range<C>> mergedRanges = mergedRangesBuilder.build();
            if (mergedRanges.isEmpty()) {
                return ImmutableRangeSet.<C>of();
            }
            if (mergedRanges.size() == 1 && Iterables.<Range>getOnlyElement((java.lang.Iterable<Range>)mergedRanges).equals(Range.<Comparable>all())) {
                return ImmutableRangeSet.<C>all();
            }
            return new ImmutableRangeSet<C>(mergedRanges);
        }
    }
    
    private static final class SerializedForm<C extends Comparable> implements Serializable {
        private final ImmutableList<Range<C>> ranges;
        
        SerializedForm(final ImmutableList<Range<C>> ranges) {
            this.ranges = ranges;
        }
        
        Object readResolve() {
            if (this.ranges.isEmpty()) {
                return ImmutableRangeSet.<Comparable>of();
            }
            if (this.ranges.equals(ImmutableList.<Range<Comparable>>of(Range.<Comparable>all()))) {
                return ImmutableRangeSet.<Comparable>all();
            }
            return new ImmutableRangeSet((ImmutableList<Range<Comparable>>)this.ranges);
        }
    }
}
