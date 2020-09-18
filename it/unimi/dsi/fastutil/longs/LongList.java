package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface LongList extends List<Long>, Comparable<List<? extends Long>>, LongCollection {
    LongListIterator iterator();
    
    LongListIterator listIterator();
    
    LongListIterator listIterator(final int integer);
    
    LongList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final long[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final long[] arr);
    
    void addElements(final int integer1, final long[] arr, final int integer3, final int integer4);
    
    boolean add(final long long1);
    
    void add(final int integer, final long long2);
    
    @Deprecated
    default void add(final int index, final Long key) {
        this.add(index, (long)key);
    }
    
    boolean addAll(final int integer, final LongCollection longCollection);
    
    boolean addAll(final int integer, final LongList longList);
    
    boolean addAll(final LongList longList);
    
    long set(final int integer, final long long2);
    
    long getLong(final int integer);
    
    int indexOf(final long long1);
    
    int lastIndexOf(final long long1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Long get(final int index) {
        return this.getLong(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((long)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((long)o);
    }
    
    @Deprecated
    default boolean add(final Long k) {
        return this.add((long)k);
    }
    
    long removeLong(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Long remove(final int index) {
        return this.removeLong(index);
    }
    
    @Deprecated
    default Long set(final int index, final Long k) {
        return this.set(index, (long)k);
    }
}
