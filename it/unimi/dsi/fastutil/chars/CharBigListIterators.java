package it.unimi.dsi.fastutil.chars;

import java.util.NoSuchElementException;
import java.io.Serializable;

public final class CharBigListIterators {
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private CharBigListIterators() {
    }
    
    public static CharBigListIterator singleton(final char element) {
        return new SingletonBigListIterator(element);
    }
    
    public static CharBigListIterator unmodifiable(final CharBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static CharBigListIterator asBigListIterator(final CharListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator implements CharBigListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public char nextChar() {
            throw new NoSuchElementException();
        }
        
        public char previousChar() {
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
            return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator implements CharBigListIterator {
        private final char element;
        private int curr;
        
        public SingletonBigListIterator(final char element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public char previousChar() {
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
    
    public static class UnmodifiableBigListIterator implements CharBigListIterator {
        protected final CharBigListIterator i;
        
        public UnmodifiableBigListIterator(final CharBigListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public char nextChar() {
            return this.i.nextChar();
        }
        
        public char previousChar() {
            return this.i.previousChar();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    public static class BigListIteratorListIterator implements CharBigListIterator {
        protected final CharListIterator i;
        
        protected BigListIteratorListIterator(final CharListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        public void set(final char ok) {
            this.i.set(ok);
        }
        
        public void add(final char ok) {
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
        
        public char nextChar() {
            return this.i.nextChar();
        }
        
        public char previousChar() {
            return this.i.previousChar();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
