package it.unimi.dsi.fastutil.booleans;

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

public class BooleanBigArrayBigList extends AbstractBooleanBigList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient boolean[][] a;
    protected long size;
    
    protected BooleanBigArrayBigList(final boolean[][] a, final boolean dummy) {
        this.a = a;
    }
    
    public BooleanBigArrayBigList(final long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0L) {
            this.a = BooleanBigArrays.EMPTY_BIG_ARRAY;
        }
        else {
            this.a = BooleanBigArrays.newBigArray(capacity);
        }
    }
    
    public BooleanBigArrayBigList() {
        this.a = BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
    }
    
    public BooleanBigArrayBigList(final BooleanCollection c) {
        this(c.size());
        final BooleanIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextBoolean());
        }
    }
    
    public BooleanBigArrayBigList(final BooleanBigList l) {
        this(l.size64());
        l.getElements(0L, this.a, 0L, this.size = l.size64());
    }
    
    public BooleanBigArrayBigList(final boolean[][] a) {
        this(a, 0L, BooleanBigArrays.length(a));
    }
    
    public BooleanBigArrayBigList(final boolean[][] a, final long offset, final long length) {
        this(length);
        BooleanBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }
    
    public BooleanBigArrayBigList(final Iterator<? extends Boolean> i) {
        this();
        while (i.hasNext()) {
            this.add((boolean)i.next());
        }
    }
    
    public BooleanBigArrayBigList(final BooleanIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextBoolean());
        }
    }
    
    public boolean[][] elements() {
        return this.a;
    }
    
    public static BooleanBigArrayBigList wrap(final boolean[][] a, final long length) {
        if (length > BooleanBigArrays.length(a)) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(BooleanBigArrays.length(a)).append(")").toString());
        }
        final BooleanBigArrayBigList l = new BooleanBigArrayBigList(a, false);
        l.size = length;
        return l;
    }
    
    public static BooleanBigArrayBigList wrap(final boolean[][] a) {
        return wrap(a, BooleanBigArrays.length(a));
    }
    
    public void ensureCapacity(final long capacity) {
        if (capacity <= this.a.length || this.a == BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            return;
        }
        this.a = BooleanBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= BooleanBigArrays.length(this.a);
    }
    
    private void grow(long capacity) {
        final long oldLength = BooleanBigArrays.length(this.a);
        if (capacity <= oldLength) {
            return;
        }
        if (this.a != BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            capacity = Math.max(oldLength + (oldLength >> 1), capacity);
        }
        else if (capacity < 10L) {
            capacity = 10L;
        }
        this.a = BooleanBigArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= BooleanBigArrays.length(this.a);
    }
    
    @Override
    public void add(final long index, final boolean k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            BooleanBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        BooleanBigArrays.set(this.a, index, k);
        ++this.size;
        assert this.size <= BooleanBigArrays.length(this.a);
    }
    
    @Override
    public boolean add(final boolean k) {
        this.grow(this.size + 1L);
        BooleanBigArrays.set(this.a, this.size++, k);
        assert this.size <= BooleanBigArrays.length(this.a);
        return true;
    }
    
    public boolean getBoolean(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return BooleanBigArrays.get(this.a, index);
    }
    
    @Override
    public long indexOf(final boolean k) {
        for (long i = 0L; i < this.size; ++i) {
            if (k == BooleanBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final boolean k) {
        long i = this.size;
        while (i-- != 0L) {
            if (k == BooleanBigArrays.get(this.a, i)) {
                return i;
            }
        }
        return -1L;
    }
    
    @Override
    public boolean removeBoolean(final long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final boolean old = BooleanBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            BooleanBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        assert this.size <= BooleanBigArrays.length(this.a);
        return old;
    }
    
    @Override
    public boolean rem(final boolean k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeBoolean(index);
        assert this.size <= BooleanBigArrays.length(this.a);
        return true;
    }
    
    @Override
    public boolean set(final long index, final boolean k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final boolean old = BooleanBigArrays.get(this.a, index);
        BooleanBigArrays.set(this.a, index, k);
        return old;
    }
    
    public boolean removeAll(final BooleanCollection c) {
        boolean[] s = null;
        boolean[] d = null;
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
        boolean[] s = null;
        boolean[] d = null;
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
        assert this.size <= BooleanBigArrays.length(this.a);
    }
    
    public long size64() {
        return this.size;
    }
    
    @Override
    public void size(final long size) {
        if (size > BooleanBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            BooleanBigArrays.fill(this.a, this.size, size, false);
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
        final long arrayLength = BooleanBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = BooleanBigArrays.trim(this.a, Math.max(n, this.size));
        assert this.size <= BooleanBigArrays.length(this.a);
    }
    
    @Override
    public void getElements(final long from, final boolean[][] a, final long offset, final long length) {
        BooleanBigArrays.copy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        BigArrays.ensureFromTo(this.size, from, to);
        BooleanBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final long index, final boolean[][] a, final long offset, final long length) {
        this.ensureIndex(index);
        BooleanBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        BooleanBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        BooleanBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    @Override
    public BooleanBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new BooleanBigListIterator() {
            long pos = index;
            long last = -1L;
            
            public boolean hasNext() {
                return this.pos < BooleanBigArrayBigList.this.size;
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public boolean nextBoolean() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final boolean[][] a = BooleanBigArrayBigList.this.a;
                final long n = this.pos++;
                this.last = n;
                return BooleanBigArrays.get(a, n);
            }
            
            public boolean previousBoolean() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final boolean[][] a = BooleanBigArrayBigList.this.a;
                final long index = this.pos - 1L;
                this.pos = index;
                this.last = index;
                return BooleanBigArrays.get(a, index);
            }
            
            public long nextIndex() {
                return this.pos;
            }
            
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            public void add(final boolean k) {
                BooleanBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final boolean k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                BooleanBigArrayBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                BooleanBigArrayBigList.this.removeBoolean(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    public BooleanBigArrayBigList clone() {
        final BooleanBigArrayBigList c = new BooleanBigArrayBigList(this.size);
        BooleanBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final BooleanBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        final boolean[][] a1 = this.a;
        final boolean[][] a2 = l.a;
        while (s-- != 0L) {
            if (BooleanBigArrays.get(a1, s) != BooleanBigArrays.get(a2, s)) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final BooleanBigArrayBigList l) {
        final long s1 = this.size64();
        final long s2 = l.size64();
        final boolean[][] a1 = this.a;
        final boolean[][] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final boolean e1 = BooleanBigArrays.get(a1, i);
            final boolean e2 = BooleanBigArrays.get(a2, i);
            final int r;
            if ((r = Boolean.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeBoolean(BooleanBigArrays.get(this.a, i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = BooleanBigArrays.newBigArray(this.size);
        for (int i = 0; i < this.size; ++i) {
            BooleanBigArrays.set(this.a, i, s.readBoolean());
        }
    }
}
