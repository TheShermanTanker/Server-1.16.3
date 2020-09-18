package it.unimi.dsi.fastutil.objects;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Comparator;
import java.io.Serializable;
import it.unimi.dsi.fastutil.PriorityQueue;

public class ObjectHeapPriorityQueue<K> implements PriorityQueue<K>, Serializable {
    private static final long serialVersionUID = 1L;
    protected transient K[] heap;
    protected int size;
    protected Comparator<? super K> c;
    
    public ObjectHeapPriorityQueue(final int capacity, final Comparator<? super K> c) {
        this.heap = (K[])ObjectArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = (K[])new Object[capacity];
        }
        this.c = c;
    }
    
    public ObjectHeapPriorityQueue(final int capacity) {
        this(capacity, null);
    }
    
    public ObjectHeapPriorityQueue(final Comparator<? super K> c) {
        this(0, c);
    }
    
    public ObjectHeapPriorityQueue() {
        this(0, null);
    }
    
    public ObjectHeapPriorityQueue(final K[] a, final int size, final Comparator<? super K> c) {
        this(c);
        ObjectHeaps.<K>makeHeap(this.heap = a, this.size = size, (java.util.Comparator<K>)c);
    }
    
    public ObjectHeapPriorityQueue(final K[] a, final Comparator<? super K> c) {
        this(a, a.length, (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectHeapPriorityQueue(final K[] a, final int size) {
        this(a, size, null);
    }
    
    public ObjectHeapPriorityQueue(final K[] a) {
        this(a, a.length);
    }
    
    public ObjectHeapPriorityQueue(final Collection<? extends K> collection, final Comparator<? super K> c) {
        this(collection.toArray(), (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectHeapPriorityQueue(final Collection<? extends K> collection) {
        this(collection, null);
    }
    
    public void enqueue(final K x) {
        if (this.size == this.heap.length) {
            this.heap = ObjectArrays.<K>grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        ObjectHeaps.<K>upHeap(this.heap, this.size, this.size - 1, (java.util.Comparator<K>)this.c);
    }
    
    public K dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final K result = this.heap[0];
        final K[] heap = this.heap;
        final int n = 0;
        final K[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        this.heap[this.size] = null;
        if (this.size != 0) {
            ObjectHeaps.<K>downHeap(this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    public K first() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    public void changed() {
        ObjectHeaps.<K>downHeap(this.heap, this.size, 0, this.c);
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        Arrays.fill((Object[])this.heap, 0, this.size, null);
        this.size = 0;
    }
    
    public void trim() {
        this.heap = ObjectArrays.<K>trim(this.heap, this.size);
    }
    
    public Comparator<? super K> comparator() {
        return this.c;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(this.heap.length);
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.heap[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.heap = (K[])new Object[s.readInt()];
        for (int i = 0; i < this.size; ++i) {
            this.heap[i] = (K)s.readObject();
        }
    }
}
