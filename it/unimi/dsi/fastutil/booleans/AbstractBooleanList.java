package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractBooleanList extends AbstractBooleanCollection implements BooleanList, BooleanStack {
    protected AbstractBooleanList() {
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
    public void add(final int index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final boolean k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public boolean removeBoolean(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean set(final int index, final boolean k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Boolean> c) {
        this.ensureIndex(index);
        final Iterator<? extends Boolean> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (boolean)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Boolean> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public BooleanListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public BooleanListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public BooleanListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new BooleanListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractBooleanList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public boolean nextBoolean() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanList this$0 = AbstractBooleanList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getBoolean(n);
            }
            
            public boolean previousBoolean() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractBooleanList this$0 = AbstractBooleanList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getBoolean(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final boolean k) {
                AbstractBooleanList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final boolean k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractBooleanList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractBooleanList.this.removeBoolean(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final boolean k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final boolean k) {
        final BooleanListIterator i = this.listIterator();
        while (i.hasNext()) {
            final boolean e = i.nextBoolean();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final boolean k) {
        final BooleanListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final boolean e = i.previousBoolean();
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
                this.add(false);
            }
        }
        else {
            while (i-- != size) {
                this.removeBoolean(i);
            }
        }
    }
    
    @Override
    public BooleanList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new BooleanSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final BooleanListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextBoolean();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final boolean[] a, int offset, int length) {
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
    public void addElements(final int index, final boolean[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final boolean[] a, int offset, int length) {
        final BooleanListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextBoolean();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final BooleanIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final boolean k = i.nextBoolean();
            h = 31 * h + (k ? 1231 : 1237);
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
        if (l instanceof BooleanList) {
            final BooleanListIterator i1 = this.listIterator();
            final BooleanListIterator i2 = ((BooleanList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextBoolean() != i2.nextBoolean()) {
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
    
    public int compareTo(final List<? extends Boolean> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof BooleanList) {
            final BooleanListIterator i1 = this.listIterator();
            final BooleanListIterator i2 = ((BooleanList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final boolean e1 = i1.nextBoolean();
                final boolean e2 = i2.nextBoolean();
                final int r;
                if ((r = Boolean.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Boolean> i3 = this.listIterator();
        final ListIterator<? extends Boolean> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final boolean o) {
        this.add(o);
    }
    
    @Override
    public boolean popBoolean() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeBoolean(this.size() - 1);
    }
    
    @Override
    public boolean topBoolean() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getBoolean(this.size() - 1);
    }
    
    @Override
    public boolean peekBoolean(final int i) {
        return this.getBoolean(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final boolean k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeBoolean(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final BooleanCollection c) {
        this.ensureIndex(index);
        final BooleanIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextBoolean());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final BooleanList l) {
        return this.addAll(index, (BooleanCollection)l);
    }
    
    @Override
    public boolean addAll(final BooleanCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final BooleanList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final BooleanIterator i = this.iterator();
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
            final boolean k = i.nextBoolean();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class BooleanSubList extends AbstractBooleanList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanList l;
        protected final int from;
        protected int to;
        
        public BooleanSubList(final BooleanList l, final int from, final int to) {
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
        public boolean add(final boolean k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final boolean k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Boolean> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public boolean getBoolean(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getBoolean(this.from + index);
        }
        
        @Override
        public boolean removeBoolean(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeBoolean(this.from + index);
        }
        
        @Override
        public boolean set(final int index, final boolean k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final boolean[] a, final int offset, final int length) {
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
        public void addElements(final int index, final boolean[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public BooleanListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new BooleanListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < BooleanSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public boolean nextBoolean() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanList l = BooleanSubList.this.l;
                    final int from = BooleanSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getBoolean(from + last);
                }
                
                public boolean previousBoolean() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final BooleanList l = BooleanSubList.this.l;
                    final int from = BooleanSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getBoolean(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final boolean k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert BooleanSubList.this.assertRange();
                }
                
                public void set(final boolean k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    BooleanSubList.this.removeBoolean(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert BooleanSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new BooleanSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final boolean k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeBoolean(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final BooleanCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final BooleanList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
