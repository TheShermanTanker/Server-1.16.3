package com.google.common.collect;

import java.util.Collections;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@GwtIncompatible
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V>, Serializable {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY;
    private final transient ImmutableList<Range<K>> ranges;
    private final transient ImmutableList<V> values;
    private static final long serialVersionUID = 0L;
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
        return (ImmutableRangeMap<K, V>)ImmutableRangeMap.EMPTY;
    }
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(final Range<K> range, final V value) {
        return new ImmutableRangeMap<K, V>(ImmutableList.of((Range<K>)range), ImmutableList.<V>of(value));
    }
    
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(final RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap<K, V>)(ImmutableRangeMap)rangeMap;
        }
        final Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
        final ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(map.size());
        final ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(map.size());
        for (final Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
            rangesBuilder.add((Range<K>)entry.getKey());
            valuesBuilder.add((V)entry.getValue());
        }
        return new ImmutableRangeMap<K, V>(rangesBuilder.build(), valuesBuilder.build());
    }
    
    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }
    
    ImmutableRangeMap(final ImmutableList<Range<K>> ranges, final ImmutableList<V> values) {
        this.ranges = ranges;
        this.values = values;
    }
    
    @Nullable
    public V get(final K key) {
        final int index = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        final Range<K> range = (Range<K>)this.ranges.get(index);
        return (V)(range.contains(key) ? this.values.get(index) : null);
    }
    
    @Nullable
    public Map.Entry<Range<K>, V> getEntry(final K key) {
        final int index = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        final Range<K> range = (Range<K>)this.ranges.get(index);
        return range.contains(key) ? Maps.<Range<K>, V>immutableEntry(range, this.values.get(index)) : null;
    }
    
    public Range<K> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        final Range<K> firstRange = (Range<K>)this.ranges.get(0);
        final Range<K> lastRange = (Range<K>)this.ranges.get(this.ranges.size() - 1);
        return Range.<K>create(firstRange.lowerBound, lastRange.upperBound);
    }
    
    @Deprecated
    public void put(final Range<K> range, final V value) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void putAll(final RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    @Deprecated
    public void remove(final Range<K> range) {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.<Range<K>, V>of();
        }
        final RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<Range<K>>(this.ranges, (java.util.Comparator<? super Range<K>>)Range.RANGE_LEX_ORDERING);
        return new ImmutableSortedMap<Range<K>, V>(rangeSet, this.values);
    }
    
    public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.<Range<K>, V>of();
        }
        final RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<Range<K>>(this.ranges.reverse(), (java.util.Comparator<? super Range<K>>)Range.RANGE_LEX_ORDERING.reverse());
        return new ImmutableSortedMap<Range<K>, V>(rangeSet, this.values.reverse());
    }
    
    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        if (Preconditions.<Range<K>>checkNotNull(range).isEmpty()) {
            return ImmutableRangeMap.<K, V>of();
        }
        if (this.ranges.isEmpty() || range.encloses(this.span())) {
            return this;
        }
        final int lowerIndex = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>upperBoundFn(), (Cut<Comparable>)range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        final int upperIndex = SortedLists.<Object, Cut<Comparable>>binarySearch((java.util.List<Object>)this.ranges, Range.<Comparable>lowerBoundFn(), (Cut<Comparable>)range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (lowerIndex >= upperIndex) {
            return ImmutableRangeMap.<K, V>of();
        }
        final int off = lowerIndex;
        final int len = upperIndex - lowerIndex;
        final ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>() {
            public int size() {
                return len;
            }
            
            public Range<K> get(final int index) {
                Preconditions.checkElementIndex(index, len);
                if (index == 0 || index == len - 1) {
                    return ((Range)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
                }
                return (Range<K>)ImmutableRangeMap.this.ranges.get(index + off);
            }
            
            @Override
            boolean isPartialView() {
                return true;
            }
        };
        final ImmutableRangeMap<K, V> outer = this;
        return new ImmutableRangeMap<K, V>(subRanges, this.values.subList(lowerIndex, upperIndex)) {
            @Override
            public ImmutableRangeMap<K, V> subRangeMap(final Range<K> subRange) {
                if (range.isConnected(subRange)) {
                    return outer.subRangeMap(subRange.intersection(range));
                }
                return ImmutableRangeMap.<K, V>of();
            }
        };
    }
    
    public int hashCode() {
        return this.asMapOfRanges().hashCode();
    }
    
    public boolean equals(@Nullable final Object o) {
        if (o instanceof RangeMap) {
            final RangeMap<?, ?> rangeMap = o;
            return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
        }
        return false;
    }
    
    public String toString() {
        return this.asMapOfRanges().toString();
    }
    
    Object writeReplace() {
        return new SerializedForm(this.asMapOfRanges());
    }
    
    static {
        EMPTY = new ImmutableRangeMap<Comparable<?>, Object>(ImmutableList.<Range<Comparable<?>>>of(), ImmutableList.of());
    }
    
    public static final class Builder<K extends Comparable<?>, V> {
        private final List<Map.Entry<Range<K>, V>> entries;
        
        public Builder() {
            this.entries = Lists.newArrayList();
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> put(final Range<K> range, final V value) {
            Preconditions.<Range<K>>checkNotNull(range);
            Preconditions.<V>checkNotNull(value);
            Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
            this.entries.add(Maps.<Range<K>, V>immutableEntry(range, value));
            return this;
        }
        
        @CanIgnoreReturnValue
        public Builder<K, V> putAll(final RangeMap<K, ? extends V> rangeMap) {
            for (final Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
                this.put((Range<K>)entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        public ImmutableRangeMap<K, V> build() {
            Collections.sort((List)this.entries, (Comparator)Range.RANGE_LEX_ORDERING.onKeys());
            final ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<Range<K>>(this.entries.size());
            final ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<V>(this.entries.size());
            for (int i = 0; i < this.entries.size(); ++i) {
                final Range<K> range = (Range<K>)((Map.Entry)this.entries.get(i)).getKey();
                if (i > 0) {
                    final Range<K> prevRange = (Range<K>)((Map.Entry)this.entries.get(i - 1)).getKey();
                    if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
                        throw new IllegalArgumentException(new StringBuilder().append("Overlapping ranges: range ").append(prevRange).append(" overlaps with entry ").append(range).toString());
                    }
                }
                rangesBuilder.add(range);
                valuesBuilder.add((V)((Map.Entry)this.entries.get(i)).getValue());
            }
            return new ImmutableRangeMap<K, V>(rangesBuilder.build(), valuesBuilder.build());
        }
    }
    
    private static class SerializedForm<K extends Comparable<?>, V> implements Serializable {
        private final ImmutableMap<Range<K>, V> mapOfRanges;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableMap<Range<K>, V> mapOfRanges) {
            this.mapOfRanges = mapOfRanges;
        }
        
        Object readResolve() {
            if (this.mapOfRanges.isEmpty()) {
                return ImmutableRangeMap.<Comparable, Object>of();
            }
            return this.createRangeMap();
        }
        
        Object createRangeMap() {
            final Builder<K, V> builder = new Builder<K, V>();
            for (final Map.Entry<Range<K>, V> entry : this.mapOfRanges.entrySet()) {
                builder.put((Range<K>)entry.getKey(), (V)entry.getValue());
            }
            return builder.build();
        }
    }
}
