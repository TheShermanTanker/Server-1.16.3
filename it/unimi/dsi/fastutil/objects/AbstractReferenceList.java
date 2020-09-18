package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.Stack;

public abstract class AbstractReferenceList<K> extends AbstractReferenceCollection<K> implements ReferenceList<K>, Stack<K> {
    protected AbstractReferenceList() {
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
    
    public void add(final int index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean add(final K k) {
        this.add(this.size(), k);
        return true;
    }
    
    public K remove(final int i) {
        throw new UnsupportedOperationException();
    }
    
    public K set(final int index, final K k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends K> c) {
        this.ensureIndex(index);
        final Iterator<? extends K> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends K> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public ObjectListIterator<K> iterator() {
        return this.listIterator();
    }
    
    @Override
    public ObjectListIterator<K> listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        this.ensureIndex(index);
        return new ObjectListIterator<K>() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractReferenceList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public K next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceList this$0 = AbstractReferenceList.this;
                final int last = this.pos++;
                this.last = last;
                return (K)this$0.get(last);
            }
            
            public K previous() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractReferenceList this$0 = AbstractReferenceList.this;
                final int n = this.pos - 1;
                this.pos = n;
                this.last = n;
                return (K)this$0.get(n);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final K k) {
                AbstractReferenceList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final K k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractReferenceList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractReferenceList.this.remove(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    public boolean contains(final Object k) {
        return this.indexOf(k) >= 0;
    }
    
    public int indexOf(final Object k) {
        final ObjectListIterator<K> i = this.listIterator();
        while (i.hasNext()) {
            final K e = (K)i.next();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    public int lastIndexOf(final Object k) {
        final ObjectListIterator<K> i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final K e = i.previous();
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
                this.add(null);
            }
        }
        else {
            while (i-- != size) {
                this.remove(i);
            }
        }
    }
    
    @Override
    public ReferenceList<K> subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new ReferenceSubList<K>(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final ObjectListIterator<K> i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.next();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final K[] a, int offset, int length) {
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
    public void addElements(final int index, final K[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final Object[] a, int offset, int length) {
        final ObjectListIterator<K> i = this.listIterator(from);
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
            a[offset++] = i.next();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    public int hashCode() {
        final ObjectIterator<K> i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final K k = (K)i.next();
            h = 31 * h + System.identityHashCode(k);
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
        final ListIterator<?> i1 = this.listIterator();
        final ListIterator<?> i2 = l.listIterator();
        while (s-- != 0) {
            if (i1.next() != i2.next()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void push(final K o) {
        this.add(o);
    }
    
    @Override
    public K pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.remove(this.size() - 1);
    }
    
    @Override
    public K top() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return (K)this.get(this.size() - 1);
    }
    
    @Override
    public K peek(final int i) {
        return (K)this.get(this.size() - 1 - i);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
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
            final K k = (K)i.next();
            if (this == k) {
                s.append("(this list)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("]");
        return s.toString();
    }
    
    public static class ReferenceSubList<K> extends AbstractReferenceList<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceList<K> l;
        protected final int from;
        protected int to;
        
        public ReferenceSubList(final ReferenceList<K> l, final int from, final int to) {
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
        public boolean add(final K k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final K k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends K> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public K get(final int index) {
            this.ensureRestrictedIndex(index);
            return (K)this.l.get(this.from + index);
        }
        
        @Override
        public K remove(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return (K)this.l.remove(this.from + index);
        }
        
        @Override
        public K set(final int index, final K k) {
            this.ensureRestrictedIndex(index);
            return (K)this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
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
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            this.ensureIndex(index);
            return new ObjectListIterator<K>() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < ReferenceSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public K next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceList<K> l = ReferenceSubList.this.l;
                    final int from = ReferenceSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return (K)l.get(from + last);
                }
                
                public K previous() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final ReferenceList<K> l = ReferenceSubList.this.l;
                    final int from = ReferenceSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return (K)l.get(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final K k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert ReferenceSubList.this.assertRange();
                }
                
                public void set(final K k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    ReferenceSubList.this.remove(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert ReferenceSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new ReferenceSubList((ReferenceList<Object>)this, from, to);
        }
    }
}
