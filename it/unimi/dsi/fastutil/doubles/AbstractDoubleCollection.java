package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractDoubleCollection extends AbstractCollection<Double> implements DoubleCollection {
    protected AbstractDoubleCollection() {
    }
    
    public abstract DoubleIterator iterator();
    
    public boolean add(final double k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final double k) {
        final DoubleIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextDouble()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final double k) {
        final DoubleIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextDouble()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Double key) {
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
    
    public double[] toArray(double[] a) {
        if (a == null || a.length < this.size()) {
            a = new double[this.size()];
        }
        DoubleIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public double[] toDoubleArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public double[] toDoubleArray(final double[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final DoubleCollection c) {
        boolean retVal = false;
        final DoubleIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextDouble())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final DoubleCollection c) {
        final DoubleIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextDouble())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final DoubleCollection c) {
        boolean retVal = false;
        final DoubleIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextDouble())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final DoubleCollection c) {
        boolean retVal = false;
        final DoubleIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextDouble())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final DoubleIterator i = this.iterator();
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
            final double k = i.nextDouble();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
