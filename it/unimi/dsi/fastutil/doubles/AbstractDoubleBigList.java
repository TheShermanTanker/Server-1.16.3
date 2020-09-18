package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractDoubleBigList extends AbstractDoubleCollection implements DoubleBigList, DoubleStack {
    protected AbstractDoubleBigList() {
    }
    
    protected void ensureIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index > this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than list size (").append(this.size64()).append(")").toString());
        }
    }
    
    protected void ensureRestrictedIndex(final long index) {
        if (index < 0L) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index >= this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size64()).append(")").toString());
        }
    }
    
    @Override
    public void add(final long index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final double k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public double removeDouble(final long i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double set(final long index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(long index, final Collection<? extends Double> c) {
        this.ensureIndex(index);
        final Iterator<? extends Double> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (Double)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Double> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public DoubleBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public DoubleBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public DoubleBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new DoubleBigListIterator() {
            long pos = index;
            long last = -1L;
            
            public boolean hasNext() {
                return this.pos < AbstractDoubleBigList.this.size64();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public double nextDouble() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleBigList this$0 = AbstractDoubleBigList.this;
                final long n = this.pos++;
                this.last = n;
                return this$0.getDouble(n);
            }
            
            public double previousDouble() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleBigList this$0 = AbstractDoubleBigList.this;
                final long long1 = this.pos - 1L;
                this.pos = long1;
                this.last = long1;
                return this$0.getDouble(long1);
            }
            
            public long nextIndex() {
                return this.pos;
            }
            
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            public void add(final double k) {
                AbstractDoubleBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final double k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractDoubleBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractDoubleBigList.this.removeDouble(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    @Override
    public boolean contains(final double k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final double k) {
        final DoubleBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final double e = i.nextDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final double k) {
        final DoubleBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final double e = i.previousDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
                return i.nextIndex();
            }
        }
        return -1L;
    }
    
    public void size(final long size) {
        long i = this.size64();
        if (size > i) {
            while (i++ < size) {
                this.add(0.0);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public DoubleBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new DoubleSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final DoubleBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0L) {
            i.nextDouble();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final double[][] a, long offset, long length) {
        this.ensureIndex(index);
        DoubleBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, DoubleBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final double[][] a) {
        this.addElements(index, a, 0L, DoubleBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final double[][] a, long offset, long length) {
        final DoubleBigListIterator i = this.listIterator(from);
        DoubleBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from + length).append(") is greater than list size (").append(this.size64()).append(")").toString());
        }
        while (length-- != 0L) {
            DoubleBigArrays.set(a, offset++, i.nextDouble());
        }
    }
    
    public void clear() {
        this.removeElements(0L, this.size64());
    }
    
    @Deprecated
    public int size() {
        return (int)Math.min(2147483647L, this.size64());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final DoubleIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final double k = i.nextDouble();
            h = 31 * h + HashCommon.double2int(k);
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BigList)) {
            return false;
        }
        final BigList<?> l = o;
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        if (l instanceof DoubleBigList) {
            final DoubleBigListIterator i1 = this.listIterator();
            final DoubleBigListIterator i2 = ((DoubleBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextDouble() != i2.nextDouble()) {
                    return false;
                }
            }
            return true;
        }
        final BigListIterator<?> i3 = this.listIterator();
        final BigListIterator<?> i4 = l.listIterator();
        while (s-- != 0L) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final BigList<? extends Double> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof DoubleBigList) {
            final DoubleBigListIterator i1 = this.listIterator();
            final DoubleBigListIterator i2 = ((DoubleBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final double e1 = i1.nextDouble();
                final double e2 = i2.nextDouble();
                final int r;
                if ((r = Double.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Double> i3 = this.listIterator();
        final BigListIterator<? extends Double> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final double o) {
        this.add(o);
    }
    
    @Override
    public double popDouble() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeDouble(this.size64() - 1L);
    }
    
    @Override
    public double topDouble() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getDouble(this.size64() - 1L);
    }
    
    @Override
    public double peekDouble(final int i) {
        return this.getDouble(this.size64() - 1L - i);
    }
    
    @Override
    public boolean rem(final double k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeDouble(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final DoubleCollection c) {
        return this.addAll(index, c);
    }
    
    @Override
    public boolean addAll(final long index, final DoubleBigList l) {
        return this.addAll(index, (DoubleCollection)l);
    }
    
    @Override
    public boolean addAll(final DoubleCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final DoubleBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Double ok) {
        this.add(index, (double)ok);
    }
    
    @Deprecated
    @Override
    public Double set(final long index, final Double ok) {
        return this.set(index, (double)ok);
    }
    
    @Deprecated
    @Override
    public Double get(final long index) {
        return this.getDouble(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((double)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((double)ok);
    }
    
    @Deprecated
    @Override
    public Double remove(final long index) {
        return this.removeDouble(index);
    }
    
    @Deprecated
    @Override
    public void push(final Double o) {
        this.push((double)o);
    }
    
    @Deprecated
    @Override
    public Double pop() {
        return this.popDouble();
    }
    
    @Deprecated
    @Override
    public Double top() {
        return this.topDouble();
    }
    
    @Deprecated
    @Override
    public Double peek(final int i) {
        return this.peekDouble(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final DoubleIterator i = this.iterator();
        long n = this.size64();
        boolean first = true;
        s.append("[");
        while (n-- != 0L) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final double k = i.nextDouble();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class DoubleSubList extends AbstractDoubleBigList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleBigList l;
        protected final long from;
        protected long to;
        
        public DoubleSubList(final DoubleBigList l, final long from, final long to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private boolean assertRange() {
            assert this.from <= this.l.size64();
            assert this.to <= this.l.size64();
            assert this.to >= this.from;
            return true;
        }
        
        @Override
        public boolean add(final double k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final long index, final double k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Double> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public double getDouble(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getDouble(this.from + index);
        }
        
        @Override
        public double removeDouble(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeDouble(this.from + index);
        }
        
        @Override
        public double set(final long index, final double k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public long size64() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final long from, final double[][] a, final long offset, final long length) {
            this.ensureIndex(from);
            if (from + length > this.size64()) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from).append(length).append(") is greater than list size (").append(this.size64()).append(")").toString());
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert this.assertRange();
        }
        
        @Override
        public void addElements(final long index, final double[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public DoubleBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new DoubleBigListIterator() {
                long pos = index;
                long last = -1L;
                
                public boolean hasNext() {
                    return this.pos < DoubleSubList.this.size64();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                public double nextDouble() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleBigList l = DoubleSubList.this.l;
                    final long from = DoubleSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getDouble(from + last);
                }
                
                public double previousDouble() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleBigList l = DoubleSubList.this.l;
                    final long from = DoubleSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getDouble(from + n);
                }
                
                public long nextIndex() {
                    return this.pos;
                }
                
                public long previousIndex() {
                    return this.pos - 1L;
                }
                
                public void add(final double k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.add(this.pos++, k);
                    this.last = -1L;
                    assert DoubleSubList.this.assertRange();
                }
                
                public void set(final double k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.removeDouble(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                    assert DoubleSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public DoubleBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new DoubleSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final double k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeDouble(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final long index, final DoubleCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final long index, final DoubleBigList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
