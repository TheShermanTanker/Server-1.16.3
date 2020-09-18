package com.google.common.collect;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import com.google.common.base.Predicate;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.NavigableMap;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class TreeRangeMap<K extends Comparable, V> implements RangeMap<K, V> {
    private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
    private static final RangeMap EMPTY_SUB_RANGE_MAP;
    
    public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
        return new TreeRangeMap<K, V>();
    }
    
    private TreeRangeMap() {
        this.entriesByLowerBound = Maps.newTreeMap();
    }
    
    @Nullable
    public V get(final K key) {
        final Map.Entry<Range<K>, V> entry = this.getEntry(key);
        return (V)((entry == null) ? null : entry.getValue());
    }
    
    @Nullable
    public Map.Entry<Range<K>, V> getEntry(final K key) {
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)this.entriesByLowerBound.floorEntry(Cut.<K>belowValue(key));
        if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
            return (Map.Entry<Range<K>, V>)mapEntry.getValue();
        }
        return null;
    }
    
    public void put(final Range<K> range, final V value) {
        if (!range.isEmpty()) {
            Preconditions.<V>checkNotNull(value);
            this.remove(range);
            this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry((Range<Comparable>)range, value));
        }
    }
    
    public void putAll(final RangeMap<K, V> rangeMap) {
        for (final Map.Entry<Range<K>, V> entry : rangeMap.asMapOfRanges().entrySet()) {
            this.put((Range<K>)entry.getKey(), entry.getValue());
        }
    }
    
    public void clear() {
        this.entriesByLowerBound.clear();
    }
    
    public Range<K> span() {
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)this.entriesByLowerBound.firstEntry();
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)this.entriesByLowerBound.lastEntry();
        if (firstEntry == null) {
            throw new NoSuchElementException();
        }
        return Range.<K>create((Cut<K>)((RangeMapEntry)firstEntry.getValue()).getKey().lowerBound, (Cut<K>)((RangeMapEntry)lastEntry.getValue()).getKey().upperBound);
    }
    
    private void putRangeMapEntry(final Cut<K> lowerBound, final Cut<K> upperBound, final V value) {
        this.entriesByLowerBound.put(lowerBound, new RangeMapEntry((Cut<Comparable>)lowerBound, (Cut<Comparable>)upperBound, value));
    }
    
    public void remove(final Range<K> rangeToRemove) {
        if (rangeToRemove.isEmpty()) {
            return;
        }
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
        if (mapEntryBelowToTruncate != null) {
            final RangeMapEntry<K, V> rangeMapEntry = (RangeMapEntry<K, V>)mapEntryBelowToTruncate.getValue();
            if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
                if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
                    this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry.getUpperBound(), ((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
                }
                this.putRangeMapEntry(rangeMapEntry.getLowerBound(), rangeToRemove.lowerBound, ((RangeMapEntry)mapEntryBelowToTruncate.getValue()).getValue());
            }
        }
        final Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
        if (mapEntryAboveToTruncate != null) {
            final RangeMapEntry<K, V> rangeMapEntry2 = (RangeMapEntry<K, V>)mapEntryAboveToTruncate.getValue();
            if (rangeMapEntry2.getUpperBound().compareTo(rangeToRemove.upperBound) > 0) {
                this.putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry2.getUpperBound(), ((RangeMapEntry)mapEntryAboveToTruncate.getValue()).getValue());
            }
        }
        this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
    }
    
    public Map<Range<K>, V> asMapOfRanges() {
        return (Map<Range<K>, V>)new AsMapOfRanges((Iterable<RangeMapEntry<K, V>>)this.entriesByLowerBound.values());
    }
    
    public Map<Range<K>, V> asDescendingMapOfRanges() {
        return (Map<Range<K>, V>)new AsMapOfRanges((Iterable<RangeMapEntry<K, V>>)this.entriesByLowerBound.descendingMap().values());
    }
    
    public RangeMap<K, V> subRangeMap(final Range<K> subRange) {
        if (subRange.equals(Range.<Comparable>all())) {
            return this;
        }
        return new SubRangeMap(subRange);
    }
    
    private RangeMap<K, V> emptySubRangeMap() {
        return (RangeMap<K, V>)TreeRangeMap.EMPTY_SUB_RANGE_MAP;
    }
    
    public boolean equals(@Nullable final Object o) {
        if (o instanceof RangeMap) {
            final RangeMap<?, ?> rangeMap = o;
            return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
        }
        return false;
    }
    
    public int hashCode() {
        return this.asMapOfRanges().hashCode();
    }
    
    public String toString() {
        return this.entriesByLowerBound.values().toString();
    }
    
    static {
        EMPTY_SUB_RANGE_MAP = new RangeMap() {
            @Nullable
            public Object get(final Comparable key) {
                return null;
            }
            
            @Nullable
            public Map.Entry<Range, Object> getEntry(final Comparable key) {
                return null;
            }
            
            public Range span() {
                throw new NoSuchElementException();
            }
            
            public void put(final Range range, final Object value) {
                Preconditions.<Range>checkNotNull(range);
                throw new IllegalArgumentException(new StringBuilder().append("Cannot insert range ").append(range).append(" into an empty subRangeMap").toString());
            }
            
            public void putAll(final RangeMap rangeMap) {
                if (!rangeMap.asMapOfRanges().isEmpty()) {
                    throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
                }
            }
            
            public void clear() {
            }
            
            public void remove(final Range range) {
                Preconditions.<Range>checkNotNull(range);
            }
            
            public Map<Range, Object> asMapOfRanges() {
                return (Map<Range, Object>)Collections.emptyMap();
            }
            
            public Map<Range, Object> asDescendingMapOfRanges() {
                return (Map<Range, Object>)Collections.emptyMap();
            }
            
            public RangeMap subRangeMap(final Range range) {
                Preconditions.<Range>checkNotNull(range);
                return this;
            }
        };
    }
    
    private static final class RangeMapEntry<K extends Comparable, V> extends AbstractMapEntry<Range<K>, V> {
        private final Range<K> range;
        private final V value;
        
        RangeMapEntry(final Cut<K> lowerBound, final Cut<K> upperBound, final V value) {
            this(Range.<K>create(lowerBound, upperBound), value);
        }
        
        RangeMapEntry(final Range<K> range, final V value) {
            this.range = range;
            this.value = value;
        }
        
        @Override
        public Range<K> getKey() {
            return this.range;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        public boolean contains(final K value) {
            return this.range.contains(value);
        }
        
        Cut<K> getLowerBound() {
            return this.range.lowerBound;
        }
        
        Cut<K> getUpperBound() {
            return this.range.upperBound;
        }
    }
    
    private final class AsMapOfRanges extends Maps.IteratorBasedAbstractMap<Range<K>, V> {
        final Iterable<Map.Entry<Range<K>, V>> entryIterable;
        
        AsMapOfRanges(final Iterable<RangeMapEntry<K, V>> entryIterable) {
            this.entryIterable = (Iterable<Map.Entry<Range<K>, V>>)entryIterable;
        }
        
        public boolean containsKey(@Nullable final Object key) {
            return this.get(key) != null;
        }
        
        public V get(@Nullable final Object key) {
            if (key instanceof Range) {
                final Range<?> range = key;
                final RangeMapEntry<K, V> rangeMapEntry = (RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
                if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
                    return rangeMapEntry.getValue();
                }
            }
            return null;
        }
        
        @Override
        public int size() {
            return TreeRangeMap.this.entriesByLowerBound.size();
        }
        
        @Override
        Iterator<Map.Entry<Range<K>, V>> entryIterator() {
            return (Iterator<Map.Entry<Range<K>, V>>)this.entryIterable.iterator();
        }
    }
    
    private class SubRangeMap implements RangeMap<K, V> {
        private final Range<K> subRange;
        
        SubRangeMap(final Range<K> subRange) {
            this.subRange = subRange;
        }
        
        @Nullable
        public V get(final K key) {
            return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
        }
        
        @Nullable
        public Map.Entry<Range<K>, V> getEntry(final K key) {
            if (this.subRange.contains(key)) {
                final Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
                if (entry != null) {
                    return Maps.<Range<K>, V>immutableEntry(((Range)entry.getKey()).intersection(this.subRange), entry.getValue());
                }
            }
            return null;
        }
        
        public Range<K> span() {
            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
            Cut<K> lowerBound;
            if (lowerEntry != null && ((RangeMapEntry)lowerEntry.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
                lowerBound = this.subRange.lowerBound;
            }
            else {
                lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
                if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
                    throw new NoSuchElementException();
                }
            }
            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> upperEntry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
            if (upperEntry == null) {
                throw new NoSuchElementException();
            }
            Cut<K> upperBound;
            if (((RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
                upperBound = this.subRange.upperBound;
            }
            else {
                upperBound = ((RangeMapEntry)upperEntry.getValue()).getUpperBound();
            }
            return Range.<K>create(lowerBound, upperBound);
        }
        
        public void put(final Range<K> range, final V value) {
            Preconditions.checkArgument(this.subRange.encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
            TreeRangeMap.this.put(range, value);
        }
        
        public void putAll(final RangeMap<K, V> rangeMap) {
            if (rangeMap.asMapOfRanges().isEmpty()) {
                return;
            }
            final Range<K> span = rangeMap.span();
            Preconditions.checkArgument(this.subRange.encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
            TreeRangeMap.this.putAll(rangeMap);
        }
        
        public void clear() {
            TreeRangeMap.this.remove(this.subRange);
        }
        
        public void remove(final Range<K> range) {
            if (range.isConnected(this.subRange)) {
                TreeRangeMap.this.remove(range.intersection(this.subRange));
            }
        }
        
        public RangeMap<K, V> subRangeMap(final Range<K> range) {
            if (!range.isConnected(this.subRange)) {
                return (RangeMap<K, V>)TreeRangeMap.this.emptySubRangeMap();
            }
            return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
        }
        
        public Map<Range<K>, V> asMapOfRanges() {
            return (Map<Range<K>, V>)new SubRangeMapAsMap();
        }
        
        public Map<Range<K>, V> asDescendingMapOfRanges() {
            return (Map<Range<K>, V>)new SubRangeMapAsMap() {
                @Override
                Iterator<Map.Entry<Range<K>, V>> entryIterator() {
                    if (SubRangeMap.this.subRange.isEmpty()) {
                        return Iterators.emptyIterator();
                    }
                    final Iterator<RangeMapEntry<K, V>> backingItr = (Iterator<RangeMapEntry<K, V>>)TreeRangeMap.this.entriesByLowerBound.headMap(SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
                    return (Iterator<Map.Entry<Range<K>, V>>)new AbstractIterator<Map.Entry<Range<K>, V>>() {
                        @Override
                        protected Map.Entry<Range<K>, V> computeNext() {
                            if (!backingItr.hasNext()) {
                                return (Map.Entry<Range<K>, V>)((AbstractIterator<Map.Entry>)this).endOfData();
                            }
                            final RangeMapEntry<K, V> entry = (RangeMapEntry<K, V>)backingItr.next();
                            if (entry.getUpperBound().compareTo(SubRangeMap.this.subRange.lowerBound) <= 0) {
                                return (Map.Entry<Range<K>, V>)((AbstractIterator<Map.Entry>)this).endOfData();
                            }
                            return Maps.<Range<K>, V>immutableEntry(entry.getKey().intersection(SubRangeMap.this.subRange), entry.getValue());
                        }
                    };
                }
            };
        }
        
        public boolean equals(@Nullable final Object o) {
            if (o instanceof RangeMap) {
                final RangeMap<?, ?> rangeMap = o;
                return this.asMapOfRanges().equals(rangeMap.asMapOfRanges());
            }
            return false;
        }
        
        public int hashCode() {
            return this.asMapOfRanges().hashCode();
        }
        
        public String toString() {
            return this.asMapOfRanges().toString();
        }
        
        class SubRangeMapAsMap extends AbstractMap<Range<K>, V> {
            public boolean containsKey(final Object key) {
                return this.get(key) != null;
            }
            
            public V get(final Object key) {
                try {
                    if (key instanceof Range) {
                        final Range<K> r = (Range<K>)key;
                        if (!SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
                            return null;
                        }
                        RangeMapEntry<K, V> candidate = null;
                        if (r.lowerBound.compareTo(SubRangeMap.this.subRange.lowerBound) == 0) {
                            final Map.Entry<Cut<K>, RangeMapEntry<K, V>> entry = (Map.Entry<Cut<K>, RangeMapEntry<K, V>>)TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
                            if (entry != null) {
                                candidate = (RangeMapEntry<K, V>)entry.getValue();
                            }
                        }
                        else {
                            candidate = (RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
                        }
                        if (candidate != null && candidate.getKey().isConnected(SubRangeMap.this.subRange) && candidate.getKey().intersection(SubRangeMap.this.subRange).equals(r)) {
                            return candidate.getValue();
                        }
                    }
                }
                catch (ClassCastException e) {
                    return null;
                }
                return null;
            }
            
            public V remove(final Object key) {
                final V value = this.get(key);
                if (value != null) {
                    final Range<K> range = (Range<K>)key;
                    TreeRangeMap.this.remove(range);
                    return value;
                }
                return null;
            }
            
            public void clear() {
                SubRangeMap.this.clear();
            }
            
            private boolean removeEntryIf(final Predicate<? super Map.Entry<Range<K>, V>> predicate) {
                final List<Range<K>> toRemove = Lists.newArrayList();
                for (final Map.Entry<Range<K>, V> entry : this.entrySet()) {
                    if (predicate.apply(entry)) {
                        toRemove.add(entry.getKey());
                    }
                }
                for (final Range<K> range : toRemove) {
                    TreeRangeMap.this.remove(range);
                }
                return !toRemove.isEmpty();
            }
            
            public Set<Range<K>> keySet() {
                return (Set<Range<K>>)new Maps.KeySet<Range<K>, V>(this) {
                    @Override
                    public boolean remove(@Nullable final Object o) {
                        return SubRangeMapAsMap.this.remove(o) != null;
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.<Map.Entry<?, ?>, Object>compose(Predicates.not(Predicates.<B>in((java.util.Collection<? extends B>)c)), Maps.keyFunction()));
                    }
                };
            }
            
            public Set<Map.Entry<Range<K>, V>> entrySet() {
                return (Set<Map.Entry<Range<K>, V>>)new Maps.EntrySet<Range<K>, V>() {
                    @Override
                    Map<Range<K>, V> map() {
                        return (Map<Range<K>, V>)SubRangeMapAsMap.this;
                    }
                    
                    public Iterator<Map.Entry<Range<K>, V>> iterator() {
                        return SubRangeMapAsMap.this.entryIterator();
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
                    }
                    
                    @Override
                    public int size() {
                        return Iterators.size(this.iterator());
                    }
                    
                    @Override
                    public boolean isEmpty() {
                        return !this.iterator().hasNext();
                    }
                };
            }
            
            Iterator<Map.Entry<Range<K>, V>> entryIterator() {
                if (SubRangeMap.this.subRange.isEmpty()) {
                    return Iterators.emptyIterator();
                }
                final Cut<K> cutToStart = MoreObjects.<Cut<K>>firstNonNull(TreeRangeMap.this.entriesByLowerBound.floorKey(SubRangeMap.this.subRange.lowerBound), SubRangeMap.this.subRange.lowerBound);
                final Iterator<RangeMapEntry<K, V>> backingItr = (Iterator<RangeMapEntry<K, V>>)TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
                return (Iterator<Map.Entry<Range<K>, V>>)new AbstractIterator<Map.Entry<Range<K>, V>>() {
                    @Override
                    protected Map.Entry<Range<K>, V> computeNext() {
                        while (backingItr.hasNext()) {
                            final RangeMapEntry<K, V> entry = (RangeMapEntry<K, V>)backingItr.next();
                            if (entry.getLowerBound().compareTo(SubRangeMap.this.subRange.upperBound) >= 0) {
                                return (Map.Entry<Range<K>, V>)((AbstractIterator<Map.Entry>)this).endOfData();
                            }
                            if (entry.getUpperBound().compareTo(SubRangeMap.this.subRange.lowerBound) > 0) {
                                return Maps.<Range<K>, V>immutableEntry(entry.getKey().intersection(SubRangeMap.this.subRange), entry.getValue());
                            }
                        }
                        return (Map.Entry<Range<K>, V>)((AbstractIterator<Map.Entry>)this).endOfData();
                    }
                };
            }
            
            public Collection<V> values() {
                return (Collection<V>)new Maps.Values<Range<K>, V>(this) {
                    @Override
                    public boolean removeAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.<Map.Entry<?, ?>, Object>compose(Predicates.in(c), Maps.valueFunction()));
                    }
                    
                    @Override
                    public boolean retainAll(final Collection<?> c) {
                        return SubRangeMapAsMap.this.removeEntryIf(Predicates.<Map.Entry<?, ?>, Object>compose(Predicates.not(Predicates.<B>in((java.util.Collection<? extends B>)c)), Maps.valueFunction()));
                    }
                };
            }
        }
    }
}
