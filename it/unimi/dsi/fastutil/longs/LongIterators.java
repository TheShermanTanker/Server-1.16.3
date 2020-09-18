package it.unimi.dsi.fastutil.longs;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.Objects;
import java.util.function.LongPredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class LongIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private LongIterators() {
    }
    
    public static LongListIterator singleton(final long element) {
        return new SingletonIterator(element);
    }
    
    public static LongListIterator wrap(final long[] array, final int offset, final int length) {
        LongArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static LongListIterator wrap(final long[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final LongIterator i, final long[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextLong();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final LongIterator i, final long[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static long[] unwrap(final LongIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        long[] array = new long[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = LongArrays.grow(array, j + 1);
            }
            array[j++] = i.nextLong();
        }
        return LongArrays.trim(array, j);
    }
    
    public static long[] unwrap(final LongIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final LongIterator i, final LongCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextLong());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final LongIterator i, final LongCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextLong());
            ++n;
        }
        return n;
    }
    
    public static int pour(final LongIterator i, final LongCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextLong());
        }
        return max - j - 1;
    }
    
    public static int pour(final LongIterator i, final LongCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static LongList pour(final LongIterator i, final int max) {
        final LongArrayList l = new LongArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static LongList pour(final LongIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static LongIterator asLongIterator(final Iterator i) {
        if (i instanceof LongIterator) {
            return (LongIterator)i;
        }
        return new IteratorWrapper((Iterator<Long>)i);
    }
    
    public static LongListIterator asLongIterator(final ListIterator i) {
        if (i instanceof LongListIterator) {
            return (LongListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Long>)i);
    }
    
    public static boolean any(final LongIterator iterator, final LongPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final LongIterator iterator, final LongPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextLong())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final LongIterator iterator, final LongPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextLong())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static LongBidirectionalIterator fromTo(final long from, final long to) {
        return new IntervalIterator(from, to);
    }
    
    public static LongIterator concat(final LongIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static LongIterator concat(final LongIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static LongIterator unmodifiable(final LongIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static LongBidirectionalIterator unmodifiable(final LongBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static LongListIterator unmodifiable(final LongListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static LongIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static LongIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    public static LongIterator wrap(final IntIterator iterator) {
        return new IntIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements LongListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public long nextLong() {
            throw new NoSuchElementException();
        }
        
        public long previousLong() {
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
            return LongIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return LongIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements LongListIterator {
        private final long element;
        private int curr;
        
        public SingletonIterator(final long element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public long previousLong() {
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
    
    private static class ArrayIterator implements LongListIterator {
        private final long[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final long[] array, final int offset, final int length) {
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
        
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public long previousLong() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final long[] array = this.array;
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
    
    private static class IteratorWrapper implements LongIterator {
        final Iterator<Long> i;
        
        public IteratorWrapper(final Iterator<Long> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public long nextLong() {
            return (long)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements LongListIterator {
        final ListIterator<Long> i;
        
        public ListIteratorWrapper(final ListIterator<Long> i) {
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
        
        public void set(final long k) {
            this.i.set(k);
        }
        
        public void add(final long k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public long nextLong() {
            return (long)this.i.next();
        }
        
        public long previousLong() {
            return (long)this.i.previous();
        }
    }
    
    private static class IntervalIterator implements LongBidirectionalIterator {
        private final long from;
        private final long to;
        long curr;
        
        public IntervalIterator(final long from, final long to) {
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
        
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.curr++;
        }
        
        public long previousLong() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return --this.curr;
        }
        
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += n;
                return n;
            }
            n = (int)(this.to - this.curr);
            this.curr = this.to;
            return n;
        }
        
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= n;
                return n;
            }
            n = (int)(this.curr - this.from);
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements LongIterator {
        final LongIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final LongIterator[] a, final int offset, final int length) {
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
        
        public long nextLong() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final LongIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final long next = a[offset].nextLong();
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
    
    public static class UnmodifiableIterator implements LongIterator {
        protected final LongIterator i;
        
        public UnmodifiableIterator(final LongIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public long nextLong() {
            return this.i.nextLong();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements LongBidirectionalIterator {
        protected final LongBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final LongBidirectionalIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public long nextLong() {
            return this.i.nextLong();
        }
        
        public long previousLong() {
            return this.i.previousLong();
        }
    }
    
    public static class UnmodifiableListIterator implements LongListIterator {
        protected final LongListIterator i;
        
        public UnmodifiableListIterator(final LongListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public long nextLong() {
            return this.i.nextLong();
        }
        
        public long previousLong() {
            return this.i.previousLong();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    protected static class ByteIteratorWrapper implements LongIterator {
        final ByteIterator iterator;
        
        public ByteIteratorWrapper(final ByteIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Long next() {
            return (long)this.iterator.nextByte();
        }
        
        public long nextLong() {
            return this.iterator.nextByte();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class ShortIteratorWrapper implements LongIterator {
        final ShortIterator iterator;
        
        public ShortIteratorWrapper(final ShortIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Long next() {
            return (long)this.iterator.nextShort();
        }
        
        public long nextLong() {
            return this.iterator.nextShort();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class IntIteratorWrapper implements LongIterator {
        final IntIterator iterator;
        
        public IntIteratorWrapper(final IntIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Long next() {
            return (long)this.iterator.nextInt();
        }
        
        public long nextLong() {
            return this.iterator.nextInt();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
}
