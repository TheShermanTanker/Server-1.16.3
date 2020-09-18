package it.unimi.dsi.fastutil.objects;

import java.util.Objects;
import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public final class ObjectBigLists {
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private ObjectBigLists() {
    }
    
    public static <K> ObjectBigList<K> shuffle(final ObjectBigList<K> l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final K t = l.get(i);
            l.set(i, l.get(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static <K> ObjectBigList<K> emptyList() {
        return (ObjectBigList<K>)ObjectBigLists.EMPTY_BIG_LIST;
    }
    
    public static <K> ObjectBigList<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ObjectBigList<K> synchronize(final ObjectBigList<K> l) {
        return new SynchronizedBigList<K>(l);
    }
    
    public static <K> ObjectBigList<K> synchronize(final ObjectBigList<K> l, final Object sync) {
        return new SynchronizedBigList<K>(l, sync);
    }
    
    public static <K> ObjectBigList<K> unmodifiable(final ObjectBigList<K> l) {
        return new UnmodifiableBigList<K>(l);
    }
    
    public static <K> ObjectBigList<K> asBigList(final ObjectList<K> list) {
        return new ListBigList<K>(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList<K> extends ObjectCollections.EmptyCollection<K> implements ObjectBigList<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        public K get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public K remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final long index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public K set(final long index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public long indexOf(final Object k) {
            return -1L;
        }
        
        public long lastIndexOf(final Object k) {
            return -1L;
        }
        
        public boolean addAll(final long i, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator() {
            return (ObjectBigListIterator<K>)ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ObjectBigListIterator<K> iterator() {
            return (ObjectBigListIterator<K>)ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long i) {
            if (i == 0L) {
                return (ObjectBigListIterator<K>)ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final Object[][] a, final long offset, final long length) {
            ObjectBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final K[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final K[][] a) {
            throw new UnsupportedOperationException();
        }
        
        public void size(final long s) {
            throw new UnsupportedOperationException();
        }
        
        public long size64() {
            return 0L;
        }
        
        public int compareTo(final BigList<? extends K> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return ObjectBigLists.EMPTY_BIG_LIST;
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
            return ObjectBigLists.EMPTY_BIG_LIST;
        }
    }
    
    public static class Singleton<K> extends AbstractObjectBigList<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final K element;
        
        protected Singleton(final K element) {
            this.element = element;
        }
        
        public K get(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return Objects.equals(k, this.element);
        }
        
        public Object[] toArray() {
            final Object[] a = { this.element };
            return a;
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator() {
            return ObjectBigListIterators.<K>singleton(this.element);
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final ObjectBigListIterator<K> l = this.listIterator();
            if (i == 1L) {
                l.next();
            }
            return l;
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0L || to != 1L) {
                return (ObjectBigList<K>)ObjectBigLists.EMPTY_BIG_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
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
    
    public static class SynchronizedBigList<K> extends ObjectCollections.SynchronizedCollection<K> implements ObjectBigList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectBigList<K> list;
        
        protected SynchronizedBigList(final ObjectBigList<K> l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final ObjectBigList<K> l) {
            super(l);
            this.list = l;
        }
        
        public K get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        public K set(final long i, final K k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        public void add(final long i, final K k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        public K remove(final long i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        public long indexOf(final Object k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        public long lastIndexOf(final Object k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final long index, final Collection<? extends K> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, (java.util.Collection<?>)c);
            }
        }
        
        @Override
        public void getElements(final long from, final Object[][] a, final long offset, final long length) {
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
        public void addElements(final long index, final K[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final K[][] a) {
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
        public ObjectBigListIterator<K> iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            synchronized (this.sync) {
                return ObjectBigLists.<K>synchronize(this.list.subList(from, to), this.sync);
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
        
        public int compareTo(final BigList<? extends K> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
    }
    
    public static class UnmodifiableBigList<K> extends ObjectCollections.UnmodifiableCollection<K> implements ObjectBigList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectBigList<K> list;
        
        protected UnmodifiableBigList(final ObjectBigList<K> l) {
            super(l);
            this.list = l;
        }
        
        public K get(final long i) {
            return this.list.get(i);
        }
        
        public K set(final long i, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final long i, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public K remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        public long indexOf(final Object k) {
            return this.list.indexOf(k);
        }
        
        public long lastIndexOf(final Object k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final long index, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final Object[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final K[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final K[][] a) {
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
        public ObjectBigListIterator<K> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator() {
            return ObjectBigListIterators.<K>unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long i) {
            return ObjectBigListIterators.<K>unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            return ObjectBigLists.<K>unmodifiable(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.list.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
        
        public int compareTo(final BigList<? extends K> o) {
            return this.list.compareTo(o);
        }
    }
    
    public static class ListBigList<K> extends AbstractObjectBigList<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final ObjectList<K> list;
        
        protected ListBigList(final ObjectList<K> list) {
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
        public ObjectBigListIterator<K> iterator() {
            return ObjectBigListIterators.<K>asBigListIterator(this.list.iterator());
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator() {
            return ObjectBigListIterators.<K>asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public ObjectBigListIterator<K> listIterator(final long index) {
            return ObjectBigListIterators.<K>asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends K> c) {
            return this.list.addAll(this.intIndex(index), (Collection)c);
        }
        
        @Override
        public ObjectBigList<K> subList(final long from, final long to) {
            return new ListBigList((ObjectList<Object>)this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final Object key) {
            return this.list.contains(key);
        }
        
        public Object[] toArray() {
            return this.list.toArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Override
        public void add(final long index, final K key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean add(final K key) {
            return this.list.add(key);
        }
        
        public K get(final long index) {
            return (K)this.list.get(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final Object k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final Object k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public K remove(final long index) {
            return (K)this.list.remove(this.intIndex(index));
        }
        
        @Override
        public K set(final long index, final K k) {
            return (K)this.list.set(this.intIndex(index), k);
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
        public boolean addAll(final Collection<? extends K> c) {
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
