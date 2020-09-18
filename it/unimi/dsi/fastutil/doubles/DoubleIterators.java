package it.unimi.dsi.fastutil.doubles;

import java.util.NoSuchElementException;
import java.io.Serializable;
import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.bytes.ByteIterator;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class DoubleIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private DoubleIterators() {
    }
    
    public static DoubleListIterator singleton(final double element) {
        return new SingletonIterator(element);
    }
    
    public static DoubleListIterator wrap(final double[] array, final int offset, final int length) {
        DoubleArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static DoubleListIterator wrap(final double[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final DoubleIterator i, final double[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextDouble();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final DoubleIterator i, final double[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static double[] unwrap(final DoubleIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        double[] array = new double[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = DoubleArrays.grow(array, j + 1);
            }
            array[j++] = i.nextDouble();
        }
        return DoubleArrays.trim(array, j);
    }
    
    public static double[] unwrap(final DoubleIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final DoubleIterator i, final DoubleCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextDouble());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final DoubleIterator i, final DoubleCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextDouble());
            ++n;
        }
        return n;
    }
    
    public static int pour(final DoubleIterator i, final DoubleCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextDouble());
        }
        return max - j - 1;
    }
    
    public static int pour(final DoubleIterator i, final DoubleCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static DoubleList pour(final DoubleIterator i, final int max) {
        final DoubleArrayList l = new DoubleArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static DoubleList pour(final DoubleIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static DoubleIterator asDoubleIterator(final Iterator i) {
        if (i instanceof DoubleIterator) {
            return (DoubleIterator)i;
        }
        return new IteratorWrapper((Iterator<Double>)i);
    }
    
    public static DoubleListIterator asDoubleIterator(final ListIterator i) {
        if (i instanceof DoubleListIterator) {
            return (DoubleListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Double>)i);
    }
    
    public static boolean any(final DoubleIterator iterator, final DoublePredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final DoubleIterator iterator, final DoublePredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.nextDouble())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final DoubleIterator iterator, final DoublePredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextDouble())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static DoubleIterator concat(final DoubleIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static DoubleIterator concat(final DoubleIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static DoubleIterator unmodifiable(final DoubleIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static DoubleBidirectionalIterator unmodifiable(final DoubleBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static DoubleListIterator unmodifiable(final DoubleListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    public static DoubleIterator wrap(final ByteIterator iterator) {
        return new ByteIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final ShortIterator iterator) {
        return new ShortIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final IntIterator iterator) {
        return new IntIteratorWrapper(iterator);
    }
    
    public static DoubleIterator wrap(final FloatIterator iterator) {
        return new FloatIteratorWrapper(iterator);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements DoubleListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public double nextDouble() {
            throw new NoSuchElementException();
        }
        
        public double previousDouble() {
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
            return DoubleIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return DoubleIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements DoubleListIterator {
        private final double element;
        private int curr;
        
        public SingletonIterator(final double element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public double previousDouble() {
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
    
    private static class ArrayIterator implements DoubleListIterator {
        private final double[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final double[] array, final int offset, final int length) {
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
        
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public double previousDouble() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final double[] array = this.array;
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
    
    private static class IteratorWrapper implements DoubleIterator {
        final Iterator<Double> i;
        
        public IteratorWrapper(final Iterator<Double> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public double nextDouble() {
            return (double)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements DoubleListIterator {
        final ListIterator<Double> i;
        
        public ListIteratorWrapper(final ListIterator<Double> i) {
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
        
        public void set(final double k) {
            this.i.set(k);
        }
        
        public void add(final double k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public double nextDouble() {
            return (double)this.i.next();
        }
        
        public double previousDouble() {
            return (double)this.i.previous();
        }
    }
    
    private static class IteratorConcatenator implements DoubleIterator {
        final DoubleIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final DoubleIterator[] a, final int offset, final int length) {
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
        
        public double nextDouble() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final DoubleIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final double next = a[offset].nextDouble();
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
    
    public static class UnmodifiableIterator implements DoubleIterator {
        protected final DoubleIterator i;
        
        public UnmodifiableIterator(final DoubleIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public double nextDouble() {
            return this.i.nextDouble();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements DoubleBidirectionalIterator {
        protected final DoubleBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final DoubleBidirectionalIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public double nextDouble() {
            return this.i.nextDouble();
        }
        
        public double previousDouble() {
            return this.i.previousDouble();
        }
    }
    
    public static class UnmodifiableListIterator implements DoubleListIterator {
        protected final DoubleListIterator i;
        
        public UnmodifiableListIterator(final DoubleListIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public double nextDouble() {
            return this.i.nextDouble();
        }
        
        public double previousDouble() {
            return this.i.previousDouble();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
    
    protected static class ByteIteratorWrapper implements DoubleIterator {
        final ByteIterator iterator;
        
        public ByteIteratorWrapper(final ByteIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Double next() {
            return (double)this.iterator.nextByte();
        }
        
        public double nextDouble() {
            return this.iterator.nextByte();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class ShortIteratorWrapper implements DoubleIterator {
        final ShortIterator iterator;
        
        public ShortIteratorWrapper(final ShortIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Double next() {
            return (double)this.iterator.nextShort();
        }
        
        public double nextDouble() {
            return this.iterator.nextShort();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class IntIteratorWrapper implements DoubleIterator {
        final IntIterator iterator;
        
        public IntIteratorWrapper(final IntIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Double next() {
            return (double)this.iterator.nextInt();
        }
        
        public double nextDouble() {
            return this.iterator.nextInt();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
    
    protected static class FloatIteratorWrapper implements DoubleIterator {
        final FloatIterator iterator;
        
        public FloatIteratorWrapper(final FloatIterator iterator) {
            this.iterator = iterator;
        }
        
        public boolean hasNext() {
            return this.iterator.hasNext();
        }
        
        @Deprecated
        public Double next() {
            return (double)this.iterator.nextFloat();
        }
        
        public double nextDouble() {
            return this.iterator.nextFloat();
        }
        
        public void remove() {
            this.iterator.remove();
        }
        
        public int skip(final int n) {
            return this.iterator.skip(n);
        }
    }
}
