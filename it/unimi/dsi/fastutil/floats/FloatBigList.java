package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.Size64;
import it.unimi.dsi.fastutil.BigList;

public interface FloatBigList extends BigList<Float>, FloatCollection, Size64, Comparable<BigList<? extends Float>> {
    FloatBigListIterator iterator();
    
    FloatBigListIterator listIterator();
    
    FloatBigListIterator listIterator(final long long1);
    
    FloatBigList subList(final long long1, final long long2);
    
    void getElements(final long long1, final float[][] arr, final long long3, final long long4);
    
    void removeElements(final long long1, final long long2);
    
    void addElements(final long long1, final float[][] arr);
    
    void addElements(final long long1, final float[][] arr, final long long3, final long long4);
    
    void add(final long long1, final float float2);
    
    boolean addAll(final long long1, final FloatCollection floatCollection);
    
    boolean addAll(final long long1, final FloatBigList floatBigList);
    
    boolean addAll(final FloatBigList floatBigList);
    
    float getFloat(final long long1);
    
    float removeFloat(final long long1);
    
    float set(final long long1, final float float2);
    
    long indexOf(final float float1);
    
    long lastIndexOf(final float float1);
    
    @Deprecated
    void add(final long long1, final Float float2);
    
    @Deprecated
    Float get(final long long1);
    
    @Deprecated
    long indexOf(final Object object);
    
    @Deprecated
    long lastIndexOf(final Object object);
    
    @Deprecated
    Float remove(final long long1);
    
    @Deprecated
    Float set(final long long1, final Float float2);
}
