package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.SafeMath;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.Collection;

public interface ByteCollection extends Collection<Byte>, ByteIterable {
    ByteIterator iterator();
    
    boolean add(final byte byte1);
    
    boolean contains(final byte byte1);
    
    boolean rem(final byte byte1);
    
    @Deprecated
    default boolean add(final Byte key) {
        return this.add((byte)key);
    }
    
    @Deprecated
    default boolean contains(final Object key) {
        return key != null && this.contains((byte)key);
    }
    
    @Deprecated
    default boolean remove(final Object key) {
        return key != null && this.rem((byte)key);
    }
    
    byte[] toByteArray();
    
    @Deprecated
    byte[] toByteArray(final byte[] arr);
    
    byte[] toArray(final byte[] arr);
    
    boolean addAll(final ByteCollection byteCollection);
    
    boolean containsAll(final ByteCollection byteCollection);
    
    boolean removeAll(final ByteCollection byteCollection);
    
    @Deprecated
    default boolean removeIf(final Predicate<? super Byte> filter) {
        return this.removeIf(key -> filter.test(SafeMath.safeIntToByte(key)));
    }
    
    default boolean removeIf(final IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final ByteIterator each = this.iterator();
        while (each.hasNext()) {
            if (filter.test((int)each.nextByte())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    boolean retainAll(final ByteCollection byteCollection);
}
