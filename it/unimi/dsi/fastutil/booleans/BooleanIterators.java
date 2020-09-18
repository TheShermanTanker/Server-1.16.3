package it.unimi.dsi.fastutil.booleans;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class BooleanIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private BooleanIterators() {
    }
    
    public static BooleanListIterator singleton(final boolean element) {
        return new SingletonIterator(element);
    }
    
    public static BooleanListIterator wrap(final boolean[] array, final int offset, final int length) {
        BooleanArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static BooleanListIterator wrap(final boolean[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final BooleanIterator i, final boolean[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextBoolean();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final BooleanIterator i, final boolean[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static boolean[] unwrap(final BooleanIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        boolean[] array = new boolean[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = BooleanArrays.grow(array, j + 1);
            }
            array[j++] = i.nextBoolean();
        }
        return BooleanArrays.trim(array, j);
    }
    
    public static boolean[] unwrap(final BooleanIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final BooleanIterator i, final BooleanCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextBoolean());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final BooleanIterator i, final BooleanCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextBoolean());
            ++n;
        }
        return n;
    }
    
    public static int pour(final BooleanIterator i, final BooleanCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextBoolean());
        }
        return max - j - 1;
    }
    
    public static int pour(final BooleanIterator i, final BooleanCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static BooleanList pour(final BooleanIterator i, final int max) {
        final BooleanArrayList l = new BooleanArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static BooleanList pour(final BooleanIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static BooleanIterator asBooleanIterator(final Iterator i) {
        if (i instanceof BooleanIterator) {
            return (BooleanIterator)i;
        }
        return new IteratorWrapper((Iterator<Boolean>)i);
    }
    
    public static BooleanListIterator asBooleanIterator(final ListIterator i) {
        if (i instanceof BooleanListIterator) {
            return (BooleanListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Boolean>)i);
    }
    
    public static boolean any(final BooleanIterator iterator, final Predicate<? super Boolean> predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final BooleanIterator iterator, final Predicate<? super Boolean> predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextBoolean())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final BooleanIterator iterator, final Predicate<? super Boolean> predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextBoolean())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static BooleanIterator concat(final BooleanIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static BooleanIterator concat(final BooleanIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static BooleanIterator unmodifiable(final BooleanIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static BooleanBidirectionalIterator unmodifiable(final BooleanBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static BooleanListIterator unmodifiable(final BooleanListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements BooleanListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public boolean nextBoolean() {
            throw new NoSuchElementException();
        }
        
        public boolean previousBoolean() {
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
            return BooleanIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return BooleanIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements BooleanListIterator {
        private final boolean element;
        private int curr;
        
        public SingletonIterator(final boolean element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public boolean previousBoolean() {
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
    
    private static class ArrayIterator implements BooleanListIterator {
        private final boolean[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final boolean[] array, final int offset, final int length) {
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
        
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public boolean previousBoolean() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final boolean[] array = this.array;
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
    
    private static class IteratorWrapper implements BooleanIterator {
        final Iterator<Boolean> i;
        
        public IteratorWrapper(final Iterator<Boolean> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public boolean nextBoolean() {
            return (boolean)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements BooleanListIterator {
        final ListIterator<Boolean> i;
        
        public ListIteratorWrapper(final ListIterator<Boolean> i) {
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
        
        public void set(final boolean k) {
            this.i.set(k);
        }
        
        public void add(final boolean k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public boolean nextBoolean() {
            return (boolean)this.i.next();
        }
        
        public boolean previousBoolean() {
            return (boolean)this.i.previous();
        }
    }
    
    private static class IteratorConcatenator implements BooleanIterator {
        final BooleanIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final BooleanIterator[] a, final int offset, final int length) {
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
        
        public boolean nextBoolean() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final BooleanIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final boolean next = a[offset].nextBoolean();
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
    
    public static class UnmodifiableIterator implements BooleanIterator {
        protected final BooleanIterator i;
        
        public UnmodifiableIterator(final BooleanIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements BooleanBidirectionalIterator {
        protected final BooleanBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final BooleanBidirectionalIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        public boolean previousBoolean() {
            return this.i.previousBoolean();
        }
    }
    
    public static class UnmodifiableListIterator implements BooleanListIterator {
        protected final BooleanListIterator i;
        
        public UnmodifiableListIterator(final BooleanListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public boolean nextBoolean() {
            return this.i.nextBoolean();
        }
        
        public boolean previousBoolean() {
            return this.i.previousBoolean();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
}
