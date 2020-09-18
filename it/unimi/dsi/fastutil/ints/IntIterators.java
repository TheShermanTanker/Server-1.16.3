package it.unimi.dsi.fastutil.ints;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class IntIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private IntIterators() {
    }
    
    public static IntListIterator singleton(final int element) {
        return new SingletonIterator(element);
    }
    
    public static IntListIterator wrap(final int[] array, final int offset, final int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static IntListIterator wrap(final int[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final IntIterator i, final int[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextInt();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final IntIterator i, final int[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static int[] unwrap(final IntIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int[] array = new int[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = IntArrays.grow(array, j + 1);
            }
            array[j++] = i.nextInt();
        }
        return IntArrays.trim(array, j);
    }
    
    public static int[] unwrap(final IntIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final IntIterator i, final IntCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final IntIterator i, final IntCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextInt());
            ++n;
        }
        return n;
    }
    
    public static int pour(final IntIterator i, final IntCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextInt());
        }
        return max - j - 1;
    }
    
    public static int pour(final IntIterator i, final IntCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static IntList pour(final IntIterator i, final int max) {
        final IntArrayList l = new IntArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static IntList pour(final IntIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static IntIterator asIntIterator(final Iterator i) {
        if (i instanceof IntIterator) {
            return (IntIterator)i;
        }
        return new IteratorWrapper((Iterator<Integer>)i);
    }
    
    public static IntListIterator asIntIterator(final ListIterator i) {
        if (i instanceof IntListIterator) {
            return (IntListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Integer>)i);
    }
    
    public static boolean any(final IntIterator iterator, final IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final IntIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextInt())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final IntIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static IntListIterator fromTo(final int from, final int to) {
        return new IntervalIterator(from, to);
    }
    
    public static IntIterator concat(final IntIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static IntIterator concat(final IntIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static IntIterator unmodifiable(final IntIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static IntBidirectionalIterator unmodifiable(final IntBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static IntListIterator unmodifiable(final IntListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static IntIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static IntIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements IntListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public int nextInt() {
            throw new NoSuchElementException();
        }
        
        public int previousInt() {
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
            return IntIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return IntIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements IntListIterator {
        private final int element;
        private int curr;
        
        public SingletonIterator(final int element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public int previousInt() {
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
    
    private static class ArrayIterator implements IntListIterator {
        private final int[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final int[] array, final int offset, final int length) {
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
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final int[] array = this.array;
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
    
    private static class IteratorWrapper implements IntIterator {
        final Iterator<Integer> i;
        
        public IteratorWrapper(final Iterator<Integer> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public int nextInt() {
            return (int)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements IntListIterator {
        final ListIterator<Integer> i;
        
        public ListIteratorWrapper(final ListIterator<Integer> i) {
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
        
        public void set(final int k) {
            this.i.set(k);
        }
        
        public void add(final int k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public int nextInt() {
            return (int)this.i.next();
        }
        
        public int previousInt() {
            return (int)this.i.previous();
        }
    }
    
    private static class IntervalIterator implements IntListIterator {
        private final int from;
        private final int to;
        int curr;
        
        public IntervalIterator(final int from, final int to) {
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
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.curr++;
        }
        
        public int previousInt() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return --this.curr;
        }
        
        public int nextIndex() {
            return this.curr - this.from;
        }
        
        public int previousIndex() {
            return this.curr - this.from - 1;
        }
        
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements IntIterator {
        final IntIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final IntIterator[] a, final int offset, final int length) {
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
        
        public int nextInt() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final IntIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final int next = a[offset].nextInt();
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
    
    public static class UnmodifiableIterator implements IntIterator {
        protected final IntIterator i;
        
        public UnmodifiableIterator(final IntIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public int nextInt() {
            return this.i.nextInt();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements IntBidirectionalIterator {
        protected final IntBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final IntBidirectionalIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public int nextInt() {
            return this.i.nextInt();
        }
        
        public int previousInt() {
            return this.i.previousInt();
        }
    }
    
    public static class UnmodifiableListIterator implements IntListIterator {
        protected final IntListIterator i;
        
        public UnmodifiableListIterator(final IntListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public int nextInt() {
            return this.i.nextInt();
        }
        
        public int previousInt() {
            return this.i.previousInt();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    protected static class ByteIteratorWrapper implements IntIterator {
        final ByteIterator iterator;
        
        public ByteIteratorWrapper(final ByteIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Integer next() {
            return (int)this.iterator.nextByte();
        }
        
        public int nextInt() {
            return this.iterator.nextByte();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class ShortIteratorWrapper implements IntIterator {
        final ShortIterator iterator;
        
        public ShortIteratorWrapper(final ShortIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Integer next() {
            return (int)this.iterator.nextShort();
        }
        
        public int nextInt() {
            return this.iterator.nextShort();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
}
