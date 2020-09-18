package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.Set;

public interface CharSet extends CharCollection, Set<Character> {
    CharIterator iterator();
    
    boolean remove(final char character);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Character o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final char k) {
        return this.remove(k);
    }
}
