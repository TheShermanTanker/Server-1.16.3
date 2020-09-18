package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.Set;

public interface IntSet extends IntCollection, Set<Integer> {
    IntIterator iterator();
    
    boolean remove(final int integer);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Integer o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final int k) {
        return this.remove(k);
    }
}
