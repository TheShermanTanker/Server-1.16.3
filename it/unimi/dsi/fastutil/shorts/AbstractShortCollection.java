package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractShortCollection extends AbstractCollection<Short> implements ShortCollection {
    protected AbstractShortCollection() {
    }
    
    public abstract ShortIterator iterator();
    
    public boolean add(final short k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final short k) {
        final ShortIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextShort()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final short k) {
        final ShortIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextShort()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Short key) {
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
    
    public short[] toArray(short[] a) {
        if (a == null || a.length < this.size()) {
            a = new short[this.size()];
        }
        ShortIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public short[] toShortArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public short[] toShortArray(final short[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final ShortCollection c) {
        boolean retVal = false;
        final ShortIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextShort())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final ShortCollection c) {
        final ShortIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextShort())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final ShortCollection c) {
        boolean retVal = false;
        final ShortIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextShort())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final ShortCollection c) {
        boolean retVal = false;
        final ShortIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextShort())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ShortIterator i = this.iterator();
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
            final short k = i.nextShort();
            s.append(String.valueOf((int)k));
        }
        s.append("}");
        return s.toString();
    }
}
