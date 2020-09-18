package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;

public final class ReferenceCollections {
    private ReferenceCollections() {
    }
    
    public static <K> ReferenceCollection<K> synchronize(final ReferenceCollection<K> c) {
        return new SynchronizedCollection<K>(c);
    }
    
    public static <K> ReferenceCollection<K> synchronize(final ReferenceCollection<K> c, final Object sync) {
        return new SynchronizedCollection<K>(c, sync);
    }
    
    public static <K> ReferenceCollection<K> unmodifiable(final ReferenceCollection<K> c) {
        return new UnmodifiableCollection<K>(c);
    }
    
    public static <K> ReferenceCollection<K> asCollection(final ObjectIterable<K> iterable) {
        if (iterable instanceof ReferenceCollection) {
            return (ReferenceCollection<K>)(ReferenceCollection)iterable;
        }
        return new IterableCollection<K>(iterable);
    }
    
    public abstract static class EmptyCollection<K> extends AbstractReferenceCollection<K> {
        protected EmptyCollection() {
        }
        
        public boolean contains(final Object k) {
            return false;
        }
        
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public int hashCode() {
            return 0;
        }
        
        public boolean equals(final Object o) {
            return o == this || (o instanceof Collection && ((Collection)o).isEmpty());
        }
        
        public boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedCollection<K> implements ReferenceCollection<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceCollection<K> collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final ReferenceCollection<K> c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final ReferenceCollection<K> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = this;
        }
        
        public boolean add(final K k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        public boolean contains(final Object k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        public boolean remove(final Object k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.collection.size();
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.collection.isEmpty();
            }
        }
        
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        public <T> T[] toArray(final T[] a) {
            synchronized (this.sync) {
                return (T[])this.collection.toArray((Object[])a);
            }
        }
        
        public ObjectIterator<K> iterator() {
            return this.collection.iterator();
        }
        
        public boolean addAll(final Collection<? extends K> c) {
            synchronized (this.sync) {
                return this.collection.addAll((Collection)c);
            }
        }
        
        public boolean containsAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.containsAll((Collection)c);
            }
        }
        
        public boolean removeAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.removeAll((Collection)c);
            }
        }
        
        public boolean retainAll(final Collection<?> c) {
            synchronized (this.sync) {
                return this.collection.retainAll((Collection)c);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.collection.clear();
            }
        }
        
        public String toString() {
            synchronized (this.sync) {
                return this.collection.toString();
            }
        }
        
        public int hashCode() {
            synchronized (this.sync) {
                return this.collection.hashCode();
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.collection.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
    
    public static class UnmodifiableCollection<K> implements ReferenceCollection<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceCollection<K> collection;
        
        protected UnmodifiableCollection(final ReferenceCollection<K> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
        }
        
        public boolean add(final K k) {
            throw new UnsupportedOperationException();
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return this.collection.size();
        }
        
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }
        
        public boolean contains(final Object o) {
            return this.collection.contains(o);
        }
        
        public ObjectIterator<K> iterator() {
            return ObjectIterators.<K>unmodifiable(this.collection.iterator());
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public <T> T[] toArray(final T[] a) {
            return (T[])this.collection.toArray((Object[])a);
        }
        
        public Object[] toArray() {
            return this.collection.toArray();
        }
        
        public boolean containsAll(final Collection<?> c) {
            return this.collection.containsAll((Collection)c);
        }
        
        public boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public String toString() {
            return this.collection.toString();
        }
        
        public int hashCode() {
            return this.collection.hashCode();
        }
        
        public boolean equals(final Object o) {
            return o == this || this.collection.equals(o);
        }
    }
    
    public static class IterableCollection<K> extends AbstractReferenceCollection<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ObjectIterable<K> iterable;
        
        protected IterableCollection(final ObjectIterable<K> iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        public int size() {
            int c = 0;
            final ObjectIterator<K> iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.next();
                ++c;
            }
            return c;
        }
        
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public ObjectIterator<K> iterator() {
            return this.iterable.iterator();
        }
    }
}
