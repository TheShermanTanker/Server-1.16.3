package com.google.common.collect;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import com.google.common.base.Predicate;

@GwtCompatible
public final class Range<C extends Comparable> implements Predicate<C>, Serializable {
    private static final Function<Range, Cut> LOWER_BOUND_FN;
    private static final Function<Range, Cut> UPPER_BOUND_FN;
    static final Ordering<Range<?>> RANGE_LEX_ORDERING;
    private static final Range<Comparable> ALL;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;
    private static final long serialVersionUID = 0L;
    
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn() {
        return (Function<Range<C>, Cut<C>>)Range.LOWER_BOUND_FN;
    }
    
    static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn() {
        return (Function<Range<C>, Cut<C>>)Range.UPPER_BOUND_FN;
    }
    
    static <C extends Comparable<?>> Range<C> create(final Cut<C> lowerBound, final Cut<C> upperBound) {
        return new Range<C>(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> open(final C lower, final C upper) {
        return Range.<C>create(Cut.aboveValue((C)lower), Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closed(final C lower, final C upper) {
        return Range.<C>create(Cut.belowValue((C)lower), Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> closedOpen(final C lower, final C upper) {
        return Range.<C>create(Cut.belowValue((C)lower), Cut.belowValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> openClosed(final C lower, final C upper) {
        return Range.<C>create(Cut.aboveValue((C)lower), Cut.aboveValue((C)upper));
    }
    
    public static <C extends Comparable<?>> Range<C> range(final C lower, final BoundType lowerType, final C upper, final BoundType upperType) {
        Preconditions.<BoundType>checkNotNull(lowerType);
        Preconditions.<BoundType>checkNotNull(upperType);
        final Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? Cut.<C>aboveValue(lower) : Cut.<C>belowValue(lower);
        final Cut<C> upperBound = (upperType == BoundType.OPEN) ? Cut.<C>belowValue(upper) : Cut.<C>aboveValue(upper);
        return Range.<C>create(lowerBound, upperBound);
    }
    
    public static <C extends Comparable<?>> Range<C> lessThan(final C endpoint) {
        return Range.<C>create(Cut.<C>belowAll(), Cut.belowValue((C)endpoint));
    }
    
    public static <C extends Comparable<?>> Range<C> atMost(final C endpoint) {
        return Range.<C>create(Cut.<C>belowAll(), Cut.aboveValue((C)endpoint));
    }
    
    public static <C extends Comparable<?>> Range<C> upTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return Range.<C>lessThan(endpoint);
            }
            case CLOSED: {
                return Range.<C>atMost(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> greaterThan(final C endpoint) {
        return Range.<C>create(Cut.aboveValue((C)endpoint), Cut.<C>aboveAll());
    }
    
    public static <C extends Comparable<?>> Range<C> atLeast(final C endpoint) {
        return Range.<C>create(Cut.belowValue((C)endpoint), Cut.<C>aboveAll());
    }
    
    public static <C extends Comparable<?>> Range<C> downTo(final C endpoint, final BoundType boundType) {
        switch (boundType) {
            case OPEN: {
                return Range.<C>greaterThan(endpoint);
            }
            case CLOSED: {
                return Range.<C>atLeast(endpoint);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    public static <C extends Comparable<?>> Range<C> all() {
        return (Range<C>)Range.ALL;
    }
    
    public static <C extends Comparable<?>> Range<C> singleton(final C value) {
        return Range.<C>closed(value, value);
    }
    
    public static <C extends Comparable<?>> Range<C> encloseAll(final Iterable<C> values) {
        Preconditions.<Iterable<C>>checkNotNull(values);
        if (values instanceof ContiguousSet) {
            return ((ContiguousSet)values).range();
        }
        final Iterator<C> valueIterator = (Iterator<C>)values.iterator();
        C max;
        C min = max = Preconditions.<C>checkNotNull(valueIterator.next());
        while (valueIterator.hasNext()) {
            final C value = Preconditions.<C>checkNotNull(valueIterator.next());
            min = Ordering.<Comparable>natural().<C>min(min, value);
            max = Ordering.<Comparable>natural().<C>max(max, value);
        }
        return Range.<C>closed(min, max);
    }
    
    private Range(final Cut<C> lowerBound, final Cut<C> upperBound) {
        this.lowerBound = Preconditions.<Cut<C>>checkNotNull(lowerBound);
        this.upperBound = Preconditions.<Cut<C>>checkNotNull(upperBound);
        if (lowerBound.compareTo(upperBound) > 0 || lowerBound == Cut.<C>aboveAll() || upperBound == Cut.<C>belowAll()) {
            throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
        }
    }
    
    public boolean hasLowerBound() {
        return this.lowerBound != Cut.<C>belowAll();
    }
    
    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }
    
    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }
    
    public boolean hasUpperBound() {
        return this.upperBound != Cut.<C>aboveAll();
    }
    
    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }
    
    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }
    
    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }
    
    public boolean contains(final C value) {
        Preconditions.<C>checkNotNull(value);
        return this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value);
    }
    
    @Deprecated
    public boolean apply(final C input) {
        return this.contains(input);
    }
    
    public boolean containsAll(final Iterable<? extends C> values) {
        if (Iterables.isEmpty(values)) {
            return true;
        }
        if (values instanceof SortedSet) {
            final SortedSet<? extends C> set = Range.cast(values);
            final Comparator<?> comparator = set.comparator();
            if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
                return this.contains((Comparable)set.first()) && this.contains((Comparable)set.last());
            }
        }
        for (final C value : values) {
            if (!this.contains(value)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean encloses(final Range<C> other) {
        return this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0;
    }
    
    public boolean isConnected(final Range<C> other) {
        return this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0;
    }
    
    public Range<C> intersection(final Range<C> connectedRange) {
        final int lowerCmp = this.lowerBound.compareTo(connectedRange.lowerBound);
        final int upperCmp = this.upperBound.compareTo(connectedRange.upperBound);
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return this;
        }
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return connectedRange;
        }
        final Cut<C> newLower = (lowerCmp >= 0) ? this.lowerBound : connectedRange.lowerBound;
        final Cut<C> newUpper = (upperCmp <= 0) ? this.upperBound : connectedRange.upperBound;
        return Range.<C>create(newLower, newUpper);
    }
    
    public Range<C> span(final Range<C> other) {
        final int lowerCmp = this.lowerBound.compareTo(other.lowerBound);
        final int upperCmp = this.upperBound.compareTo(other.upperBound);
        if (lowerCmp <= 0 && upperCmp >= 0) {
            return this;
        }
        if (lowerCmp >= 0 && upperCmp <= 0) {
            return other;
        }
        final Cut<C> newLower = (lowerCmp <= 0) ? this.lowerBound : other.lowerBound;
        final Cut<C> newUpper = (upperCmp >= 0) ? this.upperBound : other.upperBound;
        return Range.<C>create(newLower, newUpper);
    }
    
    public Range<C> canonical(final DiscreteDomain<C> domain) {
        Preconditions.<DiscreteDomain<C>>checkNotNull(domain);
        final Cut<C> lower = this.lowerBound.canonical(domain);
        final Cut<C> upper = this.upperBound.canonical(domain);
        return (lower == this.lowerBound && upper == this.upperBound) ? this : Range.<C>create(lower, upper);
    }
    
    public boolean equals(@Nullable final Object object) {
        if (object instanceof Range) {
            final Range<?> other = object;
            return this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound);
        }
        return false;
    }
    
    public int hashCode() {
        return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
    }
    
    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }
    
    private static String toString(final Cut<?> lowerBound, final Cut<?> upperBound) {
        final StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append("..");
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }
    
    private static <T> SortedSet<T> cast(final Iterable<T> iterable) {
        return (SortedSet<T>)iterable;
    }
    
    Object readResolve() {
        if (this.equals(Range.ALL)) {
            return Range.<Comparable>all();
        }
        return this;
    }
    
    static int compareOrThrow(final Comparable left, final Comparable right) {
        return left.compareTo(right);
    }
    
    static {
        LOWER_BOUND_FN = new Function<Range, Cut>() {
            public Cut apply(final Range range) {
                return range.lowerBound;
            }
        };
        UPPER_BOUND_FN = new Function<Range, Cut>() {
            public Cut apply(final Range range) {
                return range.upperBound;
            }
        };
        RANGE_LEX_ORDERING = new RangeLexOrdering();
        ALL = new Range<Comparable>(Cut.<Comparable>belowAll(), Cut.<Comparable>aboveAll());
    }
    
    private static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable {
        private static final long serialVersionUID = 0L;
        
        @Override
        public int compare(final Range<?> left, final Range<?> right) {
            return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
        }
    }
}
