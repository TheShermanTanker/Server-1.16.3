package com.google.common.collect;

import java.io.Serializable;
import java.util.NoSuchElementException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractSet;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.stream.Stream;
import com.google.common.math.IntMath;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CopyOnWriteArraySet;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import com.google.common.annotations.Beta;
import java.util.stream.Collector;
import java.util.Iterator;
import java.util.Collection;
import java.util.EnumSet;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Sets {
    private Sets() {
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final E anElement, final E... otherElements) {
        return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(EnumSet.of((Enum)anElement, (Enum[])otherElements));
    }
    
    @GwtCompatible(serializable = true)
    public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(final Iterable<E> elements) {
        if (elements instanceof ImmutableEnumSet) {
            return (ImmutableSet<E>)(ImmutableEnumSet)elements;
        }
        if (elements instanceof Collection) {
            final Collection<E> collection = (Collection<E>)elements;
            if (collection.isEmpty()) {
                return ImmutableSet.<E>of();
            }
            return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(EnumSet.copyOf((Collection)collection));
        }
        else {
            final Iterator<E> itr = (Iterator<E>)elements.iterator();
            if (itr.hasNext()) {
                final EnumSet<E> enumSet = (EnumSet<E>)EnumSet.of((Enum)itr.next());
                Iterators.addAll((java.util.Collection<Object>)enumSet, itr);
                return (ImmutableSet<E>)ImmutableEnumSet.asImmutable(enumSet);
            }
            return ImmutableSet.<E>of();
        }
    }
    
    @Beta
    public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
        return Accumulator.TO_IMMUTABLE_ENUM_SET;
    }
    
    public static <E extends Enum<E>> EnumSet<E> newEnumSet(final Iterable<E> iterable, final Class<E> elementType) {
        final EnumSet<E> set = (EnumSet<E>)EnumSet.noneOf((Class)elementType);
        Iterables.addAll((java.util.Collection<Object>)set, iterable);
        return set;
    }
    
    public static <E> HashSet<E> newHashSet() {
        return (HashSet<E>)new HashSet();
    }
    
    public static <E> HashSet<E> newHashSet(final E... elements) {
        final HashSet<E> set = Sets.<E>newHashSetWithExpectedSize(elements.length);
        Collections.addAll((Collection)set, (Object[])elements);
        return set;
    }
    
    public static <E> HashSet<E> newHashSetWithExpectedSize(final int expectedSize) {
        return (HashSet<E>)new HashSet(Maps.capacity(expectedSize));
    }
    
    public static <E> HashSet<E> newHashSet(final Iterable<? extends E> elements) {
        return (HashSet<E>)((elements instanceof Collection) ? new HashSet((Collection)Collections2.cast(elements)) : Sets.newHashSet((java.util.Iterator<?>)elements.iterator()));
    }
    
    public static <E> HashSet<E> newHashSet(final Iterator<? extends E> elements) {
        final HashSet<E> set = Sets.<E>newHashSet();
        Iterators.addAll((java.util.Collection<Object>)set, elements);
        return set;
    }
    
    public static <E> Set<E> newConcurrentHashSet() {
        return (Set<E>)Collections.newSetFromMap((Map)new ConcurrentHashMap());
    }
    
    public static <E> Set<E> newConcurrentHashSet(final Iterable<? extends E> elements) {
        final Set<E> set = Sets.<E>newConcurrentHashSet();
        Iterables.addAll((java.util.Collection<Object>)set, elements);
        return set;
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return (LinkedHashSet<E>)new LinkedHashSet();
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(final int expectedSize) {
        return (LinkedHashSet<E>)new LinkedHashSet(Maps.capacity(expectedSize));
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (LinkedHashSet<E>)new LinkedHashSet((Collection)Collections2.cast(elements));
        }
        final LinkedHashSet<E> set = Sets.<E>newLinkedHashSet();
        Iterables.addAll((java.util.Collection<Object>)set, elements);
        return set;
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return (TreeSet<E>)new TreeSet();
    }
    
    public static <E extends Comparable> TreeSet<E> newTreeSet(final Iterable<? extends E> elements) {
        final TreeSet<E> set = Sets.<E>newTreeSet();
        Iterables.addAll((java.util.Collection<Object>)set, elements);
        return set;
    }
    
    public static <E> TreeSet<E> newTreeSet(final Comparator<? super E> comparator) {
        return (TreeSet<E>)new TreeSet((Comparator)Preconditions.<Comparator<? super E>>checkNotNull(comparator));
    }
    
    public static <E> Set<E> newIdentityHashSet() {
        return (Set<E>)Collections.newSetFromMap((Map)Maps.newIdentityHashMap());
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return (CopyOnWriteArraySet<E>)new CopyOnWriteArraySet();
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(final Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = ((elements instanceof Collection) ? Collections2.cast(elements) : Lists.newArrayList((java.lang.Iterable<?>)elements));
        return (CopyOnWriteArraySet<E>)new CopyOnWriteArraySet((Collection)elementsCollection);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection) {
        if (collection instanceof EnumSet) {
            return (EnumSet<E>)EnumSet.complementOf((EnumSet)collection);
        }
        Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
        final Class<E> type = (Class<E>)((Enum)collection.iterator().next()).getDeclaringClass();
        return Sets.<E>makeComplementByHand(collection, type);
    }
    
    public static <E extends Enum<E>> EnumSet<E> complementOf(final Collection<E> collection, final Class<E> type) {
        Preconditions.<Collection<E>>checkNotNull(collection);
        return (EnumSet<E>)((collection instanceof EnumSet) ? EnumSet.complementOf((EnumSet)collection) : Sets.<Enum>makeComplementByHand((java.util.Collection<Enum>)collection, (java.lang.Class<Enum>)type));
    }
    
    private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(final Collection<E> collection, final Class<E> type) {
        final EnumSet<E> result = (EnumSet<E>)EnumSet.allOf((Class)type);
        result.removeAll((Collection)collection);
        return result;
    }
    
    @Deprecated
    public static <E> Set<E> newSetFromMap(final Map<E, Boolean> map) {
        return (Set<E>)Collections.newSetFromMap((Map)map);
    }
    
    public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.<Set<? extends E>>checkNotNull(set1, "set1");
        Preconditions.<Set<? extends E>>checkNotNull(set2, "set2");
        final Set<? extends E> set2minus1 = Sets.difference(set2, set1);
        return new SetView<E>() {
            public int size() {
                return IntMath.saturatedAdd(set1.size(), set2minus1.size());
            }
            
            public boolean isEmpty() {
                return set1.isEmpty() && set2.isEmpty();
            }
            
            @Override
            public UnmodifiableIterator<E> iterator() {
                return Iterators.<E>unmodifiableIterator(Iterators.concat((java.util.Iterator<? extends E>)set1.iterator(), (java.util.Iterator<? extends E>)set2minus1.iterator()));
            }
            
            public Stream<E> stream() {
                return (Stream<E>)Stream.concat(set1.stream(), set2minus1.stream());
            }
            
            public Stream<E> parallelStream() {
                return (Stream<E>)Stream.concat(set1.parallelStream(), set2minus1.parallelStream());
            }
            
            public boolean contains(final Object object) {
                return set1.contains(object) || set2.contains(object);
            }
            
            @Override
            public <S extends Set<E>> S copyInto(final S set) {
                set.addAll((Collection)set1);
                set.addAll((Collection)set2);
                return set;
            }
            
            @Override
            public ImmutableSet<E> immutableCopy() {
                return new ImmutableSet.Builder<E>().addAll((java.lang.Iterable<? extends E>)set1).addAll((java.lang.Iterable<? extends E>)set2).build();
            }
        };
    }
    
    public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
        Preconditions.<Set<E>>checkNotNull(set1, "set1");
        Preconditions.<Set<?>>checkNotNull(set2, "set2");
        final Predicate<Object> inSet2 = Predicates.in((java.util.Collection<?>)set2);
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                return Iterators.<E>filter((java.util.Iterator<E>)set1.iterator(), inSet2);
            }
            
            public Stream<E> stream() {
                return (Stream<E>)set1.stream().filter((java.util.function.Predicate)inSet2);
            }
            
            public Stream<E> parallelStream() {
                return (Stream<E>)set1.parallelStream().filter((java.util.function.Predicate)inSet2);
            }
            
            public int size() {
                return Iterators.size(this.iterator());
            }
            
            public boolean isEmpty() {
                return !this.iterator().hasNext();
            }
            
            public boolean contains(final Object object) {
                return set1.contains(object) && set2.contains(object);
            }
            
            public boolean containsAll(final Collection<?> collection) {
                return set1.containsAll((Collection)collection) && set2.containsAll((Collection)collection);
            }
        };
    }
    
    public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
        Preconditions.<Set<E>>checkNotNull(set1, "set1");
        Preconditions.<Set<?>>checkNotNull(set2, "set2");
        final Predicate<Object> notInSet2 = Predicates.not(Predicates.in((java.util.Collection<?>)set2));
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                return Iterators.<E>filter((java.util.Iterator<E>)set1.iterator(), notInSet2);
            }
            
            public Stream<E> stream() {
                return (Stream<E>)set1.stream().filter((java.util.function.Predicate)notInSet2);
            }
            
            public Stream<E> parallelStream() {
                return (Stream<E>)set1.parallelStream().filter((java.util.function.Predicate)notInSet2);
            }
            
            public int size() {
                return Iterators.size(this.iterator());
            }
            
            public boolean isEmpty() {
                return set2.containsAll((Collection)set1);
            }
            
            public boolean contains(final Object element) {
                return set1.contains(element) && !set2.contains(element);
            }
        };
    }
    
    public static <E> SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
        Preconditions.<Set<? extends E>>checkNotNull(set1, "set1");
        Preconditions.<Set<? extends E>>checkNotNull(set2, "set2");
        return new SetView<E>() {
            @Override
            public UnmodifiableIterator<E> iterator() {
                final Iterator<? extends E> itr1 = set1.iterator();
                final Iterator<? extends E> itr2 = set2.iterator();
                return new AbstractIterator<E>() {
                    public E computeNext() {
                        while (itr1.hasNext()) {
                            final E elem1 = (E)itr1.next();
                            if (!set2.contains(elem1)) {
                                return elem1;
                            }
                        }
                        while (itr2.hasNext()) {
                            final E elem2 = (E)itr2.next();
                            if (!set1.contains(elem2)) {
                                return elem2;
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
            
            public int size() {
                return Iterators.size(this.iterator());
            }
            
            public boolean isEmpty() {
                return set1.equals(set2);
            }
            
            public boolean contains(final Object element) {
                return set1.contains(element) ^ set2.contains(element);
            }
        };
    }
    
    public static <E> Set<E> filter(final Set<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof SortedSet) {
            return Sets.filter((java.util.SortedSet<Object>)unfiltered, predicate);
        }
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.<E>and(filtered.predicate, predicate);
            return (Set<E>)new FilteredSet((java.util.Set<Object>)filtered.unfiltered, combinedPredicate);
        }
        return (Set<E>)new FilteredSet((java.util.Set<Object>)Preconditions.<Set<E>>checkNotNull(unfiltered), Preconditions.<Predicate<? super Object>>checkNotNull(predicate));
    }
    
    public static <E> SortedSet<E> filter(final SortedSet<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.<E>and(filtered.predicate, predicate);
            return (SortedSet<E>)new FilteredSortedSet((java.util.SortedSet<Object>)filtered.unfiltered, combinedPredicate);
        }
        return (SortedSet<E>)new FilteredSortedSet((java.util.SortedSet<Object>)Preconditions.<SortedSet<E>>checkNotNull(unfiltered), Preconditions.<Predicate<? super Object>>checkNotNull(predicate));
    }
    
    @GwtIncompatible
    public static <E> NavigableSet<E> filter(final NavigableSet<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredSet) {
            final FilteredSet<E> filtered = (FilteredSet<E>)(FilteredSet)unfiltered;
            final Predicate<E> combinedPredicate = Predicates.<E>and(filtered.predicate, predicate);
            return (NavigableSet<E>)new FilteredNavigableSet((java.util.NavigableSet<Object>)filtered.unfiltered, combinedPredicate);
        }
        return (NavigableSet<E>)new FilteredNavigableSet((java.util.NavigableSet<Object>)Preconditions.<NavigableSet<E>>checkNotNull(unfiltered), Preconditions.<Predicate<? super Object>>checkNotNull(predicate));
    }
    
    public static <B> Set<List<B>> cartesianProduct(final List<? extends Set<? extends B>> sets) {
        return CartesianSet.<B>create(sets);
    }
    
    public static <B> Set<List<B>> cartesianProduct(final Set<? extends B>... sets) {
        return Sets.<B>cartesianProduct((java.util.List<? extends java.util.Set<? extends B>>)Arrays.asList((Object[])sets));
    }
    
    @GwtCompatible(serializable = false)
    public static <E> Set<Set<E>> powerSet(final Set<E> set) {
        return (Set<Set<E>>)new PowerSet((java.util.Set<Object>)set);
    }
    
    static int hashCodeImpl(final Set<?> s) {
        int hashCode = 0;
        for (final Object o : s) {
            hashCode += ((o != null) ? o.hashCode() : 0);
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    static boolean equalsImpl(final Set<?> s, @Nullable final Object object) {
        if (s == object) {
            return true;
        }
        if (object instanceof Set) {
            final Set<?> o = object;
            try {
                return s.size() == o.size() && s.containsAll((Collection)o);
            }
            catch (NullPointerException ignored) {
                return false;
            }
            catch (ClassCastException ignored2) {
                return false;
            }
        }
        return false;
    }
    
    public static <E> NavigableSet<E> unmodifiableNavigableSet(final NavigableSet<E> set) {
        if (set instanceof ImmutableSortedSet || set instanceof UnmodifiableNavigableSet) {
            return set;
        }
        return (NavigableSet<E>)new UnmodifiableNavigableSet((java.util.NavigableSet<Object>)set);
    }
    
    @GwtIncompatible
    public static <E> NavigableSet<E> synchronizedNavigableSet(final NavigableSet<E> navigableSet) {
        return Synchronized.<E>navigableSet(navigableSet);
    }
    
    static boolean removeAllImpl(final Set<?> set, final Iterator<?> iterator) {
        boolean changed = false;
        while (iterator.hasNext()) {
            changed |= set.remove(iterator.next());
        }
        return changed;
    }
    
    static boolean removeAllImpl(final Set<?> set, Collection<?> collection) {
        Preconditions.<Collection<?>>checkNotNull(collection);
        if (collection instanceof Multiset) {
            collection = ((Multiset)collection).elementSet();
        }
        if (collection instanceof Set && collection.size() > set.size()) {
            return Iterators.removeAll(set.iterator(), collection);
        }
        return removeAllImpl(set, collection.iterator());
    }
    
    @Beta
    @GwtIncompatible
    public static <K extends Comparable<? super K>> NavigableSet<K> subSet(final NavigableSet<K> set, final Range<K> range) {
        if (set.comparator() != null && set.comparator() != Ordering.<Comparable>natural() && range.hasLowerBound() && range.hasUpperBound()) {
            Preconditions.checkArgument(set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0, "set is using a custom comparator which is inconsistent with the natural ordering.");
        }
        if (range.hasLowerBound() && range.hasUpperBound()) {
            return (NavigableSet<K>)set.subSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED, range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        if (range.hasLowerBound()) {
            return (NavigableSet<K>)set.tailSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED);
        }
        if (range.hasUpperBound()) {
            return (NavigableSet<K>)set.headSet(range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        return Preconditions.<NavigableSet<K>>checkNotNull(set);
    }
    
    abstract static class ImprovedAbstractSet<E> extends AbstractSet<E> {
        public boolean removeAll(final Collection<?> c) {
            return Sets.removeAllImpl(this, c);
        }
        
        public boolean retainAll(final Collection<?> c) {
            return super.retainAll((Collection)Preconditions.<Collection<?>>checkNotNull(c));
        }
    }
    
    abstract static class ImprovedAbstractSet<E> extends AbstractSet<E> {
        public boolean removeAll(final Collection<?> c) {
            return Sets.removeAllImpl(this, c);
        }
        
        public boolean retainAll(final Collection<?> c) {
            return super.retainAll((Collection)Preconditions.<Collection<?>>checkNotNull(c));
        }
    }
    
    private static final class Accumulator<E extends Enum<E>> {
        static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET;
        private EnumSet<E> set;
        
        void add(final E e) {
            if (this.set == null) {
                this.set = (EnumSet<E>)EnumSet.of((Enum)e);
            }
            else {
                this.set.add(e);
            }
        }
        
        Accumulator<E> combine(final Accumulator<E> other) {
            if (this.set == null) {
                return other;
            }
            if (other.set == null) {
                return this;
            }
            this.set.addAll((Collection)other.set);
            return this;
        }
        
        ImmutableSet<E> toImmutableSet() {
            return (this.set == null) ? ImmutableSet.<E>of() : ImmutableEnumSet.asImmutable(this.set);
        }
        
        static {
            TO_IMMUTABLE_ENUM_SET = Collector.of(Accumulator::new, Accumulator::add, Accumulator::combine, Accumulator::toImmutableSet, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
        }
    }
    
    public abstract static class SetView<E> extends AbstractSet<E> {
        private SetView() {
        }
        
        public ImmutableSet<E> immutableCopy() {
            return ImmutableSet.<E>copyOf((java.util.Collection<? extends E>)this);
        }
        
        @CanIgnoreReturnValue
        public <S extends Set<E>> S copyInto(final S set) {
            set.addAll((Collection)this);
            return set;
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean add(final E e) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean remove(final Object object) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean addAll(final Collection<? extends E> newElements) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean removeAll(final Collection<?> oldElements) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @CanIgnoreReturnValue
        public final boolean retainAll(final Collection<?> elementsToKeep) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }
        
        public abstract UnmodifiableIterator<E> iterator();
    }
    
    private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
        FilteredSet(final Set<E> unfiltered, final Predicate<? super E> predicate) {
            super((Collection)unfiltered, predicate);
        }
        
        public boolean equals(@Nullable final Object object) {
            return Sets.equalsImpl(this, object);
        }
        
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }
    
    private static class FilteredSortedSet<E> extends FilteredSet<E> implements SortedSet<E> {
        FilteredSortedSet(final SortedSet<E> unfiltered, final Predicate<? super E> predicate) {
            super((Set)unfiltered, predicate);
        }
        
        public Comparator<? super E> comparator() {
            return ((SortedSet)this.unfiltered).comparator();
        }
        
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return (SortedSet<E>)new FilteredSortedSet((java.util.SortedSet<Object>)((SortedSet)this.unfiltered).subSet(fromElement, toElement), this.predicate);
        }
        
        public SortedSet<E> headSet(final E toElement) {
            return (SortedSet<E>)new FilteredSortedSet((java.util.SortedSet<Object>)((SortedSet)this.unfiltered).headSet(toElement), this.predicate);
        }
        
        public SortedSet<E> tailSet(final E fromElement) {
            return (SortedSet<E>)new FilteredSortedSet((java.util.SortedSet<Object>)((SortedSet)this.unfiltered).tailSet(fromElement), this.predicate);
        }
        
        public E first() {
            return (E)this.iterator().next();
        }
        
        public E last() {
            SortedSet<E> sortedUnfiltered = (SortedSet<E>)this.unfiltered;
            E element;
            while (true) {
                element = (E)sortedUnfiltered.last();
                if (this.predicate.apply(element)) {
                    break;
                }
                sortedUnfiltered = (SortedSet<E>)sortedUnfiltered.headSet(element);
            }
            return element;
        }
    }
    
    @GwtIncompatible
    private static class FilteredNavigableSet<E> extends FilteredSortedSet<E> implements NavigableSet<E> {
        FilteredNavigableSet(final NavigableSet<E> unfiltered, final Predicate<? super E> predicate) {
            super((SortedSet)unfiltered, predicate);
        }
        
        NavigableSet<E> unfiltered() {
            return (NavigableSet<E>)this.unfiltered;
        }
        
        @Nullable
        public E lower(final E e) {
            return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.headSet(e, false).descendingIterator(), (E)null);
        }
        
        @Nullable
        public E floor(final E e) {
            return Iterators.<E>getNext((java.util.Iterator<? extends E>)this.headSet(e, true).descendingIterator(), (E)null);
        }
        
        public E ceiling(final E e) {
            return Iterables.<E>getFirst((java.lang.Iterable<? extends E>)this.tailSet(e, true), (E)null);
        }
        
        public E higher(final E e) {
            return Iterables.<E>getFirst((java.lang.Iterable<? extends E>)this.tailSet(e, false), (E)null);
        }
        
        public E pollFirst() {
            return Iterables.<E>removeFirstMatching((java.lang.Iterable<E>)this.unfiltered(), this.predicate);
        }
        
        public E pollLast() {
            return Iterables.<E>removeFirstMatching((java.lang.Iterable<E>)this.unfiltered().descendingSet(), this.predicate);
        }
        
        public NavigableSet<E> descendingSet() {
            return Sets.<E>filter((java.util.NavigableSet<E>)this.unfiltered().descendingSet(), this.predicate);
        }
        
        public Iterator<E> descendingIterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered().descendingIterator(), this.predicate);
        }
        
        @Override
        public E last() {
            return (E)this.descendingIterator().next();
        }
        
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return Sets.<E>filter((java.util.NavigableSet<E>)this.unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
        }
        
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return Sets.<E>filter((java.util.NavigableSet<E>)this.unfiltered().headSet(toElement, inclusive), this.predicate);
        }
        
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return Sets.<E>filter((java.util.NavigableSet<E>)this.unfiltered().tailSet(fromElement, inclusive), this.predicate);
        }
    }
    
    private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>> {
        private final transient ImmutableList<ImmutableSet<E>> axes;
        private final transient CartesianList<E> delegate;
        
        static <E> Set<List<E>> create(final List<? extends Set<? extends E>> sets) {
            final ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder<ImmutableSet<E>>(sets.size());
            for (final Set<? extends E> set : sets) {
                final ImmutableSet<E> copy = ImmutableSet.<E>copyOf((java.util.Collection<? extends E>)set);
                if (copy.isEmpty()) {
                    return ImmutableSet.of();
                }
                axesBuilder.add(copy);
            }
            final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
            final ImmutableList<List<E>> listAxes = new ImmutableList<List<E>>() {
                public int size() {
                    return axes.size();
                }
                
                public List<E> get(final int index) {
                    return (List<E>)((ImmutableSet)axes.get(index)).asList();
                }
                
                @Override
                boolean isPartialView() {
                    return true;
                }
            };
            return (Set<List<E>>)new CartesianSet((ImmutableList<ImmutableSet<Object>>)axes, new CartesianList<>((ImmutableList<java.util.List<Object>>)listAxes));
        }
        
        private CartesianSet(final ImmutableList<ImmutableSet<E>> axes, final CartesianList<E> delegate) {
            this.axes = axes;
            this.delegate = delegate;
        }
        
        @Override
        protected Collection<List<E>> delegate() {
            return (Collection<List<E>>)this.delegate;
        }
        
        public boolean equals(@Nullable final Object object) {
            if (object instanceof CartesianSet) {
                final CartesianSet<?> that = object;
                return this.axes.equals(that.axes);
            }
            return super.equals(object);
        }
        
        public int hashCode() {
            int adjust = this.size() - 1;
            for (int i = 0; i < this.axes.size(); ++i) {
                adjust *= 31;
                adjust = ~(~adjust);
            }
            int hash = 1;
            for (final Set<E> axis : this.axes) {
                hash = 31 * hash + this.size() / axis.size() * axis.hashCode();
                hash = ~(~hash);
            }
            hash += adjust;
            return ~(~hash);
        }
    }
    
    private static final class SubSet<E> extends AbstractSet<E> {
        private final ImmutableMap<E, Integer> inputSet;
        private final int mask;
        
        SubSet(final ImmutableMap<E, Integer> inputSet, final int mask) {
            this.inputSet = inputSet;
            this.mask = mask;
        }
        
        public Iterator<E> iterator() {
            return (Iterator<E>)new UnmodifiableIterator<E>() {
                final ImmutableList<E> elements = SubSet.this.inputSet.keySet().asList();
                int remainingSetBits = SubSet.this.mask;
                
                public boolean hasNext() {
                    return this.remainingSetBits != 0;
                }
                
                public E next() {
                    final int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
                    if (index == 32) {
                        throw new NoSuchElementException();
                    }
                    this.remainingSetBits &= ~(1 << index);
                    return (E)this.elements.get(index);
                }
            };
        }
        
        public int size() {
            return Integer.bitCount(this.mask);
        }
        
        public boolean contains(@Nullable final Object o) {
            final Integer index = this.inputSet.get(o);
            return index != null && (this.mask & 1 << index) != 0x0;
        }
    }
    
    private static final class PowerSet<E> extends AbstractSet<Set<E>> {
        final ImmutableMap<E, Integer> inputSet;
        
        PowerSet(final Set<E> input) {
            this.inputSet = Maps.<E>indexMap((java.util.Collection<E>)input);
            Preconditions.checkArgument(this.inputSet.size() <= 30, "Too many elements to create power set: %s > 30", this.inputSet.size());
        }
        
        public int size() {
            return 1 << this.inputSet.size();
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Iterator<Set<E>> iterator() {
            return (Iterator<Set<E>>)new AbstractIndexedListIterator<Set<E>>(this.size()) {
                @Override
                protected Set<E> get(final int setBits) {
                    return (Set<E>)new SubSet((ImmutableMap<Object, Integer>)PowerSet.this.inputSet, setBits);
                }
            };
        }
        
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof Set) {
                final Set<?> set = obj;
                return this.inputSet.keySet().containsAll((Collection)set);
            }
            return false;
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof PowerSet) {
                final PowerSet<?> that = obj;
                return this.inputSet.equals(that.inputSet);
            }
            return super.equals(obj);
        }
        
        public int hashCode() {
            return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
        }
        
        public String toString() {
            return new StringBuilder().append("powerSet(").append(this.inputSet).append(")").toString();
        }
    }
    
    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
        private final NavigableSet<E> delegate;
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableNavigableSet(final NavigableSet<E> delegate) {
            this.delegate = Preconditions.<NavigableSet<E>>checkNotNull(delegate);
        }
        
        @Override
        protected SortedSet<E> delegate() {
            return (SortedSet<E>)Collections.unmodifiableSortedSet((SortedSet)this.delegate);
        }
        
        public E lower(final E e) {
            return (E)this.delegate.lower(e);
        }
        
        public E floor(final E e) {
            return (E)this.delegate.floor(e);
        }
        
        public E ceiling(final E e) {
            return (E)this.delegate.ceiling(e);
        }
        
        public E higher(final E e) {
            return (E)this.delegate.higher(e);
        }
        
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }
        
        public E pollLast() {
            throw new UnsupportedOperationException();
        }
        
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result == null) {
                final UnmodifiableNavigableSet descendingSet = new UnmodifiableNavigableSet((java.util.NavigableSet<Object>)this.delegate.descendingSet());
                this.descendingSet = descendingSet;
                result = descendingSet;
                result.descendingSet = this;
            }
            return (NavigableSet<E>)result;
        }
        
        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator((java.util.Iterator<?>)this.delegate.descendingIterator());
        }
        
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.headSet(toElement, inclusive));
        }
        
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.tailSet(fromElement, inclusive));
        }
    }
    
    static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
        private final NavigableSet<E> delegate;
        private transient UnmodifiableNavigableSet<E> descendingSet;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableNavigableSet(final NavigableSet<E> delegate) {
            this.delegate = Preconditions.<NavigableSet<E>>checkNotNull(delegate);
        }
        
        @Override
        protected SortedSet<E> delegate() {
            return (SortedSet<E>)Collections.unmodifiableSortedSet((SortedSet)this.delegate);
        }
        
        public E lower(final E e) {
            return (E)this.delegate.lower(e);
        }
        
        public E floor(final E e) {
            return (E)this.delegate.floor(e);
        }
        
        public E ceiling(final E e) {
            return (E)this.delegate.ceiling(e);
        }
        
        public E higher(final E e) {
            return (E)this.delegate.higher(e);
        }
        
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }
        
        public E pollLast() {
            throw new UnsupportedOperationException();
        }
        
        public NavigableSet<E> descendingSet() {
            UnmodifiableNavigableSet<E> result = this.descendingSet;
            if (result == null) {
                final UnmodifiableNavigableSet descendingSet = new UnmodifiableNavigableSet<E>((NavigableSet<E>)this.delegate.descendingSet());
                this.descendingSet = (UnmodifiableNavigableSet<E>)descendingSet;
                result = (UnmodifiableNavigableSet<E>)descendingSet;
                result.descendingSet = this;
            }
            return (NavigableSet<E>)result;
        }
        
        public Iterator<E> descendingIterator() {
            return Iterators.unmodifiableIterator((java.util.Iterator<?>)this.delegate.descendingIterator());
        }
        
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
        }
        
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.headSet(toElement, inclusive));
        }
        
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return Sets.<E>unmodifiableNavigableSet((java.util.NavigableSet<E>)this.delegate.tailSet(fromElement, inclusive));
        }
    }
    
    @GwtIncompatible
    static class DescendingSet<E> extends ForwardingNavigableSet<E> {
        private final NavigableSet<E> forward;
        
        DescendingSet(final NavigableSet<E> forward) {
            this.forward = forward;
        }
        
        @Override
        protected NavigableSet<E> delegate() {
            return this.forward;
        }
        
        @Override
        public E lower(final E e) {
            return (E)this.forward.higher(e);
        }
        
        @Override
        public E floor(final E e) {
            return (E)this.forward.ceiling(e);
        }
        
        @Override
        public E ceiling(final E e) {
            return (E)this.forward.floor(e);
        }
        
        @Override
        public E higher(final E e) {
            return (E)this.forward.lower(e);
        }
        
        @Override
        public E pollFirst() {
            return (E)this.forward.pollLast();
        }
        
        @Override
        public E pollLast() {
            return (E)this.forward.pollFirst();
        }
        
        @Override
        public NavigableSet<E> descendingSet() {
            return this.forward;
        }
        
        @Override
        public Iterator<E> descendingIterator() {
            return (Iterator<E>)this.forward.iterator();
        }
        
        @Override
        public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
            return (NavigableSet<E>)this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
        }
        
        @Override
        public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
            return (NavigableSet<E>)this.forward.tailSet(toElement, inclusive).descendingSet();
        }
        
        @Override
        public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
            return (NavigableSet<E>)this.forward.headSet(fromElement, inclusive).descendingSet();
        }
        
        @Override
        public Comparator<? super E> comparator() {
            final Comparator<? super E> forwardComparator = this.forward.comparator();
            if (forwardComparator == null) {
                return Ordering.<Comparable>natural().reverse();
            }
            return DescendingSet.reverse(forwardComparator);
        }
        
        private static <T> Ordering<T> reverse(final Comparator<T> forward) {
            return Ordering.<T>from(forward).<T>reverse();
        }
        
        @Override
        public E first() {
            return (E)this.forward.last();
        }
        
        @Override
        public SortedSet<E> headSet(final E toElement) {
            return this.standardHeadSet(toElement);
        }
        
        @Override
        public E last() {
            return (E)this.forward.first();
        }
        
        @Override
        public SortedSet<E> subSet(final E fromElement, final E toElement) {
            return this.standardSubSet(fromElement, toElement);
        }
        
        @Override
        public SortedSet<E> tailSet(final E fromElement) {
            return this.standardTailSet(fromElement);
        }
        
        public Iterator<E> iterator() {
            return (Iterator<E>)this.forward.descendingIterator();
        }
        
        public Object[] toArray() {
            return this.standardToArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return this.<T>standardToArray(array);
        }
        
        public String toString() {
            return this.standardToString();
        }
    }
}
