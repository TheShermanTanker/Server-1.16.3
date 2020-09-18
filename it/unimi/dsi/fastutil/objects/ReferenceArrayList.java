package it.unimi.dsi.fastutil.objects;

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

public class ReferenceArrayList<K> extends AbstractReferenceList<K> implements RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -7046029254386353131L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected final boolean wrapped;
    protected transient K[] a;
    protected int size;
    
    protected ReferenceArrayList(final K[] a, final boolean dummy) {
        this.a = a;
        this.wrapped = true;
    }
    
    public ReferenceArrayList(final int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Initial capacity (").append(capacity).append(") is negative").toString());
        }
        if (capacity == 0) {
            this.a = (K[])ObjectArrays.EMPTY_ARRAY;
        }
        else {
            this.a = (K[])new Object[capacity];
        }
        this.wrapped = false;
    }
    
    public ReferenceArrayList() {
        this.a = (K[])ObjectArrays.DEFAULT_EMPTY_ARRAY;
        this.wrapped = false;
    }
    
    public ReferenceArrayList(final Collection<? extends K> c) {
        this(c.size());
        this.size = ObjectIterators.<K>unwrap((java.util.Iterator<? extends K>)c.iterator(), this.a);
    }
    
    public ReferenceArrayList(final ReferenceCollection<? extends K> c) {
        this(c.size());
        this.size = ObjectIterators.<K>unwrap((java.util.Iterator<? extends K>)c.iterator(), this.a);
    }
    
    public ReferenceArrayList(final ReferenceList<? extends K> l) {
        this(l.size());
        l.getElements(0, this.a, 0, this.size = l.size());
    }
    
    public ReferenceArrayList(final K[] a) {
        this(a, 0, a.length);
    }
    
    public ReferenceArrayList(final K[] a, final int offset, final int length) {
        this(length);
        System.arraycopy(a, offset, this.a, 0, length);
        this.size = length;
    }
    
    public ReferenceArrayList(final Iterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public ReferenceArrayList(final ObjectIterator<? extends K> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next());
        }
    }
    
    public K[] elements() {
        return this.a;
    }
    
    public static <K> ReferenceArrayList<K> wrap(final K[] a, final int length) {
        if (length > a.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The specified length (").append(length).append(") is greater than the array size (").append(a.length).append(")").toString());
        }
        final ReferenceArrayList<K> l = new ReferenceArrayList<K>(a, false);
        l.size = length;
        return l;
    }
    
    public static <K> ReferenceArrayList<K> wrap(final K[] a) {
        return ReferenceArrayList.<K>wrap(a, a.length);
    }
    
    public void ensureCapacity(final int capacity) {
        if (capacity <= this.a.length || this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            return;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.<K>ensureCapacity(this.a, capacity, this.size);
        }
        else if (capacity > this.a.length) {
            final Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
        assert this.size <= this.a.length;
    }
    
    private void grow(int capacity) {
        if (capacity <= this.a.length) {
            return;
        }
        if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min(this.a.length + (long)(this.a.length >> 1), 2147483639L), (long)capacity);
        }
        else if (capacity < 10) {
            capacity = 10;
        }
        if (this.wrapped) {
            this.a = ObjectArrays.<K>forceCapacity(this.a, capacity, this.size);
        }
        else {
            final Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[])t;
        }
        assert this.size <= this.a.length;
    }
    
    @Override
    public void add(final int index, final K k) {
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
    public boolean add(final K k) {
        this.grow(this.size + 1);
        this.a[this.size++] = k;
        assert this.size <= this.a.length;
        return true;
    }
    
    public K get(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        return this.a[index];
    }
    
    @Override
    public int indexOf(final Object k) {
        for (int i = 0; i < this.size; ++i) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final Object k) {
        int i = this.size;
        while (i-- != 0) {
            if (k == this.a[i]) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public K remove(final int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final K old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        assert this.size <= this.a.length;
        return old;
    }
    
    public boolean remove(final Object k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.remove(index);
        assert this.size <= this.a.length;
        return true;
    }
    
    @Override
    public K set(final int index, final K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size).append(")").toString());
        }
        final K old = this.a[index];
        this.a[index] = k;
        return old;
    }
    
    @Override
    public void clear() {
        Arrays.fill((Object[])this.a, 0, this.size, null);
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
            Arrays.fill((Object[])this.a, this.size, size, null);
        }
        else {
            Arrays.fill((Object[])this.a, size, this.size, null);
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
        final K[] t = (K[])new Object[Math.max(n, this.size)];
        System.arraycopy(this.a, 0, t, 0, this.size);
        this.a = t;
        assert this.size <= this.a.length;
    }
    
    @Override
    public void getElements(final int from, final Object[] a, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        it.unimi.dsi.fastutil.Arrays.ensureFromTo(this.size, from, to);
        System.arraycopy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
        int i = to - from;
        while (i-- != 0) {
            this.a[this.size + i] = null;
        }
    }
    
    @Override
    public void addElements(final int index, final K[] a, final int offset, final int length) {
        this.ensureIndex(index);
        ObjectArrays.<K>ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        System.arraycopy(this.a, index, this.a, index + length, this.size - index);
        System.arraycopy(a, offset, this.a, index, length);
        this.size += length;
    }
    
    public boolean removeAll(final Collection<?> c) {
        final Object[] a = this.a;
        int j = 0;
        for (int i = 0; i < this.size; ++i) {
            if (!c.contains(a[i])) {
                a[j++] = a[i];
            }
        }
        Arrays.fill(a, j, this.size, null);
        final boolean modified = this.size != j;
        this.size = j;
        return modified;
    }
    
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new ObjectListIterator<K>() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < ReferenceArrayList.this.size;
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ReferenceArrayList.this.a;
                final int last = this.pos++;
                this.last = last;
                return a[last];
            }
            
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final K[] a = ReferenceArrayList.this.a;
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
            
            public void add(final K k) {
                ReferenceArrayList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final K k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ReferenceArrayList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                ReferenceArrayList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public ReferenceArrayList<K> clone() {
        final ReferenceArrayList<K> c = new ReferenceArrayList<K>(this.size);
        System.arraycopy(this.a, 0, c.a, 0, this.size);
        c.size = this.size;
        return c;
    }
    
    public boolean equals(final ReferenceArrayList<K> l) {
        if (l == this) {
            return true;
        }
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        final K[] a1 = this.a;
        final K[] a2 = l.a;
        while (s-- != 0) {
            if (a1[s] != a2[s]) {
                return false;
            }
        }
        return true;
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = (K[])new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = (K)s.readObject();
        }
    }
}
