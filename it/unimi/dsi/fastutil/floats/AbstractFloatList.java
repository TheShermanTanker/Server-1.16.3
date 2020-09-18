package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractFloatList extends AbstractFloatCollection implements FloatList, FloatStack {
    protected AbstractFloatList() {
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
    public void add(final int index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final float k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public float removeFloat(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float set(final int index, final float k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Float> c) {
        this.ensureIndex(index);
        final Iterator<? extends Float> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (float)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Float> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public FloatListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public FloatListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public FloatListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new FloatListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractFloatList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatList this$0 = AbstractFloatList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getFloat(n);
            }
            
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractFloatList this$0 = AbstractFloatList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getFloat(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final float k) {
                AbstractFloatList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final float k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractFloatList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractFloatList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final float k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final float k) {
        final FloatListIterator i = this.listIterator();
        while (i.hasNext()) {
            final float e = i.nextFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final float k) {
        final FloatListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final float e = i.previousFloat();
            if (Float.floatToIntBits(k) == Float.floatToIntBits(e)) {
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
                this.add(0.0f);
            }
        }
        else {
            while (i-- != size) {
                this.removeFloat(i);
            }
        }
    }
    
    @Override
    public FloatList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new FloatSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final FloatListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextFloat();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final float[] a, int offset, int length) {
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
    public void addElements(final int index, final float[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final float[] a, int offset, int length) {
        final FloatListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextFloat();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final FloatIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final float k = i.nextFloat();
            h = 31 * h + HashCommon.float2int(k);
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
        if (l instanceof FloatList) {
            final FloatListIterator i1 = this.listIterator();
            final FloatListIterator i2 = ((FloatList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextFloat() != i2.nextFloat()) {
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
    
    public int compareTo(final List<? extends Float> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof FloatList) {
            final FloatListIterator i1 = this.listIterator();
            final FloatListIterator i2 = ((FloatList)l).listIterator();
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
        final ListIterator<? extends Float> i3 = this.listIterator();
        final ListIterator<? extends Float> i4 = l.listIterator();
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
        return this.removeFloat(this.size() - 1);
    }
    
    @Override
    public float topFloat() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getFloat(this.size() - 1);
    }
    
    @Override
    public float peekFloat(final int i) {
        return this.getFloat(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final float k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeFloat(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final FloatCollection c) {
        this.ensureIndex(index);
        final FloatIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextFloat());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final FloatList l) {
        return this.addAll(index, (FloatCollection)l);
    }
    
    @Override
    public boolean addAll(final FloatCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final FloatList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final FloatIterator i = this.iterator();
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
            final float k = i.nextFloat();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class FloatSubList extends AbstractFloatList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatList l;
        protected final int from;
        protected int to;
        
        public FloatSubList(final FloatList l, final int from, final int to) {
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
        public boolean add(final float k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final float k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Float> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public float getFloat(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getFloat(this.from + index);
        }
        
        @Override
        public float removeFloat(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeFloat(this.from + index);
        }
        
        @Override
        public float set(final int index, final float k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final float[] a, final int offset, final int length) {
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
        public void addElements(final int index, final float[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public FloatListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new FloatListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < FloatSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public float nextFloat() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final FloatList l = FloatSubList.this.l;
                    final int from = FloatSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getFloat(from + last);
                }
                
                public float previousFloat() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final FloatList l = FloatSubList.this.l;
                    final int from = FloatSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getFloat(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final float k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert FloatSubList.this.assertRange();
                }
                
                public void set(final float k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    FloatSubList.this.removeFloat(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert FloatSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public FloatList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new FloatSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final float k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeFloat(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final FloatCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final FloatList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
