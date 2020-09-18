package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public final class LongBigLists {
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private LongBigLists() {
    }
    
    public static LongBigList shuffle(final LongBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final long t = l.getLong(i);
            l.set(i, l.getLong(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static LongBigList singleton(final long element) {
        return new Singleton(element);
    }
    
    public static LongBigList singleton(final Object element) {
        return new Singleton((long)element);
    }
    
    public static LongBigList synchronize(final LongBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static LongBigList synchronize(final LongBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static LongBigList unmodifiable(final LongBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static LongBigList asBigList(final LongList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends LongCollections.EmptyCollection implements LongBigList, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public long getLong(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean rem(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long removeLong(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long set(final long index, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final long k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final long k) {
            return -1L;
        }
        
        public boolean addAll(final long i, final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final LongBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final long index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean add(final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public Long set(final long index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object k) {
            return -1L;
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public LongBigListIterator listIterator() {
            return LongBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public LongBigListIterator iterator() {
            return LongBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public LongBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return LongBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final long[][] a, final long offset, final long length) {
            LongBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final long[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final long[][] a) {
            throw new UnsupportedOperationException();
        }
        
        public void size(final long s) {
            throw new UnsupportedOperationException();
        }
        
        public long size64() {
            return 0L;
        }
        
        public int compareTo(final BigList<? extends Long> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return LongBigLists.EMPTY_BIG_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof BigList && ((BigList)o).isEmpty();
        }
        
        public String toString() {
            return "[]";
        }
        
        private Object readResolve() {
            return LongBigLists.EMPTY_BIG_LIST;
        }
    }
    
    public static class Singleton extends AbstractLongBigList implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final long element;
        
        protected Singleton(final long element) {
            this.element = element;
        }
        
        public long getLong(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long removeLong(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final long k) {
            return k == this.element;
        }
        
        public long[] toLongArray() {
            final long[] a = { this.element };
            return a;
        }
        
        @Override
        public LongBigListIterator listIterator() {
            return LongBigListIterators.singleton(this.element);
        }
        
        @Override
        public LongBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final LongBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.nextLong();
            }
            return l;
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0L || to != 1L) {
                return LongBigLists.EMPTY_BIG_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final LongBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public long size64() {
            return 1L;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedBigList extends LongCollections.SynchronizedCollection implements LongBigList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongBigList list;
        
        protected SynchronizedBigList(final LongBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final LongBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public long getLong(final long i) {
            synchronized (this.sync) {
                return this.list.getLong(i);
            }
        }
        
        @Override
        public long set(final long i, final long k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public long removeLong(final long i) {
            synchronized (this.sync) {
                return this.list.removeLong(i);
            }
        }
        
        @Override
        public long indexOf(final long k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final long k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final long index, final Collection<? extends Long> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final long[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final long index, final long[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final long[][] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Deprecated
        public void size(final long size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        public long size64() {
            synchronized (this.sync) {
                return this.list.size64();
            }
        }
        
        @Override
        public LongBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public LongBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public LongBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return LongBigLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.list.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.list.hashCode();
            }
        }
        
        public int compareTo(final BigList<? extends Long> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final LongCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final LongBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final LongBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Deprecated
        @Override
        public void add(final long i, final Long k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Deprecated
        @Override
        public Long get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Deprecated
        @Override
        public Long set(final long index, final Long k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Deprecated
        @Override
        public Long remove(final long i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableBigList extends LongCollections.UnmodifiableCollection implements LongBigList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongBigList list;
        
        protected UnmodifiableBigList(final LongBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public long getLong(final long i) {
            return this.list.getLong(i);
        }
        
        @Override
        public long set(final long i, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long removeLong(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final long k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final long k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final long index, final Collection<? extends Long> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final long[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final long[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final long[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public void size(final long size) {
            this.list.size(size);
        }
        
        public long size64() {
            return this.list.size64();
        }
        
        @Override
        public LongBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public LongBigListIterator listIterator() {
            return LongBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public LongBigListIterator listIterator(final long i) {
            return LongBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            return LongBigLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.list.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
        
        public int compareTo(final BigList<? extends Long> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final LongCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final LongBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final LongBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long get(final long i) {
            return this.list.get(i);
        }
        
        @Deprecated
        @Override
        public void add(final long i, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long set(final long index, final Long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Long remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
    
    public static class ListBigList extends AbstractLongBigList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final LongList list;
        
        protected ListBigList(final LongList list) {
            this.list = list;
        }
        
        private int intIndex(final long index) {
            if (index >= 2147483647L) {
                throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices");
            }
            return (int)index;
        }
        
        public long size64() {
            return this.list.size();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(this.intIndex(size));
        }
        
        @Override
        public LongBigListIterator iterator() {
            return LongBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public LongBigListIterator listIterator() {
            return LongBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public LongBigListIterator listIterator(final long index) {
            return LongBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Long> c) {
            return this.list.addAll(this.intIndex(index), (Collection)c);
        }
        
        @Override
        public LongBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final long key) {
            return this.list.contains(key);
        }
        
        public long[] toLongArray() {
            return this.list.toLongArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Deprecated
        public long[] toLongArray(final long[] a) {
            return this.list.toArray(a);
        }
        
        @Override
        public boolean addAll(final long index, final LongCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final LongCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean addAll(final long index, final LongBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final LongBigList c) {
            return this.list.addAll(c);
        }
        
        public boolean containsAll(final LongCollection c) {
            return this.list.containsAll(c);
        }
        
        public boolean removeAll(final LongCollection c) {
            return this.list.removeAll(c);
        }
        
        public boolean retainAll(final LongCollection c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public void add(final long index, final long key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean add(final long key) {
            return this.list.add(key);
        }
        
        public long getLong(final long index) {
            return this.list.getLong(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final long k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final long k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public long removeLong(final long index) {
            return this.list.removeLong(this.intIndex(index));
        }
        
        @Override
        public long set(final long index, final long k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
        
        public <T> T[] toArray(final T[] a) {
            return (T[])this.list.toArray((Object[])a);
        }
        
        public boolean containsAll(final Collection<?> c) {
            return this.list.containsAll((Collection)c);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Long> c) {
            return this.list.addAll((Collection)c);
        }
        
        public boolean removeAll(final Collection<?> c) {
            return this.list.removeAll((Collection)c);
        }
        
        public boolean retainAll(final Collection<?> c) {
            return this.list.retainAll((Collection)c);
        }
        
        @Override
        public void clear() {
            this.list.clear();
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
    }
}
