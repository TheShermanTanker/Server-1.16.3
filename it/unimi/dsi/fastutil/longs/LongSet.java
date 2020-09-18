package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.Set;

public interface LongSet extends LongCollection, Set<Long> {
    LongIterator iterator();
    
    boolean remove(final long long1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Long o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final long k) {
        return this.remove(k);
    }
}
