package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import it.unimi.dsi.fastutil.BigArrays;
import java.util.Iterator;
import java.io.Serializable;
import java.util.RandomAccess;

public class LongBigArrayBigList extends AbstractLongBigList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient long[][] a;
    protected long size;
    
    protected LongBigArrayBigList(final long[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public LongBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0L) {
            this.a = LongBigArrays.EMPTY_BIG_ARRAY;
        }
        else {
            this.a = LongBigArrays.newBigArray(capacity);
        }
    }
    
    public LongBigArrayBigList() {
        this.a = LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
    }
    
    public LongBigArrayBigList(final LongCollection c) {
        this(c.size());
        final LongIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextLong());
        }
    }
    
    public LongBigArrayBigList(final LongBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public LongBigArrayBigList(final long[][] a) {
        this(a, 0L, LongBigArrays.length(a));
    }
    
    public LongBigArrayBigList(final long[][] a, final long offset, final long length) {
        this(length);
        LongBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public LongBigArrayBigList(final Iterator<? extends Long> i) {
        this();
        while (i.hasNext()) {
            this.add((long)i.next());
        }
    }
    
    public LongBigArrayBigList(final LongIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextLong());
        }
    }
    
    public long[][] elements() {
        return this.a;
    }
    
    public static LongBigArrayBigList wrap(final long[][] a, final long length) {
        if (length > LongBigArrays.length(a)) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(LongBigArrays.length(a)).append(")").toString());
        }
        final LongBigArrayBigList l = new LongBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static LongBigArrayBigList wrap(final long[][] a) {
        return wrap(a, LongBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        if (capacity <= this.a.length || this.a == LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            return;
        }
        this.a = LongBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= LongBigArrays.length(this.a);
    }
    
    private void grow(long capacity) {
        final long oldLength = LongBigArrays.length(this.a);
        if (capacity <= oldLength) {
            return;
        }
        if (this.a != LongBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            capacity = Math.max(oldLength + (oldLength >> 1), capacity);
        }
        else if (capacity < 10L) {
            capacity = 10L;
        }
        this.a = LongBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= LongBigArrays.length(this.a);
    }
    
    @Override
    public void add(final long index, final long k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            LongBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        LongBigArrays.set(this.a, index, k);
        ++this.size;
        assert this.size <= LongBigArrays.length(this.a);
    }
    
    @Override
    public boolean add(final long k) {
        this.grow(this.size + 1L);
        LongBigArrays.set(this.a, this.size++, k);
        assert this.size <= LongBigArrays.length(this.a);
        return true;
    }
    
    public long getLong(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return LongBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final long k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == LongBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final long k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == LongBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long removeLong(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final long old = LongBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            LongBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        assert this.size <= LongBigArrays.length(this.a);
        return old;
    }
    
    @Override
    public boolean rem(final long k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeLong(index);
        assert this.size <= LongBigArrays.length(this.a);
        return true;
    }
    
    @Override
    public long set(final long index, final long k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final long old = LongBigArrays.get(this.a, index);
        LongBigArrays.set(this.a, index, k);
        return old;
    }
    
    public boolean removeAll(final LongCollection c) {
        long[] s = null;
        long[] d = null;
        int ss = -1;
        int sd = 134217728;
        int ds = -1;
        int dd = 134217728;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 134217728) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(s[sd])) {
                if (dd == 134217728) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        final long j = BigArrays.index(ds, dd);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    public boolean removeAll(final Collection<?> c) {
        long[] s = null;
        long[] d = null;
        int ss = -1;
        int sd = 134217728;
        int ds = -1;
        int dd = 134217728;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 134217728) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(s[sd])) {
                if (dd == 134217728) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        final long j = BigArrays.index(ds, dd);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public void clear() {
        this.size = 0L;
        assert this.size <= LongBigArrays.length(this.a);
    }
    
    public long size64() {
        return this.size;
    }
    
    @Override
    public void size(final long size) {
        if (size > LongBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            LongBigArrays.fill(this.a, this.size, size, 0L);
        }
        this.size = size;
    }
    
    public boolean isEmpty() {
        return this.size == 0L;
    }
    
    public void trim() {
        this.trim(0L);
    }
    
    public void trim(final long n) {
        final long arrayLength = LongBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = LongBigArrays.trim(this.a, Math.max(n, this.size));
        assert this.size <= LongBigArrays.length(this.a);
    }
    
    @Override
    public void getElements(final long from, final long[][] a, final long offset, final long length) {
        LongBigArrays.copy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        BigArrays.ensureFromTo(this.size, from, to);
        LongBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final long index, final long[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        LongBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        LongBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        LongBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public LongBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new LongBigListIterator() {
            long pos = index;
            long last = -1L;
            
            public boolean hasNext() {
                return this.pos < LongBigArrayBigList.this.size;
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public long nextLong() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final long[][] a = LongBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return LongBigArrays.get(a, n);
            }
            
            public long previousLong() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final long[][] a = LongBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return LongBigArrays.get(a, index);
            }
            
            public long nextIndex() {
                return this.pos;
            }
            
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            public void add(final long k) {
                LongBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final long k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                LongBigArrayBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                LongBigArrayBigList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public LongBigArrayBigList clone() {
        final LongBigArrayBigList c = new LongBigArrayBigList(this.size);
        LongBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final LongBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final long[][] a1 = this.a;
        final long[][] a2 = l.a;
        while (s-- != 0L) {
            if (LongBigArrays.get(a1, s) != LongBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final LongBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final long[][] a1 = this.a;
        final long[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final long e1 = LongBigArrays.get(a1, i);
            final long e2 = LongBigArrays.get(a2, i);
            final int r;
            if ((r = Long.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeLong(LongBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = LongBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            LongBigArrays.set(this.a, i, s.readLong());
        }
    }
}
