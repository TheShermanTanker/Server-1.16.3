package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface LongCollection extends Collection<Long>, LongIterable {
    LongIterator iterator();
    
    boolean add(final long long1);
    
    boolean contains(final long long1);
    
    boolean rem(final long long1);
    
    @Deprecated
    default boolean add(final Long key) {
        return this.add((long)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((long)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((long)key);
    }
    
    long[] toLongArray();
    
    @Deprecated
    long[] toLongArray(final long[] arr);
    
    long[] toArray(final long[] arr);
    
    boolean addAll(final LongCollection longCollection);
    
    boolean containsAll(final LongCollection longCollection);
    
    boolean removeAll(final LongCollection longCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Long> filter) {
        return this.removeIf(key -> filter.test(key));
    }
    
    default boolean removeIf(final LongPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final LongIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextLong())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final LongCollection longCollection);
}
