package it.unimi.dsi.fastutil;

import java.util.Collection;

public interface BigList<K> extends Collection<K>, Size64 {
    K get(final long long1);
    
    K remove(final long long1);
    
    K set(final long long1, final K object);
    
    void add(final long long1, final K object);
    
    void size(final long long1);
    
    boolean addAll(final long long1, final Collection<? extends K> collection);
    
    long indexOf(final Object object);
    
    long lastIndexOf(final Object object);
    
    BigListIterator<K> listIterator();
    
    BigListIterator<K> listIterator(final long long1);
    
    BigList<K> subList(final long long1, final long long2);
    
    @Deprecated
    default int size() {
        return super.size();
    }
}
