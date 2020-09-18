package com.google.common.collect;

import java.util.stream.Stream;
import com.google.common.base.Joiner;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.List;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import com.google.common.annotations.Beta;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Optional;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class FluentIterable<E> implements Iterable<E> {
    private final Optional<Iterable<E>> iterableDelegate;
    
    protected FluentIterable() {
        this.iterableDelegate = Optional.<Iterable<E>>absent();
    }
    
    FluentIterable(final Iterable<E> iterable) {
        Preconditions.<Iterable<E>>checkNotNull(iterable);
        this.iterableDelegate = Optional.<Iterable<E>>fromNullable((this != iterable) ? iterable : null);
    }
    
    private Iterable<E> getDelegate() {
        return this.iterableDelegate.or((Iterable<E>)this);
    }
    
    public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
        return (iterable instanceof FluentIterable) ? ((FluentIterable)iterable) : new FluentIterable<E>(iterable) {
            public Iterator<E> iterator() {
                return (Iterator<E>)iterable.iterator();
            }
        };
    }
    
    @Beta
    public static <E> FluentIterable<E> from(final E[] elements) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Arrays.asList((Object[])elements));
    }
    
    @Deprecated
    public static <E> FluentIterable<E> from(final FluentIterable<E> iterable) {
        return Preconditions.<FluentIterable<E>>checkNotNull(iterable);
    }
    
    @Beta
    public static <T> FluentIterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b) {
        return FluentIterable.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)ImmutableList.<Iterable<? extends T>>of(a, b));
    }
    
    @Beta
    public static <T> FluentIterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c) {
        return FluentIterable.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)ImmutableList.<Iterable<? extends T>>of(a, b, c));
    }
    
    @Beta
    public static <T> FluentIterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c, final Iterable<? extends T> d) {
        return FluentIterable.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)ImmutableList.<Iterable<? extends T>>of(a, b, c, d));
    }
    
    @Beta
    public static <T> FluentIterable<T> concat(final Iterable<? extends T>... inputs) {
        return FluentIterable.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)ImmutableList.<Iterable<? extends T>>copyOf(inputs));
    }
    
    @Beta
    public static <T> FluentIterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
        Preconditions.<Iterable<? extends Iterable<? extends T>>>checkNotNull(inputs);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.<T>concat((java.util.Iterator<? extends java.util.Iterator<? extends T>>)Iterables.transform((java.lang.Iterable<Object>)inputs, Iterables.toIterator()).iterator());
            }
        };
    }
    
    @Beta
    public static <E> FluentIterable<E> of() {
        return FluentIterable.<E>from((java.lang.Iterable<E>)ImmutableList.of());
    }
    
    @Deprecated
    @Beta
    public static <E> FluentIterable<E> of(final E[] elements) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Lists.<E>newArrayList(elements));
    }
    
    @Beta
    public static <E> FluentIterable<E> of(@Nullable final E element, final E... elements) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Lists.<E>asList(element, elements));
    }
    
    public String toString() {
        return Iterables.toString(this.getDelegate());
    }
    
    public final int size() {
        return Iterables.size(this.getDelegate());
    }
    
    public final boolean contains(@Nullable final Object target) {
        return Iterables.contains(this.getDelegate(), target);
    }
    
    public final FluentIterable<E> cycle() {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Iterables.<E>cycle(this.getDelegate()));
    }
    
    @Beta
    public final FluentIterable<E> append(final Iterable<? extends E> other) {
        return FluentIterable.<E>from(FluentIterable.<E>concat(this.getDelegate(), other));
    }
    
    @Beta
    public final FluentIterable<E> append(final E... elements) {
        return FluentIterable.<E>from(FluentIterable.<E>concat(this.getDelegate(), (java.lang.Iterable<? extends E>)Arrays.asList((Object[])elements)));
    }
    
    public final FluentIterable<E> filter(final Predicate<? super E> predicate) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Iterables.<E>filter(this.getDelegate(), predicate));
    }
    
    @GwtIncompatible
    public final <T> FluentIterable<T> filter(final Class<T> type) {
        return FluentIterable.<T>from((java.lang.Iterable<T>)Iterables.<E>filter(this.getDelegate(), (java.lang.Class<E>)type));
    }
    
    public final boolean anyMatch(final Predicate<? super E> predicate) {
        return Iterables.any(this.getDelegate(), predicate);
    }
    
    public final boolean allMatch(final Predicate<? super E> predicate) {
        return Iterables.all(this.getDelegate(), predicate);
    }
    
    public final Optional<E> firstMatch(final Predicate<? super E> predicate) {
        return Iterables.<E>tryFind(this.getDelegate(), predicate);
    }
    
    public final <T> FluentIterable<T> transform(final Function<? super E, T> function) {
        return FluentIterable.<T>from(Iterables.<Object, T>transform(this.getDelegate(), function));
    }
    
    public <T> FluentIterable<T> transformAndConcat(final Function<? super E, ? extends Iterable<? extends T>> function) {
        return FluentIterable.<T>from(FluentIterable.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)this.transform(function)));
    }
    
    public final Optional<E> first() {
        final Iterator<E> iterator = (Iterator<E>)this.getDelegate().iterator();
        return iterator.hasNext() ? Optional.<E>of(iterator.next()) : Optional.<E>absent();
    }
    
    public final Optional<E> last() {
        final Iterable<E> iterable = this.getDelegate();
        if (iterable instanceof List) {
            final List<E> list = (List<E>)iterable;
            if (list.isEmpty()) {
                return Optional.<E>absent();
            }
            return Optional.<E>of(list.get(list.size() - 1));
        }
        else {
            final Iterator<E> iterator = (Iterator<E>)iterable.iterator();
            if (!iterator.hasNext()) {
                return Optional.<E>absent();
            }
            if (iterable instanceof SortedSet) {
                final SortedSet<E> sortedSet = (SortedSet<E>)iterable;
                return Optional.<E>of(sortedSet.last());
            }
            E current;
            do {
                current = (E)iterator.next();
            } while (iterator.hasNext());
            return Optional.<E>of(current);
        }
    }
    
    public final FluentIterable<E> skip(final int numberToSkip) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Iterables.<E>skip(this.getDelegate(), numberToSkip));
    }
    
    public final FluentIterable<E> limit(final int maxSize) {
        return FluentIterable.<E>from((java.lang.Iterable<E>)Iterables.<E>limit(this.getDelegate(), maxSize));
    }
    
    public final boolean isEmpty() {
        return !this.getDelegate().iterator().hasNext();
    }
    
    public final ImmutableList<E> toList() {
        return ImmutableList.<E>copyOf(this.getDelegate());
    }
    
    public final ImmutableList<E> toSortedList(final Comparator<? super E> comparator) {
        return Ordering.from(comparator).<E>immutableSortedCopy(this.getDelegate());
    }
    
    public final ImmutableSet<E> toSet() {
        return ImmutableSet.<E>copyOf(this.getDelegate());
    }
    
    public final ImmutableSortedSet<E> toSortedSet(final Comparator<? super E> comparator) {
        return ImmutableSortedSet.<E>copyOf(comparator, this.getDelegate());
    }
    
    public final ImmutableMultiset<E> toMultiset() {
        return ImmutableMultiset.<E>copyOf(this.getDelegate());
    }
    
    public final <V> ImmutableMap<E, V> toMap(final Function<? super E, V> valueFunction) {
        return Maps.<E, V>toMap(this.getDelegate(), valueFunction);
    }
    
    public final <K> ImmutableListMultimap<K, E> index(final Function<? super E, K> keyFunction) {
        return Multimaps.<K, E>index(this.getDelegate(), keyFunction);
    }
    
    public final <K> ImmutableMap<K, E> uniqueIndex(final Function<? super E, K> keyFunction) {
        return Maps.<K, E>uniqueIndex(this.getDelegate(), keyFunction);
    }
    
    @GwtIncompatible
    public final E[] toArray(final Class<E> type) {
        return Iterables.<E>toArray(this.getDelegate(), type);
    }
    
    @CanIgnoreReturnValue
    public final <C extends Collection<? super E>> C copyInto(final C collection) {
        Preconditions.<C>checkNotNull(collection);
        final Iterable<E> iterable = this.getDelegate();
        if (iterable instanceof Collection) {
            collection.addAll((Collection)Collections2.<E>cast(iterable));
        }
        else {
            for (final E item : iterable) {
                collection.add(item);
            }
        }
        return collection;
    }
    
    @Beta
    public final String join(final Joiner joiner) {
        return joiner.join(this);
    }
    
    public final E get(final int position) {
        return Iterables.<E>get(this.getDelegate(), position);
    }
    
    public final Stream<E> stream() {
        return Streams.<E>stream(this.getDelegate());
    }
    
    private static class FromIterableFunction<E> implements Function<Iterable<E>, FluentIterable<E>> {
        public FluentIterable<E> apply(final Iterable<E> fromObject) {
            return FluentIterable.<E>from(fromObject);
        }
    }
}
