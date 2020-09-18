package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractFloatCollection extends AbstractCollection<Float> implements FloatCollection {
    protected AbstractFloatCollection() {
    }
    
    public abstract FloatIterator iterator();
    
    public boolean add(final float k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean contains(final float k) {
        final FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextFloat()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean rem(final float k) {
        final FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (k == iterator.nextFloat()) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean add(final Float key) {
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
    
    public float[] toArray(float[] a) {
        if (a == null || a.length < this.size()) {
            a = new float[this.size()];
        }
        FloatIterators.unwrap(this.iterator(), a);
        return a;
    }
    
    public float[] toFloatArray() {
        return this.toArray(null);
    }
    
    @Deprecated
    public float[] toFloatArray(final float[] a) {
        return this.toArray(a);
    }
    
    public boolean addAll(final FloatCollection c) {
        boolean retVal = false;
        final FloatIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.add(i.nextFloat())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean containsAll(final FloatCollection c) {
        final FloatIterator i = c.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.nextFloat())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean removeAll(final FloatCollection c) {
        boolean retVal = false;
        final FloatIterator i = c.iterator();
        while (i.hasNext()) {
            if (this.rem(i.nextFloat())) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public boolean retainAll(final FloatCollection c) {
        boolean retVal = false;
        final FloatIterator i = this.iterator();
        while (i.hasNext()) {
            if (!c.contains(i.nextFloat())) {
                i.remove();
                retVal = true;
            }
        }
        return retVal;
    }
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final FloatIterator i = this.iterator();
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
            final float k = i.nextFloat();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}
