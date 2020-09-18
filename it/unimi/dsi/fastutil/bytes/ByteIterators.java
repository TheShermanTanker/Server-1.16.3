package it.unimi.dsi.fastutil.bytes;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class ByteIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ByteIterators() {
    }
    
    public static ByteListIterator singleton(final byte element) {
        return new SingletonIterator(element);
    }
    
    public static ByteListIterator wrap(final byte[] array, final int offset, final int length) {
        ByteArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static ByteListIterator wrap(final byte[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final ByteIterator i, final byte[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextByte();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final ByteIterator i, final byte[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static byte[] unwrap(final ByteIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        byte[] array = new byte[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ByteArrays.grow(array, j + 1);
            }
            array[j++] = i.nextByte();
        }
        return ByteArrays.trim(array, j);
    }
    
    public static byte[] unwrap(final ByteIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final ByteIterator i, final ByteCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextByte());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final ByteIterator i, final ByteCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextByte());
            ++n;
        }
        return n;
    }
    
    public static int pour(final ByteIterator i, final ByteCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextByte());
        }
        return max - j - 1;
    }
    
    public static int pour(final ByteIterator i, final ByteCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static ByteList pour(final ByteIterator i, final int max) {
        final ByteArrayList l = new ByteArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static ByteList pour(final ByteIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static ByteIterator asByteIterator(final Iterator i) {
        if (i instanceof ByteIterator) {
            return (ByteIterator)i;
        }
        return new IteratorWrapper((Iterator<Byte>)i);
    }
    
    public static ByteListIterator asByteIterator(final ListIterator i) {
        if (i instanceof ByteListIterator) {
            return (ByteListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Byte>)i);
    }
    
    public static boolean any(final ByteIterator iterator, final IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final ByteIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test((int)iterator.nextByte())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final ByteIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test((int)iterator.nextByte())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static ByteListIterator fromTo(final byte from, final byte to) {
        return new IntervalIterator(from, to);
    }
    
    public static ByteIterator concat(final ByteIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static ByteIterator concat(final ByteIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static ByteIterator unmodifiable(final ByteIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static ByteBidirectionalIterator unmodifiable(final ByteBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static ByteListIterator unmodifiable(final ByteListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements ByteListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
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
        
        public int nextIndex() {
            return 0;
        }
        
        public int previousIndex() {
            return -1;
        }
        
        public int skip(final int n) {
            return 0;
        }
        
        public int back(final int n) {
            return 0;
        }
        
        public Object clone() {
            return ByteIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ByteIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements ByteListIterator {
        private final byte element;
        private int curr;
        
        public SingletonIterator(final byte element) {
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
        
        public int nextIndex() {
            return this.curr;
        }
        
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class ArrayIterator implements ByteListIterator {
        private final byte[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final byte[] array, final int offset, final int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }
        
        public boolean hasNext() {
            return this.curr < this.length;
        }
        
        public boolean hasPrevious() {
            return this.curr > 0;
        }
        
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public byte previousByte() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final byte[] array = this.array;
            final int offset = this.offset;
            final int curr = this.curr - 1;
            this.curr = curr;
            return array[offset + curr];
        }
        
        public int skip(int n) {
            if (n <= this.length - this.curr) {
                this.curr += n;
                return n;
            }
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
        }
        
        public int back(int n) {
            if (n <= this.curr) {
                this.curr -= n;
                return n;
            }
            n = this.curr;
            this.curr = 0;
            return n;
        }
        
        public int nextIndex() {
            return this.curr;
        }
        
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class IteratorWrapper implements ByteIterator {
        final Iterator<Byte> i;
        
        public IteratorWrapper(final Iterator<Byte> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public byte nextByte() {
            return (byte)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements ByteListIterator {
        final ListIterator<Byte> i;
        
        public ListIteratorWrapper(final ListIterator<Byte> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
        
        public void set(final byte k) {
            this.i.set(k);
        }
        
        public void add(final byte k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public byte nextByte() {
            return (byte)this.i.next();
        }
        
        public byte previousByte() {
            return (byte)this.i.previous();
        }
    }
    
    private static class IntervalIterator implements ByteListIterator {
        private final byte from;
        private final byte to;
        byte curr;
        
        public IntervalIterator(final byte from, final byte to) {
            this.curr = from;
            this.from = from;
            this.to = to;
        }
        
        public boolean hasNext() {
            return this.curr < this.to;
        }
        
        public boolean hasPrevious() {
            return this.curr > this.from;
        }
        
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final byte curr = this.curr;
            this.curr = (byte)(curr + 1);
            return curr;
        }
        
        public byte previousByte() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (byte)(--this.curr);
        }
        
        public int nextIndex() {
            return this.curr - this.from;
        }
        
        public int previousIndex() {
            return this.curr - this.from - 1;
        }
        
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += (byte)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (byte)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements ByteIterator {
        final ByteIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ByteIterator[] a, final int offset, final int length) {
            this.lastOffset = -1;
            this.a = a;
            this.offset = offset;
            this.length = length;
            this.advance();
        }
        
        private void advance() {
            while (this.length != 0 && !this.a[this.offset].hasNext()) {
                --this.length;
                ++this.offset;
            }
        }
        
        public boolean hasNext() {
            return this.length > 0;
        }
        
        public byte nextByte() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ByteIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final byte next = a[offset].nextByte();
            this.advance();
            return next;
        }
        
        public void remove() {
            if (this.lastOffset == -1) {
                throw new IllegalStateException();
            }
            this.a[this.lastOffset].remove();
        }
        
        public int skip(final int n) {
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n && this.length != 0) {
                skipped += this.a[this.offset].skip(n - skipped);
                if (this.a[this.offset].hasNext()) {
                    break;
                }
                --this.length;
                ++this.offset;
            }
            return skipped;
        }
    }
    
    public static class UnmodifiableIterator implements ByteIterator {
        protected final ByteIterator i;
        
        public UnmodifiableIterator(final ByteIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public byte nextByte() {
            return this.i.nextByte();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements ByteBidirectionalIterator {
        protected final ByteBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final ByteBidirectionalIterator i) {
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
    }
    
    public static class UnmodifiableListIterator implements ByteListIterator {
        protected final ByteListIterator i;
        
        public UnmodifiableListIterator(final ByteListIterator i) {
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
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
}
