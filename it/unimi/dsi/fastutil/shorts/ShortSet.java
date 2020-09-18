package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.Set;

public interface ShortSet extends ShortCollection, Set<Short> {
    ShortIterator iterator();
    
    boolean remove(final short short1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Short o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final short k) {
        return this.remove(k);
    }
}
