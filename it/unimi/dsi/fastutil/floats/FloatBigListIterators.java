package it.unimi.dsi.fastutil.floats;

import java.util.NoSuchElementException;
import java.io.Serializable;

public final class FloatBigListIterators {
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private FloatBigListIterators() {
    }
    
    public static FloatBigListIterator singleton(final float element) {
        return new SingletonBigListIterator(element);
    }
    
    public static FloatBigListIterator unmodifiable(final FloatBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static FloatBigListIterator asBigListIterator(final FloatListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator implements FloatBigListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public float nextFloat() {
            throw new NoSuchElementException();
        }
        
        public float previousFloat() {
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
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return FloatBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator implements FloatBigListIterator {
        private final float element;
        private int curr;
        
        public SingletonBigListIterator(final float element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public float nextFloat() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public float previousFloat() {
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
    
    public static class UnmodifiableBigListIterator implements FloatBigListIterator {
        protected final FloatBigListIterator i;
        
        public UnmodifiableBigListIterator(final FloatBigListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public float nextFloat() {
            return this.i.nextFloat();
        }
        
        public float previousFloat() {
            return this.i.previousFloat();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    public static class BigListIteratorListIterator implements FloatBigListIterator {
        protected final FloatListIterator i;
        
        protected BigListIteratorListIterator(final FloatListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        public void set(final float ok) {
            this.i.set(ok);
        }
        
        public void add(final float ok) {
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
        
        public float nextFloat() {
            return this.i.nextFloat();
        }
        
        public float previousFloat() {
            return this.i.previousFloat();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
