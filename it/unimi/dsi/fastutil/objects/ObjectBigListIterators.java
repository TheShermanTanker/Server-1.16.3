package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.io.Serializable;

public final class ObjectBigListIterators {
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private ObjectBigListIterators() {
    }
    
    public static <K> ObjectBigListIterator<K> singleton(final K element) {
        return new SingletonBigListIterator<K>(element);
    }
    
    public static <K> ObjectBigListIterator<K> unmodifiable(final ObjectBigListIterator<K> i) {
        return new UnmodifiableBigListIterator<K>(i);
    }
    
    public static <K> ObjectBigListIterator<K> asBigListIterator(final ObjectListIterator<K> i) {
        return new BigListIteratorListIterator<K>(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator<K> implements ObjectBigListIterator<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public K next() {
            throw new NoSuchElementException();
        }
        
        public K previous() {
            throw new NoSuchElementException();
        }
        
        public long nextIndex() {
            return 0L;
        }
        
        public long previousIndex() {
            return -1L;
        }
        
        public long skip(final long n) {
            return 0L;
        }
        
        public long back(final long n) {
            return 0L;
        }
        
        public Object clone() {
            return ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator<K> implements ObjectBigListIterator<K> {
        private final K element;
        private int curr;
        
        public SingletonBigListIterator(final K element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = 0;
            return this.element;
        }
        
        public long nextIndex() {
            return this.curr;
        }
        
        public long previousIndex() {
            return this.curr - 1;
        }
    }
    
    public static class UnmodifiableBigListIterator<K> implements ObjectBigListIterator<K> {
        protected final ObjectBigListIterator<K> i;
        
        public UnmodifiableBigListIterator(final ObjectBigListIterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public K next() {
            return (K)this.i.next();
        }
        
        public K previous() {
            return this.i.previous();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    public static class BigListIteratorListIterator<K> implements ObjectBigListIterator<K> {
        protected final ObjectListIterator<K> i;
        
        protected BigListIteratorListIterator(final ObjectListIterator<K> i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        public void set(final K ok) {
            this.i.set(ok);
        }
        
        public void add(final K ok) {
            this.i.add(ok);
        }
        
        public int back(final int n) {
            return this.i.back(n);
        }
        
        public long back(final long n) {
            return this.i.back(this.intDisplacement(n));
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public int skip(final int n) {
            return this.i.skip(n);
        }
        
        public long skip(final long n) {
            return this.i.skip(this.intDisplacement(n));
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public K next() {
            return (K)this.i.next();
        }
        
        public K previous() {
            return this.i.previous();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
