package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.Set;

public interface BooleanSet extends BooleanCollection, Set<Boolean> {
    BooleanIterator iterator();
    
    boolean remove(final boolean boolean1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Boolean o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final boolean k) {
        return this.remove(k);
    }
}
