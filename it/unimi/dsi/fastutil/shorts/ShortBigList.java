package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface ShortBigList extends BigList<Short>, ShortCollection, Size64, Comparable<BigList<? extends Short>> {
    ShortBigListIterator iterator();
    
    ShortBigListIterator listIterator();
    
    ShortBigListIterator listIterator(final long long1);
    
    ShortBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final short[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final short[][] arr);
    
    void addElements(final long long1, final short[][] arr, final long long3, final long long4);
    
    void add(final long long1, final short short2);
    
    boolean addAll(final long long1, final ShortCollection shortCollection);
    
    boolean addAll(final long long1, final ShortBigList shortBigList);
    
    boolean addAll(final ShortBigList shortBigList);
    
    short getShort(final long long1);
    
    short removeShort(final long long1);
    
    short set(final long long1, final short short2);
    
    long indexOf(final short short1);
    
    long lastIndexOf(final short short1);
    
    @Deprecated
    void add(final long long1, final Short short2);
    
    @Deprecated
    Short get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Short remove(final long long1);
    
    @Deprecated
    Short set(final long long1, final Short short2);
}
