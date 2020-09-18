package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractLongList extends AbstractLongCollection implements LongList, LongStack {
    protected AbstractLongList() {
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
    public void add(final int index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final long k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public long removeLong(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public long set(final int index, final long k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Long> c) {
        this.ensureIndex(index);
        final Iterator<? extends Long> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (long)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Long> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public LongListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public LongListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public LongListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new LongListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractLongList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public long nextLong() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractLongList this$0 = AbstractLongList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getLong(n);
            }
            
            public long previousLong() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractLongList this$0 = AbstractLongList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getLong(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final long k) {
                AbstractLongList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final long k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractLongList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractLongList.this.removeLong(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final long k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final long k) {
        final LongListIterator i = this.listIterator();
        while (i.hasNext()) {
            final long e = i.nextLong();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final long k) {
        final LongListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final long e = i.previousLong();
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
                this.add(0L);
            }
        }
        else {
            while (i-- != size) {
                this.removeLong(i);
            }
        }
    }
    
    @Override
    public LongList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new LongSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final LongListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextLong();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final long[] a, int offset, int length) {
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
    public void addElements(final int index, final long[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final long[] a, int offset, int length) {
        final LongListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextLong();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final LongIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final long k = i.nextLong();
            h = 31 * h + HashCommon.long2int(k);
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
        if (l instanceof LongList) {
            final LongListIterator i1 = this.listIterator();
            final LongListIterator i2 = ((LongList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextLong() != i2.nextLong()) {
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
    
    public int compareTo(final List<? extends Long> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof LongList) {
            final LongListIterator i1 = this.listIterator();
            final LongListIterator i2 = ((LongList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final long e1 = i1.nextLong();
                final long e2 = i2.nextLong();
                final int r;
                if ((r = Long.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Long> i3 = this.listIterator();
        final ListIterator<? extends Long> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final long o) {
        this.add(o);
    }
    
    @Override
    public long popLong() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeLong(this.size() - 1);
    }
    
    @Override
    public long topLong() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getLong(this.size() - 1);
    }
    
    @Override
    public long peekLong(final int i) {
        return this.getLong(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final long k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeLong(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final LongCollection c) {
        this.ensureIndex(index);
        final LongIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextLong());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final LongList l) {
        return this.addAll(index, (LongCollection)l);
    }
    
    @Override
    public boolean addAll(final LongCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final LongList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final LongIterator i = this.iterator();
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
            final long k = i.nextLong();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class LongSubList extends AbstractLongList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongList l;
        protected final int from;
        protected int to;
        
        public LongSubList(final LongList l, final int from, final int to) {
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
        public boolean add(final long k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final long k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Long> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public long getLong(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getLong(this.from + index);
        }
        
        @Override
        public long removeLong(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeLong(this.from + index);
        }
        
        @Override
        public long set(final int index, final long k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final long[] a, final int offset, final int length) {
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
        public void addElements(final int index, final long[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public LongListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new LongListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < LongSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public long nextLong() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final LongList l = LongSubList.this.l;
                    final int from = LongSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getLong(from + last);
                }
                
                public long previousLong() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final LongList l = LongSubList.this.l;
                    final int from = LongSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getLong(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final long k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert LongSubList.this.assertRange();
                }
                
                public void set(final long k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    LongSubList.this.removeLong(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert LongSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public LongList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new LongSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final long k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeLong(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final LongCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final LongList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
