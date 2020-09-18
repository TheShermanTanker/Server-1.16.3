package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface IntCollection extends Collection<Integer>, IntIterable {
    IntIterator iterator();
    
    boolean add(final int integer);
    
    boolean contains(final int integer);
    
    boolean rem(final int integer);
    
    @Deprecated
    default boolean add(final Integer key) {
        return this.add((int)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((int)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((int)key);
    }
    
    int[] toIntArray();
    
    @Deprecated
    int[] toIntArray(final int[] arr);
    
    int[] toArray(final int[] arr);
    
    boolean addAll(final IntCollection intCollection);
    
    boolean containsAll(final IntCollection intCollection);
    
    boolean removeAll(final IntCollection intCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Integer> filter) {
        return this.removeIf(key -> filter.test(key));
    }
    
    default boolean removeIf(final IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final IntIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final IntCollection intCollection);
}
