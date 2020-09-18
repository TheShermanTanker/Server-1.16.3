package it.unimi.dsi.fastutil.ints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class IntLists {
    public static final EmptyList EMPTY_LIST;
    
    private IntLists() {
    }
    
    public static IntList shuffle(final IntList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final int t = l.getInt(i);
            l.set(i, l.getInt(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static IntList singleton(final int element) {
        return new Singleton(element);
    }
    
    public static IntList singleton(final Object element) {
        return new Singleton((int)element);
    }
    
    public static IntList synchronize(final IntList l) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }
    
    public static IntList synchronize(final IntList l, final Object sync) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }
    
    public static IntList unmodifiable(final IntList l) {
        return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends IntCollections.EmptyCollection implements IntList, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public int getInt(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int set(final int index, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final int k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final int k) {
            return -1;
        }
        
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer get(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean add(final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer remove(final int k) {
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
        public IntListIterator listIterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntListIterator iterator() {
            return IntIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            if (i == 0) {
                return IntIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
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
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        public int compareTo(final List<? extends Integer> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return IntLists.EMPTY_LIST;
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
            return IntLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton extends AbstractIntList implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final int element;
        
        protected Singleton(final int element) {
            this.element = element;
        }
        
        public int getInt(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final int k) {
            return k == this.element;
        }
        
        public int[] toIntArray() {
            final int[] a = { this.element };
            return a;
        }
        
        @Override
        public IntListIterator listIterator() {
            return IntIterators.singleton(this.element);
        }
        
        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final IntListIterator l = this.listIterator();
            if (i == 1) {
                l.nextInt();
            }
            return l;
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0 || to != 1) {
                return IntLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final IntCollection c) {
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
    
    public static class SynchronizedList extends IntCollections.SynchronizedCollection implements IntList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;
        
        protected SynchronizedList(final IntList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final IntList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final int i) {
            synchronized (this.sync) {
                return this.list.getInt(i);
            }
        }
        
        @Override
        public int set(final int i, final int k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final int k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public int removeInt(final int i) {
            synchronized (this.sync) {
                return this.list.removeInt(i);
            }
        }
        
        @Override
        public int indexOf(final int k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final int k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends Integer> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, (Collection)c);
            }
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
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
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
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
        public IntListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
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
        
        public int compareTo(final List<? extends Integer> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final IntCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final IntList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final IntList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Deprecated
        @Override
        public Integer get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Integer k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Deprecated
        @Override
        public Integer set(final int index, final Integer k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Deprecated
        @Override
        public Integer remove(final int i) {
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
        
        protected SynchronizedRandomAccessList(final IntList l, final Object sync) {
            super(l, sync);
        }
        
        protected SynchronizedRandomAccessList(final IntList l) {
            super(l);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
            }
        }
    }
    
    public static class UnmodifiableList extends IntCollections.UnmodifiableCollection implements IntList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final IntList list;
        
        protected UnmodifiableList(final IntList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public int getInt(final int i) {
            return this.list.getInt(i);
        }
        
        @Override
        public int set(final int i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final int k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int removeInt(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final int k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final int k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final int index, final Collection<? extends Integer> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final int[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final int[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public IntListIterator listIterator() {
            return IntIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public IntListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public IntListIterator listIterator(final int i) {
            return IntIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public IntList subList(final int from, final int to) {
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
        
        public int compareTo(final List<? extends Integer> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final IntCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final IntList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final IntList l) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer get(final int i) {
            return this.list.get(i);
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer set(final int index, final Integer k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Integer remove(final int i) {
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
        
        protected UnmodifiableRandomAccessList(final IntList l) {
            super(l);
        }
        
        @Override
        public IntList subList(final int from, final int to) {
            return new UnmodifiableRandomAccessList(this.list.subList(from, to));
        }
    }
}
