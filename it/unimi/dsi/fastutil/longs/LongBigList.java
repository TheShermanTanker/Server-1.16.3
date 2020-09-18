package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface LongBigList extends BigList<Long>, LongCollection, Size64, Comparable<BigList<? extends Long>> {
    LongBigListIterator iterator();
    
    LongBigListIterator listIterator();
    
    LongBigListIterator listIterator(final long long1);
    
    LongBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final long[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final long[][] arr);
    
    void addElements(final long long1, final long[][] arr, final long long3, final long long4);
    
    void add(final long long1, final long long2);
    
    boolean addAll(final long long1, final LongCollection longCollection);
    
    boolean addAll(final long long1, final LongBigList longBigList);
    
    boolean addAll(final LongBigList longBigList);
    
    long getLong(final long long1);
    
    long removeLong(final long long1);
    
    long set(final long long1, final long long2);
    
    long indexOf(final long long1);
    
    long lastIndexOf(final long long1);
    
    @Deprecated
    void add(final long long1, final Long long2);
    
    @Deprecated
    Long get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Long remove(final long long1);
    
    @Deprecated
    Long set(final long long1, final Long long2);
}
