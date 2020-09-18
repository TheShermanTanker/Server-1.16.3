package it.unimi.dsi.fastutil.chars;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.function.IntPredicate;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

public final class CharCollections {
    private CharCollections() {
    }
    
    public static CharCollection synchronize(final CharCollection c) {
        return new SynchronizedCollection(c);
    }
    
    public static CharCollection synchronize(final CharCollection c, final Object sync) {
        return new SynchronizedCollection(c, sync);
    }
    
    public static CharCollection unmodifiable(final CharCollection c) {
        return new UnmodifiableCollection(c);
    }
    
    public static CharCollection asCollection(final CharIterable iterable) {
        if (iterable instanceof CharCollection) {
            return (CharCollection)iterable;
        }
        return new IterableCollection(iterable);
    }
    
    public abstract static class EmptyCollection extends AbstractCharCollection {
        protected EmptyCollection() {
        }
        
        @Override
        public boolean contains(final char k) {
            return false;
        }
        
        public Object[] toArray() {
            return ObjectArrays.EMPTY_ARRAY;
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return CharIterators.EMPTY_ITERATOR;
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
        
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean removeAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean retainAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
    }
    
    public static class SynchronizedCollection implements CharCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharCollection collection;
        protected final Object sync;
        
        protected SynchronizedCollection(final CharCollection c, final Object sync) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = sync;
        }
        
        protected SynchronizedCollection(final CharCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
            this.sync = this;
        }
        
        public boolean add(final char k) {
            synchronized (this.sync) {
                return this.collection.add(k);
            }
        }
        
        public boolean contains(final char k) {
            synchronized (this.sync) {
                return this.collection.contains(k);
            }
        }
        
        public boolean rem(final char k) {
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
        
        public char[] toCharArray() {
            synchronized (this.sync) {
                return this.collection.toCharArray();
            }
        }
        
        public Object[] toArray() {
            synchronized (this.sync) {
                return this.collection.toArray();
            }
        }
        
        @Deprecated
        public char[] toCharArray(final char[] a) {
            return this.toArray(a);
        }
        
        public char[] toArray(final char[] a) {
            synchronized (this.sync) {
                return this.collection.toArray(a);
            }
        }
        
        public boolean addAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.addAll(c);
            }
        }
        
        public boolean containsAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.containsAll(c);
            }
        }
        
        public boolean removeAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.removeAll(c);
            }
        }
        
        public boolean removeIf(final IntPredicate filter) {
            synchronized (this.sync) {
                return this.collection.removeIf(filter);
            }
        }
        
        public boolean retainAll(final CharCollection c) {
            synchronized (this.sync) {
                return this.collection.retainAll(c);
            }
        }
        
        @Deprecated
        public boolean add(final Character k) {
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
        
        public CharIterator iterator() {
            return this.collection.iterator();
        }
        
        public boolean addAll(final Collection<? extends Character> c) {
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
    
    public static class UnmodifiableCollection implements CharCollection, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharCollection collection;
        
        protected UnmodifiableCollection(final CharCollection c) {
            if (c == null) {
                throw new NullPointerException();
            }
            this.collection = c;
        }
        
        public boolean add(final char k) {
            throw new UnsupportedOperationException();
        }
        
        public boolean rem(final char k) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return this.collection.size();
        }
        
        public boolean isEmpty() {
            return this.collection.isEmpty();
        }
        
        public boolean contains(final char o) {
            return this.collection.contains(o);
        }
        
        public CharIterator iterator() {
            return CharIterators.unmodifiable(this.collection.iterator());
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
        
        public boolean addAll(final Collection<? extends Character> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean add(final Character k) {
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
        
        public char[] toCharArray() {
            return this.collection.toCharArray();
        }
        
        @Deprecated
        public char[] toCharArray(final char[] a) {
            return this.toArray(a);
        }
        
        public char[] toArray(final char[] a) {
            return this.collection.toArray(a);
        }
        
        public boolean containsAll(final CharCollection c) {
            return this.collection.containsAll(c);
        }
        
        public boolean addAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final CharCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final CharCollection c) {
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
    
    public static class IterableCollection extends AbstractCharCollection implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final CharIterable iterable;
        
        protected IterableCollection(final CharIterable iterable) {
            if (iterable == null) {
                throw new NullPointerException();
            }
            this.iterable = iterable;
        }
        
        public int size() {
            int c = 0;
            final CharIterator iterator = this.iterator();
            while (iterator.hasNext()) {
                iterator.nextChar();
                ++c;
            }
            return c;
        }
        
        public boolean isEmpty() {
            return !this.iterable.iterator().hasNext();
        }
        
        @Override
        public CharIterator iterator() {
            return this.iterable.iterator();
        }
    }
}
