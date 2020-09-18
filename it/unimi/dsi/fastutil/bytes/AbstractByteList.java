package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractByteList extends AbstractByteCollection implements ByteList, ByteStack {
    protected AbstractByteList() {
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
    public void add(final int index, final byte k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final byte k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public byte removeByte(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public byte set(final int index, final byte k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Byte> c) {
        this.ensureIndex(index);
        final Iterator<? extends Byte> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (byte)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Byte> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public ByteListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public ByteListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ByteListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new ByteListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractByteList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public byte nextByte() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractByteList this$0 = AbstractByteList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getByte(n);
            }
            
            public byte previousByte() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractByteList this$0 = AbstractByteList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getByte(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final byte k) {
                AbstractByteList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final byte k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractByteList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractByteList.this.removeByte(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final byte k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final byte k) {
        final ByteListIterator i = this.listIterator();
        while (i.hasNext()) {
            final byte e = i.nextByte();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final byte k) {
        final ByteListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final byte e = i.previousByte();
            if (k == e) {
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
                this.add((byte)0);
            }
        }
        else {
            while (i-- != size) {
                this.removeByte(i);
            }
        }
    }
    
    @Override
    public ByteList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new ByteSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final ByteListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextByte();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final byte[] a, int offset, int length) {
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
    public void addElements(final int index, final byte[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final byte[] a, int offset, int length) {
        final ByteListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextByte();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final ByteIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final byte k = i.nextByte();
            h = 31 * h + k;
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
        if (l instanceof ByteList) {
            final ByteListIterator i1 = this.listIterator();
            final ByteListIterator i2 = ((ByteList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextByte() != i2.nextByte()) {
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
    
    public int compareTo(final List<? extends Byte> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof ByteList) {
            final ByteListIterator i1 = this.listIterator();
            final ByteListIterator i2 = ((ByteList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final byte e1 = i1.nextByte();
                final byte e2 = i2.nextByte();
                final int r;
                if ((r = Byte.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Byte> i3 = this.listIterator();
        final ListIterator<? extends Byte> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final byte o) {
        this.add(o);
    }
    
    @Override
    public byte popByte() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeByte(this.size() - 1);
    }
    
    @Override
    public byte topByte() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getByte(this.size() - 1);
    }
    
    @Override
    public byte peekByte(final int i) {
        return this.getByte(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final byte k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeByte(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final ByteCollection c) {
        this.ensureIndex(index);
        final ByteIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextByte());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final ByteList l) {
        return this.addAll(index, (ByteCollection)l);
    }
    
    @Override
    public boolean addAll(final ByteCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final ByteList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ByteIterator i = this.iterator();
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
            final byte k = i.nextByte();
            s.append(String.valueOf((int)k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ByteSubList extends AbstractByteList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteList l;
        protected final int from;
        protected int to;
        
        public ByteSubList(final ByteList l, final int from, final int to) {
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
        public boolean add(final byte k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final byte k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Byte> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public byte getByte(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getByte(this.from + index);
        }
        
        @Override
        public byte removeByte(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeByte(this.from + index);
        }
        
        @Override
        public byte set(final int index, final byte k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final byte[] a, final int offset, final int length) {
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
        public void addElements(final int index, final byte[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public ByteListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new ByteListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < ByteSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public byte nextByte() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ByteList l = ByteSubList.this.l;
                    final int from = ByteSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getByte(from + last);
                }
                
                public byte previousByte() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ByteList l = ByteSubList.this.l;
                    final int from = ByteSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getByte(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final byte k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert ByteSubList.this.assertRange();
                }
                
                public void set(final byte k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ByteSubList.this.removeByte(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert ByteSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public ByteList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new ByteSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final byte k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeByte(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final ByteCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final ByteList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
