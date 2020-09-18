package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface BooleanList extends List<Boolean>, Comparable<List<? extends Boolean>>, BooleanCollection {
    BooleanListIterator iterator();
    
    BooleanListIterator listIterator();
    
    BooleanListIterator listIterator(final int integer);
    
    BooleanList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final boolean[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final boolean[] arr);
    
    void addElements(final int integer1, final boolean[] arr, final int integer3, final int integer4);
    
    boolean add(final boolean boolean1);
    
    void add(final int integer, final boolean boolean2);
    
    @Deprecated
    default void add(final int index, final Boolean key) {
        this.add(index, (boolean)key);
    }
    
    boolean addAll(final int integer, final BooleanCollection booleanCollection);
    
    boolean addAll(final int integer, final BooleanList booleanList);
    
    boolean addAll(final BooleanList booleanList);
    
    boolean set(final int integer, final boolean boolean2);
    
    boolean getBoolean(final int integer);
    
    int indexOf(final boolean boolean1);
    
    int lastIndexOf(final boolean boolean1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Boolean get(final int index) {
        return this.getBoolean(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((boolean)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((boolean)o);
    }
    
    @Deprecated
    default boolean add(final Boolean k) {
        return this.add((boolean)k);
    }
    
    boolean removeBoolean(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Boolean remove(final int index) {
        return this.removeBoolean(index);
    }
    
    @Deprecated
    default Boolean set(final int index, final Boolean k) {
        return this.set(index, (boolean)k);
    }
}
