package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface DoubleList extends List<Double>, Comparable<List<? extends Double>>, DoubleCollection {
    DoubleListIterator iterator();
    
    DoubleListIterator listIterator();
    
    DoubleListIterator listIterator(final int integer);
    
    DoubleList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final double[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final double[] arr);
    
    void addElements(final int integer1, final double[] arr, final int integer3, final int integer4);
    
    boolean add(final double double1);
    
    void add(final int integer, final double double2);
    
    @Deprecated
    default void add(final int index, final Double key) {
        this.add(index, (double)key);
    }
    
    boolean addAll(final int integer, final DoubleCollection doubleCollection);
    
    boolean addAll(final int integer, final DoubleList doubleList);
    
    boolean addAll(final DoubleList doubleList);
    
    double set(final int integer, final double double2);
    
    double getDouble(final int integer);
    
    int indexOf(final double double1);
    
    int lastIndexOf(final double double1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Double get(final int index) {
        return this.getDouble(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((double)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((double)o);
    }
    
    @Deprecated
    default boolean add(final Double k) {
        return this.add((double)k);
    }
    
    double removeDouble(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Double remove(final int index) {
        return this.removeDouble(index);
    }
    
    @Deprecated
    default Double set(final int index, final Double k) {
        return this.set(index, (double)k);
    }
}
