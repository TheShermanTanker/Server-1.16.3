package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractDoubleList extends AbstractDoubleCollection implements DoubleList, DoubleStack {
    protected AbstractDoubleList() {
    }
    
    protected void ensureIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index > this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than list size (").append(this.size()).append(")").toString());
        }
    }
    
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is negative").toString());
        }
        if (index >= this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Index (").append(index).append(") is greater than or equal to list size (").append(this.size()).append(")").toString());
        }
    }
    
    @Override
    public void add(final int index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final double k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public double removeDouble(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public double set(final int index, final double k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Double> c) {
        this.ensureIndex(index);
        final Iterator<? extends Double> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (double)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Double> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public DoubleListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public DoubleListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public DoubleListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new DoubleListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractDoubleList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public double nextDouble() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleList this$0 = AbstractDoubleList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getDouble(n);
            }
            
            public double previousDouble() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractDoubleList this$0 = AbstractDoubleList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getDouble(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final double k) {
                AbstractDoubleList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final double k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractDoubleList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractDoubleList.this.removeDouble(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final double k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final double k) {
        final DoubleListIterator i = this.listIterator();
        while (i.hasNext()) {
            final double e = i.nextDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final double k) {
        final DoubleListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final double e = i.previousDouble();
            if (Double.doubleToLongBits(k) == Double.doubleToLongBits(e)) {
                return i.nextIndex();
            }
        }
        return -1;
    }
    
    @Override
    public void size(final int size) {
        int i = this.size();
        if (size > i) {
            while (i++ < size) {
                this.add(0.0);
            }
        }
        else {
            while (i-- != size) {
                this.removeDouble(i);
            }
        }
    }
    
    @Override
    public DoubleList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new DoubleSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final DoubleListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextDouble();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final double[] a, int offset, int length) {
        this.ensureIndex(index);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("Offset (").append(offset).append(") is negative").toString());
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("End index (").append(offset + length).append(") is greater than array length (").append(a.length).append(")").toString());
        }
        while (length-- != 0) {
            this.add(index++, a[offset++]);
        }
    }
    
    @Override
    public void addElements(final int index, final double[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final double[] a, int offset, int length) {
        final DoubleListIterator i = this.listIterator(from);
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("Offset (").append(offset).append(") is negative").toString());
        }
        if (offset + length > a.length) {
            throw new ArrayIndexOutOfBoundsException(new StringBuilder().append("End index (").append(offset + length).append(") is greater than array length (").append(a.length).append(")").toString());
        }
        if (from + length > this.size()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from + length).append(") is greater than list size (").append(this.size()).append(")").toString());
        }
        while (length-- != 0) {
            a[offset++] = i.nextDouble();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final DoubleIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final double k = i.nextDouble();
            h = 31 * h + HashCommon.double2int(k);
        }
        return h;
    }
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }
        final List<?> l = o;
        int s = this.size();
        if (s != l.size()) {
            return false;
        }
        if (l instanceof DoubleList) {
            final DoubleListIterator i1 = this.listIterator();
            final DoubleListIterator i2 = ((DoubleList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextDouble() != i2.nextDouble()) {
                    return false;
                }
            }
            return true;
        }
        final ListIterator<?> i3 = this.listIterator();
        final ListIterator<?> i4 = l.listIterator();
        while (s-- != 0) {
            if (!this.valEquals(i3.next(), i4.next())) {
                return false;
            }
        }
        return true;
    }
    
    public int compareTo(final List<? extends Double> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof DoubleList) {
            final DoubleListIterator i1 = this.listIterator();
            final DoubleListIterator i2 = ((DoubleList)l).listIterator();
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
        final ListIterator<? extends Double> i3 = this.listIterator();
        final ListIterator<? extends Double> i4 = l.listIterator();
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
        return this.removeDouble(this.size() - 1);
    }
    
    @Override
    public double topDouble() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getDouble(this.size() - 1);
    }
    
    @Override
    public double peekDouble(final int i) {
        return this.getDouble(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final double k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeDouble(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final DoubleCollection c) {
        this.ensureIndex(index);
        final DoubleIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextDouble());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final DoubleList l) {
        return this.addAll(index, (DoubleCollection)l);
    }
    
    @Override
    public boolean addAll(final DoubleCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final DoubleList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final DoubleIterator i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("[");
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
        s.append("]");
        return s.toString();
    }
    
    public static class DoubleSubList extends AbstractDoubleList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final DoubleList l;
        protected final int from;
        protected int to;
        
        public DoubleSubList(final DoubleList l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }
        
        private boolean assertRange() {
            assert this.from <= this.l.size();
            assert this.to <= this.l.size();
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
        public void add(final int index, final double k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Double> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public double getDouble(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getDouble(this.from + index);
        }
        
        @Override
        public double removeDouble(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeDouble(this.from + index);
        }
        
        @Override
        public double set(final int index, final double k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final double[] a, final int offset, final int length) {
            this.ensureIndex(from);
            if (from + length > this.size()) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from).append(length).append(") is greater than list size (").append(this.size()).append(")").toString());
            }
            this.l.getElements(this.from + from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            this.l.removeElements(this.from + from, this.from + to);
            this.to -= to - from;
            assert this.assertRange();
        }
        
        @Override
        public void addElements(final int index, final double[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public DoubleListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new DoubleListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < DoubleSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public double nextDouble() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleList l = DoubleSubList.this.l;
                    final int from = DoubleSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getDouble(from + last);
                }
                
                public double previousDouble() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final DoubleList l = DoubleSubList.this.l;
                    final int from = DoubleSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getDouble(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final double k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert DoubleSubList.this.assertRange();
                }
                
                public void set(final double k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    DoubleSubList.this.removeDouble(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert DoubleSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public DoubleList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new DoubleSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final double k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeDouble(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final DoubleCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final DoubleList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
