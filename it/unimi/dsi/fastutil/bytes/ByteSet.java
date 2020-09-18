package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.Set;

public interface ByteSet extends ByteCollection, Set<Byte> {
    ByteIterator iterator();
    
    boolean remove(final byte byte1);
    
    @Deprecated
    default boolean remove(final Object o) {
        return super.remove(o);
    }
    
    @Deprecated
    default boolean add(final Byte o) {
        return super.add(o);
    }
    
    @Deprecated
    default boolean contains(final Object o) {
        return super.contains(o);
    }
    
    @Deprecated
    default boolean rem(final byte k) {
        return this.remove(k);
    }
}
