package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractBooleanCollection extends AbstractCollection<Boolean> implements BooleanCollection {
    protected AbstractBooleanCollection() {
    }
    
    public abstract BooleanIterator iterator();
    
    public boolean add(final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final boolean k) {
        final BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextBoolean()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final boolean k) {
        final BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextBoolean()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Boolean key) {
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
    
    public boolean[] toArray(boolean[] a) {
        if (a == null || a.length < this.size()) {
            a = new boolean[this.size()];
        }
        BooleanIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public boolean[] toBooleanArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public boolean[] toBooleanArray(final boolean[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final BooleanCollection c) {
        boolean retVal = false;
        final BooleanIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextBoolean())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final BooleanCollection c) {
        final BooleanIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextBoolean())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final BooleanCollection c) {
        boolean retVal = false;
        final BooleanIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextBoolean())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final BooleanCollection c) {
        boolean retVal = false;
        final BooleanIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextBoolean())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final BooleanIterator i = this.iterator();
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
            final boolean k = i.nextBoolean();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
