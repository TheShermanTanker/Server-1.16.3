package com.google.common.collect;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ListIterator;
import com.google.common.annotations.Beta;
import java.util.Comparator;
import java.util.Enumeration;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import java.util.Collections;
import java.util.Arrays;
import java.util.NoSuchElementException;
import com.google.common.annotations.GwtIncompatible;
import java.util.List;
import com.google.common.base.Objects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import javax.annotation.Nullable;
import com.google.common.primitives.Ints;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Iterators {
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR;
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR;
    
    private Iterators() {
    }
    
    static <T> UnmodifiableIterator<T> emptyIterator() {
        return Iterators.emptyListIterator();
    }
    
    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return (UnmodifiableListIterator<T>)Iterators.EMPTY_LIST_ITERATOR;
    }
    
    static <T> Iterator<T> emptyModifiableIterator() {
        return (Iterator<T>)Iterators.EMPTY_MODIFIABLE_ITERATOR;
    }
    
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
        Preconditions.<Iterator<? extends T>>checkNotNull(iterator);
        if (iterator instanceof UnmodifiableIterator) {
            final UnmodifiableIterator<T> result = (UnmodifiableIterator<T>)(UnmodifiableIterator)iterator;
            return result;
        }
        return new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            public T next() {
                return (T)iterator.next();
            }
        };
    }
    
    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final UnmodifiableIterator<T> iterator) {
        return Preconditions.<UnmodifiableIterator<T>>checkNotNull(iterator);
    }
    
    public static int size(final Iterator<?> iterator) {
        long count = 0L;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        return Ints.saturatedCast(count);
    }
    
    public static boolean contains(final Iterator<?> iterator, @Nullable final Object element) {
        return Iterators.any(iterator, Predicates.equalTo(element));
    }
    
    @CanIgnoreReturnValue
    public static boolean removeAll(final Iterator<?> removeFrom, final Collection<?> elementsToRemove) {
        return Iterators.removeIf(removeFrom, Predicates.in(elementsToRemove));
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean removeIf(final Iterator<T> removeFrom, final Predicate<? super T> predicate) {
        Preconditions.<Predicate<? super T>>checkNotNull(predicate);
        boolean modified = false;
        while (removeFrom.hasNext()) {
            if (predicate.apply(removeFrom.next())) {
                removeFrom.remove();
                modified = true;
            }
        }
        return modified;
    }
    
    @CanIgnoreReturnValue
    public static boolean retainAll(final Iterator<?> removeFrom, final Collection<?> elementsToRetain) {
        return Iterators.removeIf(removeFrom, Predicates.not(Predicates.in((java.util.Collection<? extends T>)elementsToRetain)));
    }
    
    public static boolean elementsEqual(final Iterator<?> iterator1, final Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            final Object o1 = iterator1.next();
            final Object o2 = iterator2.next();
            if (!Objects.equal(o1, o2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }
    
    public static String toString(final Iterator<?> iterator) {
        return Collections2.STANDARD_JOINER.appendTo(new StringBuilder().append('['), iterator).append(']').toString();
    }
    
    @CanIgnoreReturnValue
    public static <T> T getOnlyElement(final Iterator<T> iterator) {
        final T first = (T)iterator.next();
        if (!iterator.hasNext()) {
            return first;
        }
        final StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
        for (int i = 0; i < 4 && iterator.hasNext(); ++i) {
            sb.append(", ").append(iterator.next());
        }
        if (iterator.hasNext()) {
            sb.append(", ...");
        }
        sb.append('>');
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Nullable
    @CanIgnoreReturnValue
    public static <T> T getOnlyElement(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return iterator.hasNext() ? Iterators.<T>getOnlyElement((java.util.Iterator<T>)iterator) : defaultValue;
    }
    
    @GwtIncompatible
    public static <T> T[] toArray(final Iterator<? extends T> iterator, final Class<T> type) {
        final List<T> list = Lists.newArrayList((java.util.Iterator<?>)iterator);
        return Iterables.<T>toArray((java.lang.Iterable<? extends T>)list, type);
    }
    
    @CanIgnoreReturnValue
    public static <T> boolean addAll(final Collection<T> addTo, final Iterator<? extends T> iterator) {
        Preconditions.<Collection<T>>checkNotNull(addTo);
        Preconditions.<Iterator<? extends T>>checkNotNull(iterator);
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }
    
    public static int frequency(final Iterator<?> iterator, @Nullable final Object element) {
        return size(Iterators.filter(iterator, Predicates.equalTo(element)));
    }
    
    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.<Iterable<T>>checkNotNull(iterable);
        return (Iterator<T>)new Iterator<T>() {
            Iterator<T> iterator = Iterators.<T>emptyModifiableIterator();
            
            public boolean hasNext() {
                return this.iterator.hasNext() || iterable.iterator().hasNext();
            }
            
            public T next() {
                if (!this.iterator.hasNext()) {
                    this.iterator = (Iterator<T>)iterable.iterator();
                    if (!this.iterator.hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                return (T)this.iterator.next();
            }
            
            public void remove() {
                this.iterator.remove();
            }
        };
    }
    
    @SafeVarargs
    public static <T> Iterator<T> cycle(final T... elements) {
        return Iterators.<T>cycle((java.lang.Iterable<T>)Lists.<T>newArrayList(elements));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b) {
        Preconditions.<Iterator<? extends T>>checkNotNull(a);
        Preconditions.<Iterator<? extends T>>checkNotNull(b);
        return Iterators.<T>concat((java.util.Iterator<? extends java.util.Iterator<? extends T>>)new ConsumingQueueIterator((Object[])new Iterator[] { a, b }));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c) {
        Preconditions.<Iterator<? extends T>>checkNotNull(a);
        Preconditions.<Iterator<? extends T>>checkNotNull(b);
        Preconditions.<Iterator<? extends T>>checkNotNull(c);
        return Iterators.<T>concat((java.util.Iterator<? extends java.util.Iterator<? extends T>>)new ConsumingQueueIterator((Object[])new Iterator[] { a, b, c }));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T> a, final Iterator<? extends T> b, final Iterator<? extends T> c, final Iterator<? extends T> d) {
        Preconditions.<Iterator<? extends T>>checkNotNull(a);
        Preconditions.<Iterator<? extends T>>checkNotNull(b);
        Preconditions.<Iterator<? extends T>>checkNotNull(c);
        Preconditions.<Iterator<? extends T>>checkNotNull(d);
        return Iterators.<T>concat((java.util.Iterator<? extends java.util.Iterator<? extends T>>)new ConsumingQueueIterator((Object[])new Iterator[] { a, b, c, d }));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends T>... inputs) {
        for (final Iterator<? extends T> input : Preconditions.<Iterator<? extends T>[]>checkNotNull(inputs)) {
            Preconditions.<Iterator<? extends T>>checkNotNull(input);
        }
        return Iterators.<T>concat((java.util.Iterator<? extends java.util.Iterator<? extends T>>)new ConsumingQueueIterator((Object[])inputs));
    }
    
    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> inputs) {
        return (Iterator<T>)new ConcatenatedIterator(inputs);
    }
    
    public static <T> UnmodifiableIterator<List<T>> partition(final Iterator<T> iterator, final int size) {
        return Iterators.<T>partitionImpl(iterator, size, false);
    }
    
    public static <T> UnmodifiableIterator<List<T>> paddedPartition(final Iterator<T> iterator, final int size) {
        return Iterators.<T>partitionImpl(iterator, size, true);
    }
    
    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
        Preconditions.<Iterator<T>>checkNotNull(iterator);
        Preconditions.checkArgument(size > 0);
        return new UnmodifiableIterator<List<T>>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            public List<T> next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Object[] array = new Object[size];
                int count;
                for (count = 0; count < size && iterator.hasNext(); ++count) {
                    array[count] = iterator.next();
                }
                for (int i = count; i < size; ++i) {
                    array[i] = null;
                }
                final List<T> list = (List<T>)Collections.unmodifiableList(Arrays.asList(array));
                return (List<T>)((pad || count == size) ? list : list.subList(0, count));
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
        Preconditions.<Iterator<T>>checkNotNull(unfiltered);
        Preconditions.<Predicate<? super T>>checkNotNull(retainIfTrue);
        return new AbstractIterator<T>() {
            @Override
            protected T computeNext() {
                while (unfiltered.hasNext()) {
                    final T element = (T)unfiltered.next();
                    if (retainIfTrue.apply(element)) {
                        return element;
                    }
                }
                return this.endOfData();
            }
        };
    }
    
    @GwtIncompatible
    public static <T> UnmodifiableIterator<T> filter(final Iterator<?> unfiltered, final Class<T> desiredType) {
        return Iterators.<T>filter(unfiltered, Predicates.instanceOf(desiredType));
    }
    
    public static <T> boolean any(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return Iterators.<T>indexOf(iterator, predicate) != -1;
    }
    
    public static <T> boolean all(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.<Predicate<? super T>>checkNotNull(predicate);
        while (iterator.hasNext()) {
            final T element = (T)iterator.next();
            if (!predicate.apply(element)) {
                return false;
            }
        }
        return true;
    }
    
    public static <T> T find(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        return (T)Iterators.<T>filter(iterator, predicate).next();
    }
    
    @Nullable
    public static <T> T find(final Iterator<? extends T> iterator, final Predicate<? super T> predicate, @Nullable final T defaultValue) {
        return Iterators.<T>getNext((java.util.Iterator<? extends T>)Iterators.filter(iterator, predicate), defaultValue);
    }
    
    public static <T> Optional<T> tryFind(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        final UnmodifiableIterator<T> filteredIterator = Iterators.<T>filter(iterator, predicate);
        return filteredIterator.hasNext() ? Optional.<T>of(filteredIterator.next()) : Optional.<T>absent();
    }
    
    public static <T> int indexOf(final Iterator<T> iterator, final Predicate<? super T> predicate) {
        Preconditions.<Predicate<? super T>>checkNotNull(predicate, "predicate");
        int i = 0;
        while (iterator.hasNext()) {
            final T current = (T)iterator.next();
            if (predicate.apply(current)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static <F, T> Iterator<T> transform(final Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
        Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        return (Iterator<T>)new TransformedIterator<F, T>(fromIterator) {
            @Override
            T transform(final F from) {
                return function.apply(from);
            }
        };
    }
    
    public static <T> T get(final Iterator<T> iterator, final int position) {
        checkNonnegative(position);
        final int skipped = advance(iterator, position);
        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("position (").append(position).append(") must be less than the number of elements that remained (").append(skipped).append(")").toString());
        }
        return (T)iterator.next();
    }
    
    static void checkNonnegative(final int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("position (").append(position).append(") must not be negative").toString());
        }
    }
    
    @Nullable
    public static <T> T get(final Iterator<? extends T> iterator, final int position, @Nullable final T defaultValue) {
        checkNonnegative(position);
        advance(iterator, position);
        return Iterators.<T>getNext(iterator, defaultValue);
    }
    
    @Nullable
    public static <T> T getNext(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return (T)(iterator.hasNext() ? iterator.next() : defaultValue);
    }
    
    public static <T> T getLast(final Iterator<T> iterator) {
        T current;
        do {
            current = (T)iterator.next();
        } while (iterator.hasNext());
        return current;
    }
    
    @Nullable
    public static <T> T getLast(final Iterator<? extends T> iterator, @Nullable final T defaultValue) {
        return iterator.hasNext() ? Iterators.<T>getLast((java.util.Iterator<T>)iterator) : defaultValue;
    }
    
    @CanIgnoreReturnValue
    public static int advance(final Iterator<?> iterator, final int numberToAdvance) {
        Preconditions.<Iterator<?>>checkNotNull(iterator);
        Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
        int i;
        for (i = 0; i < numberToAdvance && iterator.hasNext(); ++i) {
            iterator.next();
        }
        return i;
    }
    
    public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
        Preconditions.<Iterator<T>>checkNotNull(iterator);
        Preconditions.checkArgument(limitSize >= 0, "limit is negative");
        return (Iterator<T>)new Iterator<T>() {
            private int count;
            
            public boolean hasNext() {
                return this.count < limitSize && iterator.hasNext();
            }
            
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                ++this.count;
                return (T)iterator.next();
            }
            
            public void remove() {
                iterator.remove();
            }
        };
    }
    
    public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
        Preconditions.<Iterator<T>>checkNotNull(iterator);
        return (Iterator<T>)new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            public T next() {
                final T next = (T)iterator.next();
                iterator.remove();
                return next;
            }
            
            public String toString() {
                return "Iterators.consumingIterator(...)";
            }
        };
    }
    
    @Nullable
    static <T> T pollNext(final Iterator<T> iterator) {
        if (iterator.hasNext()) {
            final T result = (T)iterator.next();
            iterator.remove();
            return result;
        }
        return null;
    }
    
    static void clear(final Iterator<?> iterator) {
        Preconditions.<Iterator<?>>checkNotNull(iterator);
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
    
    @SafeVarargs
    public static <T> UnmodifiableIterator<T> forArray(final T... array) {
        return Iterators.<T>forArray(array, 0, array.length, 0);
    }
    
    static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, final int length, final int index) {
        Preconditions.checkArgument(length >= 0);
        final int end = offset + length;
        Preconditions.checkPositionIndexes(offset, end, array.length);
        Preconditions.checkPositionIndex(index, length);
        if (length == 0) {
            return Iterators.<T>emptyListIterator();
        }
        return new AbstractIndexedListIterator<T>(length, index) {
            @Override
            protected T get(final int index) {
                return array[offset + index];
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
        return new UnmodifiableIterator<T>() {
            boolean done;
            
            public boolean hasNext() {
                return !this.done;
            }
            
            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return value;
            }
        };
    }
    
    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
        Preconditions.<Enumeration<T>>checkNotNull(enumeration);
        return new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
            
            public T next() {
                return (T)enumeration.nextElement();
            }
        };
    }
    
    public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
        Preconditions.<Iterator<T>>checkNotNull(iterator);
        return (Enumeration<T>)new Enumeration<T>() {
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }
            
            public T nextElement() {
                return (T)iterator.next();
            }
        };
    }
    
    public static <T> PeekingIterator<T> peekingIterator(final Iterator<? extends T> iterator) {
        if (iterator instanceof PeekingImpl) {
            final PeekingImpl<T> peeking = (PeekingImpl<T>)(PeekingImpl)iterator;
            return peeking;
        }
        return new PeekingImpl<T>(iterator);
    }
    
    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(final PeekingIterator<T> iterator) {
        return Preconditions.<PeekingIterator<T>>checkNotNull(iterator);
    }
    
    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> comparator) {
        Preconditions.<Iterable<? extends Iterator<? extends T>>>checkNotNull(iterators, "iterators");
        Preconditions.<Comparator<? super T>>checkNotNull(comparator, "comparator");
        return new MergingIterator<T>(iterators, comparator);
    }
    
    static <T> ListIterator<T> cast(final Iterator<T> iterator) {
        return (ListIterator<T>)iterator;
    }
    
    static {
        EMPTY_LIST_ITERATOR = new UnmodifiableListIterator<Object>() {
            public boolean hasNext() {
                return false;
            }
            
            public Object next() {
                throw new NoSuchElementException();
            }
            
            public boolean hasPrevious() {
                return false;
            }
            
            public Object previous() {
                throw new NoSuchElementException();
            }
            
            public int nextIndex() {
                return 0;
            }
            
            public int previousIndex() {
                return -1;
            }
        };
        EMPTY_MODIFIABLE_ITERATOR = (Iterator)new Iterator<Object>() {
            public boolean hasNext() {
                return false;
            }
            
            public Object next() {
                throw new NoSuchElementException();
            }
            
            public void remove() {
                CollectPreconditions.checkRemove(false);
            }
        };
    }
    
    private static class PeekingImpl<E> implements PeekingIterator<E> {
        private final Iterator<? extends E> iterator;
        private boolean hasPeeked;
        private E peekedElement;
        
        public PeekingImpl(final Iterator<? extends E> iterator) {
            this.iterator = Preconditions.<Iterator<? extends E>>checkNotNull(iterator);
        }
        
        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }
        
        public E next() {
            if (!this.hasPeeked) {
                return (E)this.iterator.next();
            }
            final E result = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return result;
        }
        
        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }
        
        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = (E)this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }
    }
    
    private static class MergingIterator<T> extends UnmodifiableIterator<T> {
        final Queue<PeekingIterator<T>> queue;
        
        public MergingIterator(final Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
            final Comparator<PeekingIterator<T>> heapComparator = (Comparator<PeekingIterator<T>>)new Comparator<PeekingIterator<T>>() {
                public int compare(final PeekingIterator<T> o1, final PeekingIterator<T> o2) {
                    return itemComparator.compare(o1.peek(), o2.peek());
                }
            };
            this.queue = (Queue<PeekingIterator<T>>)new PriorityQueue(2, (Comparator)heapComparator);
            for (final Iterator<? extends T> iterator : iterators) {
                if (iterator.hasNext()) {
                    this.queue.add(Iterators.peekingIterator((java.util.Iterator<?>)iterator));
                }
            }
        }
        
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }
        
        public T next() {
            final PeekingIterator<T> nextIter = (PeekingIterator<T>)this.queue.remove();
            final T next = nextIter.next();
            if (nextIter.hasNext()) {
                this.queue.add(nextIter);
            }
            return next;
        }
    }
    
    private static class ConcatenatedIterator<T> extends MultitransformedIterator<Iterator<? extends T>, T> {
        public ConcatenatedIterator(final Iterator<? extends Iterator<? extends T>> iterators) {
            super(ConcatenatedIterator.getComponentIterators((java.util.Iterator<? extends java.util.Iterator<?>>)iterators));
        }
        
        @Override
        Iterator<? extends T> transform(final Iterator<? extends T> iterator) {
            return iterator;
        }
        
        private static <T> Iterator<Iterator<? extends T>> getComponentIterators(final Iterator<? extends Iterator<? extends T>> iterators) {
            return (Iterator<Iterator<? extends T>>)new MultitransformedIterator<Iterator<? extends T>, Iterator<? extends T>>(iterators) {
                @Override
                Iterator<? extends Iterator<? extends T>> transform(final Iterator<? extends T> iterator) {
                    if (iterator instanceof ConcatenatedIterator) {
                        final ConcatenatedIterator<? extends T> concatIterator = (ConcatenatedIterator)iterator;
                        return ConcatenatedIterator.getComponentIterators((java.util.Iterator<? extends java.util.Iterator<?>>)concatIterator.backingIterator);
                    }
                    return Iterators.<Iterator<? extends T>>singletonIterator(iterator);
                }
            };
        }
    }
}
