package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.ints.IntArrays;

public class LongHeapSemiIndirectPriorityQueue implements LongIndirectPriorityQueue {
    protected final long[] refArray;
    protected int[] heap;
    protected int size;
    protected LongComparator c;
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int capacity, final LongComparator c) {
        this.heap = IntArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.heap = new int[capacity];
        }
        this.refArray = refArray;
        this.c = c;
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int capacity) {
        this(refArray, capacity, null);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final LongComparator c) {
        this(refArray, refArray.length, c);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray) {
        this(refArray, refArray.length, null);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int[] a, final int size, final LongComparator c) {
        this(refArray, 0, c);
        LongSemiIndirectHeaps.makeHeap(refArray, this.heap = a, this.size = size, c);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int[] a, final LongComparator c) {
        this(refArray, a, a.length, c);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int[] a, final int size) {
        this(refArray, a, size, null);
    }
    
    public LongHeapSemiIndirectPriorityQueue(final long[] refArray, final int[] a) {
        this(refArray, a, a.length);
    }
    
    protected void ensureElement(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index >= this.refArray.length) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is larger than or equal to reference array size (").append(this.refArray.length).append(")").toString());
        }
    }
    
    public void enqueue(final int x) {
        this.ensureElement(x);
        if (this.size == this.heap.length) {
            this.heap = IntArrays.grow(this.heap, this.size + 1);
        }
        this.heap[this.size++] = x;
        LongSemiIndirectHeaps.upHeap(this.refArray, this.heap, this.size, this.size - 1, this.c);
    }
    
    public int dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        final int result = this.heap[0];
        final int[] heap = this.heap;
        final int n = 0;
        final int[] heap2 = this.heap;
        final int size = this.size - 1;
        this.size = size;
        heap[n] = heap2[size];
        if (this.size != 0) {
            LongSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c);
        }
        return result;
    }
    
    public int first() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.heap[0];
    }
    
    public void changed() {
        LongSemiIndirectHeaps.downHeap(this.refArray, this.heap, this.size, 0, this.c);
    }
    
    public void allChanged() {
        LongSemiIndirectHeaps.makeHeap(this.refArray, this.heap, this.size, this.c);
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        this.size = 0;
    }
    
    public void trim() {
        this.heap = IntArrays.trim(this.heap, this.size);
    }
    
    public LongComparator comparator() {
        return this.c;
    }
    
    public int front(final int[] a) {
        return (this.c == null) ? LongSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a) : LongSemiIndirectHeaps.front(this.refArray, this.heap, this.size, a, this.c);
    }
    
    public String toString() {
        final StringBuffer s = new StringBuffer();
        s.append("[");
        for (int i = 0; i < this.size; ++i) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(this.refArray[this.heap[i]]);
        }
        s.append("]");
        return s.toString();
    }
}
