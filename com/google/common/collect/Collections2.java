package com.google.common.collect;

import java.util.Arrays;
import java.util.ArrayList;
import com.google.common.math.IntMath;
import java.util.Collections;
import com.google.common.math.LongMath;
import java.util.function.Consumer;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.AbstractCollection;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.List;
import com.google.common.base.Predicates;
import com.google.common.base.Function;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Collection;
import com.google.common.base.Joiner;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER;
    
    private Collections2() {
    }
    
    public static <E> Collection<E> filter(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
        if (unfiltered instanceof FilteredCollection) {
            return (Collection<E>)((FilteredCollection)unfiltered).createCombined(predicate);
        }
        return (Collection<E>)new FilteredCollection((java.util.Collection<Object>)Preconditions.<Collection<E>>checkNotNull(unfiltered), Preconditions.<Predicate<? super Object>>checkNotNull(predicate));
    }
    
    static boolean safeContains(final Collection<?> collection, @Nullable final Object object) {
        Preconditions.<Collection<?>>checkNotNull(collection);
        try {
            return collection.contains(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    static boolean safeRemove(final Collection<?> collection, @Nullable final Object object) {
        Preconditions.<Collection<?>>checkNotNull(collection);
        try {
            return collection.remove(object);
        }
        catch (ClassCastException e) {
            return false;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }
    
    public static <F, T> Collection<T> transform(final Collection<F> fromCollection, final Function<? super F, T> function) {
        return (Collection<T>)new TransformedCollection((java.util.Collection<Object>)fromCollection, function);
    }
    
    static boolean containsAllImpl(final Collection<?> self, final Collection<?> c) {
        return Iterables.all((java.lang.Iterable<Object>)c, Predicates.in(self));
    }
    
    static String toStringImpl(final Collection<?> collection) {
        final StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
        Collections2.STANDARD_JOINER.appendTo(sb, Iterables.transform((java.lang.Iterable<Object>)collection, new Function<Object, Object>() {
            public Object apply(final Object input) {
                return (input == collection) ? "(this Collection)" : input;
            }
        }));
        return sb.append(']').toString();
    }
    
    static StringBuilder newStringBuilderForCollection(final int size) {
        CollectPreconditions.checkNonnegative(size, "size");
        return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
    }
    
    static <T> Collection<T> cast(final Iterable<T> iterable) {
        return (Collection<T>)iterable;
    }
    
    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(final Iterable<E> elements) {
        return Collections2.<E>orderedPermutations(elements, (java.util.Comparator<? super E>)Ordering.<Comparable>natural());
    }
    
    @Beta
    public static <E> Collection<List<E>> orderedPermutations(final Iterable<E> elements, final Comparator<? super E> comparator) {
        return (Collection<List<E>>)new OrderedPermutationCollection((java.lang.Iterable<Object>)elements, (java.util.Comparator<? super Object>)comparator);
    }
    
    @Beta
    public static <E> Collection<List<E>> permutations(final Collection<E> elements) {
        return (Collection<List<E>>)new PermutationCollection(ImmutableList.copyOf((java.util.Collection<?>)elements));
    }
    
    private static boolean isPermutation(final List<?> first, final List<?> second) {
        if (first.size() != second.size()) {
            return false;
        }
        final Multiset<?> firstMultiset = HashMultiset.create((java.lang.Iterable<?>)first);
        final Multiset<?> secondMultiset = HashMultiset.create((java.lang.Iterable<?>)second);
        return firstMultiset.equals(secondMultiset);
    }
    
    private static boolean isPositiveInt(final long n) {
        return n >= 0L && n <= 2147483647L;
    }
    
    static {
        STANDARD_JOINER = Joiner.on(", ").useForNull("null");
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf(collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Collection<E> unfiltered;
        final Predicate<? super E> predicate;
        
        FilteredCollection(final Collection<E> unfiltered, final Predicate<? super E> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }
        
        FilteredCollection<E> createCombined(final Predicate<? super E> newPredicate) {
            return new FilteredCollection<E>(this.unfiltered, Predicates.and(this.predicate, newPredicate));
        }
        
        public boolean add(final E element) {
            Preconditions.checkArgument(this.predicate.apply(element));
            return this.unfiltered.add(element);
        }
        
        public boolean addAll(final Collection<? extends E> collection) {
            for (final E element : collection) {
                Preconditions.checkArgument(this.predicate.apply(element));
            }
            return this.unfiltered.addAll((Collection)collection);
        }
        
        public void clear() {
            Iterables.removeIf((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public boolean contains(@Nullable final Object element) {
            if (Collections2.safeContains(this.unfiltered, element)) {
                final E e = (E)element;
                return this.predicate.apply(e);
            }
            return false;
        }
        
        public boolean containsAll(final Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }
        
        public boolean isEmpty() {
            return !Iterables.any((java.lang.Iterable<Object>)this.unfiltered, this.predicate);
        }
        
        public Iterator<E> iterator() {
            return Iterators.filter((java.util.Iterator<Object>)this.unfiltered.iterator(), this.predicate);
        }
        
        public Spliterator<E> spliterator() {
            return CollectSpliterators.<E>filter((java.util.Spliterator<E>)this.unfiltered.spliterator(), (java.util.function.Predicate<? super E>)this.predicate);
        }
        
        public void forEach(final Consumer<? super E> action) {
            Preconditions.<Consumer<? super E>>checkNotNull(action);
            this.unfiltered.forEach(e -> {
                if (this.predicate.test(e)) {
                    action.accept(e);
                }
            });
        }
        
        public boolean remove(final Object element) {
            return this.contains(element) && this.unfiltered.remove(element);
        }
        
        public boolean removeAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)collection::contains);
        }
        
        public boolean retainAll(final Collection<?> collection) {
            return this.removeIf((java.util.function.Predicate<? super E>)(element -> !collection.contains(element)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super E> filter) {
            Preconditions.<java.util.function.Predicate<? super E>>checkNotNull(filter);
            return this.unfiltered.removeIf(element -> this.predicate.apply(element) && filter.test(element));
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public Object[] toArray() {
            return Lists.newArrayList(this.iterator()).toArray();
        }
        
        public <T> T[] toArray(final T[] array) {
            return (T[])Lists.newArrayList(this.iterator()).toArray((Object[])array);
        }
    }
    
    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;
        
        TransformedCollection(final Collection<F> fromCollection, final Function<? super F, ? extends T> function) {
            this.fromCollection = Preconditions.<Collection<F>>checkNotNull(fromCollection);
            this.function = Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        }
        
        public void clear() {
            this.fromCollection.clear();
        }
        
        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }
        
        public Iterator<T> iterator() {
            return Iterators.<Object, T>transform((java.util.Iterator<Object>)this.fromCollection.iterator(), this.function);
        }
        
        public Spliterator<T> spliterator() {
            return CollectSpliterators.<Object, T>map((java.util.Spliterator<Object>)this.fromCollection.spliterator(), (java.util.function.Function<? super Object, ? extends T>)this.function);
        }
        
        public void forEach(final Consumer<? super T> action) {
            Preconditions.<Consumer<? super T>>checkNotNull(action);
            this.fromCollection.forEach(f -> action.accept(this.function.apply((Object)f)));
        }
        
        public boolean removeIf(final java.util.function.Predicate<? super T> filter) {
            Preconditions.<java.util.function.Predicate<? super T>>checkNotNull(filter);
            return this.fromCollection.removeIf(element -> filter.test(this.function.apply((Object)element)));
        }
        
        public int size() {
            return this.fromCollection.size();
        }
    }
    
    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;
        final Comparator<? super E> comparator;
        final int size;
        
        OrderedPermutationCollection(final Iterable<E> input, final Comparator<? super E> comparator) {
            this.inputList = Ordering.from(comparator).<E>immutableSortedCopy(input);
            this.comparator = comparator;
            this.size = OrderedPermutationCollection.calculateSize((java.util.List<Object>)this.inputList, (java.util.Comparator<? super Object>)comparator);
        }
        
        private static <E> int calculateSize(final List<E> sortedInputList, final Comparator<? super E> comparator) {
            long permutations = 1L;
            int n;
            int r;
            for (n = 1, r = 1; n < sortedInputList.size(); ++n, ++r) {
                final int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
                if (comparison < 0) {
                    permutations *= LongMath.binomial(n, r);
                    r = 0;
                    if (!isPositiveInt(permutations)) {
                        return Integer.MAX_VALUE;
                    }
                }
            }
            permutations *= LongMath.binomial(n, r);
            if (!isPositiveInt(permutations)) {
                return Integer.MAX_VALUE;
            }
            return (int)permutations;
        }
        
        public int size() {
            return this.size;
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Iterator<List<E>> iterator() {
            return (Iterator<List<E>>)new OrderedPermutationIterator((java.util.List<Object>)this.inputList, (java.util.Comparator<? super Object>)this.comparator);
        }
        
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof List) {
                final List<?> list = obj;
                return isPermutation(this.inputList, list);
            }
            return false;
        }
        
        public String toString() {
            return new StringBuilder().append("orderedPermutationCollection(").append(this.inputList).append(")").toString();
        }
    }
    
    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        List<E> nextPermutation;
        final Comparator<? super E> comparator;
        
        OrderedPermutationIterator(final List<E> list, final Comparator<? super E> comparator) {
            this.nextPermutation = Lists.newArrayList((java.lang.Iterable<?>)list);
            this.comparator = comparator;
        }
        
        @Override
        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return (List<E>)this.endOfData();
            }
            final ImmutableList<E> next = ImmutableList.<E>copyOf((java.util.Collection<? extends E>)this.nextPermutation);
            this.calculateNextPermutation();
            return (List<E>)next;
        }
        
        void calculateNextPermutation() {
            final int j = this.findNextJ();
            if (j == -1) {
                this.nextPermutation = null;
                return;
            }
            final int l = this.findNextL(j);
            Collections.swap((List)this.nextPermutation, j, l);
            final int n = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(j + 1, n));
        }
        
        int findNextJ() {
            for (int k = this.nextPermutation.size() - 2; k >= 0; --k) {
                if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
                    return k;
                }
            }
            return -1;
        }
        
        int findNextL(final int j) {
            final E ak = (E)this.nextPermutation.get(j);
            for (int l = this.nextPermutation.size() - 1; l > j; --l) {
                if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
                    return l;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }
    
    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        List<E> nextPermutation;
        final Comparator<? super E> comparator;
        
        OrderedPermutationIterator(final List<E> list, final Comparator<? super E> comparator) {
            this.nextPermutation = (java.util.List<E>)Lists.newArrayList((java.lang.Iterable<?>)list);
            this.comparator = comparator;
        }
        
        @Override
        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return (List<E>)this.endOfData();
            }
            final ImmutableList<E> next = ImmutableList.<E>copyOf((java.util.Collection<? extends E>)this.nextPermutation);
            this.calculateNextPermutation();
            return (List<E>)next;
        }
        
        void calculateNextPermutation() {
            final int j = this.findNextJ();
            if (j == -1) {
                this.nextPermutation = null;
                return;
            }
            final int l = this.findNextL(j);
            Collections.swap((List)this.nextPermutation, j, l);
            final int n = this.nextPermutation.size();
            Collections.reverse(this.nextPermutation.subList(j + 1, n));
        }
        
        int findNextJ() {
            for (int k = this.nextPermutation.size() - 2; k >= 0; --k) {
                if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
                    return k;
                }
            }
            return -1;
        }
        
        int findNextL(final int j) {
            final E ak = (E)this.nextPermutation.get(j);
            for (int l = this.nextPermutation.size() - 1; l > j; --l) {
                if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
                    return l;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }
    
    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;
        
        PermutationCollection(final ImmutableList<E> input) {
            this.inputList = input;
        }
        
        public int size() {
            return IntMath.factorial(this.inputList.size());
        }
        
        public boolean isEmpty() {
            return false;
        }
        
        public Iterator<List<E>> iterator() {
            return (Iterator<List<E>>)new PermutationIterator((java.util.List<Object>)this.inputList);
        }
        
        public boolean contains(@Nullable final Object obj) {
            if (obj instanceof List) {
                final List<?> list = obj;
                return isPermutation(this.inputList, list);
            }
            return false;
        }
        
        public String toString() {
            return new StringBuilder().append("permutations(").append(this.inputList).append(")").toString();
        }
    }
    
    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
        final List<E> list;
        final int[] c;
        final int[] o;
        int j;
        
        PermutationIterator(final List<E> list) {
            this.list = (List<E>)new ArrayList((Collection)list);
            final int n = list.size();
            this.c = new int[n];
            this.o = new int[n];
            Arrays.fill(this.c, 0);
            Arrays.fill(this.o, 1);
            this.j = Integer.MAX_VALUE;
        }
        
        @Override
        protected List<E> computeNext() {
            if (this.j <= 0) {
                return (List<E>)this.endOfData();
            }
            final ImmutableList<E> next = ImmutableList.<E>copyOf((java.util.Collection<? extends E>)this.list);
            this.calculateNextPermutation();
            return (List<E>)next;
        }
        
        void calculateNextPermutation() {
            this.j = this.list.size() - 1;
            int s = 0;
            if (this.j == -1) {
                return;
            }
            while (true) {
                final int q = this.c[this.j] + this.o[this.j];
                if (q < 0) {
                    this.switchDirection();
                }
                else {
                    if (q != this.j + 1) {
                        Collections.swap((List)this.list, this.j - this.c[this.j] + s, this.j - q + s);
                        this.c[this.j] = q;
                        break;
                    }
                    if (this.j == 0) {
                        break;
                    }
                    ++s;
                    this.switchDirection();
                }
            }
        }
        
        void switchDirection() {
            this.o[this.j] = -this.o[this.j];
            --this.j;
        }
    }
}
