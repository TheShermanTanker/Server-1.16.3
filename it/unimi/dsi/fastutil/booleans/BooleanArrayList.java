package it.unimi.dsi.fastutil.booleans;

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

public class BooleanArrayList extends AbstractBooleanList implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient boolean[] a;
    protected int size;
    
    protected BooleanArrayList(final boolean[] a, final boolean dummy) {
        this.a = a;
    }
    
    public BooleanArrayList(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0) {
            this.a = BooleanArrays.EMPTY_ARRAY;
        }
        else {
            this.a = new boolean[capacity];
        }
    }
    
    public BooleanArrayList() {
        this.a = BooleanArrays.DEFAULT_EMPTY_ARRAY;
    }
    
    public BooleanArrayList(final Collection<? extends Boolean> c) {
        this(c.size());
        this.size = BooleanIterators.unwrap(BooleanIterators.asBooleanIterator(c.iterator()), this.a);
    }
    
    public BooleanArrayList(final BooleanCollection c) {
        this(c.size());
        this.size = BooleanIterators.unwrap(c.iterator(), this.a);
    }
    
    public BooleanArrayList(final BooleanList l) {
        this(l.size());
        l.getElements(0, this.a, 0, this.size = l.size());
    }
    
    public BooleanArrayList(final boolean[] a) {
        this(a, 0, a.length);
    }
    
    public BooleanArrayList(final boolean[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public BooleanArrayList(final Iterator<? extends Boolean> i) {
        this();
        while (i.hasNext()) {
            this.add((boolean)i.next());
        }
    }
    
    public BooleanArrayList(final BooleanIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextBoolean());
        }
    }
    
    public boolean[] elements() {
        return this.a;
    }
    
    public static BooleanArrayList wrap(final boolean[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(a.length).append(")").toString());
        }
        final BooleanArrayList l = new BooleanArrayList(a, false);
        l.size = length;
        return l;
    }
    
    public static BooleanArrayList wrap(final boolean[] a) {
        return wrap(a, a.length);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity <= this.a.length || this.a == BooleanArrays.DEFAULT_EMPTY_ARRAY) {
            return;
        }
        this.a = BooleanArrays.ensureCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != BooleanArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min(this.a.length + (long)(this.a.length >> 1), 2147483639L), (long)capacity);
        }
        else if (capacity < 10) {
            capacity = 10;
        }
        this.a = BooleanArrays.forceCapacity(this.a, capacity, this.size);
        assert this.size <= this.a.length;
    }
    
    @Override
    public void add(final int index, final boolean k) {
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
    public boolean add(final boolean k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        assert this.size <= this.a.length;
        return true;
    }
    
    public boolean getBoolean(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final boolean k) {
        for (int i = 0; i < this.size; ++i) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final boolean k) {
        int i = this.size;
        while (i-- != 0) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean removeBoolean(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final boolean old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        assert this.size <= this.a.length;
        return old;
    }
    
    @Override
    public boolean rem(final boolean k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeBoolean(index);
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean set(final int index, final boolean k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final boolean old = this.a[index];
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
            Arrays.fill(this.a, this.size, size, false);
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
        final boolean[] t = new boolean[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        assert this.size <= this.a.length;
    }
    
    @Override
    public void getElements(final int from, final boolean[] a, final int offset, final int length) {
        BooleanArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }
    
    @Override
    public void addElements(final int index, final boolean[] a, final int offset, final int length) {
        this.ensureIndex(index);
        BooleanArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    public boolean[] toArray(boolean[] a) {
        if (a == null || a.length < this.size) {
            a = new boolean[this.size];
        }
        System.arraycopy(this.a, 0, a, 0, this.size);
        return a;
    }
    
    @Override
    public boolean addAll(int index, final BooleanCollection c) {
        this.ensureIndex(index);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        }
        final BooleanIterator i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = i.nextBoolean();
        }
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public boolean addAll(final int index, final BooleanList l) {
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
    
    public boolean removeAll(final BooleanCollection c) {
        final boolean[] a = this.a;
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
        final boolean[] a = this.a;
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
    public BooleanListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new BooleanListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < BooleanArrayList.this.size;
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public boolean nextBoolean() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final boolean[] a = BooleanArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            public boolean previousBoolean() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final boolean[] a = BooleanArrayList.this.a;
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
            
            public void add(final boolean k) {
                BooleanArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final boolean k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                BooleanArrayList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                BooleanArrayList.this.removeBoolean(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public BooleanArrayList clone() {
        final BooleanArrayList c = new BooleanArrayList(this.size);
        System.arraycopy(this.a, 0, c.a, 0, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final BooleanArrayList l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final boolean[] a1 = this.a;
        final boolean[] a2 = l.a;
        while (s-- != 0) {
            if (a1[s] != a2[s]) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final BooleanArrayList l) {
        final int s1 = this.size();
        final int s2 = l.size();
        final boolean[] a1 = this.a;
        final boolean[] a2 = l.a;
        int i;
        for (i = 0; i < s1 && i < s2; ++i) {
            final boolean e1 = a1[i];
            final boolean e2 = a2[i];
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
            s.writeBoolean(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new boolean[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readBoolean();
        }
    }
}
