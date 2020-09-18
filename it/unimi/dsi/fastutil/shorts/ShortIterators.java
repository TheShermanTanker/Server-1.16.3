package it.unimi.dsi.fastutil.shorts;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class ShortIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ShortIterators() {
    }
    
    public static ShortListIterator singleton(final short element) {
        return new SingletonIterator(element);
    }
    
    public static ShortListIterator wrap(final short[] array, final int offset, final int length) {
        ShortArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static ShortListIterator wrap(final short[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final ShortIterator i, final short[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextShort();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final ShortIterator i, final short[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static short[] unwrap(final ShortIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        short[] array = new short[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ShortArrays.grow(array, j + 1);
            }
            array[j++] = i.nextShort();
        }
        return ShortArrays.trim(array, j);
    }
    
    public static short[] unwrap(final ShortIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final ShortIterator i, final ShortCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextShort());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final ShortIterator i, final ShortCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextShort());
            ++n;
        }
        return n;
    }
    
    public static int pour(final ShortIterator i, final ShortCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextShort());
        }
        return max - j - 1;
    }
    
    public static int pour(final ShortIterator i, final ShortCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static ShortList pour(final ShortIterator i, final int max) {
        final ShortArrayList l = new ShortArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static ShortList pour(final ShortIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static ShortIterator asShortIterator(final Iterator i) {
        if (i instanceof ShortIterator) {
            return (ShortIterator)i;
        }
        return new IteratorWrapper((Iterator<Short>)i);
    }
    
    public static ShortListIterator asShortIterator(final ListIterator i) {
        if (i instanceof ShortListIterator) {
            return (ShortListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Short>)i);
    }
    
    public static boolean any(final ShortIterator iterator, final IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final ShortIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test((int)iterator.nextShort())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final ShortIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test((int)iterator.nextShort())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static ShortListIterator fromTo(final short from, final short to) {
        return new IntervalIterator(from, to);
    }
    
    public static ShortIterator concat(final ShortIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static ShortIterator concat(final ShortIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static ShortIterator unmodifiable(final ShortIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static ShortBidirectionalIterator unmodifiable(final ShortBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static ShortListIterator unmodifiable(final ShortListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static ShortIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements ShortListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
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
            return ShortIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ShortIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements ShortListIterator {
        private final short element;
        private int curr;
        
        public SingletonIterator(final short element) {
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
        
        public int nextIndex() {
            return this.curr;
        }
        
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class ArrayIterator implements ShortListIterator {
        private final short[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final short[] array, final int offset, final int length) {
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
        
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public short previousShort() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final short[] array = this.array;
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
    
    private static class IteratorWrapper implements ShortIterator {
        final Iterator<Short> i;
        
        public IteratorWrapper(final Iterator<Short> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public short nextShort() {
            return (short)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements ShortListIterator {
        final ListIterator<Short> i;
        
        public ListIteratorWrapper(final ListIterator<Short> i) {
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
        
        public void set(final short k) {
            this.i.set(k);
        }
        
        public void add(final short k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public short nextShort() {
            return (short)this.i.next();
        }
        
        public short previousShort() {
            return (short)this.i.previous();
        }
    }
    
    private static class IntervalIterator implements ShortListIterator {
        private final short from;
        private final short to;
        short curr;
        
        public IntervalIterator(final short from, final short to) {
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
        
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final short curr = this.curr;
            this.curr = (short)(curr + 1);
            return curr;
        }
        
        public short previousShort() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (short)(--this.curr);
        }
        
        public int nextIndex() {
            return this.curr - this.from;
        }
        
        public int previousIndex() {
            return this.curr - this.from - 1;
        }
        
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += (short)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (short)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements ShortIterator {
        final ShortIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ShortIterator[] a, final int offset, final int length) {
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
        
        public short nextShort() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ShortIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final short next = a[offset].nextShort();
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
    
    public static class UnmodifiableIterator implements ShortIterator {
        protected final ShortIterator i;
        
        public UnmodifiableIterator(final ShortIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public short nextShort() {
            return this.i.nextShort();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements ShortBidirectionalIterator {
        protected final ShortBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final ShortBidirectionalIterator i) {
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
    }
    
    public static class UnmodifiableListIterator implements ShortListIterator {
        protected final ShortListIterator i;
        
        public UnmodifiableListIterator(final ShortListIterator i) {
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
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    protected static class ByteIteratorWrapper implements ShortIterator {
        final ByteIterator iterator;
        
        public ByteIteratorWrapper(final ByteIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Short next() {
            return (short)this.iterator.nextByte();
        }
        
        public short nextShort() {
            return this.iterator.nextByte();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
}
