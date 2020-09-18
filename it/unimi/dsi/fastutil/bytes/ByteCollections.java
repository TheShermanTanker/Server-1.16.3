package it.unimi.dsi.fastutil.bytes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.IntPredicate;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public final class ByteCollections {
    private ByteCollections() {
    }
    
    public static ByteCollection synchronize(final ByteCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static ByteCollection synchronize(final ByteCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static ByteCollection unmodifiable(final ByteCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static ByteCollection asCollection(final ByteIterable iterable) {
        if (iterable instanceof ByteCollection) {
            return (ByteCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractByteCollection {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final byte k) {
            return false;
        }
        
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return ByteIterators.EMPTY_ITERATOR;
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
        
        public boolean addAll(final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedCollection implements ByteCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final ByteCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final ByteCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = this;
        }
        
        public boolean add(final byte k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        public boolean contains(final byte k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        public boolean rem(final byte k) {
            synchronized (this.sync) {
                return this.collection.rem(k);
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
        
        public byte[] toByteArray() {
            synchronized (this.sync) {
                return this.collection.toByteArray();
            }
        }
        
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Deprecated
        public byte[] toByteArray(final byte[] a) {
            return this.toArray(a);
        }
        
        public byte[] toArray(final byte[] a) {
            synchronized (this.sync) {
                return this.collection.toArray(a);
            }
        }
        
        public boolean addAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        public boolean containsAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        public boolean removeAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        public boolean removeIf(final IntPredicate filter) {
            synchronized (this.sync) {
                return this.collection.removeIf(filter);
            }
        }
        
        public boolean retainAll(final ByteCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Deprecated
        public boolean add(final Byte k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        @Deprecated
        public boolean contains(final Object k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        @Deprecated
        public boolean remove(final Object k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
        }
        
        public <T> T[] toArray(final T[] a) {
            synchronized (this.sync) {
                return (T[])this.collection.toArray((Object[])a);
            }
        }
        
        public ByteIterator iterator() {
            return this.collection.iterator();
        }
        
        public boolean addAll(final Collection<? extends Byte> c) {
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
    
    public static class UnmodifiableCollection implements ByteCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteCollection collection;
        
        protected UnmodifiableCollection(final ByteCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
        }
        
        public boolean add(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return this.collection.size();
        }
        
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }
        
        public boolean contains(final byte o) {
            return this.collection.contains(o);
        }
        
        public ByteIterator iterator() {
            return ByteIterators.unmodifiable(this.collection.iterator());
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
        
        public boolean addAll(final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean add(final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean contains(final Object k) {
            return this.collection.contains(k);
        }
        
        @Deprecated
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public byte[] toByteArray() {
            return this.collection.toByteArray();
        }
        
        @Deprecated
        public byte[] toByteArray(final byte[] a) {
            return this.toArray(a);
        }
        
        public byte[] toArray(final byte[] a) {
            return this.collection.toArray(a);
        }
        
        public boolean containsAll(final ByteCollection c) {
            return this.collection.containsAll(c);
        }
        
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final ByteCollection c) {
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
    
    public static class IterableCollection extends AbstractByteCollection implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteIterable iterable;
        
        protected IterableCollection(final ByteIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        public int size() {
            int c = 0;
            final ByteIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.nextByte();
                ++c;
            }
            return c;
        }
        
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public ByteIterator iterator() {
            return this.iterable.iterator();
        }
    }
}
