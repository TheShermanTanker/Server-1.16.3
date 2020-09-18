package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface DoubleBigList extends BigList<Double>, DoubleCollection, Size64, Comparable<BigList<? extends Double>> {
    DoubleBigListIterator iterator();
    
    DoubleBigListIterator listIterator();
    
    DoubleBigListIterator listIterator(final long long1);
    
    DoubleBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final double[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final double[][] arr);
    
    void addElements(final long long1, final double[][] arr, final long long3, final long long4);
    
    void add(final long long1, final double double2);
    
    boolean addAll(final long long1, final DoubleCollection doubleCollection);
    
    boolean addAll(final long long1, final DoubleBigList doubleBigList);
    
    boolean addAll(final DoubleBigList doubleBigList);
    
    double getDouble(final long long1);
    
    double removeDouble(final long long1);
    
    double set(final long long1, final double double2);
    
    long indexOf(final double double1);
    
    long lastIndexOf(final double double1);
    
    @Deprecated
    void add(final long long1, final Double double2);
    
    @Deprecated
    Double get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Double remove(final long long1);
    
    @Deprecated
    Double set(final long long1, final Double double2);
}
