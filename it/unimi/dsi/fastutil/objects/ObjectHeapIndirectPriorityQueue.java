package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Arrays;
import java.util.Comparator;

public class ObjectHeapIndirectPriorityQueue<K> extends ObjectHeapSemiIndirectPriorityQueue<K> {
    protected final int[] inv;
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int capacity, final Comparator<? super K> c) {
        super(refArray, capacity, c);
        if (capacity > 0) {
            this.heap = new int[capacity];
        }
        this.c = c;
        Arrays.fill(this.inv = new int[refArray.length], -1);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int capacity) {
        this(refArray, capacity, null);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final Comparator<? super K> c) {
        this(refArray, refArray.length, (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray) {
        this(refArray, refArray.length, null);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int[] a, final int size, final Comparator<? super K> c) {
        this(refArray, 0, (java.util.Comparator<? super Object>)c);
        this.heap = a;
        this.size = size;
        int i = size;
        while (i-- != 0) {
            if (this.inv[a[i]] != -1) {
                throw new IllegalArgumentException(new StringBuilder().append("Index ").append(a[i]).append(" appears twice in the heap").toString());
            }
            this.inv[a[i]] = i;
        }
        ObjectIndirectHeaps.<K>makeHeap(refArray, a, this.inv, size, (java.util.Comparator<K>)c);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int[] a, final Comparator<? super K> c) {
        this(refArray, a, a.length, (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int[] a, final int size) {
        this(refArray, a, size, null);
    }
    
    public ObjectHeapIndirectPriorityQueue(final K[] refArray, final int[] a) {
        this(refArray, a, a.length);
    }
    
    @Override
    public void enqueue(final int x) {
        if (this.inv[x] >= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Index ").append(x).append(" belongs to the queue").toString());
        }
        if (this.size == this.heap.length) {
            this.heap = IntArrays.grow(this.heap, this.size + 1);
        }
        this.inv[this.heap[this.size] = x] = this.size++;
        ObjectIndirectHeaps.<K>upHeap(this.refArray, this.heap, this.inv, this.size, this.size - 1, (java.util.Comparator<K>)this.c);
    }
    
    public boolean contains(final int index) {
        return this.inv[index] >= 0;
    }
    
    @Override
    public int dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int result = this.heap[0];
        if (--this.size != 0) {
            this.inv[this.heap[0] = this.heap[this.size]] = 0;
        }
        this.inv[result] = -1;
        if (this.size != 0) {
            ObjectIndirectHeaps.<K>downHeap(this.refArray, this.heap, this.inv, this.size, 0, (java.util.Comparator<K>)this.c);
        }
        return result;
    }
    
    @Override
    public void changed() {
        ObjectIndirectHeaps.<K>downHeap(this.refArray, this.heap, this.inv, this.size, 0, (java.util.Comparator<K>)this.c);
    }
    
    public void changed(final int index) {
        final int pos = this.inv[index];
        if (pos < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Index ").append(index).append(" does not belong to the queue").toString());
        }
        final int newPos = ObjectIndirectHeaps.<K>upHeap(this.refArray, this.heap, this.inv, this.size, pos, (java.util.Comparator<K>)this.c);
        ObjectIndirectHeaps.<K>downHeap(this.refArray, this.heap, this.inv, this.size, newPos, (java.util.Comparator<K>)this.c);
    }
    
    @Override
    public void allChanged() {
        ObjectIndirectHeaps.<K>makeHeap(this.refArray, this.heap, this.inv, this.size, (java.util.Comparator<K>)this.c);
    }
    
    public boolean remove(final int index) {
        final int result = this.inv[index];
        if (result < 0) {
            return false;
        }
        this.inv[index] = -1;
        if (result < --this.size) {
            this.inv[this.heap[result] = this.heap[this.size]] = result;
            final int newPos = ObjectIndirectHeaps.<K>upHeap(this.refArray, this.heap, this.inv, this.size, result, (java.util.Comparator<K>)this.c);
            ObjectIndirectHeaps.<K>downHeap(this.refArray, this.heap, this.inv, this.size, newPos, (java.util.Comparator<K>)this.c);
        }
        return true;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.inv, -1);
    }
}
