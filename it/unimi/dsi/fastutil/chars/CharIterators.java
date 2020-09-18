package it.unimi.dsi.fastutil.chars;

import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.ListIterator;
import java.util.Iterator;

public final class CharIterators {
    public static final EmptyIterator EMPTY_ITERATOR;
    
    private CharIterators() {
    }
    
    public static CharListIterator singleton(final char element) {
        return new SingletonIterator(element);
    }
    
    public static CharListIterator wrap(final char[] array, final int offset, final int length) {
        CharArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator(array, offset, length);
    }
    
    public static CharListIterator wrap(final char[] array) {
        return new ArrayIterator(array, 0, array.length);
    }
    
    public static int unwrap(final CharIterator i, final char[] array, int offset, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.nextChar();
        }
        return max - j - 1;
    }
    
    public static int unwrap(final CharIterator i, final char[] array) {
        return unwrap(i, array, 0, array.length);
    }
    
    public static char[] unwrap(final CharIterator i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        char[] array = new char[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = CharArrays.grow(array, j + 1);
            }
            array[j++] = i.nextChar();
        }
        return CharArrays.trim(array, j);
    }
    
    public static char[] unwrap(final CharIterator i) {
        return unwrap(i, Integer.MAX_VALUE);
    }
    
    public static int unwrap(final CharIterator i, final CharCollection c, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.nextChar());
        }
        return max - j - 1;
    }
    
    public static long unwrap(final CharIterator i, final CharCollection c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.nextChar());
            ++n;
        }
        return n;
    }
    
    public static int pour(final CharIterator i, final CharCollection s, final int max) {
        if (max < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("The maximum number of elements (").append(max).append(") is negative").toString());
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.nextChar());
        }
        return max - j - 1;
    }
    
    public static int pour(final CharIterator i, final CharCollection s) {
        return pour(i, s, Integer.MAX_VALUE);
    }
    
    public static CharList pour(final CharIterator i, final int max) {
        final CharArrayList l = new CharArrayList();
        pour(i, l, max);
        l.trim();
        return l;
    }
    
    public static CharList pour(final CharIterator i) {
        return pour(i, Integer.MAX_VALUE);
    }
    
    public static CharIterator asCharIterator(final Iterator i) {
        if (i instanceof CharIterator) {
            return (CharIterator)i;
        }
        return new IteratorWrapper((Iterator<Character>)i);
    }
    
    public static CharListIterator asCharIterator(final ListIterator i) {
        if (i instanceof CharListIterator) {
            return (CharListIterator)i;
        }
        return new ListIteratorWrapper((ListIterator<Character>)i);
    }
    
    public static boolean any(final CharIterator iterator, final IntPredicate predicate) {
        return indexOf(iterator, predicate) != -1;
    }
    
    public static boolean all(final CharIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        while (iterator.hasNext()) {
            if (!predicate.test((int)iterator.nextChar())) {
                return false;
            }
        }
        return true;
    }
    
    public static int indexOf(final CharIterator iterator, final IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test((int)iterator.nextChar())) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static CharListIterator fromTo(final char from, final char to) {
        return new IntervalIterator(from, to);
    }
    
    public static CharIterator concat(final CharIterator[] a) {
        return concat(a, 0, a.length);
    }
    
    public static CharIterator concat(final CharIterator[] a, final int offset, final int length) {
        return new IteratorConcatenator(a, offset, length);
    }
    
    public static CharIterator unmodifiable(final CharIterator i) {
        return new UnmodifiableIterator(i);
    }
    
    public static CharBidirectionalIterator unmodifiable(final CharBidirectionalIterator i) {
        return new UnmodifiableBidirectionalIterator(i);
    }
    
    public static CharListIterator unmodifiable(final CharListIterator i) {
        return new UnmodifiableListIterator(i);
    }
    
    static {
        EMPTY_ITERATOR = new EmptyIterator();
    }
    
    public static class EmptyIterator implements CharListIterator, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyIterator() {
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
            return CharIterators.EMPTY_ITERATOR;
        }
        
        private Object readResolve() {
            return CharIterators.EMPTY_ITERATOR;
        }
    }
    
    private static class SingletonIterator implements CharListIterator {
        private final char element;
        private int curr;
        
        public SingletonIterator(final char element) {
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
        
        public int nextIndex() {
            return this.curr;
        }
        
        public int previousIndex() {
            return this.curr - 1;
        }
    }
    
    private static class ArrayIterator implements CharListIterator {
        private final char[] array;
        private final int offset;
        private final int length;
        private int curr;
        
        public ArrayIterator(final char[] array, final int offset, final int length) {
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
        
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }
        
        public char previousChar() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            final char[] array = this.array;
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
    
    private static class IteratorWrapper implements CharIterator {
        final Iterator<Character> i;
        
        public IteratorWrapper(final Iterator<Character> i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public char nextChar() {
            return (char)this.i.next();
        }
    }
    
    private static class ListIteratorWrapper implements CharListIterator {
        final ListIterator<Character> i;
        
        public ListIteratorWrapper(final ListIterator<Character> i) {
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
        
        public void set(final char k) {
            this.i.set(k);
        }
        
        public void add(final char k) {
            this.i.add(k);
        }
        
        public void remove() {
            this.i.remove();
        }
        
        public char nextChar() {
            return (char)this.i.next();
        }
        
        public char previousChar() {
            return (char)this.i.previous();
        }
    }
    
    private static class IntervalIterator implements CharListIterator {
        private final char from;
        private final char to;
        char curr;
        
        public IntervalIterator(final char from, final char to) {
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
        
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final char curr = this.curr;
            this.curr = (char)(curr + '\u0001');
            return curr;
        }
        
        public char previousChar() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return (char)(--this.curr);
        }
        
        public int nextIndex() {
            return this.curr - this.from;
        }
        
        public int previousIndex() {
            return this.curr - this.from - 1;
        }
        
        public int skip(int n) {
            if (this.curr + n <= this.to) {
                this.curr += (char)n;
                return n;
            }
            n = this.to - this.curr;
            this.curr = this.to;
            return n;
        }
        
        public int back(int n) {
            if (this.curr - n >= this.from) {
                this.curr -= (char)n;
                return n;
            }
            n = this.curr - this.from;
            this.curr = this.from;
            return n;
        }
    }
    
    private static class IteratorConcatenator implements CharIterator {
        final CharIterator[] a;
        int offset;
        int length;
        int lastOffset;
        
        public IteratorConcatenator(final CharIterator[] a, final int offset, final int length) {
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
        
        public char nextChar() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final CharIterator[] a = this.a;
            final int offset = this.offset;
            this.lastOffset = offset;
            final char next = a[offset].nextChar();
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
    
    public static class UnmodifiableIterator implements CharIterator {
        protected final CharIterator i;
        
        public UnmodifiableIterator(final CharIterator i) {
            this.i = i;
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public char nextChar() {
            return this.i.nextChar();
        }
    }
    
    public static class UnmodifiableBidirectionalIterator implements CharBidirectionalIterator {
        protected final CharBidirectionalIterator i;
        
        public UnmodifiableBidirectionalIterator(final CharBidirectionalIterator i) {
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
    }
    
    public static class UnmodifiableListIterator implements CharListIterator {
        protected final CharListIterator i;
        
        public UnmodifiableListIterator(final CharListIterator i) {
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
        
        public int nextIndex() {
            return this.i.nextIndex();
        }
        
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }
}
