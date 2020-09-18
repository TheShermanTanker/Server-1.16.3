package com.google.common.collect;

import java.util.SortedSet;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class SortedIterables {
    private SortedIterables() {
    }
    
    public static boolean hasSameComparator(final Comparator<?> comparator, final Iterable<?> elements) {
        Preconditions.<Comparator<?>>checkNotNull(comparator);
        Preconditions.<Iterable<?>>checkNotNull(elements);
        Comparator<?> comparator2;
        if (elements instanceof SortedSet) {
            comparator2 = SortedIterables.comparator((java.util.SortedSet<Object>)elements);
        }
        else {
            if (!(elements instanceof SortedIterable)) {
                return false;
            }
            comparator2 = ((SortedIterable)elements).comparator();
        }
        return comparator.equals(comparator2);
    }
    
    public static <E> Comparator<? super E> comparator(final SortedSet<E> sortedSet) {
        Comparator<? super E> result = sortedSet.comparator();
        if (result == null) {
            result = Ordering.<Comparable>natural();
        }
        return result;
    }
}
