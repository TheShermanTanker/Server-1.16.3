package it.unimi.dsi.fastutil.ints;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection {
    protected AbstractIntCollection() {
    }
    
    public abstract IntIterator iterator();
    
    public boolean add(final int k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final int k) {
        final IntIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextInt()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Integer key) {
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
    
    public int[] toArray(int[] a) {
        if (a == null || a.length < this.size()) {
            a = new int[this.size()];
        }
        IntIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public int[] toIntArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public int[] toIntArray(final int[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final IntCollection c) {
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextInt())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextInt())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final IntCollection c) {
        boolean retVal = false;
        final IntIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextInt())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final IntIterator i = this.iterator();
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
            final int k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
