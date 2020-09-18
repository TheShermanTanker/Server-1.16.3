package io.netty.util.internal;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public final class EmptyPriorityQueue<T> implements PriorityQueue<T> {
    private static final PriorityQueue<Object> INSTANCE;
    
    private EmptyPriorityQueue() {
    }
    
    public static <V> EmptyPriorityQueue<V> instance() {
        return (EmptyPriorityQueue<V>)(EmptyPriorityQueue)EmptyPriorityQueue.INSTANCE;
    }
    
    public boolean removeTyped(final T node) {
        return false;
    }
    
    public boolean containsTyped(final T node) {
        return false;
    }
    
    public void priorityChanged(final T node) {
    }
    
    public int size() {
        return 0;
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    public boolean contains(final Object o) {
        return false;
    }
    
    public Iterator<T> iterator() {
        return (Iterator<T>)Collections.emptyList().iterator();
    }
    
    public Object[] toArray() {
        return EmptyArrays.EMPTY_OBJECTS;
    }
    
    public <T1> T1[] toArray(final T1[] a) {
        if (a.length > 0) {
            a[0] = null;
        }
        return a;
    }
    
    public boolean add(final T t) {
        return false;
    }
    
    public boolean remove(final Object o) {
        return false;
    }
    
    public boolean containsAll(final Collection<?> c) {
        return false;
    }
    
    public boolean addAll(final Collection<? extends T> c) {
        return false;
    }
    
    public boolean removeAll(final Collection<?> c) {
        return false;
    }
    
    public boolean retainAll(final Collection<?> c) {
        return false;
    }
    
    public void clear() {
    }
    
    public void clearIgnoringIndexes() {
    }
    
    public boolean equals(final Object o) {
        return o instanceof PriorityQueue && ((PriorityQueue)o).isEmpty();
    }
    
    public int hashCode() {
        return 0;
    }
    
    public boolean offer(final T t) {
        return false;
    }
    
    public T remove() {
        throw new NoSuchElementException();
    }
    
    public T poll() {
        return null;
    }
    
    public T element() {
        throw new NoSuchElementException();
    }
    
    public T peek() {
        return null;
    }
    
    public String toString() {
        return EmptyPriorityQueue.class.getSimpleName();
    }
    
    static {
        INSTANCE = new EmptyPriorityQueue<>();
    }
}
