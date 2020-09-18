package it.unimi.dsi.fastutil.shorts;

import java.util.NoSuchElementException;
import java.io.Serializable;

public final class ShortBigListIterators {
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private ShortBigListIterators() {
    }
    
    public static ShortBigListIterator singleton(final short element) {
        return new SingletonBigListIterator(element);
    }
    
    public static ShortBigListIterator unmodifiable(final ShortBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static ShortBigListIterator asBigListIterator(final ShortListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator implements ShortBigListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public short nextShort() {
            throw new NoSuchElementException();
        }
        
        public short previousShort() {
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
            return ShortBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return ShortBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator implements ShortBigListIterator {
        private final short element;
        private int curr;
        
        public SingletonBigListIterator(final short element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public short previousShort() {
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
    
    public static class UnmodifiableBigListIterator implements ShortBigListIterator {
        protected final ShortBigListIterator i;
        
        public UnmodifiableBigListIterator(final ShortBigListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public short nextShort() {
            return this.i.nextShort();
        }
        
        public short previousShort() {
            return this.i.previousShort();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    public static class BigListIteratorListIterator implements ShortBigListIterator {
        protected final ShortListIterator i;
        
        protected BigListIteratorListIterator(final ShortListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        public void set(final short ok) {
            this.i.set(ok);
        }
        
        public void add(final short ok) {
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
        
        public short nextShort() {
            return this.i.nextShort();
        }
        
        public short previousShort() {
            return this.i.previousShort();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
