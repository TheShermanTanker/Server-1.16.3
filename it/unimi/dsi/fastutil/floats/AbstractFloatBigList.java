package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import it.unimi.dsi.fastutil.BigListIterator;
import it.unimi.dsi.fastutil.BigList;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractFloatBigList extends AbstractFloatCollection implements FloatBigList, FloatStack {
    protected AbstractFloatBigList() {
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
    public void add(final long index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final float k) {
        this.add(this.size64(), k);
        return true;
    }
    
    @Override
    public float removeFloat(final long i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float set(final long index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(long index, final Collection<? extends Float> c) {
        this.ensureIndex(index);
        final Iterator<? extends Float> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (Float)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Float> c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public FloatBigListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public FloatBigListIterator listIterator() {
        return this.listIterator(0L);
    }
    
    @Override
    public FloatBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new FloatBigListIterator() {
            long pos = index;
            long last = -1L;
            
            public boolean hasNext() {
                return this.pos < AbstractFloatBigList.this.size64();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0L;
            }
            
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatBigList this$0 = AbstractFloatBigList.this;
                final long n = this.pos++;
                this.last = n;
                return this$0.getFloat(n);
            }
            
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatBigList this$0 = AbstractFloatBigList.this;
                final long long1 = this.pos - 1L;
                this.pos = long1;
                this.last = long1;
                return this$0.getFloat(long1);
            }
            
            public long nextIndex() {
                return this.pos;
            }
            
            public long previousIndex() {
                return this.pos - 1L;
            }
            
            public void add(final float k) {
                AbstractFloatBigList.this.add(this.pos++, k);
                this.last = -1L;
            }
            
            public void set(final float k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractFloatBigList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                AbstractFloatBigList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }
    
    @Override
    public boolean contains(final float k) {
        return this.indexOf(k) >= 0L;
    }
    
    @Override
    public long indexOf(final float k) {
        final FloatBigListIterator i = this.listIterator();
        while (i.hasNext()) {
            final float e = i.nextFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
                return i.previousIndex();
            }
        }
        return -1L;
    }
    
    @Override
    public long lastIndexOf(final float k) {
        final FloatBigListIterator i = this.listIterator(this.size64());
        while (i.hasPrevious()) {
            final float e = i.previousFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
                return i.nextIndex();
            }
        }
        return -1L;
    }
    
    public void size(final long size) {
        long i = this.size64();
        if (size > i) {
            while (i++ < size) {
                this.add(0.0f);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public FloatBigList subList(final long from, final long to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new FloatSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final long from, final long to) {
        this.ensureIndex(to);
        final FloatBigListIterator i = this.listIterator(from);
        long n = to - from;
        if (n < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0L) {
            i.nextFloat();
            i.remove();
        }
    }
    
    @Override
    public void addElements(long index, final float[][] a, long offset, long length) {
        this.ensureIndex(index);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        while (length-- != 0L) {
            this.add(index++, FloatBigArrays.get(a, offset++));
        }
    }
    
    @Override
    public void addElements(final long index, final float[][] a) {
        this.addElements(index, a, 0L, FloatBigArrays.length(a));
    }
    
    @Override
    public void getElements(final long from, final float[][] a, long offset, long length) {
        final FloatBigListIterator i = this.listIterator(from);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        if (from + length > this.size64()) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("End index (").append(from + length).append(") is greater than list size (").append(this.size64()).append(")").toString());
        }
        while (length-- != 0L) {
            FloatBigArrays.set(a, offset++, i.nextFloat());
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
        final FloatIterator i = this.iterator();
        int h = 1;
        long s = this.size64();
        while (s-- != 0L) {
            final float k = i.nextFloat();
            h = 31 * h + HashCommon.float2int(k);
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
        if (l instanceof FloatBigList) {
            final FloatBigListIterator i1 = this.listIterator();
            final FloatBigListIterator i2 = ((FloatBigList)l).listIterator();
            while (s-- != 0L) {
                if (i1.nextFloat() != i2.nextFloat()) {
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
    
    public int compareTo(final BigList<? extends Float> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof FloatBigList) {
            final FloatBigListIterator i1 = this.listIterator();
            final FloatBigListIterator i2 = ((FloatBigList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final float e1 = i1.nextFloat();
                final float e2 = i2.nextFloat();
                final int r;
                if ((r = Float.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final BigListIterator<? extends Float> i3 = this.listIterator();
        final BigListIterator<? extends Float> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final float o) {
        this.add(o);
    }
    
    @Override
    public float popFloat() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeFloat(this.size64() - 1L);
    }
    
    @Override
    public float topFloat() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getFloat(this.size64() - 1L);
    }
    
    @Override
    public float peekFloat(final int i) {
        return this.getFloat(this.size64() - 1L - i);
    }
    
    @Override
    public boolean rem(final float k) {
        final long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeFloat(index);
        return true;
    }
    
    @Override
    public boolean addAll(final long index, final FloatCollection c) {
        return this.addAll(index, c);
    }
    
    @Override
    public boolean addAll(final long index, final FloatBigList l) {
        return this.addAll(index, (FloatCollection)l);
    }
    
    @Override
    public boolean addAll(final FloatCollection c) {
        return this.addAll(this.size64(), c);
    }
    
    @Override
    public boolean addAll(final FloatBigList l) {
        return this.addAll(this.size64(), l);
    }
    
    @Deprecated
    @Override
    public void add(final long index, final Float ok) {
        this.add(index, (float)ok);
    }
    
    @Deprecated
    @Override
    public Float set(final long index, final Float ok) {
        return this.set(index, (float)ok);
    }
    
    @Deprecated
    @Override
    public Float get(final long index) {
        return this.getFloat(index);
    }
    
    @Deprecated
    @Override
    public long indexOf(final Object ok) {
        return this.indexOf((float)ok);
    }
    
    @Deprecated
    @Override
    public long lastIndexOf(final Object ok) {
        return this.lastIndexOf((float)ok);
    }
    
    @Deprecated
    @Override
    public Float remove(final long index) {
        return this.removeFloat(index);
    }
    
    @Deprecated
    @Override
    public void push(final Float o) {
        this.push((float)o);
    }
    
    @Deprecated
    @Override
    public Float pop() {
        return this.popFloat();
    }
    
    @Deprecated
    @Override
    public Float top() {
        return this.topFloat();
    }
    
    @Deprecated
    @Override
    public Float peek(final int i) {
        return this.peekFloat(i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final FloatIterator i = this.iterator();
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
            final float k = i.nextFloat();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class FloatSubList extends AbstractFloatBigList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatBigList l;
        protected final long from;
        protected long to;
        
        public FloatSubList(final FloatBigList l, final long from, final long to) {
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
        public boolean add(final float k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final long index, final float k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Float> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, c);
        }
        
        public float getFloat(final long index) {
            this.ensureRestrictedIndex(index);
            return this.l.getFloat(this.from + index);
        }
        
        @Override
        public float removeFloat(final long index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeFloat(this.from + index);
        }
        
        @Override
        public float set(final long index, final float k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public long size64() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final long from, final float[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final float[][] a, final long offset, final long length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public FloatBigListIterator listIterator(final long index) {
            this.ensureIndex(index);
            return new FloatBigListIterator() {
                long pos = index;
                long last = -1L;
                
                public boolean hasNext() {
                    return this.pos < FloatSubList.this.size64();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0L;
                }
                
                public float nextFloat() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final FloatBigList l = FloatSubList.this.l;
                    final long from = FloatSubList.this.from;
                    final long last = this.pos++;
                    this.last = last;
                    return l.getFloat(from + last);
                }
                
                public float previousFloat() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final FloatBigList l = FloatSubList.this.l;
                    final long from = FloatSubList.this.from;
                    final long n = this.pos - 1L;
                    this.pos = n;
                    this.last = n;
                    return l.getFloat(from + n);
                }
                
                public long nextIndex() {
                    return this.pos;
                }
                
                public long previousIndex() {
                    return this.pos - 1L;
                }
                
                public void add(final float k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.add(this.pos++, k);
                    this.last = -1L;
                    assert FloatSubList.this.assertRange();
                }
                
                public void set(final float k) {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1L) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.removeFloat(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1L;
                    assert FloatSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public FloatBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new FloatSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final float k) {
            final long index = this.indexOf(k);
            if (index == -1L) {
                return false;
            }
            --this.to;
            this.l.removeFloat(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final long index, final FloatCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final long index, final FloatBigList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
