package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface IntBigList extends BigList<Integer>, IntCollection, Size64, Comparable<BigList<? extends Integer>> {
    IntBigListIterator iterator();
    
    IntBigListIterator listIterator();
    
    IntBigListIterator listIterator(final long long1);
    
    IntBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final int[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final int[][] arr);
    
    void addElements(final long long1, final int[][] arr, final long long3, final long long4);
    
    void add(final long long1, final int integer);
    
    boolean addAll(final long long1, final IntCollection intCollection);
    
    boolean addAll(final long long1, final IntBigList intBigList);
    
    boolean addAll(final IntBigList intBigList);
    
    int getInt(final long long1);
    
    int removeInt(final long long1);
    
    int set(final long long1, final int integer);
    
    long indexOf(final int integer);
    
    long lastIndexOf(final int integer);
    
    @Deprecated
    void add(final long long1, final Integer integer);
    
    @Deprecated
    Integer get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Integer remove(final long long1);
    
    @Deprecated
    Integer set(final long long1, final Integer integer);
}
