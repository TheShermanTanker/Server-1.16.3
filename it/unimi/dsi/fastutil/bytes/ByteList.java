package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface ByteList extends List<Byte>, Comparable<List<? extends Byte>>, ByteCollection {
    ByteListIterator iterator();
    
    ByteListIterator listIterator();
    
    ByteListIterator listIterator(final int integer);
    
    ByteList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final byte[] arr);
    
    void addElements(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    boolean add(final byte byte1);
    
    void add(final int integer, final byte byte2);
    
    @Deprecated
    default void add(final int index, final Byte key) {
        this.add(index, (byte)key);
    }
    
    boolean addAll(final int integer, final ByteCollection byteCollection);
    
    boolean addAll(final int integer, final ByteList byteList);
    
    boolean addAll(final ByteList byteList);
    
    byte set(final int integer, final byte byte2);
    
    byte getByte(final int integer);
    
    int indexOf(final byte byte1);
    
    int lastIndexOf(final byte byte1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Byte get(final int index) {
        return this.getByte(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((byte)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((byte)o);
    }
    
    @Deprecated
    default boolean add(final Byte k) {
        return this.add((byte)k);
    }
    
    byte removeByte(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Byte remove(final int index) {
        return this.removeByte(index);
    }
    
    @Deprecated
    default Byte set(final int index, final Byte k) {
        return this.set(index, (byte)k);
    }
}
