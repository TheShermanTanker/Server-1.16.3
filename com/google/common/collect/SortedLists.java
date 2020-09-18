package com.google.common.collect;

import java.util.RandomAccess;
import javax.annotation.Nullable;
import com.google.common.base.Function;
import java.util.Comparator;
import com.google.common.base.Preconditions;
import java.util.List;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
final class SortedLists {
    private SortedLists() {
    }
    
    public static <E extends Comparable> int binarySearch(final List<? extends E> list, final E e, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.<E>checkNotNull(e);
        return SortedLists.<E>binarySearch(list, e, (java.util.Comparator<? super E>)Ordering.<Comparable>natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K extends Comparable> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, @Nullable final K key, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        return SortedLists.<E, K>binarySearch(list, keyFunction, key, (java.util.Comparator<? super K>)Ordering.<Comparable>natural(), presentBehavior, absentBehavior);
    }
    
    public static <E, K> int binarySearch(final List<E> list, final Function<? super E, K> keyFunction, @Nullable final K key, final Comparator<? super K> keyComparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        return SortedLists.<K>binarySearch(Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
    }
    
    public static <E> int binarySearch(List<? extends E> list, @Nullable final E key, final Comparator<? super E> comparator, final KeyPresentBehavior presentBehavior, final KeyAbsentBehavior absentBehavior) {
        Preconditions.<Comparator<? super E>>checkNotNull(comparator);
        Preconditions.<List<? extends E>>checkNotNull(list);
        Preconditions.<KeyPresentBehavior>checkNotNull(presentBehavior);
        Preconditions.<KeyAbsentBehavior>checkNotNull(absentBehavior);
        if (!(list instanceof RandomAccess)) {
            list = Lists.newArrayList((java.lang.Iterable<?>)list);
        }
        int lower = 0;
        int upper = list.size() - 1;
        while (lower <= upper) {
            final int middle = lower + upper >>> 1;
            final int c = comparator.compare(key, list.get(middle));
            if (c < 0) {
                upper = middle - 1;
            }
            else {
                if (c <= 0) {
                    return lower + presentBehavior.<E>resultIndex(comparator, key, (java.util.List<? extends E>)list.subList(lower, upper + 1), middle - lower);
                }
                lower = middle + 1;
            }
        }
        return absentBehavior.resultIndex(lower);
    }
    
    public enum KeyPresentBehavior {
        ANY_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return foundIndex;
            }
        }, 
        LAST_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                int lower = foundIndex;
                int upper = list.size() - 1;
                while (lower < upper) {
                    final int middle = lower + upper + 1 >>> 1;
                    final int c = comparator.compare(list.get(middle), key);
                    if (c > 0) {
                        upper = middle - 1;
                    }
                    else {
                        lower = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_PRESENT {
            @Override
             <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                int lower = 0;
                int upper = foundIndex;
                while (lower < upper) {
                    final int middle = lower + upper >>> 1;
                    final int c = comparator.compare(list.get(middle), key);
                    if (c < 0) {
                        lower = middle + 1;
                    }
                    else {
                        upper = middle;
                    }
                }
                return lower;
            }
        }, 
        FIRST_AFTER {
            public <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$4.LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
            }
        }, 
        LAST_BEFORE {
            public <E> int resultIndex(final Comparator<? super E> comparator, final E key, final List<? extends E> list, final int foundIndex) {
                return SortedLists$KeyPresentBehavior$5.FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
            }
        };
        
        abstract <E> int resultIndex(final Comparator<? super E> comparator, final E object, final List<? extends E> list, final int integer);
    }
    
    public enum KeyAbsentBehavior {
        NEXT_LOWER {
            @Override
            int resultIndex(final int higherIndex) {
                return higherIndex - 1;
            }
        }, 
        NEXT_HIGHER {
            public int resultIndex(final int higherIndex) {
                return higherIndex;
            }
        }, 
        INVERTED_INSERTION_INDEX {
            public int resultIndex(final int higherIndex) {
                return ~higherIndex;
            }
        };
        
        abstract int resultIndex(final int integer);
    }
}
