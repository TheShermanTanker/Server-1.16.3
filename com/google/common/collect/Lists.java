package com.google.common.collect;

import java.util.NoSuchElementException;
import java.math.RoundingMode;
import java.util.function.Predicate;
import java.util.AbstractSequentialList;
import com.google.common.math.IntMath;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ListIterator;
import com.google.common.base.Objects;
import com.google.common.annotations.Beta;
import java.util.RandomAccess;
import com.google.common.base.Function;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Lists {
    private Lists() {
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList() {
        return (ArrayList<E>)new ArrayList();
    }
    
    @SafeVarargs
    @CanIgnoreReturnValue
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final E... elements) {
        Preconditions.<E[]>checkNotNull(elements);
        final int capacity = computeArrayListCapacity(elements.length);
        final ArrayList<E> list = (ArrayList<E>)new ArrayList(capacity);
        Collections.addAll((Collection)list, (Object[])elements);
        return list;
    }
    
    @VisibleForTesting
    static int computeArrayListCapacity(final int arraySize) {
        CollectPreconditions.checkNonnegative(arraySize, "arraySize");
        return Ints.saturatedCast(5L + arraySize + arraySize / 10);
    }
    
    @CanIgnoreReturnValue
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final Iterable<? extends E> elements) {
        Preconditions.<Iterable<? extends E>>checkNotNull(elements);
        return (ArrayList<E>)((elements instanceof Collection) ? new ArrayList((Collection)Collections2.cast(elements)) : Lists.newArrayList((java.util.Iterator<?>)elements.iterator()));
    }
    
    @CanIgnoreReturnValue
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(final Iterator<? extends E> elements) {
        final ArrayList<E> list = Lists.<E>newArrayList();
        Iterators.addAll((java.util.Collection<Object>)list, elements);
        return list;
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithCapacity(final int initialArraySize) {
        CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
        return (ArrayList<E>)new ArrayList(initialArraySize);
    }
    
    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithExpectedSize(final int estimatedSize) {
        return (ArrayList<E>)new ArrayList(computeArrayListCapacity(estimatedSize));
    }
    
    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList() {
        return (LinkedList<E>)new LinkedList();
    }
    
    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList(final Iterable<? extends E> elements) {
        final LinkedList<E> list = Lists.<E>newLinkedList();
        Iterables.addAll((java.util.Collection<Object>)list, elements);
        return list;
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return (CopyOnWriteArrayList<E>)new CopyOnWriteArrayList();
    }
    
    @GwtIncompatible
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(final Iterable<? extends E> elements) {
        final Collection<? extends E> elementsCollection = ((elements instanceof Collection) ? Collections2.cast(elements) : Lists.newArrayList((java.lang.Iterable<?>)elements));
        return (CopyOnWriteArrayList<E>)new CopyOnWriteArrayList((Collection)elementsCollection);
    }
    
    public static <E> List<E> asList(@Nullable final E first, final E[] rest) {
        return (List<E>)new OnePlusArrayList(first, rest);
    }
    
    public static <E> List<E> asList(@Nullable final E first, @Nullable final E second, final E[] rest) {
        return (List<E>)new TwoPlusArrayList(first, second, rest);
    }
    
    public static <B> List<List<B>> cartesianProduct(final List<? extends List<? extends B>> lists) {
        return CartesianList.<B>create(lists);
    }
    
    @SafeVarargs
    public static <B> List<List<B>> cartesianProduct(final List<? extends B>... lists) {
        return Lists.<B>cartesianProduct((java.util.List<? extends java.util.List<? extends B>>)Arrays.asList((Object[])lists));
    }
    
    public static <F, T> List<T> transform(final List<F> fromList, final Function<? super F, ? extends T> function) {
        return (List<T>)((fromList instanceof RandomAccess) ? new TransformingRandomAccessList((java.util.List<Object>)fromList, function) : new TransformingSequentialList((java.util.List<Object>)fromList, function));
    }
    
    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        Preconditions.<List<T>>checkNotNull(list);
        Preconditions.checkArgument(size > 0);
        return (List<List<T>>)((list instanceof RandomAccess) ? new RandomAccessPartition<>(list, size) : new Partition(list, size));
    }
    
    public static ImmutableList<Character> charactersOf(final String string) {
        return new StringAsImmutableList(Preconditions.<String>checkNotNull(string));
    }
    
    @Beta
    public static List<Character> charactersOf(final CharSequence sequence) {
        return (List<Character>)new CharSequenceAsList(Preconditions.<CharSequence>checkNotNull(sequence));
    }
    
    public static <T> List<T> reverse(final List<T> list) {
        if (list instanceof ImmutableList) {
            return (List<T>)((ImmutableList)list).reverse();
        }
        if (list instanceof ReverseList) {
            return ((ReverseList)list).getForwardList();
        }
        if (list instanceof RandomAccess) {
            return (List<T>)new RandomAccessReverseList((java.util.List<Object>)list);
        }
        return (List<T>)new ReverseList((java.util.List<Object>)list);
    }
    
    static int hashCodeImpl(final List<?> list) {
        int hashCode = 1;
        for (final Object o : list) {
            hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
            hashCode = ~(~hashCode);
        }
        return hashCode;
    }
    
    static boolean equalsImpl(final List<?> thisList, @Nullable final Object other) {
        if (other == Preconditions.<List<?>>checkNotNull(thisList)) {
            return true;
        }
        if (!(other instanceof List)) {
            return false;
        }
        final List<?> otherList = other;
        final int size = thisList.size();
        if (size != otherList.size()) {
            return false;
        }
        if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
            for (int i = 0; i < size; ++i) {
                if (!Objects.equal(thisList.get(i), otherList.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
    }
    
    static <E> boolean addAllImpl(final List<E> list, final int index, final Iterable<? extends E> elements) {
        boolean changed = false;
        final ListIterator<E> listIterator = (ListIterator<E>)list.listIterator(index);
        for (final E e : elements) {
            listIterator.add(e);
            changed = true;
        }
        return changed;
    }
    
    static int indexOfImpl(final List<?> list, @Nullable final Object element) {
        if (list instanceof RandomAccess) {
            return indexOfRandomAccess(list, element);
        }
        final ListIterator<?> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(element, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }
    
    private static int indexOfRandomAccess(final List<?> list, @Nullable final Object element) {
        final int size = list.size();
        if (element == null) {
            for (int i = 0; i < size; ++i) {
                if (list.get(i) == null) {
                    return i;
                }
            }
        }
        else {
            for (int i = 0; i < size; ++i) {
                if (element.equals(list.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    static int lastIndexOfImpl(final List<?> list, @Nullable final Object element) {
        if (list instanceof RandomAccess) {
            return lastIndexOfRandomAccess(list, element);
        }
        final ListIterator<?> listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(element, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }
    
    private static int lastIndexOfRandomAccess(final List<?> list, @Nullable final Object element) {
        if (element == null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                if (list.get(i) == null) {
                    return i;
                }
            }
        }
        else {
            for (int i = list.size() - 1; i >= 0; --i) {
                if (element.equals(list.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    static <E> ListIterator<E> listIteratorImpl(final List<E> list, final int index) {
        return (ListIterator<E>)new AbstractListWrapper((List<E>)list).listIterator(index);
    }
    
    static <E> List<E> subListImpl(final List<E> list, final int fromIndex, final int toIndex) {
        List<E> wrapper;
        if (list instanceof RandomAccess) {
            wrapper = (List<E>)new RandomAccessListWrapper<E>(list) {
                private static final long serialVersionUID = 0L;
                
                public ListIterator<E> listIterator(final int index) {
                    return (ListIterator<E>)this.backingList.listIterator(index);
                }
            };
        }
        else {
            wrapper = (List<E>)new AbstractListWrapper<E>(list) {
                private static final long serialVersionUID = 0L;
                
                public ListIterator<E> listIterator(final int index) {
                    return (ListIterator<E>)this.backingList.listIterator(index);
                }
            };
        }
        return (List<E>)wrapper.subList(fromIndex, toIndex);
    }
    
    static <T> List<T> cast(final Iterable<T> iterable) {
        return (List<T>)iterable;
    }
    
    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        final E first;
        final E[] rest;
        private static final long serialVersionUID = 0L;
        
        OnePlusArrayList(@Nullable final E first, final E[] rest) {
            this.first = first;
            this.rest = Preconditions.<E[]>checkNotNull(rest);
        }
        
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 1);
        }
        
        public E get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return (index == 0) ? this.first : this.rest[index - 1];
        }
    }
    
    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        final E first;
        final E second;
        final E[] rest;
        private static final long serialVersionUID = 0L;
        
        TwoPlusArrayList(@Nullable final E first, @Nullable final E second, final E[] rest) {
            this.first = first;
            this.second = second;
            this.rest = Preconditions.<E[]>checkNotNull(rest);
        }
        
        public int size() {
            return IntMath.saturatedAdd(this.rest.length, 2);
        }
        
        public E get(final int index) {
            switch (index) {
                case 0: {
                    return this.first;
                }
                case 1: {
                    return this.second;
                }
                default: {
                    Preconditions.checkElementIndex(index, this.size());
                    return this.rest[index - 2];
                }
            }
        }
    }
    
    private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
        final List<F> fromList;
        final Function<? super F, ? extends T> function;
        private static final long serialVersionUID = 0L;
        
        TransformingSequentialList(final List<F> fromList, final Function<? super F, ? extends T> function) {
            this.fromList = Preconditions.<List<F>>checkNotNull(fromList);
            this.function = Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        }
        
        public void clear() {
            this.fromList.clear();
        }
        
        public int size() {
            return this.fromList.size();
        }
        
        public ListIterator<T> listIterator(final int index) {
            return (ListIterator<T>)new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                @Override
                T transform(final F from) {
                    return (T)TransformingSequentialList.this.function.apply(from);
                }
            };
        }
        
        public boolean removeIf(final Predicate<? super T> filter) {
            Preconditions.<Predicate<? super T>>checkNotNull(filter);
            return this.fromList.removeIf(element -> filter.test(this.function.apply((Object)element)));
        }
    }
    
    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        final List<F> fromList;
        final Function<? super F, ? extends T> function;
        private static final long serialVersionUID = 0L;
        
        TransformingRandomAccessList(final List<F> fromList, final Function<? super F, ? extends T> function) {
            this.fromList = Preconditions.<List<F>>checkNotNull(fromList);
            this.function = Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        }
        
        public void clear() {
            this.fromList.clear();
        }
        
        public T get(final int index) {
            return (T)this.function.apply(this.fromList.get(index));
        }
        
        public Iterator<T> iterator() {
            return (Iterator<T>)this.listIterator();
        }
        
        public ListIterator<T> listIterator(final int index) {
            return (ListIterator<T>)new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                @Override
                T transform(final F from) {
                    return (T)TransformingRandomAccessList.this.function.apply(from);
                }
            };
        }
        
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }
        
        public boolean removeIf(final Predicate<? super T> filter) {
            Preconditions.<Predicate<? super T>>checkNotNull(filter);
            return this.fromList.removeIf(element -> filter.test(this.function.apply((Object)element)));
        }
        
        public T remove(final int index) {
            return (T)this.function.apply(this.fromList.remove(index));
        }
        
        public int size() {
            return this.fromList.size();
        }
    }
    
    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        final List<F> fromList;
        final Function<? super F, ? extends T> function;
        private static final long serialVersionUID = 0L;
        
        TransformingRandomAccessList(final List<F> fromList, final Function<? super F, ? extends T> function) {
            this.fromList = Preconditions.<List<F>>checkNotNull(fromList);
            this.function = Preconditions.<Function<? super F, ? extends T>>checkNotNull(function);
        }
        
        public void clear() {
            this.fromList.clear();
        }
        
        public T get(final int index) {
            return (T)this.function.apply(this.fromList.get(index));
        }
        
        public Iterator<T> iterator() {
            return (Iterator<T>)this.listIterator();
        }
        
        public ListIterator<T> listIterator(final int index) {
            return (ListIterator<T>)new TransformedListIterator<F, T>(this.fromList.listIterator(index)) {
                @Override
                T transform(final F from) {
                    return (T)TransformingRandomAccessList.this.function.apply(from);
                }
            };
        }
        
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }
        
        public boolean removeIf(final Predicate<? super T> filter) {
            Preconditions.<Predicate<? super T>>checkNotNull(filter);
            return this.fromList.removeIf(element -> filter.test(this.function.apply((Object)element)));
        }
        
        public T remove(final int index) {
            return (T)this.function.apply(this.fromList.remove(index));
        }
        
        public int size() {
            return this.fromList.size();
        }
    }
    
    private static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;
        
        Partition(final List<T> list, final int size) {
            this.list = list;
            this.size = size;
        }
        
        public List<T> get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            final int start = index * this.size;
            final int end = Math.min(start + this.size, this.list.size());
            return (List<T>)this.list.subList(start, end);
        }
        
        public int size() {
            return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
        }
        
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }
    
    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
        RandomAccessPartition(final List<T> list, final int size) {
            super(list, size);
        }
    }
    
    private static final class StringAsImmutableList extends ImmutableList<Character> {
        private final String string;
        
        StringAsImmutableList(final String string) {
            this.string = string;
        }
        
        @Override
        public int indexOf(@Nullable final Object object) {
            return (object instanceof Character) ? this.string.indexOf((int)(char)object) : -1;
        }
        
        @Override
        public int lastIndexOf(@Nullable final Object object) {
            return (object instanceof Character) ? this.string.lastIndexOf((int)(char)object) : -1;
        }
        
        @Override
        public ImmutableList<Character> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        public Character get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.string.charAt(index);
        }
        
        public int size() {
            return this.string.length();
        }
    }
    
    private static final class CharSequenceAsList extends AbstractList<Character> {
        private final CharSequence sequence;
        
        CharSequenceAsList(final CharSequence sequence) {
            this.sequence = sequence;
        }
        
        public Character get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.sequence.charAt(index);
        }
        
        public int size() {
            return this.sequence.length();
        }
    }
    
    private static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;
        
        ReverseList(final List<T> forwardList) {
            this.forwardList = Preconditions.<List<T>>checkNotNull(forwardList);
        }
        
        List<T> getForwardList() {
            return this.forwardList;
        }
        
        private int reverseIndex(final int index) {
            final int size = this.size();
            Preconditions.checkElementIndex(index, size);
            return size - 1 - index;
        }
        
        private int reversePosition(final int index) {
            final int size = this.size();
            Preconditions.checkPositionIndex(index, size);
            return size - index;
        }
        
        public void add(final int index, @Nullable final T element) {
            this.forwardList.add(this.reversePosition(index), element);
        }
        
        public void clear() {
            this.forwardList.clear();
        }
        
        public T remove(final int index) {
            return (T)this.forwardList.remove(this.reverseIndex(index));
        }
        
        protected void removeRange(final int fromIndex, final int toIndex) {
            this.subList(fromIndex, toIndex).clear();
        }
        
        public T set(final int index, @Nullable final T element) {
            return (T)this.forwardList.set(this.reverseIndex(index), element);
        }
        
        public T get(final int index) {
            return (T)this.forwardList.get(this.reverseIndex(index));
        }
        
        public int size() {
            return this.forwardList.size();
        }
        
        public List<T> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.<T>reverse((java.util.List<T>)this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)));
        }
        
        public Iterator<T> iterator() {
            return (Iterator<T>)this.listIterator();
        }
        
        public ListIterator<T> listIterator(final int index) {
            final int start = this.reversePosition(index);
            final ListIterator<T> forwardIterator = (ListIterator<T>)this.forwardList.listIterator(start);
            return (ListIterator<T>)new ListIterator<T>() {
                boolean canRemoveOrSet;
                final /* synthetic */ ListIterator val$forwardIterator;
                
                public void add(final T e) {
                    this.val$forwardIterator.add(e);
                    this.val$forwardIterator.previous();
                    this.canRemoveOrSet = false;
                }
                
                public boolean hasNext() {
                    return this.val$forwardIterator.hasPrevious();
                }
                
                public boolean hasPrevious() {
                    return this.val$forwardIterator.hasNext();
                }
                
                public T next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)this.val$forwardIterator.previous();
                }
                
                public int nextIndex() {
                    return ReverseList.this.reversePosition(this.val$forwardIterator.nextIndex());
                }
                
                public T previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)this.val$forwardIterator.next();
                }
                
                public int previousIndex() {
                    return this.nextIndex() - 1;
                }
                
                public void remove() {
                    CollectPreconditions.checkRemove(this.canRemoveOrSet);
                    this.val$forwardIterator.remove();
                    this.canRemoveOrSet = false;
                }
                
                public void set(final T e) {
                    Preconditions.checkState(this.canRemoveOrSet);
                    this.val$forwardIterator.set(e);
                }
            };
        }
    }
    
    private static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;
        
        ReverseList(final List<T> forwardList) {
            this.forwardList = Preconditions.<List<T>>checkNotNull(forwardList);
        }
        
        List<T> getForwardList() {
            return this.forwardList;
        }
        
        private int reverseIndex(final int index) {
            final int size = this.size();
            Preconditions.checkElementIndex(index, size);
            return size - 1 - index;
        }
        
        private int reversePosition(final int index) {
            final int size = this.size();
            Preconditions.checkPositionIndex(index, size);
            return size - index;
        }
        
        public void add(final int index, @Nullable final T element) {
            this.forwardList.add(this.reversePosition(index), element);
        }
        
        public void clear() {
            this.forwardList.clear();
        }
        
        public T remove(final int index) {
            return (T)this.forwardList.remove(this.reverseIndex(index));
        }
        
        protected void removeRange(final int fromIndex, final int toIndex) {
            this.subList(fromIndex, toIndex).clear();
        }
        
        public T set(final int index, @Nullable final T element) {
            return (T)this.forwardList.set(this.reverseIndex(index), element);
        }
        
        public T get(final int index) {
            return (T)this.forwardList.get(this.reverseIndex(index));
        }
        
        public int size() {
            return this.forwardList.size();
        }
        
        public List<T> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.<T>reverse((java.util.List<T>)this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)));
        }
        
        public Iterator<T> iterator() {
            return (Iterator<T>)this.listIterator();
        }
        
        public ListIterator<T> listIterator(final int index) {
            final int start = this.reversePosition(index);
            final ListIterator<T> forwardIterator = (ListIterator<T>)this.forwardList.listIterator(start);
            return (ListIterator<T>)new ListIterator<T>() {
                boolean canRemoveOrSet;
                final /* synthetic */ ListIterator val$forwardIterator;
                
                public void add(final T e) {
                    this.val$forwardIterator.add(e);
                    this.val$forwardIterator.previous();
                    this.canRemoveOrSet = false;
                }
                
                public boolean hasNext() {
                    return this.val$forwardIterator.hasPrevious();
                }
                
                public boolean hasPrevious() {
                    return this.val$forwardIterator.hasNext();
                }
                
                public T next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)this.val$forwardIterator.previous();
                }
                
                public int nextIndex() {
                    return ReverseList.this.reversePosition(this.val$forwardIterator.nextIndex());
                }
                
                public T previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)this.val$forwardIterator.next();
                }
                
                public int previousIndex() {
                    return this.nextIndex() - 1;
                }
                
                public void remove() {
                    CollectPreconditions.checkRemove(this.canRemoveOrSet);
                    this.val$forwardIterator.remove();
                    this.canRemoveOrSet = false;
                }
                
                public void set(final T e) {
                    Preconditions.checkState(this.canRemoveOrSet);
                    this.val$forwardIterator.set(e);
                }
            };
        }
    }
    
    private static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;
        
        ReverseList(final List<T> forwardList) {
            this.forwardList = Preconditions.<List<T>>checkNotNull(forwardList);
        }
        
        List<T> getForwardList() {
            return this.forwardList;
        }
        
        private int reverseIndex(final int index) {
            final int size = this.size();
            Preconditions.checkElementIndex(index, size);
            return size - 1 - index;
        }
        
        private int reversePosition(final int index) {
            final int size = this.size();
            Preconditions.checkPositionIndex(index, size);
            return size - index;
        }
        
        public void add(final int index, @Nullable final T element) {
            this.forwardList.add(this.reversePosition(index), element);
        }
        
        public void clear() {
            this.forwardList.clear();
        }
        
        public T remove(final int index) {
            return (T)this.forwardList.remove(this.reverseIndex(index));
        }
        
        protected void removeRange(final int fromIndex, final int toIndex) {
            this.subList(fromIndex, toIndex).clear();
        }
        
        public T set(final int index, @Nullable final T element) {
            return (T)this.forwardList.set(this.reverseIndex(index), element);
        }
        
        public T get(final int index) {
            return (T)this.forwardList.get(this.reverseIndex(index));
        }
        
        public int size() {
            return this.forwardList.size();
        }
        
        public List<T> subList(final int fromIndex, final int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size());
            return Lists.<T>reverse((java.util.List<T>)this.forwardList.subList(this.reversePosition(toIndex), this.reversePosition(fromIndex)));
        }
        
        public Iterator<T> iterator() {
            return (Iterator<T>)this.listIterator();
        }
        
        public ListIterator<T> listIterator(final int index) {
            final int start = this.reversePosition(index);
            final ListIterator<T> forwardIterator = (ListIterator<T>)this.forwardList.listIterator(start);
            return (ListIterator<T>)new ListIterator<T>() {
                boolean canRemoveOrSet;
                final /* synthetic */ ListIterator val$forwardIterator;
                
                public void add(final T e) {
                    forwardIterator.add(e);
                    forwardIterator.previous();
                    this.canRemoveOrSet = false;
                }
                
                public boolean hasNext() {
                    return forwardIterator.hasPrevious();
                }
                
                public boolean hasPrevious() {
                    return forwardIterator.hasNext();
                }
                
                public T next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)forwardIterator.previous();
                }
                
                public int nextIndex() {
                    return ReverseList.this.reversePosition(forwardIterator.nextIndex());
                }
                
                public T previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    this.canRemoveOrSet = true;
                    return (T)forwardIterator.next();
                }
                
                public int previousIndex() {
                    return this.nextIndex() - 1;
                }
                
                public void remove() {
                    CollectPreconditions.checkRemove(this.canRemoveOrSet);
                    forwardIterator.remove();
                    this.canRemoveOrSet = false;
                }
                
                public void set(final T e) {
                    Preconditions.checkState(this.canRemoveOrSet);
                    forwardIterator.set(e);
                }
            };
        }
    }
    
    private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
        RandomAccessReverseList(final List<T> forwardList) {
            super(forwardList);
        }
    }
    
    private static class AbstractListWrapper<E> extends AbstractList<E> {
        final List<E> backingList;
        
        AbstractListWrapper(final List<E> backingList) {
            this.backingList = Preconditions.<List<E>>checkNotNull(backingList);
        }
        
        public void add(final int index, final E element) {
            this.backingList.add(index, element);
        }
        
        public boolean addAll(final int index, final Collection<? extends E> c) {
            return this.backingList.addAll(index, (Collection)c);
        }
        
        public E get(final int index) {
            return (E)this.backingList.get(index);
        }
        
        public E remove(final int index) {
            return (E)this.backingList.remove(index);
        }
        
        public E set(final int index, final E element) {
            return (E)this.backingList.set(index, element);
        }
        
        public boolean contains(final Object o) {
            return this.backingList.contains(o);
        }
        
        public int size() {
            return this.backingList.size();
        }
    }
    
    private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {
        RandomAccessListWrapper(final List<E> backingList) {
            super(backingList);
        }
    }
}
