package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection {
    protected AbstractLongCollection() {
    }
    
    public abstract LongIterator iterator();
    
    public boolean add(final long k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final long k) {
        final LongIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final long k) {
        final LongIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextLong()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Long key) {
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
    
    public long[] toArray(long[] a) {
        if (a == null || a.length < this.size()) {
            a = new long[this.size()];
        }
        LongIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public long[] toLongArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public long[] toLongArray(final long[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final LongCollection c) {
        boolean retVal = false;
        final LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final LongCollection c) {
        final LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextLong())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final LongCollection c) {
        boolean retVal = false;
        final LongIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextLong())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final LongCollection c) {
        boolean retVal = false;
        final LongIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextLong())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final LongIterator i = this.iterator();
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
            final long k = i.nextLong();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
