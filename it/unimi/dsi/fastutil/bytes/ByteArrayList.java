package it.unimi.dsi.fastutil.bytes;

import java.util.ListIterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;

public class ByteArrayList extends AbstractByteList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient byte[] a;
    protected int size;
    
    protected ByteArrayList(final byte[] a, final boolean dummy) {
        this.a = a;
    }
    
    public ByteArrayList(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0) {
            this.a = ByteArrays.EMPTY_ARRAY;
        }
        else {
            this.a = new byte[capacity];
        }
    }
    
    public ByteArrayList() {
        this.a = ByteArrays.DEFAULT_EMPTY_ARRAY;
    }
    
    public ByteArrayList(final Collection<? extends Byte> c) {
        this(c.size());
        this.size = ByteIterators.unwrap(ByteIterators.asByteIterator(c.iterator()), this.a);
    }
    
    public ByteArrayList(final ByteCollection c) {
        this(c.size());
        this.size = ByteIterators.unwrap(c.iterator(), this.a);
    }
    
    public ByteArrayList(final ByteList l) {
        this(l.size());
        l.getElements(0, this.a, 0, this.size = l.size());
    }
    
    public ByteArrayList(final byte[] a) {
        this(a, 0, a.length);
    }
    
    public ByteArrayList(final byte[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public ByteArrayList(final Iterator<? extends Byte> i) {
        this();
        while (i.hasNext()) {
            this.add((byte)i.next());
        }
    }
    
    public ByteArrayList(final ByteIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextByte());
        }
    }
    
    public byte[] elements() {
        return this.a;
    }
    
    public static ByteArrayList wrap(final byte[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(a.length).append(")").toString());
        }
        final ByteArrayList l = new ByteArrayList(a, false);
        l.size = length;
        return l;
    }
    
    public static ByteArrayList wrap(final byte[] a) {
        return wrap(a, a.length);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity <= this.a.length || this.a == ByteArrays.DEFAULT_EMPTY_ARRAY) {
            return;
        }
        this.a = ByteArrays.ensureCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != ByteArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min(this.a.length + (long)(this.a.length >> 1), 2147483639L), (long)capacity);
        }
        else if (capacity < 10) {
            capacity = 10;
        }
        this.a = ByteArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    @Override
    public void add(final int index, final byte k) {
        this.ensureIndex(index);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        ++this.size;
        assert this.size <= this.a.length;
    }
    
    @Override
    public boolean add(final byte k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        assert this.size <= this.a.length;
        return true;
    }
    
    public byte getByte(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final byte k) {
        for (int i = 0; i < this.size; ++i) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final byte k) {
        int i = this.size;
        while (i-- != 0) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public byte removeByte(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final byte old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        assert this.size <= this.a.length;
        return old;
    }
    
    @Override
    public boolean rem(final byte k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeByte(index);
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public byte set(final int index, final byte k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final byte old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
        this.size = 0;
        assert this.size <= this.a.length;
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public void size(final int size) {
        if (size > this.a.length) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            Arrays.fill(this.a, this.size, size, (byte)0);
        }
        this.size = size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public void trim() {
        this.trim(0);
    }
    
    public void trim(final int n) {
        if (n >= this.a.length || this.size == this.a.length) {
            return;
        }
        final byte[] t = new byte[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        assert this.size <= this.a.length;
    }
    
    @Override
    public void getElements(final int from, final byte[] a, final int offset, final int length) {
        ByteArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final int index, final byte[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ByteArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    public byte[] toArray(byte[] a) {
        if (a == null || a.length < this.size) {
            a = new byte[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }
    
    @Override
    public boolean addAll(int index, final ByteCollection c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        }
        final ByteIterator i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = i.nextByte();
        }
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean addAll(final int index, final ByteList l) {
        this.ensureIndex(index);
        final int n = l.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        }
        l.getElements(0, this.a, index, n);
        this.size += n;
        assert this.size <= this.a.length;
        return true;
    }
    
    public boolean removeAll(final ByteCollection c) {
        final byte[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    public boolean removeAll(final Collection<?> c) {
        final byte[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public ByteListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new ByteListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < ByteArrayList.this.size;
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public byte nextByte() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final byte[] a = ByteArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            public byte previousByte() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final byte[] a = ByteArrayList.this.a;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return a[n];
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final byte k) {
                ByteArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final byte k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ByteArrayList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ByteArrayList.this.removeByte(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public ByteArrayList clone() {
        final ByteArrayList c = new ByteArrayList(this.size);
        System.arraycopy(this.a, 0, c.a, 0, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final ByteArrayList l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final byte[] a1 = this.a;
        final byte[] a2 = l.a;
        while (s-- != 0) {
            if (a1[s] != a2[s]) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final ByteArrayList l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final byte[] a1 = this.a;
        final byte[] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final byte e1 = a1[i];
            final byte e2 = a2[i];
            final int r;
            if ((r = Byte.compare(e1, e2)) != 0) {
                return r;
            }
        }
        return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeByte((int)this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new byte[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readByte();
        }
    }
}
