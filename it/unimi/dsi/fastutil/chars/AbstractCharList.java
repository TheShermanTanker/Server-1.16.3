package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.ListIterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Collection;

public abstract class AbstractCharList extends AbstractCharCollection implements CharList, CharStack {
    protected AbstractCharList() {
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
    public void add(final int index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final char k) {
        this.add(this.size(), k);
        return true;
    }
    
    @Override
    public char removeChar(final int i) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public char set(final int index, final char k) {
        throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int index, final Collection<? extends Character> c) {
        this.ensureIndex(index);
        final Iterator<? extends Character> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, (char)i.next());
        }
        return retVal;
    }
    
    public boolean addAll(final Collection<? extends Character> c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public CharListIterator iterator() {
        return this.listIterator();
    }
    
    @Override
    public CharListIterator listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public CharListIterator listIterator(final int index) {
        this.ensureIndex(index);
        return new CharListIterator() {
            int pos = index;
            int last = -1;
            
            public boolean hasNext() {
                return this.pos < AbstractCharList.this.size();
            }
            
            public boolean hasPrevious() {
                return this.pos > 0;
            }
            
            public char nextChar() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharList this$0 = AbstractCharList.this;
                final int n = this.pos++;
                this.last = n;
                return this$0.getChar(n);
            }
            
            public char previousChar() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                final AbstractCharList this$0 = AbstractCharList.this;
                final int integer = this.pos - 1;
                this.pos = integer;
                this.last = integer;
                return this$0.getChar(integer);
            }
            
            public int nextIndex() {
                return this.pos;
            }
            
            public int previousIndex() {
                return this.pos - 1;
            }
            
            public void add(final char k) {
                AbstractCharList.this.add(this.pos++, k);
                this.last = -1;
            }
            
            public void set(final char k) {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractCharList.this.set(this.last, k);
            }
            
            public void remove() {
                if (this.last == -1) {
                    throw new IllegalStateException();
                }
                AbstractCharList.this.removeChar(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1;
            }
        };
    }
    
    @Override
    public boolean contains(final char k) {
        return this.indexOf(k) >= 0;
    }
    
    @Override
    public int indexOf(final char k) {
        final CharListIterator i = this.listIterator();
        while (i.hasNext()) {
            final char e = i.nextChar();
            if (k == e) {
                return i.previousIndex();
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(final char k) {
        final CharListIterator i = this.listIterator(this.size());
        while (i.hasPrevious()) {
            final char e = i.previousChar();
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
                this.add('\0');
            }
        }
        else {
            while (i-- != size) {
                this.removeChar(i);
            }
        }
    }
    
    @Override
    public CharList subList(final int from, final int to) {
        this.ensureIndex(from);
        this.ensureIndex(to);
        if (from > to) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        return new CharSubList(this, from, to);
    }
    
    @Override
    public void removeElements(final int from, final int to) {
        this.ensureIndex(to);
        final CharListIterator i = this.listIterator(from);
        int n = to - from;
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
        }
        while (n-- != 0) {
            i.nextChar();
            i.remove();
        }
    }
    
    @Override
    public void addElements(int index, final char[] a, int offset, int length) {
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
    public void addElements(final int index, final char[] a) {
        this.addElements(index, a, 0, a.length);
    }
    
    @Override
    public void getElements(final int from, final char[] a, int offset, int length) {
        final CharListIterator i = this.listIterator(from);
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
            a[offset++] = i.nextChar();
        }
    }
    
    public void clear() {
        this.removeElements(0, this.size());
    }
    
    private boolean valEquals(final Object a, final Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }
    
    public int hashCode() {
        final CharIterator i = this.iterator();
        int h = 1;
        int s = this.size();
        while (s-- != 0) {
            final char k = i.nextChar();
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
        if (l instanceof CharList) {
            final CharListIterator i1 = this.listIterator();
            final CharListIterator i2 = ((CharList)l).listIterator();
            while (s-- != 0) {
                if (i1.nextChar() != i2.nextChar()) {
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
    
    public int compareTo(final List<? extends Character> l) {
        if (l == this) {
            return 0;
        }
        if (l instanceof CharList) {
            final CharListIterator i1 = this.listIterator();
            final CharListIterator i2 = ((CharList)l).listIterator();
            while (i1.hasNext() && i2.hasNext()) {
                final char e1 = i1.nextChar();
                final char e2 = i2.nextChar();
                final int r;
                if ((r = Character.compare(e1, e2)) != 0) {
                    return r;
                }
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        final ListIterator<? extends Character> i3 = this.listIterator();
        final ListIterator<? extends Character> i4 = l.listIterator();
        while (i3.hasNext() && i4.hasNext()) {
            final int r;
            if ((r = ((Comparable)i3.next()).compareTo(i4.next())) != 0) {
                return r;
            }
        }
        return i4.hasNext() ? -1 : (i3.hasNext() ? 1 : 0);
    }
    
    @Override
    public void push(final char o) {
        this.add(o);
    }
    
    @Override
    public char popChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.removeChar(this.size() - 1);
    }
    
    @Override
    public char topChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.getChar(this.size() - 1);
    }
    
    @Override
    public char peekChar(final int i) {
        return this.getChar(this.size() - 1 - i);
    }
    
    @Override
    public boolean rem(final char k) {
        final int index = this.indexOf(k);
        if (index == -1) {
            return false;
        }
        this.removeChar(index);
        return true;
    }
    
    @Override
    public boolean addAll(int index, final CharCollection c) {
        this.ensureIndex(index);
        final CharIterator i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) {
            this.add(index++, i.nextChar());
        }
        return retVal;
    }
    
    @Override
    public boolean addAll(final int index, final CharList l) {
        return this.addAll(index, (CharCollection)l);
    }
    
    @Override
    public boolean addAll(final CharCollection c) {
        return this.addAll(this.size(), c);
    }
    
    @Override
    public boolean addAll(final CharList l) {
        return this.addAll(this.size(), l);
    }
    
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final CharIterator i = this.iterator();
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
            final char k = i.nextChar();
            s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }
    
    public static class CharSubList extends AbstractCharList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharList l;
        protected final int from;
        protected int to;
        
        public CharSubList(final CharList l, final int from, final int to) {
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
        public boolean add(final char k) {
            this.l.add(this.to, k);
            ++this.to;
            assert this.assertRange();
            return true;
        }
        
        @Override
        public void add(final int index, final char k) {
            this.ensureIndex(index);
            this.l.add(this.from + index, k);
            ++this.to;
            assert this.assertRange();
        }
        
        @Override
        public boolean addAll(final int index, final Collection<? extends Character> c) {
            this.ensureIndex(index);
            this.to += c.size();
            return this.l.addAll(this.from + index, (Collection)c);
        }
        
        public char getChar(final int index) {
            this.ensureRestrictedIndex(index);
            return this.l.getChar(this.from + index);
        }
        
        @Override
        public char removeChar(final int index) {
            this.ensureRestrictedIndex(index);
            --this.to;
            return this.l.removeChar(this.from + index);
        }
        
        @Override
        public char set(final int index, final char k) {
            this.ensureRestrictedIndex(index);
            return this.l.set(this.from + index, k);
        }
        
        public int size() {
            return this.to - this.from;
        }
        
        @Override
        public void getElements(final int from, final char[] a, final int offset, final int length) {
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
        public void addElements(final int index, final char[] a, final int offset, final int length) {
            this.ensureIndex(index);
            this.l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert this.assertRange();
        }
        
        @Override
        public CharListIterator listIterator(final int index) {
            this.ensureIndex(index);
            return new CharListIterator() {
                int pos = index;
                int last = -1;
                
                public boolean hasNext() {
                    return this.pos < CharSubList.this.size();
                }
                
                public boolean hasPrevious() {
                    return this.pos > 0;
                }
                
                public char nextChar() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    final CharList l = CharSubList.this.l;
                    final int from = CharSubList.this.from;
                    final int last = this.pos++;
                    this.last = last;
                    return l.getChar(from + last);
                }
                
                public char previousChar() {
                    if (!this.hasPrevious()) {
                        throw new NoSuchElementException();
                    }
                    final CharList l = CharSubList.this.l;
                    final int from = CharSubList.this.from;
                    final int n = this.pos - 1;
                    this.pos = n;
                    this.last = n;
                    return l.getChar(from + n);
                }
                
                public int nextIndex() {
                    return this.pos;
                }
                
                public int previousIndex() {
                    return this.pos - 1;
                }
                
                public void add(final char k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.add(this.pos++, k);
                    this.last = -1;
                    assert CharSubList.this.assertRange();
                }
                
                public void set(final char k) {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.set(this.last, k);
                }
                
                public void remove() {
                    if (this.last == -1) {
                        throw new IllegalStateException();
                    }
                    CharSubList.this.removeChar(this.last);
                    if (this.last < this.pos) {
                        --this.pos;
                    }
                    this.last = -1;
                    assert CharSubList.this.assertRange();
                }
            };
        }
        
        @Override
        public CharList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IllegalArgumentException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            return new CharSubList(this, from, to);
        }
        
        @Override
        public boolean rem(final char k) {
            final int index = this.indexOf(k);
            if (index == -1) {
                return false;
            }
            --this.to;
            this.l.removeChar(this.from + index);
            assert this.assertRange();
            return true;
        }
        
        @Override
        public boolean addAll(final int index, final CharCollection c) {
            this.ensureIndex(index);
            return super.addAll(index, c);
        }
        
        @Override
        public boolean addAll(final int index, final CharList l) {
            this.ensureIndex(index);
            return super.addAll(index, l);
        }
    }
}
