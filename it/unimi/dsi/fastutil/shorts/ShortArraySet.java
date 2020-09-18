package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.Collection;
import java.io.Serializable;

public class ShortArraySet extends AbstractShortSet implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private transient short[] a;
    private int size;
    
    public ShortArraySet(final short[] a) {
        this.a = a;
        this.size = a.length;
    }
    
    public ShortArraySet() {
        this.a = ShortArrays.EMPTY_ARRAY;
    }
    
    public ShortArraySet(final int capacity) {
        this.a = new short[capacity];
    }
    
    public ShortArraySet(final ShortCollection c) {
        this(c.size());
        this.addAll(c);
    }
    
    public ShortArraySet(final Collection<? extends Short> c) {
        this(c.size());
        this.addAll((Collection)c);
    }
    
    public ShortArraySet(final short[] a, final int size) {
        this.a = a;
        this.size = size;
        if (size > a.length) {
            throw new IllegalArgumentException(new StringBuilder().append("The provided size (").append(size).append(") is larger than or equal to the array size (").append(a.length).append(")").toString());
        }
    }
    
    private int findKey(final short o) {
        int i = this.size;
        while (i-- != 0) {
            if (this.a[i] == o) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ShortIterator iterator() {
        return new ShortIterator() {
            int next = 0;
            
            public boolean hasNext() {
                return this.next < ShortArraySet.this.size;
            }
            
            public short nextShort() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return ShortArraySet.this.a[this.next++];
            }
            
            public void remove() {
                final int tail = ShortArraySet.this.size-- - this.next--;
                System.arraycopy(ShortArraySet.this.a, this.next + 1, ShortArraySet.this.a, this.next, tail);
            }
        };
    }
    
    public boolean contains(final short k) {
        return this.findKey(k) != -1;
    }
    
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean remove(final short k) {
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
    
    public boolean add(final short k) {
        final int pos = this.findKey(k);
        if (pos != -1) {
            return false;
        }
        if (this.size == this.a.length) {
            final short[] b = new short[(this.size == 0) ? 2 : (this.size * 2)];
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
    
    public ShortArraySet clone() {
        ShortArraySet c;
        try {
            c = (ShortArraySet)super.clone();
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
            s.writeShort((int)this.a[i]);
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = new short[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.a[i] = s.readShort();
        }
    }
}
