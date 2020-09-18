package it.unimi.dsi.fastutil.booleans;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public final class BooleanCollections {
    private BooleanCollections() {
    }
    
    public static BooleanCollection synchronize(final BooleanCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static BooleanCollection synchronize(final BooleanCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static BooleanCollection unmodifiable(final BooleanCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static BooleanCollection asCollection(final BooleanIterable iterable) {
        if (iterable instanceof BooleanCollection) {
            return (BooleanCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractBooleanCollection {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final boolean k) {
            return false;
        }
        
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public BooleanBidirectionalIterator iterator() {
            return BooleanIterators.EMPTY_ITERATOR;
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
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedCollection implements BooleanCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final BooleanCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final BooleanCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = this;
        }
        
        public boolean add(final boolean k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        public boolean contains(final boolean k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        public boolean rem(final boolean k) {
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
        
        public boolean[] toBooleanArray() {
            synchronized (this.sync) {
                return this.collection.toBooleanArray();
            }
        }
        
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Deprecated
        public boolean[] toBooleanArray(final boolean[] a) {
            return this.toArray(a);
        }
        
        public boolean[] toArray(final boolean[] a) {
            synchronized (this.sync) {
                return this.collection.toArray(a);
            }
        }
        
        public boolean addAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        public boolean containsAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        public boolean removeAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        public boolean retainAll(final BooleanCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Deprecated
        public boolean add(final Boolean k) {
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
        
        public BooleanIterator iterator() {
            return this.collection.iterator();
        }
        
        public boolean addAll(final Collection<? extends Boolean> c) {
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
    
    public static class UnmodifiableCollection implements BooleanCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanCollection collection;
        
        protected UnmodifiableCollection(final BooleanCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
        }
        
        public boolean add(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        public boolean rem(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return this.collection.size();
        }
        
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }
        
        public boolean contains(final boolean o) {
            return this.collection.contains(o);
        }
        
        public BooleanIterator iterator() {
            return BooleanIterators.unmodifiable(this.collection.iterator());
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
        
        public boolean addAll(final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean add(final Boolean k) {
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
        
        public boolean[] toBooleanArray() {
            return this.collection.toBooleanArray();
        }
        
        @Deprecated
        public boolean[] toBooleanArray(final boolean[] a) {
            return this.toArray(a);
        }
        
        public boolean[] toArray(final boolean[] a) {
            return this.collection.toArray(a);
        }
        
        public boolean containsAll(final BooleanCollection c) {
            return this.collection.containsAll(c);
        }
        
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final BooleanCollection c) {
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
    
    public static class IterableCollection extends AbstractBooleanCollection implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final BooleanIterable iterable;
        
        protected IterableCollection(final BooleanIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        public int size() {
            int c = 0;
            final BooleanIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.nextBoolean();
                ++c;
            }
            return c;
        }
        
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public BooleanIterator iterator() {
            return this.iterable.iterator();
        }
    }
}
