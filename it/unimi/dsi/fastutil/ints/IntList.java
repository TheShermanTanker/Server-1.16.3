package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection {
    IntListIterator iterator();
    
    IntListIterator listIterator();
    
    IntListIterator listIterator(final int integer);
    
    IntList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final int[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final int[] arr);
    
    void addElements(final int integer1, final int[] arr, final int integer3, final int integer4);
    
    boolean add(final int integer);
    
    void add(final int integer1, final int integer2);
    
    @Deprecated
    default void add(final int index, final Integer key) {
        this.add(index, (int)key);
    }
    
    boolean addAll(final int integer, final IntCollection intCollection);
    
    boolean addAll(final int integer, final IntList intList);
    
    boolean addAll(final IntList intList);
    
    int set(final int integer1, final int integer2);
    
    int getInt(final int integer);
    
    int indexOf(final int integer);
    
    int lastIndexOf(final int integer);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Integer get(final int index) {
        return this.getInt(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((int)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((int)o);
    }
    
    @Deprecated
    default boolean add(final Integer k) {
        return this.add((int)k);
    }
    
    int removeInt(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Integer remove(final int index) {
        return this.removeInt(index);
    }
    
    @Deprecated
    default Integer set(final int index, final Integer k) {
        return this.set(index, (int)k);
    }
}
