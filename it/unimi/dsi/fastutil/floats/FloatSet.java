package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.Set;

public interface FloatSet extends FloatCollection, Set<Float> {
    FloatIterator iterator();
    
    boolean remove(final float float1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Float o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final float k) {
        return this.remove(k);
    }
}
