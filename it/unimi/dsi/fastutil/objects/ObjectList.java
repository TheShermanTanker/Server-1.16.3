package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;

public interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K> {
    ObjectListIterator<K> iterator();
    
    ObjectListIterator<K> listIterator();
    
    ObjectListIterator<K> listIterator(final int integer);
    
    ObjectList<K> subList(final int integer1, final int integer2);
    
    void size(final int integer);
    
    void getElements(final int integer1, final Object[] arr, final int integer3, final int integer4);
    
    void removeElements(final int integer1, final int integer2);
    
    void addElements(final int integer, final K[] arr);
    
    void addElements(final int integer1, final K[] arr, final int integer3, final int integer4);
}
