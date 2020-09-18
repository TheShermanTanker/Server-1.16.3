package com.google.common.cache;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(emulated = true)
final class LongAdder extends Striped64 implements Serializable, LongAddable {
    private static final long serialVersionUID = 7249069246863182397L;
    
    @Override
    final long fn(final long v, final long x) {
        return v + x;
    }
    
    public LongAdder() {
    }
    
    public void add(final long x) {
        final Cell[] as;
        final long b;
        if ((as = this.cells) != null || !this.casBase(b = this.base, b + x)) {
            boolean uncontended = true;
            final int[] hc;
            final int n;
            final Cell a;
            final long v;
            if ((hc = (int[])LongAdder.threadHashCode.get()) == null || as == null || (n = as.length) < 1 || (a = as[n - 1 & hc[0]]) == null || !(uncontended = a.cas(v = a.value, v + x))) {
                this.retryUpdate(x, hc, uncontended);
            }
        }
    }
    
    public void increment() {
        this.add(1L);
    }
    
    public void decrement() {
        this.add(-1L);
    }
    
    public long sum() {
        long sum = this.base;
        final Cell[] as = this.cells;
        if (as != null) {
            for (final Cell a : as) {
                if (a != null) {
                    sum += a.value;
                }
            }
        }
        return sum;
    }
    
    public void reset() {
        this.internalReset(0L);
    }
    
    public long sumThenReset() {
        long sum = this.base;
        final Cell[] as = this.cells;
        this.base = 0L;
        if (as != null) {
            for (final Cell a : as) {
                if (a != null) {
                    sum += a.value;
                    a.value = 0L;
                }
            }
        }
        return sum;
    }
    
    public String toString() {
        return Long.toString(this.sum());
    }
    
    public long longValue() {
        return this.sum();
    }
    
    public int intValue() {
        return (int)this.sum();
    }
    
    public float floatValue() {
        return (float)this.sum();
    }
    
    public double doubleValue() {
        return (double)this.sum();
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(this.sum());
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.busy = 0;
        this.cells = null;
        this.base = s.readLong();
    }
}