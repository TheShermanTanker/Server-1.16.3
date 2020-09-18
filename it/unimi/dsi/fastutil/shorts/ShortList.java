package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface ShortList extends List<Short>, Comparable<List<? extends Short>>, ShortCollection {
    ShortListIterator iterator();
    
    ShortListIterator listIterator();
    
    ShortListIterator listIterator(final int integer);
    
    ShortList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final short[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final short[] arr);
    
    void addElements(final int integer1, final short[] arr, final int integer3, final int integer4);
    
    boolean add(final short short1);
    
    void add(final int integer, final short short2);
    
    @Deprecated
    default void add(final int index, final Short key) {
        this.add(index, (short)key);
    }
    
    boolean addAll(final int integer, final ShortCollection shortCollection);
    
    boolean addAll(final int integer, final ShortList shortList);
    
    boolean addAll(final ShortList shortList);
    
    short set(final int integer, final short short2);
    
    short getShort(final int integer);
    
    int indexOf(final short short1);
    
    int lastIndexOf(final short short1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Short get(final int index) {
        return this.getShort(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((short)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((short)o);
    }
    
    @Deprecated
    default boolean add(final Short k) {
        return this.add((short)k);
    }
    
    short removeShort(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Short remove(final int index) {
        return this.removeShort(index);
    }
    
    @Deprecated
    default Short set(final int index, final Short k) {
        return this.set(index, (short)k);
    }
}
