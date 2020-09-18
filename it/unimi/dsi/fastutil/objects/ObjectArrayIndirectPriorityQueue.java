package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.Comparator;
import it.unimi.dsi.fastutil.IndirectPriorityQueue;

public class ObjectArrayIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {
    protected K[] refArray;
    protected int[] array;
    protected int size;
    protected Comparator<? super K> c;
    protected int firstIndex;
    protected boolean firstIndexValid;
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int capacity, final Comparator<? super K> c) {
        this.array = IntArrays.EMPTY_ARRAY;
        if (capacity > 0) {
            this.array = new int[capacity];
        }
        this.refArray = refArray;
        this.c = c;
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int capacity) {
        this(refArray, capacity, null);
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final Comparator<? super K> c) {
        this(refArray, refArray.length, (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray) {
        this(refArray, refArray.length, null);
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int[] a, final int size, final Comparator<? super K> c) {
        this(refArray, 0, (java.util.Comparator<? super Object>)c);
        this.array = a;
        this.size = size;
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int[] a, final Comparator<? super K> c) {
        this(refArray, a, a.length, (java.util.Comparator<? super Object>)c);
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int[] a, final int size) {
        this(refArray, a, size, null);
    }
    
    public ObjectArrayIndirectPriorityQueue(final K[] refArray, final int[] a) {
        this(refArray, a, a.length);
    }
    
    private int findFirst() {
        if (this.firstIndexValid) {
            return this.firstIndex;
        }
        this.firstIndexValid = true;
        int i = this.size;
        int firstIndex = --i;
        K first = this.refArray[this.array[firstIndex]];
        if (this.c == null) {
            while (i-- != 0) {
                if (((Comparable)this.refArray[this.array[i]]).compareTo(first) < 0) {
                    first = this.refArray[this.array[firstIndex = i]];
                }
            }
        }
        else {
            while (i-- != 0) {
                if (this.c.compare(this.refArray[this.array[i]], first) < 0) {
                    first = this.refArray[this.array[firstIndex = i]];
                }
            }
        }
        return this.firstIndex = firstIndex;
    }
    
    private int findLast() {
        int i = this.size;
        int lastIndex = --i;
        K last = this.refArray[this.array[lastIndex]];
        if (this.c == null) {
            while (i-- != 0) {
                if (((Comparable)last).compareTo(this.refArray[this.array[i]]) < 0) {
                    last = this.refArray[this.array[lastIndex = i]];
                }
            }
        }
        else {
            while (i-- != 0) {
                if (this.c.compare(last, this.refArray[this.array[i]]) < 0) {
                    last = this.refArray[this.array[lastIndex = i]];
                }
            }
        }
        return lastIndex;
    }
    
    protected final void ensureNonEmpty() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
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
        if (this.size == this.array.length) {
            this.array = IntArrays.grow(this.array, this.size + 1);
        }
        if (this.firstIndexValid) {
            if (this.c == null) {
                if (((Comparable)this.refArray[x]).compareTo(this.refArray[this.array[this.firstIndex]]) < 0) {
                    this.firstIndex = this.size;
                }
            }
            else if (this.c.compare(this.refArray[x], this.refArray[this.array[this.firstIndex]]) < 0) {
                this.firstIndex = this.size;
            }
        }
        else {
            this.firstIndexValid = false;
        }
        this.array[this.size++] = x;
    }
    
    public int dequeue() {
        this.ensureNonEmpty();
        final int firstIndex = this.findFirst();
        final int result = this.array[firstIndex];
        final int size = this.size - 1;
        this.size = size;
        if (size != 0) {
            System.arraycopy(this.array, firstIndex + 1, this.array, firstIndex, this.size - firstIndex);
        }
        this.firstIndexValid = false;
        return result;
    }
    
    public int first() {
        this.ensureNonEmpty();
        return this.array[this.findFirst()];
    }
    
    public int last() {
        this.ensureNonEmpty();
        return this.array[this.findLast()];
    }
    
    public void changed() {
        this.ensureNonEmpty();
        this.firstIndexValid = false;
    }
    
    public void changed(final int index) {
        this.ensureElement(index);
        if (index == this.firstIndex) {
            this.firstIndexValid = false;
        }
    }
    
    public void allChanged() {
        this.firstIndexValid = false;
    }
    
    public boolean remove(final int index) {
        this.ensureElement(index);
        final int[] a = this.array;
        int i = this.size;
        while (i-- != 0 && a[i] != index) {}
        if (i < 0) {
            return false;
        }
        this.firstIndexValid = false;
        if (--this.size != 0) {
            System.arraycopy(a, i + 1, a, i, this.size - i);
        }
        return true;
    }
    
    public int front(final int[] a) {
        final K top = this.refArray[this.array[this.findFirst()]];
        int i = this.size;
        int c = 0;
        while (i-- != 0) {
            if (top.equals(this.refArray[this.array[i]])) {
                a[c++] = this.array[i];
            }
        }
        return c;
    }
    
    public int size() {
        return this.size;
    }
    
    public void clear() {
        this.size = 0;
        this.firstIndexValid = false;
    }
    
    public void trim() {
        this.array = IntArrays.trim(this.array, this.size);
    }
    
    public Comparator<? super K> comparator() {
        return this.c;
    }
    
    public String toString() {
        final StringBuffer s = new StringBuffer();
        s.append("[");
        for (int i = 0; i < this.size; ++i) {
            if (i != 0) {
                s.append(", ");
            }
            s.append(this.refArray[this.array[i]]);
        }
        s.append("]");
        return s.toString();
    }
}
