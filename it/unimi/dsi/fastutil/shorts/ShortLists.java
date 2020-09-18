package it.unimi.dsi.fastutil.shorts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class ShortLists {
    public static final EmptyList EMPTY_LIST;
    
    private ShortLists() {
    }
    
    public static ShortList shuffle(final ShortList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final short t = l.getShort(i);
            l.set(i, l.getShort(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static ShortList singleton(final short element) {
        return new Singleton(element);
    }
    
    public static ShortList singleton(final Object element) {
        return new Singleton((short)element);
    }
    
    public static ShortList synchronize(final ShortList l) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }
    
    public static ShortList synchronize(final ShortList l, final Object sync) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }
    
    public static ShortList unmodifiable(final ShortList l) {
        return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends ShortCollections.EmptyCollection implements ShortList, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public short getShort(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean rem(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short removeShort(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short set(final int index, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final short k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final short k) {
            return -1;
        }
        
        public boolean addAll(final int i, final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ShortList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final int index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short get(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean add(final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short set(final int index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public int indexOf(final Object k) {
            return -1;
        }
        
        @Deprecated
        @Override
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        @Override
        public ShortListIterator listIterator() {
            return ShortIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ShortListIterator iterator() {
            return ShortIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ShortListIterator listIterator(final int i) {
            if (i == 0) {
                return ShortIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final short[] a, final int offset, final int length) {
            if (from == 0 && length == 0 && offset >= 0 && offset <= a.length) {
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final short[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final short[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        public int compareTo(final List<? extends Short> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return ShortLists.EMPTY_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof List && ((List)o).isEmpty();
        }
        
        public String toString() {
            return "[]";
        }
        
        private Object readResolve() {
            return ShortLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton extends AbstractShortList implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final short element;
        
        protected Singleton(final short element) {
            this.element = element;
        }
        
        public short getShort(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short removeShort(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final short k) {
            return k == this.element;
        }
        
        public short[] toShortArray() {
            final short[] a = { this.element };
            return a;
        }
        
        @Override
        public ShortListIterator listIterator() {
            return ShortIterators.singleton(this.element);
        }
        
        @Override
        public ShortListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ShortListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final ShortListIterator l = this.listIterator();
            if (i == 1) {
                l.nextShort();
            }
            return l;
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0 || to != 1) {
                return ShortLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ShortList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 1;
        }
        
        @Override
        public void size(final int size) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedList extends ShortCollections.SynchronizedCollection implements ShortList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortList list;
        
        protected SynchronizedList(final ShortList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final ShortList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public short getShort(final int i) {
            synchronized (this.sync) {
                return this.list.getShort(i);
            }
        }
        
        @Override
        public short set(final int i, final short k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final short k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public short removeShort(final int i) {
            synchronized (this.sync) {
                return this.list.removeShort(i);
            }
        }
        
        @Override
        public int indexOf(final short k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final short k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends Short> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, (Collection)c);
            }
        }
        
        @Override
        public void getElements(final int from, final short[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final int index, final short[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final short[] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Override
        public void size(final int size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        @Override
        public ShortListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ShortListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ShortListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedList(this.list.subList(from, to), this.sync);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.collection.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.collection.hashCode();
            }
        }
        
        public int compareTo(final List<? extends Short> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final ShortCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final ShortList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final ShortList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Deprecated
        @Override
        public Short get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Short k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Deprecated
        @Override
        public Short set(final int index, final Short k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Deprecated
        @Override
        public Short remove(final int i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Deprecated
        @Override
        public int indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Deprecated
        @Override
        public int lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
    
    public static class SynchronizedRandomAccessList extends SynchronizedList implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0L;
        
        protected SynchronizedRandomAccessList(final ShortList l, final Object sync) {
            super(l, sync);
        }
        
        protected SynchronizedRandomAccessList(final ShortList l) {
            super(l);
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
            }
        }
    }
    
    public static class UnmodifiableList extends ShortCollections.UnmodifiableCollection implements ShortList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ShortList list;
        
        protected UnmodifiableList(final ShortList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public short getShort(final int i) {
            return this.list.getShort(i);
        }
        
        @Override
        public short set(final int i, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final short k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public short removeShort(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final short k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final short k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final int index, final Collection<? extends Short> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final short[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final short[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final short[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public ShortListIterator listIterator() {
            return ShortIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ShortListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ShortListIterator listIterator(final int i) {
            return ShortIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            return new UnmodifiableList(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
        
        public int compareTo(final List<? extends Short> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final ShortCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ShortList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final ShortList l) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short get(final int i) {
            return this.list.get(i);
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short set(final int index, final Short k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Short remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public int indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Deprecated
        @Override
        public int lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
    
    public static class UnmodifiableRandomAccessList extends UnmodifiableList implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0L;
        
        protected UnmodifiableRandomAccessList(final ShortList l) {
            super(l);
        }
        
        @Override
        public ShortList subList(final int from, final int to) {
            return new UnmodifiableRandomAccessList(this.list.subList(from, to));
        }
    }
}
