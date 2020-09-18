package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface ShortCollection extends Collection<Short>, ShortIterable {
    ShortIterator iterator();
    
    boolean add(final short short1);
    
    boolean contains(final short short1);
    
    boolean rem(final short short1);
    
    @Deprecated
    default boolean add(final Short key) {
        return this.add((short)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((short)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((short)key);
    }
    
    short[] toShortArray();
    
    @Deprecated
    short[] toShortArray(final short[] arr);
    
    short[] toArray(final short[] arr);
    
    boolean addAll(final ShortCollection shortCollection);
    
    boolean containsAll(final ShortCollection shortCollection);
    
    boolean removeAll(final ShortCollection shortCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Short> filter) {
        return this.removeIf(key -> filter.test(SafeMath.safeIntToShort(key)));
    }
    
    default boolean removeIf(final IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final ShortIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test((int)each.nextShort())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final ShortCollection shortCollection);
}
