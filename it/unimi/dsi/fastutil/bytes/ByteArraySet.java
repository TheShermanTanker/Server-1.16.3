package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

public class ByteArraySet extends AbstractByteSet implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient byte[] a;
    private int size;
    
    public ByteArraySet(final byte[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public ByteArraySet() {
        this.a = ByteArrays.EMPTY_ARRAY;
    }
    
    public ByteArraySet(final int capacity) {
        this.a = new byte[capacity];
    }
    
    public ByteArraySet(final ByteCollection c) {
        this(c.size());
        this.addAll(c);
    }
    
    public ByteArraySet(final Collection<? extends Byte> c) {
        this(c.size());
        this.addAll((Collection)c);
    }
    
    public ByteArraySet(final byte[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The provided size (").append(size).append(") is larger than or equal to the array size (").append(a.length).append(")").toString());
        }
    }
    
    private int findKey(final byte o) {
        int i = this.size;
        while (i-- != 0) {
            if (this.a[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ByteIterator iterator() {
        return new ByteIterator() {
            int next = 0;
            
            public boolean hasNext() {
                return this.next < ByteArraySet.this.size;
            }
            
            public byte nextByte() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return ByteArraySet.this.a[this.next++];
            }
            
            public void remove() {
                final int tail = ByteArraySet.this.size-- - this.next--;
                System.arraycopy(ByteArraySet.this.a, this.next + 1, ByteArraySet.this.a, this.next, tail);
            }
        };
    }
    
    public boolean contains(final byte k) {
        return this.findKey(k) != -1;
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean remove(final byte k) {
        final int pos = this.findKey(k);
        if (pos == -1) {
            return false;
        }
        for (int tail = this.size - pos - 1, i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
        }
        --this.size;
        return true;
    }
    
    public boolean add(final byte k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final byte[] b = new byte[(this.size == 0) ? 2 : (this.size * 2)];
            int i = this.size;
            while (i-- != 0) {
                b[i] = this.a[i];
            }
            this.a = b;
        }
        this.a[this.size++] = k;
        return true;
    }
    
    public void clear() {
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public ByteArraySet clone() {
        ByteArraySet c;
        try {
            c = (ByteArraySet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.a = this.a.clone();
        return c;
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
