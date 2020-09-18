package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface DoubleCollection extends Collection<Double>, DoubleIterable {
    DoubleIterator iterator();
    
    boolean add(final double double1);
    
    boolean contains(final double double1);
    
    boolean rem(final double double1);
    
    @Deprecated
    default boolean add(final Double key) {
        return this.add((double)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((double)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((double)key);
    }
    
    double[] toDoubleArray();
    
    @Deprecated
    double[] toDoubleArray(final double[] arr);
    
    double[] toArray(final double[] arr);
    
    boolean addAll(final DoubleCollection doubleCollection);
    
    boolean containsAll(final DoubleCollection doubleCollection);
    
    boolean removeAll(final DoubleCollection doubleCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Double> filter) {
        return this.removeIf(key -> filter.test(key));
    }
    
    default boolean removeIf(final DoublePredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final DoubleIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextDouble())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final DoubleCollection doubleCollection);
}
