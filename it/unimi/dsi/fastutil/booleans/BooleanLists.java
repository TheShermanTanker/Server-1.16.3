package it.unimi.dsi.fastutil.booleans;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class BooleanLists {
    public static final EmptyList EMPTY_LIST;
    
    private BooleanLists() {
    }
    
    public static BooleanList shuffle(final BooleanList l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final boolean t = l.getBoolean(i);
            l.set(i, l.getBoolean(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static BooleanList singleton(final boolean element) {
        return new Singleton(element);
    }
    
    public static BooleanList singleton(final Object element) {
        return new Singleton((boolean)element);
    }
    
    public static BooleanList synchronize(final BooleanList l) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
    }
    
    public static BooleanList synchronize(final BooleanList l, final Object sync) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
    }
    
    public static BooleanList unmodifiable(final BooleanList l) {
        return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList extends BooleanCollections.EmptyCollection implements BooleanList, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        @Override
        public boolean getBoolean(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeBoolean(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int index, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean set(final int index, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final boolean k) {
            return -1;
        }
        
        @Override
        public int lastIndexOf(final boolean k) {
            return -1;
        }
        
        public boolean addAll(final int i, final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final BooleanList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final int index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean get(final int index) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public boolean add(final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean set(final int index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean remove(final int k) {
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
        public BooleanListIterator listIterator() {
            return BooleanIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public BooleanListIterator iterator() {
            return BooleanIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public BooleanListIterator listIterator(final int i) {
            if (i == 0) {
                return BooleanIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final boolean[] a, final int offset, final int length) {
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
        public void addElements(final int index, final boolean[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final boolean[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        public int compareTo(final List<? extends Boolean> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return BooleanLists.EMPTY_LIST;
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
            return BooleanLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton extends AbstractBooleanList implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final boolean element;
        
        protected Singleton(final boolean element) {
            this.element = element;
        }
        
        public boolean getBoolean(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeBoolean(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final boolean k) {
            return k == this.element;
        }
        
        public boolean[] toBooleanArray() {
            final boolean[] a = { this.element };
            return a;
        }
        
        @Override
        public BooleanListIterator listIterator() {
            return BooleanIterators.singleton(this.element);
        }
        
        @Override
        public BooleanListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public BooleanListIterator listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final BooleanListIterator l = this.listIterator();
            if (i == 1) {
                l.nextBoolean();
            }
            return l;
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0 || to != 1) {
                return BooleanLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final BooleanList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int i, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final BooleanCollection c) {
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
    
    public static class SynchronizedList extends BooleanCollections.SynchronizedCollection implements BooleanList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanList list;
        
        protected SynchronizedList(final BooleanList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final BooleanList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public boolean getBoolean(final int i) {
            synchronized (this.sync) {
                return this.list.getBoolean(i);
            }
        }
        
        @Override
        public boolean set(final int i, final boolean k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final int i, final boolean k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public boolean removeBoolean(final int i) {
            synchronized (this.sync) {
                return this.list.removeBoolean(i);
            }
        }
        
        @Override
        public int indexOf(final boolean k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public int lastIndexOf(final boolean k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends Boolean> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, (Collection)c);
            }
        }
        
        @Override
        public void getElements(final int from, final boolean[] a, final int offset, final int length) {
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
        public void addElements(final int index, final boolean[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final boolean[] a) {
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
        public BooleanListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public BooleanListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public BooleanListIterator listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
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
        
        public int compareTo(final List<? extends Boolean> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final int index, final BooleanCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final int index, final BooleanList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final BooleanList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Deprecated
        @Override
        public Boolean get(final int i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Boolean k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Deprecated
        @Override
        public Boolean set(final int index, final Boolean k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Deprecated
        @Override
        public Boolean remove(final int i) {
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
        
        protected SynchronizedRandomAccessList(final BooleanList l, final Object sync) {
            super(l, sync);
        }
        
        protected SynchronizedRandomAccessList(final BooleanList l) {
            super(l);
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
            }
        }
    }
    
    public static class UnmodifiableList extends BooleanCollections.UnmodifiableCollection implements BooleanList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanList list;
        
        protected UnmodifiableList(final BooleanList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public boolean getBoolean(final int i) {
            return this.list.getBoolean(i);
        }
        
        @Override
        public boolean set(final int i, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final int i, final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeBoolean(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public int indexOf(final boolean k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public int lastIndexOf(final boolean k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final int index, final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final boolean[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final boolean[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final boolean[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public BooleanListIterator listIterator() {
            return BooleanIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public BooleanListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public BooleanListIterator listIterator(final int i) {
            return BooleanIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
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
        
        public int compareTo(final List<? extends Boolean> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final int index, final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final BooleanList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final int index, final BooleanList l) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean get(final int i) {
            return this.list.get(i);
        }
        
        @Deprecated
        @Override
        public void add(final int i, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean set(final int index, final Boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Boolean remove(final int i) {
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
        
        protected UnmodifiableRandomAccessList(final BooleanList l) {
            super(l);
        }
        
        @Override
        public BooleanList subList(final int from, final int to) {
            return new UnmodifiableRandomAccessList(this.list.subList(from, to));
        }
    }
}
