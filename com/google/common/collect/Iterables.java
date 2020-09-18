package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.Queue;
import java.util.NoSuchElementException;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import java.util.function.Consumer;
import java.util.List;
import java.util.stream.Stream;
import java.util.Spliterator;
import java.util.Set;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Predicate;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Iterables {
    private Iterables() {
    }
    
    public static <T> Iterable<T> unmodifiableIterable(final Iterable<? extends T> iterable) {
        Preconditions.<Iterable<? extends T>>checkNotNull(iterable);
        if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection) {
            final Iterable<T> result = (Iterable<T>)iterable;
            return result;
        }
        return (Iterable<T>)new UnmodifiableIterable((Iterable)iterable);
    }
    
    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(final ImmutableCollection<E> iterable) {
        return Preconditions.checkNotNull((Iterable)iterable);
    }
    
    public static int size(final Iterable<?> iterable) {
        return (iterable instanceof Collection) ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
    }
    
    public static boolean contains(final Iterable<?> iterable, @Nullable final Object element) {
        if (iterable instanceof Collection) {
            final Collection<?> collection = iterable;
            return Collections2.safeContains(collection, element);
        }
        return Iterators.contains(iterable.iterator(), element);
    }
    
    @CanIgnoreReturnValue
    public static boolean removeAll(final Iterable<?> removeFrom, final Collection<?> elementsToRemove) {
        return (removeFrom instanceof Collection) ? ((Collection)removeFrom).removeAll((Collection)Preconditions.<Collection<?>>checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
    }
    
    @CanIgnoreReturnValue
    public static boolean retainAll(final Iterable<?> removeFrom, final Collection<?> elementsToRetain) {
        return (removeFrom instanceof Collection) ? ((Collection)removeFrom).retainAll((Collection)Preconditions.<Collection<?>>checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean removeIf(final Iterable<T> removeFrom, final Predicate<? super T> predicate) {
        if (removeFrom instanceof Collection) {
            return ((Collection)removeFrom).removeIf((java.util.function.Predicate)predicate);
        }
        return Iterators.removeIf((java.util.Iterator<Object>)removeFrom.iterator(), predicate);
    }
    
    @Nullable
    static <T> T removeFirstMatching(final Iterable<T> removeFrom, final Predicate<? super T> predicate) {
        Preconditions.<Predicate<? super T>>checkNotNull(predicate);
        final Iterator<T> iterator = (Iterator<T>)removeFrom.iterator();
        while (iterator.hasNext()) {
            final T next = (T)iterator.next();
            if (predicate.apply(next)) {
                iterator.remove();
                return next;
            }
        }
        return null;
    }
    
    public static boolean elementsEqual(final Iterable<?> iterable1, final Iterable<?> iterable2) {
        if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
            final Collection<?> collection1 = iterable1;
            final Collection<?> collection2 = iterable2;
            if (collection1.size() != collection2.size()) {
                return false;
            }
        }
        return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
    }
    
    public static String toString(final Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }
    
    public static <T> T getOnlyElement(final Iterable<T> iterable) {
        return Iterators.<T>getOnlyElement((java.util.Iterator<T>)iterable.iterator());
    }
    
    @Nullable
    public static <T> T getOnlyElement(final Iterable<? extends T> iterable, @Nullable final T defaultValue) {
        return Iterators.<T>getOnlyElement((java.util.Iterator<? extends T>)iterable.iterator(), defaultValue);
    }
    
    @GwtIncompatible
    public static <T> T[] toArray(final Iterable<? extends T> iterable, final Class<T> type) {
        return Iterables.<T>toArray(iterable, (T[])ObjectArrays.<T>newArray((java.lang.Class<T>)type, 0));
    }
    
    static <T> T[] toArray(final Iterable<? extends T> iterable, final T[] array) {
        final Collection<? extends T> collection = Iterables.castOrCopyToCollection(iterable);
        return (T[])collection.toArray((Object[])array);
    }
    
    static Object[] toArray(final Iterable<?> iterable) {
        return Iterables.castOrCopyToCollection(iterable).toArray();
    }
    
    private static <E> Collection<E> castOrCopyToCollection(final Iterable<E> iterable) {
        return (Collection<E>)((iterable instanceof Collection) ? ((Collection)iterable) : Lists.newArrayList((java.util.Iterator<?>)iterable.iterator()));
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean addAll(final Collection<T> addTo, final Iterable<? extends T> elementsToAdd) {
        if (elementsToAdd instanceof Collection) {
            final Collection<? extends T> c = Collections2.cast(elementsToAdd);
            return addTo.addAll((Collection)c);
        }
        return Iterators.<T>addAll(addTo, (java.util.Iterator<? extends T>)Preconditions.<Iterable<? extends T>>checkNotNull(elementsToAdd).iterator());
    }
    
    public static int frequency(final Iterable<?> iterable, @Nullable final Object element) {
        if (iterable instanceof Multiset) {
            return ((Multiset)iterable).count(element);
        }
        if (iterable instanceof Set) {
            return ((Set)iterable).contains(element) ? 1 : 0;
        }
        return Iterators.frequency(iterable.iterator(), element);
    }
    
    public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.<T>cycle(iterable);
            }
            
            public Spliterator<T> spliterator() {
                return (Spliterator<T>)Stream.generate(() -> iterable).flatMap(Streams::stream).spliterator();
            }
            
            @Override
            public String toString() {
                return iterable.toString() + " (cycled)";
            }
        };
    }
    
    public static <T> Iterable<T> cycle(final T... elements) {
        return Iterables.<T>cycle((java.lang.Iterable<T>)Lists.<T>newArrayList(elements));
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b) {
        return FluentIterable.concat((java.lang.Iterable<?>)a, (java.lang.Iterable<?>)b);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c) {
        return FluentIterable.concat((java.lang.Iterable<?>)a, (java.lang.Iterable<?>)b, (java.lang.Iterable<?>)c);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T> a, final Iterable<? extends T> b, final Iterable<? extends T> c, final Iterable<? extends T> d) {
        return FluentIterable.concat((java.lang.Iterable<?>)a, (java.lang.Iterable<?>)b, (java.lang.Iterable<?>)c, (java.lang.Iterable<?>)d);
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends T>... inputs) {
        return Iterables.<T>concat((java.lang.Iterable<? extends java.lang.Iterable<? extends T>>)ImmutableList.<Iterable<? extends T>>copyOf(inputs));
    }
    
    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
        return FluentIterable.concat((java.lang.Iterable<? extends java.lang.Iterable<?>>)inputs);
    }
    
    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return (Iterable<List<T>>)new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.partition((java.util.Iterator<Object>)iterable.iterator(), size);
            }
        };
    }
    
    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        Preconditions.checkArgument(size > 0);
        return (Iterable<List<T>>)new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.paddedPartition((java.util.Iterator<Object>)iterable.iterator(), size);
            }
        };
    }
    
    public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> retainIfTrue) {
        Preconditions.<Iterable<T>>checkNotNull(unfiltered);
        Preconditions.<Predicate<? super T>>checkNotNull(retainIfTrue);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter((java.util.Iterator<Object>)unfiltered.iterator(), retainIfTrue);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                unfiltered.forEach(a -> {
                    if (retainIfTrue.test(a)) {
                        action.accept(a);
                    }
                });
            }
            
            public Spliterator<T> spliterator() {
                return CollectSpliterators.<T>filter((java.util.Spliterator<T>)unfiltered.spliterator(), (java.util.function.Predicate<? super T>)retainIfTrue);
            }
        };
    }
    
    @GwtIncompatible
    public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> desiredType) {
        Preconditions.<Iterable<?>>checkNotNull(unfiltered);
        Preconditions.<Class<T>>checkNotNull(desiredType);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(unfiltered.iterator(), (java.lang.Class<Object>)desiredType);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                unfiltered.forEach(o -> {
                    if (desiredType.isInstance(o)) {
                        action.accept(desiredType.cast(o));
                    }
                });
            }
            
            public Spliterator<T> spliterator() {
                return CollectSpliterators.<T>filter((java.util.Spliterator<T>)unfiltered.spliterator(), (java.util.function.Predicate<? super T>)desiredType::isInstance);
            }
        };
    }
    
    public static <T> boolean any(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.any((java.util.Iterator<Object>)iterable.iterator(), predicate);
    }
    
    public static <T> boolean all(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.all((java.util.Iterator<Object>)iterable.iterator(), predicate);
    }
    
    public static <T> T find(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.<T>find((java.util.Iterator<T>)iterable.iterator(), predicate);
    }
    
    @Nullable
    public static <T> T find(final Iterable<? extends T> iterable, final Predicate<? super T> predicate, @Nullable final T defaultValue) {
        return Iterators.<T>find((java.util.Iterator<? extends T>)iterable.iterator(), predicate, defaultValue);
    }
    
    public static <T> Optional<T> tryFind(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.<T>tryFind((java.util.Iterator<T>)iterable.iterator(), predicate);
    }
    
    public static <T> int indexOf(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        return Iterators.indexOf((java.util.Iterator<Object>)iterable.iterator(), predicate);
    }
    
    public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
        Preconditions.<Iterable<F>>checkNotNull(fromIterable);
        Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.<Object, T>transform((java.util.Iterator<Object>)fromIterable.iterator(), function);
            }
            
            public void forEach(final Consumer<? super T> action) {
                Preconditions.<Consumer<? super T>>checkNotNull(action);
                fromIterable.forEach(f -> action.accept(function.apply(f)));
            }
            
            public Spliterator<T> spliterator() {
                return CollectSpliterators.<Object, T>map((java.util.Spliterator<Object>)fromIterable.spliterator(), (java.util.function.Function<? super Object, ? extends T>)function);
            }
        };
    }
    
    public static <T> T get(final Iterable<T> iterable, final int position) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        return (iterable instanceof List) ? ((List)iterable).get(position) : Iterators.<T>get((java.util.Iterator<T>)iterable.iterator(), position);
    }
    
    @Nullable
    public static <T> T get(final Iterable<? extends T> iterable, final int position, @Nullable final T defaultValue) {
        Preconditions.<Iterable<? extends T>>checkNotNull(iterable);
        Iterators.checkNonnegative(position);
        if (iterable instanceof List) {
            final List<? extends T> list = Lists.cast(iterable);
            return (T)((position < list.size()) ? list.get(position) : defaultValue);
        }
        final Iterator<? extends T> iterator = iterable.iterator();
        Iterators.advance(iterator, position);
        return Iterators.<T>getNext(iterator, defaultValue);
    }
    
    @Nullable
    public static <T> T getFirst(final Iterable<? extends T> iterable, @Nullable final T defaultValue) {
        return Iterators.<T>getNext((java.util.Iterator<? extends T>)iterable.iterator(), defaultValue);
    }
    
    public static <T> T getLast(final Iterable<T> iterable) {
        if (!(iterable instanceof List)) {
            return Iterators.<T>getLast((java.util.Iterator<T>)iterable.iterator());
        }
        final List<T> list = (List<T>)iterable;
        if (list.isEmpty()) {
            throw new NoSuchElementException();
        }
        return Iterables.<T>getLastInNonemptyList(list);
    }
    
    @Nullable
    public static <T> T getLast(final Iterable<? extends T> iterable, @Nullable final T defaultValue) {
        if (iterable instanceof Collection) {
            final Collection<? extends T> c = Collections2.cast(iterable);
            if (c.isEmpty()) {
                return defaultValue;
            }
            if (iterable instanceof List) {
                return Iterables.<T>getLastInNonemptyList((java.util.List<T>)Lists.<T>cast((java.lang.Iterable<T>)iterable));
            }
        }
        return Iterators.<T>getLast((java.util.Iterator<? extends T>)iterable.iterator(), defaultValue);
    }
    
    private static <T> T getLastInNonemptyList(final List<T> list) {
        return (T)list.get(list.size() - 1);
    }
    
    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        Preconditions.checkArgument(numberToSkip >= 0, "number to skip cannot be negative");
        if (iterable instanceof List) {
            final List<T> list = (List<T>)iterable;
            return (Iterable<T>)new FluentIterable<T>() {
                public Iterator<T> iterator() {
                    final int toSkip = Math.min(list.size(), numberToSkip);
                    return (Iterator<T>)list.subList(toSkip, list.size()).iterator();
                }
            };
        }
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> iterator = (Iterator<T>)iterable.iterator();
                Iterators.advance(iterator, numberToSkip);
                return (Iterator<T>)new Iterator<T>() {
                    boolean atStart = true;
                    
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }
                    
                    public T next() {
                        final T result = (T)iterator.next();
                        this.atStart = false;
                        return result;
                    }
                    
                    public void remove() {
                        CollectPreconditions.checkRemove(!this.atStart);
                        iterator.remove();
                    }
                };
            }
            
            public Spliterator<T> spliterator() {
                return (Spliterator<T>)Streams.stream((java.lang.Iterable<Object>)iterable).skip((long)numberToSkip).spliterator();
            }
        };
    }
    
    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.<T>limit((java.util.Iterator<T>)iterable.iterator(), limitSize);
            }
            
            public Spliterator<T> spliterator() {
                return (Spliterator<T>)Streams.stream((java.lang.Iterable<Object>)iterable).limit((long)limitSize).spliterator();
            }
        };
    }
    
    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        if (iterable instanceof Queue) {
            return (Iterable<T>)new FluentIterable<T>() {
                public Iterator<T> iterator() {
                    return (Iterator<T>)new ConsumingQueueIterator((java.util.Queue<Object>)iterable);
                }
                
                @Override
                public String toString() {
                    return "Iterables.consumingIterable(...)";
                }
            };
        }
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        return (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.<T>consumingIterator((java.util.Iterator<T>)iterable.iterator());
            }
            
            @Override
            public String toString() {
                return "Iterables.consumingIterable(...)";
            }
        };
    }
    
    public static boolean isEmpty(final Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection)iterable).isEmpty();
        }
        return !iterable.iterator().hasNext();
    }
    
    @Beta
    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
        Preconditions.<Iterable<? extends Iterable<? extends T>>>checkNotNull(iterables, "iterables");
        Preconditions.<Comparator<? super T>>checkNotNull(comparator, "comparator");
        final Iterable<T> iterable = (Iterable<T>)new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.mergeSorted(Iterables.transform((java.lang.Iterable<Object>)iterables, Iterables.toIterator()), (java.util.Comparator<? super Object>)comparator);
            }
        };
        return (Iterable<T>)new UnmodifiableIterable((Iterable)iterable);
    }
    
    static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new Function<Iterable<? extends T>, Iterator<? extends T>>() {
            public Iterator<? extends T> apply(final Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }
    
    private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
        private final Iterable<? extends T> iterable;
        
        private UnmodifiableIterable(final Iterable<? extends T> iterable) {
            this.iterable = iterable;
        }
        
        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator((java.util.Iterator<?>)this.iterable.iterator());
        }
        
        public void forEach(final Consumer<? super T> action) {
            this.iterable.forEach((Consumer)action);
        }
        
        public Spliterator<T> spliterator() {
            return (Spliterator<T>)this.iterable.spliterator();
        }
        
        @Override
        public String toString() {
            return this.iterable.toString();
        }
    }
}
