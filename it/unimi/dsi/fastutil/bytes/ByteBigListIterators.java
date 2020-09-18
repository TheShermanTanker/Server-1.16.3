package it.unimi.dsi.fastutil.bytes;

import java.util.NoSuchElementException;
import java.io.Serializable;

public final class ByteBigListIterators {
    public static final EmptyBigListIterator EMPTY_BIG_LIST_ITERATOR;
    
    private ByteBigListIterators() {
    }
    
    public static ByteBigListIterator singleton(final byte element) {
        return new SingletonBigListIterator(element);
    }
    
    public static ByteBigListIterator unmodifiable(final ByteBigListIterator i) {
        return new UnmodifiableBigListIterator(i);
    }
    
    public static ByteBigListIterator asBigListIterator(final ByteListIterator i) {
        return new BigListIteratorListIterator(i);
    }
    
    static {
        EMPTY_BIG_LIST_ITERATOR = new EmptyBigListIterator();
    }
    
    public static class EmptyBigListIterator implements ByteBigListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigListIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public byte nextByte() {
            throw new NoSuchElementException();
        }
        
        public byte previousByte() {
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
            return ByteBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        private Object readResolve() {
            return ByteBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
    }
    
    private static class SingletonBigListIterator implements ByteBigListIterator {
        private final byte element;
        private int curr;
        
        public SingletonBigListIterator(final byte element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public byte previousByte() {
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
    
    public static class UnmodifiableBigListIterator implements ByteBigListIterator {
        protected final ByteBigListIterator i;
        
        public UnmodifiableBigListIterator(final ByteBigListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public byte nextByte() {
            return this.i.nextByte();
        }
        
        public byte previousByte() {
            return this.i.previousByte();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    public static class BigListIteratorListIterator implements ByteBigListIterator {
        protected final ByteListIterator i;
        
        protected BigListIteratorListIterator(final ByteListIterator i) {
            this.i = i;
        }
        
        private int intDisplacement(final long n) {
            if (n < -2147483648L || n > 2147483647L) {
                throw new IndexOutOfBoundsException("This big iterator is restricted to 32-bit displacements");
            }
            return (int)n;
        }
        
        public void set(final byte ok) {
            this.i.set(ok);
        }
        
        public void add(final byte ok) {
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
        
        public byte nextByte() {
            return this.i.nextByte();
        }
        
        public byte previousByte() {
            return this.i.previousByte();
        }
        
        public long nextIndex() {
            return this.i.nextIndex();
        }
        
        public long previousIndex() {
            return this.i.previousIndex();
        }
    }
}
