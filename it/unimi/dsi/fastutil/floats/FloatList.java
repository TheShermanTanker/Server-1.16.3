package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface FloatList extends List<Float>, Comparable<List<? extends Float>>, FloatCollection {
    FloatListIterator iterator();
    
    FloatListIterator listIterator();
    
    FloatListIterator listIterator(final int integer);
    
    FloatList subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final float[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final float[] arr);
    
    void addElements(final int integer1, final float[] arr, final int integer3, final int integer4);
    
    boolean add(final float float1);
    
    void add(final int integer, final float float2);
    
    @Deprecated
    default void add(final int index, final Float key) {
        this.add(index, (float)key);
    }
    
    boolean addAll(final int integer, final FloatCollection floatCollection);
    
    boolean addAll(final int integer, final FloatList floatList);
    
    boolean addAll(final FloatList floatList);
    
    float set(final int integer, final float float2);
    
    float getFloat(final int integer);
    
    int indexOf(final float float1);
    
    int lastIndexOf(final float float1);
    
    @Deprecated
    default boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    default Float get(final int index) {
        return this.getFloat(index);
    }
    
    @Deprecated
    default int indexOf(final Object o) {
        return this.indexOf((float)o);
    }
    
    @Deprecated
    default int lastIndexOf(final Object o) {
        return this.lastIndexOf((float)o);
    }
    
    @Deprecated
    default boolean add(final Float k) {
        return this.add((float)k);
    }
    
    float removeFloat(final int integer);
    
    @Deprecated
    default boolean remove(final Object key) {
        return super.remove(key);
    }
    
    @Deprecated
    default Float remove(final int index) {
        return this.removeFloat(index);
    }
    
    @Deprecated
    default Float set(final int index, final Float k) {
        return this.set(index, (float)k);
    }
}
