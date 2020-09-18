package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface ByteBigList extends BigList<Byte>, ByteCollection, Size64, Comparable<BigList<? extends Byte>> {
    ByteBigListIterator iterator();
    
    ByteBigListIterator listIterator();
    
    ByteBigListIterator listIterator(final long long1);
    
    ByteBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final byte[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final byte[][] arr);
    
    void addElements(final long long1, final byte[][] arr, final long long3, final long long4);
    
    void add(final long long1, final byte byte2);
    
    boolean addAll(final long long1, final ByteCollection byteCollection);
    
    boolean addAll(final long long1, final ByteBigList byteBigList);
    
    boolean addAll(final ByteBigList byteBigList);
    
    byte getByte(final long long1);
    
    byte removeByte(final long long1);
    
    byte set(final long long1, final byte byte2);
    
    long indexOf(final byte byte1);
    
    long lastIndexOf(final byte byte1);
    
    @Deprecated
    void add(final long long1, final Byte byte2);
    
    @Deprecated
    Byte get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Byte remove(final long long1);
    
    @Deprecated
    Byte set(final long long1, final Byte byte2);
}
