package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractByteCollection extends AbstractCollection<Byte> implements ByteCollection {
    protected AbstractByteCollection() {
    }
    
    public abstract ByteIterator iterator();
    
    public boolean add(final byte k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final byte k) {
        final ByteIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextByte()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final byte k) {
        final ByteIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextByte()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Byte key) {
        return super.add(key);
    }
    
    @Deprecated
    public boolean contains(final Object key) {
        return super.contains(key);
    }
    
    @Deprecated
    public boolean remove(final Object key) {
        return super.remove(key);
    }
    
    public byte[] toArray(byte[] a) {
        if (a == null || a.length < this.size()) {
            a = new byte[this.size()];
        }
        ByteIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public byte[] toByteArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public byte[] toByteArray(final byte[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final ByteCollection c) {
        boolean retVal = false;
        final ByteIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextByte())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final ByteCollection c) {
        final ByteIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextByte())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final ByteCollection c) {
        boolean retVal = false;
        final ByteIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextByte())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final ByteCollection c) {
        boolean retVal = false;
        final ByteIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextByte())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ByteIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final byte k = i.nextByte();
            s.append(String.valueOf((int)k));
        }
        s.append("}");
        return s.toString();
    }
}
