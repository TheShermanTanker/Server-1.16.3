package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import it.unimi.dsi.fastutil.HashCommon;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class LongArrayFIFOQueue implements LongPriorityQueue, Serializable {
    private static final long serialVersionUID = 0L;
    public static final int INITIAL_CAPACITY = 4;
    protected transient long[] array;
    protected transient int length;
    protected transient int start;
    protected transient int end;
    
    public LongArrayFIFOQueue(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        this.array = new long[Math.max(1, capacity)];
        this.length = this.array.length;
    }
    
    public LongArrayFIFOQueue() {
        this(4);
    }
    
    public LongComparator comparator() {
        return null;
    }
    
    public long dequeueLong() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        final long t = this.array[this.start];
        if (++this.start == this.length) {
            this.start = 0;
        }
        this.reduce();
        return t;
    }
    
    public long dequeueLastLong() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        if (this.end == 0) {
            this.end = this.length;
        }
        final long[] array = this.array;
        final int end = this.end - 1;
        this.end = end;
        final long t = array[end];
        this.reduce();
        return t;
    }
    
    private final void resize(final int size, final int newLength) {
        final long[] newArray = new long[newLength];
        if (this.start >= this.end) {
            if (size != 0) {
                System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
                System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
            }
        }
        else {
            System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
        }
        this.start = 0;
        this.end = size;
        this.array = newArray;
        this.length = newLength;
    }
    
    private final void expand() {
        this.resize(this.length, (int)Math.min(2147483639L, 2L * this.length));
    }
    
    private final void reduce() {
        final int size = this.size();
        if (this.length > 4 && size <= this.length / 4) {
            this.resize(size, this.length / 2);
        }
    }
    
    public void enqueue(final long x) {
        this.array[this.end++] = x;
        if (this.end == this.length) {
            this.end = 0;
        }
        if (this.end == this.start) {
            this.expand();
        }
    }
    
    public void enqueueFirst(final long x) {
        if (this.start == 0) {
            this.start = this.length;
        }
        this.array[--this.start] = x;
        if (this.end == this.start) {
            this.expand();
        }
    }
    
    public long firstLong() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        return this.array[this.start];
    }
    
    public long lastLong() {
        if (this.start == this.end) {
            throw new NoSuchElementException();
        }
        return this.array[((this.end == 0) ? this.length : this.end) - 1];
    }
    
    public void clear() {
        final int n = 0;
        this.end = n;
        this.start = n;
    }
    
    public void trim() {
        final int size = this.size();
        final long[] newArray = new long[size + 1];
        if (this.start <= this.end) {
            System.arraycopy(this.array, this.start, newArray, 0, this.end - this.start);
        }
        else {
            System.arraycopy(this.array, this.start, newArray, 0, this.length - this.start);
            System.arraycopy(this.array, 0, newArray, this.length - this.start, this.end);
        }
        this.start = 0;
        final int end = size;
        this.end = end;
        this.length = end + 1;
        this.array = newArray;
    }
    
    public int size() {
        final int apparentLength = this.end - this.start;
        return (apparentLength >= 0) ? apparentLength : (this.length + apparentLength);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        int size = this.size();
        s.writeInt(size);
        int i = this.start;
        while (size-- != 0) {
            s.writeLong(this.array[i++]);
            if (i == this.length) {
                i = 0;
            }
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.end = s.readInt();
        final int nextPowerOfTwo = HashCommon.nextPowerOfTwo(this.end + 1);
        this.length = nextPowerOfTwo;
        this.array = new long[nextPowerOfTwo];
        for (int i = 0; i < this.end; ++i) {
            this.array[i] = s.readLong();
        }
    }
}
