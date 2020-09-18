package it.unimi.dsi.fastutil.objects;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class ObjectIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private ObjectIterators() {
    }
    
    public static <K> ObjectIterator<K> emptyIterator() {
        return (ObjectIterator<K>)ObjectIterators.EMPTY_ITERATOR;
    }
    
    public static <K> ObjectListIterator<K> singleton(final K element) {
        return new SingletonIterator<K>(element);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array, final int offset, final int length) {
        ObjectArrays.<K>ensureOffsetLength(array, offset, length);
        return new ArrayIterator<K>(array, offset, length);
    }
    
    public static <K> ObjectListIterator<K> wrap(final K[] array) {
        return new ArrayIterator<K>(array, 0, array.length);
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = (K)i.next();
        }
        return max - j - 1;
    }
    
    public static <K> int unwrap(final Iterator<? extends K> i, final K[] array) {
        return ObjectIterators.<K>unwrap(i, array, 0, array.length);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        K[] array = (K[])new Object[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ObjectArrays.<K>grow(array, j + 1);
            }
            array[j++] = (K)i.next();
        }
        return ObjectArrays.<K>trim(array, j);
    }
    
    public static <K> K[] unwrap(final Iterator<? extends K> i) {
        return ObjectIterators.<K>unwrap(i, Integer.MAX_VALUE);
    }
    
    public static <K> int unwrap(final Iterator<K> i, final ObjectCollection<? super K> c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.next());
        }
        return max - j - 1;
    }
    
    public static <K> long unwrap(final Iterator<K> i, final ObjectCollection<? super K> c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.next());
            ++n;
        }
        return n;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.next());
        }
        return max - j - 1;
    }
    
    public static <K> int pour(final Iterator<K> i, final ObjectCollection<? super K> s) {
        return ObjectIterators.<K>pour(i, s, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i, final int max) {
        final ObjectArrayList<K> l = new ObjectArrayList<K>();
        ObjectIterators.<K>pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static <K> ObjectList<K> pour(final Iterator<K> i) {
        return ObjectIterators.<K>pour(i, Integer.MAX_VALUE);
    }
    
    public static <K> ObjectIterator<K> asObjectIterator(final Iterator<K> i) {
        if (i instanceof ObjectIterator) {
            return (ObjectIterator<K>)(ObjectIterator)i;
        }
        return new IteratorWrapper<K>(i);
    }
    
    public static <K> ObjectListIterator<K> asObjectIterator(final ListIterator<K> i) {
        if (i instanceof ObjectListIterator) {
            return (ObjectListIterator<K>)(ObjectListIterator)i;
        }
        return new ListIteratorWrapper<K>(i);
    }
    
    public static <K> boolean any(final ObjectIterator<K> iterator, final Predicate<? super K> predicate) {
        return ObjectIterators.<K>indexOf(iterator, predicate) != -1;
    }
    
    public static <K> boolean all(final ObjectIterator<K> iterator, final Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public static <K> int indexOf(final ObjectIterator<K> iterator, final Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>[] a) {
        return ObjectIterators.<K>concat(a, 0, a.length);
    }
    
    public static <K> ObjectIterator<K> concat(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
        return new IteratorConcatenator<K>(a, offset, length);
    }
    
    public static <K> ObjectIterator<K> unmodifiable(final ObjectIterator<K> i) {
        return new UnmodifiableIterator<K>(i);
    }
    
    public static <K> ObjectBidirectionalIterator<K> unmodifiable(final ObjectBidirectionalIterator<K> i) {
        return new UnmodifiableBidirectionalIterator<K>(i);
    }
    
    public static <K> ObjectListIterator<K> unmodifiable(final ObjectListIterator<K> i) {
        return new UnmodifiableListIterator<K>(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator<K> implements ObjectListIterator<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
        }
        
        public boolean hasNext() {
            return false;
        }
        
        public boolean hasPrevious() {
            return false;
        }
        
        public K next() {
            throw new NoSuchElementException();
        }
        
        public K previous() {
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
            return ObjectIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return ObjectIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator<K> implements ObjectListIterator<K> {
        private final K element;
        private int curr;
        
        public SingletonIterator(final K element) {
            this.element = element;
        }
        
        public boolean hasNext() {
            return this.curr == 0;
        }
        
        public boolean hasPrevious() {
            return this.curr == 1;
        }
        
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }
        
        public K previous() {
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
    
    private static class ArrayIterator<K> implements ObjectListIterator<K> {
        private final K[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final K[] array, final int offset, final int length) {
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
        
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final K[] array = this.array;
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
    
    private static class IteratorWrapper<K> implements ObjectIterator<K> {
        final Iterator<K> i;
        
        public IteratorWrapper(final Iterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public K next() {
            return (K)this.i.next();
        }
    }
    
    private static class IteratorWrapper<K> implements ObjectIterator<K> {
        final Iterator<K> i;
        
        public IteratorWrapper(final Iterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public K next() {
            return (K)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper<K> implements ObjectListIterator<K> {
        final ListIterator<K> i;
        
        public ListIteratorWrapper(final ListIterator<K> i) {
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
        
        public void set(final K k) {
            this.i.set(k);
        }
        
        public void add(final K k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public K next() {
            return (K)this.i.next();
        }
        
        public K previous() {
            return (K)this.i.previous();
        }
    }
    
    private static class IteratorConcatenator<K> implements ObjectIterator<K> {
        final ObjectIterator<? extends K>[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final ObjectIterator<? extends K>[] a, final int offset, final int length) {
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
        
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final ObjectIterator<? extends K>[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final K next = (K)a[offset].next();
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
    
    public static class UnmodifiableIterator<K> implements ObjectIterator<K> {
        protected final ObjectIterator<K> i;
        
        public UnmodifiableIterator(final ObjectIterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public K next() {
            return (K)this.i.next();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<K> i;
        
        public UnmodifiableBidirectionalIterator(final ObjectBidirectionalIterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public K next() {
            return (K)this.i.next();
        }
        
        public K previous() {
            return this.i.previous();
        }
    }
    
    public static class UnmodifiableListIterator<K> implements ObjectListIterator<K> {
        protected final ObjectListIterator<K> i;
        
        public UnmodifiableListIterator(final ObjectListIterator<K> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
        
        public K next() {
            return (K)this.i.next();
        }
        
        public K previous() {
            return this.i.previous();
        }
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
}
