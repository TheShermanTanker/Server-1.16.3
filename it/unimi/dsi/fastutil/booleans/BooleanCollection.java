package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.Collection;

public interface BooleanCollection extends Collection<Boolean>, BooleanIterable {
    BooleanIterator iterator();
    
    boolean add(final boolean boolean1);
    
    boolean contains(final boolean boolean1);
    
    boolean rem(final boolean boolean1);
    
    @Deprecated
    default boolean add(final Boolean key) {
        return this.add((boolean)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((boolean)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((boolean)key);
    }
    
    boolean[] toBooleanArray();
    
    @Deprecated
    boolean[] toBooleanArray(final boolean[] arr);
    
    boolean[] toArray(final boolean[] arr);
    
    boolean addAll(final BooleanCollection booleanCollection);
    
    boolean containsAll(final BooleanCollection booleanCollection);
    
    boolean removeAll(final BooleanCollection booleanCollection);
    
    boolean retainAll(final BooleanCollection booleanCollection);
}
