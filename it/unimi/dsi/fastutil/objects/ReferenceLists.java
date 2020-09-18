package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.Random;

public final class ReferenceLists {
    public static final EmptyList EMPTY_LIST;
    
    private ReferenceLists() {
    }
    
    public static <K> ReferenceList<K> shuffle(final ReferenceList<K> l, final Random random) {
        int i = l.size();
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final K t = (K)l.get(i);
            l.set(i, l.get(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static <K> ReferenceList<K> emptyList() {
        return (ReferenceList<K>)ReferenceLists.EMPTY_LIST;
    }
    
    public static <K> ReferenceList<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ReferenceList<K> synchronize(final ReferenceList<K> l) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList<K>(l) : new SynchronizedList<K>(l);
    }
    
    public static <K> ReferenceList<K> synchronize(final ReferenceList<K> l, final Object sync) {
        return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList<K>(l, sync) : new SynchronizedList<K>(l, sync);
    }
    
    public static <K> ReferenceList<K> unmodifiable(final ReferenceList<K> l) {
        return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList<K>(l) : new UnmodifiableList<K>(l);
    }
    
    static {
        EMPTY_LIST = new EmptyList();
    }
    
    public static class EmptyList<K> extends ReferenceCollections.EmptyCollection<K> implements ReferenceList<K>, RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyList() {
        }
        
        public K get(final int i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public K remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final int index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public K set(final int index, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public int indexOf(final Object k) {
            return -1;
        }
        
        public int lastIndexOf(final Object k) {
            return -1;
        }
        
        public boolean addAll(final int i, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectListIterator<K> listIterator() {
            return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            if (i == 0) {
                return (ObjectListIterator<K>)ObjectIterators.EMPTY_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            if (from == 0 && to == 0) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
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
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int s) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return ReferenceLists.EMPTY_LIST;
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
            return ReferenceLists.EMPTY_LIST;
        }
    }
    
    public static class Singleton<K> extends AbstractReferenceList<K> implements RandomAccess, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final K element;
        
        protected Singleton(final K element) {
            this.element = element;
        }
        
        public K get(final int i) {
            if (i == 0) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public K remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final Object k) {
            return k == this.element;
        }
        
        public Object[] toArray() {
            final Object[] a = { this.element };
            return a;
        }
        
        @Override
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.<K>singleton(this.element);
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            if (i > 1 || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            final ObjectListIterator<K> l = this.listIterator();
            if (i == 1) {
                l.next();
            }
            return l;
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0 || to != 1) {
                return (ReferenceList<K>)ReferenceLists.EMPTY_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final int i, final Collection<? extends K> c) {
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
    
    public static class SynchronizedList<K> extends ReferenceCollections.SynchronizedCollection<K> implements ReferenceList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceList<K> list;
        
        protected SynchronizedList(final ReferenceList<K> l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedList(final ReferenceList<K> l) {
            super(l);
            this.list = l;
        }
        
        public K get(final int i) {
            synchronized (this.sync) {
                return (K)this.list.get(i);
            }
        }
        
        public K set(final int i, final K k) {
            synchronized (this.sync) {
                return (K)this.list.set(i, k);
            }
        }
        
        public void add(final int i, final K k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        public K remove(final int i) {
            synchronized (this.sync) {
                return (K)this.list.remove(i);
            }
        }
        
        public int indexOf(final Object k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        public int lastIndexOf(final Object k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final int index, final Collection<? extends K> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, (Collection)c);
            }
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
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
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final int index, final K[] a) {
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
        public ObjectListIterator<K> listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedList((ReferenceList<Object>)this.list.subList(from, to), this.sync);
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
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
    
    public static class SynchronizedRandomAccessList<K> extends SynchronizedList<K> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0L;
        
        protected SynchronizedRandomAccessList(final ReferenceList<K> l, final Object sync) {
            super(l, sync);
        }
        
        protected SynchronizedRandomAccessList(final ReferenceList<K> l) {
            super(l);
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            synchronized (this.sync) {
                return new SynchronizedRandomAccessList((ReferenceList<Object>)this.list.subList(from, to), this.sync);
            }
        }
    }
    
    public static class UnmodifiableList<K> extends ReferenceCollections.UnmodifiableCollection<K> implements ReferenceList<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceList<K> list;
        
        protected UnmodifiableList(final ReferenceList<K> l) {
            super(l);
            this.list = l;
        }
        
        public K get(final int i) {
            return (K)this.list.get(i);
        }
        
        public K set(final int i, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public void add(final int i, final K k) {
            throw new UnsupportedOperationException();
        }
        
        public K remove(final int i) {
            throw new UnsupportedOperationException();
        }
        
        public int indexOf(final Object k) {
            return this.list.indexOf(k);
        }
        
        public int lastIndexOf(final Object k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final int index, final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final int from, final int to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final K[] a, final int offset, final int length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final int index, final K[] a) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void size(final int size) {
            this.list.size(size);
        }
        
        @Override
        public ObjectListIterator<K> listIterator() {
            return ObjectIterators.<K>unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return this.listIterator();
        }
        
        @Override
        public ObjectListIterator<K> listIterator(final int i) {
            return ObjectIterators.<K>unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            return new UnmodifiableList((ReferenceList<Object>)this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
    }
    
    public static class UnmodifiableRandomAccessList<K> extends UnmodifiableList<K> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0L;
        
        protected UnmodifiableRandomAccessList(final ReferenceList<K> l) {
            super(l);
        }
        
        @Override
        public ReferenceList<K> subList(final int from, final int to) {
            return new UnmodifiableRandomAccessList((ReferenceList<Object>)this.list.subList(from, to));
        }
    }
}
