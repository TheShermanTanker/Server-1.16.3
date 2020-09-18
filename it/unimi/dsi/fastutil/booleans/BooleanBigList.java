package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface BooleanBigList extends BigList<Boolean>, BooleanCollection, Size64, Comparable<BigList<? extends Boolean>> {
    BooleanBigListIterator iterator();
    
    BooleanBigListIterator listIterator();
    
    BooleanBigListIterator listIterator(final long long1);
    
    BooleanBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final boolean[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final boolean[][] arr);
    
    void addElements(final long long1, final boolean[][] arr, final long long3, final long long4);
    
    void add(final long long1, final boolean boolean2);
    
    boolean addAll(final long long1, final BooleanCollection booleanCollection);
    
    boolean addAll(final long long1, final BooleanBigList booleanBigList);
    
    boolean addAll(final BooleanBigList booleanBigList);
    
    boolean getBoolean(final long long1);
    
    boolean removeBoolean(final long long1);
    
    boolean set(final long long1, final boolean boolean2);
    
    long indexOf(final boolean boolean1);
    
    long lastIndexOf(final boolean boolean1);
    
    @Deprecated
    void add(final long long1, final Boolean boolean2);
    
    @Deprecated
    Boolean get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Boolean remove(final long long1);
    
    @Deprecated
    Boolean set(final long long1, final Boolean boolean2);
}
