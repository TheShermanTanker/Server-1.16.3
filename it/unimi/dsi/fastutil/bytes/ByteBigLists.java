package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.BigListIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.BigList;
import java.util.Collection;
import java.io.Serializable;
import java.util.Random;

public final class ByteBigLists {
    public static final EmptyBigList EMPTY_BIG_LIST;
    
    private ByteBigLists() {
    }
    
    public static ByteBigList shuffle(final ByteBigList l, final Random random) {
        long i = l.size64();
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final byte t = l.getByte(i);
            l.set(i, l.getByte(p));
            l.set(p, t);
        }
        return l;
    }
    
    public static ByteBigList singleton(final byte element) {
        return new Singleton(element);
    }
    
    public static ByteBigList singleton(final Object element) {
        return new Singleton((byte)element);
    }
    
    public static ByteBigList synchronize(final ByteBigList l) {
        return new SynchronizedBigList(l);
    }
    
    public static ByteBigList synchronize(final ByteBigList l, final Object sync) {
        return new SynchronizedBigList(l, sync);
    }
    
    public static ByteBigList unmodifiable(final ByteBigList l) {
        return new UnmodifiableBigList(l);
    }
    
    public static ByteBigList asBigList(final ByteList list) {
        return new ListBigList(list);
    }
    
    static {
        EMPTY_BIG_LIST = new EmptyBigList();
    }
    
    public static class EmptyBigList extends ByteCollections.EmptyCollection implements ByteBigList, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyBigList() {
        }
        
        @Override
        public byte getByte(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte removeByte(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long index, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte set(final long index, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final byte k) {
            return -1L;
        }
        
        @Override
        public long lastIndexOf(final byte k) {
            return -1L;
        }
        
        public boolean addAll(final long i, final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ByteBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public void add(final long index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public boolean add(final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte get(final long i) {
            throw new IndexOutOfBoundsException();
        }
        
        @Deprecated
        @Override
        public Byte set(final long index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object k) {
            return -1L;
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object k) {
            return -1L;
        }
        
        @Override
        public ByteBigListIterator listIterator() {
            return ByteBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ByteBigListIterator iterator() {
            return ByteBigListIterators.EMPTY_BIG_LIST_ITERATOR;
        }
        
        @Override
        public ByteBigListIterator listIterator(final long i) {
            if (i == 0L) {
                return ByteBigListIterators.EMPTY_BIG_LIST_ITERATOR;
            }
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            if (from == 0L && to == 0L) {
                return this;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public void getElements(final long from, final byte[][] a, final long offset, final long length) {
            ByteBigArrays.ensureOffsetLength(a, offset, length);
            if (from != 0L) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final byte[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final byte[][] a) {
            throw new UnsupportedOperationException();
        }
        
        public void size(final long s) {
            throw new UnsupportedOperationException();
        }
        
        public long size64() {
            return 0L;
        }
        
        public int compareTo(final BigList<? extends Byte> o) {
            if (o == this) {
                return 0;
            }
            return o.isEmpty() ? 0 : -1;
        }
        
        public Object clone() {
            return ByteBigLists.EMPTY_BIG_LIST;
        }
        
        @Override
        public int hashCode() {
            return 1;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof BigList && ((BigList)o).isEmpty();
        }
        
        public String toString() {
            return "[]";
        }
        
        private Object readResolve() {
            return ByteBigLists.EMPTY_BIG_LIST;
        }
    }
    
    public static class Singleton extends AbstractByteBigList implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final byte element;
        
        protected Singleton(final byte element) {
            this.element = element;
        }
        
        public byte getByte(final long i) {
            if (i == 0L) {
                return this.element;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public boolean rem(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte removeByte(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean contains(final byte k) {
            return k == this.element;
        }
        
        public byte[] toByteArray() {
            final byte[] a = { this.element };
            return a;
        }
        
        @Override
        public ByteBigListIterator listIterator() {
            return ByteBigListIterators.singleton(this.element);
        }
        
        @Override
        public ByteBigListIterator listIterator(final long i) {
            if (i > 1L || i < 0L) {
                throw new IndexOutOfBoundsException();
            }
            final ByteBigListIterator l = this.listIterator();
            if (i == 1L) {
                l.nextByte();
            }
            return l;
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            this.ensureIndex(from);
            this.ensureIndex(to);
            if (from > to) {
                throw new IndexOutOfBoundsException(new StringBuilder().append("Start index (").append(from).append(") is greater than end index (").append(to).append(")").toString());
            }
            if (from != 0L || to != 1L) {
                return ByteBigLists.EMPTY_BIG_LIST;
            }
            return this;
        }
        
        @Override
        public boolean addAll(final long i, final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ByteBigList c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long i, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public long size64() {
            return 1L;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedBigList extends ByteCollections.SynchronizedCollection implements ByteBigList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteBigList list;
        
        protected SynchronizedBigList(final ByteBigList l, final Object sync) {
            super(l, sync);
            this.list = l;
        }
        
        protected SynchronizedBigList(final ByteBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public byte getByte(final long i) {
            synchronized (this.sync) {
                return this.list.getByte(i);
            }
        }
        
        @Override
        public byte set(final long i, final byte k) {
            synchronized (this.sync) {
                return this.list.set(i, k);
            }
        }
        
        @Override
        public void add(final long i, final byte k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Override
        public byte removeByte(final long i) {
            synchronized (this.sync) {
                return this.list.removeByte(i);
            }
        }
        
        @Override
        public long indexOf(final byte k) {
            synchronized (this.sync) {
                return this.list.indexOf(k);
            }
        }
        
        @Override
        public long lastIndexOf(final byte k) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(k);
            }
        }
        
        public boolean addAll(final long index, final Collection<? extends Byte> c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public void getElements(final long from, final byte[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.getElements(from, a, offset, length);
            }
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            synchronized (this.sync) {
                this.list.removeElements(from, to);
            }
        }
        
        @Override
        public void addElements(final long index, final byte[][] a, final long offset, final long length) {
            synchronized (this.sync) {
                this.list.addElements(index, a, offset, length);
            }
        }
        
        @Override
        public void addElements(final long index, final byte[][] a) {
            synchronized (this.sync) {
                this.list.addElements(index, a);
            }
        }
        
        @Deprecated
        public void size(final long size) {
            synchronized (this.sync) {
                this.list.size(size);
            }
        }
        
        public long size64() {
            synchronized (this.sync) {
                return this.list.size64();
            }
        }
        
        @Override
        public ByteBigListIterator iterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ByteBigListIterator listIterator() {
            return this.list.listIterator();
        }
        
        @Override
        public ByteBigListIterator listIterator(final long i) {
            return this.list.listIterator(i);
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            synchronized (this.sync) {
                return ByteBigLists.synchronize(this.list.subList(from, to), this.sync);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.list.equals(o);
            }
        }
        
        @Override
        public int hashCode() {
            synchronized (this.sync) {
                return this.list.hashCode();
            }
        }
        
        public int compareTo(final BigList<? extends Byte> o) {
            synchronized (this.sync) {
                return this.list.compareTo(o);
            }
        }
        
        @Override
        public boolean addAll(final long index, final ByteCollection c) {
            synchronized (this.sync) {
                return this.list.addAll(index, c);
            }
        }
        
        @Override
        public boolean addAll(final long index, final ByteBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(index, l);
            }
        }
        
        @Override
        public boolean addAll(final ByteBigList l) {
            synchronized (this.sync) {
                return this.list.addAll(l);
            }
        }
        
        @Deprecated
        @Override
        public void add(final long i, final Byte k) {
            synchronized (this.sync) {
                this.list.add(i, k);
            }
        }
        
        @Deprecated
        @Override
        public Byte get(final long i) {
            synchronized (this.sync) {
                return this.list.get(i);
            }
        }
        
        @Deprecated
        @Override
        public Byte set(final long index, final Byte k) {
            synchronized (this.sync) {
                return this.list.set(index, k);
            }
        }
        
        @Deprecated
        @Override
        public Byte remove(final long i) {
            synchronized (this.sync) {
                return this.list.remove(i);
            }
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.indexOf(o);
            }
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object o) {
            synchronized (this.sync) {
                return this.list.lastIndexOf(o);
            }
        }
    }
    
    public static class UnmodifiableBigList extends ByteCollections.UnmodifiableCollection implements ByteBigList, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ByteBigList list;
        
        protected UnmodifiableBigList(final ByteBigList l) {
            super(l);
            this.list = l;
        }
        
        @Override
        public byte getByte(final long i) {
            return this.list.getByte(i);
        }
        
        @Override
        public byte set(final long i, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void add(final long i, final byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public byte removeByte(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public long indexOf(final byte k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final byte k) {
            return this.list.lastIndexOf(k);
        }
        
        public boolean addAll(final long index, final Collection<? extends Byte> c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void getElements(final long from, final byte[][] a, final long offset, final long length) {
            this.list.getElements(from, a, offset, length);
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final byte[][] a, final long offset, final long length) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void addElements(final long index, final byte[][] a) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public void size(final long size) {
            this.list.size(size);
        }
        
        public long size64() {
            return this.list.size64();
        }
        
        @Override
        public ByteBigListIterator iterator() {
            return this.listIterator();
        }
        
        @Override
        public ByteBigListIterator listIterator() {
            return ByteBigListIterators.unmodifiable(this.list.listIterator());
        }
        
        @Override
        public ByteBigListIterator listIterator(final long i) {
            return ByteBigListIterators.unmodifiable(this.list.listIterator(i));
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            return ByteBigLists.unmodifiable(this.list.subList(from, to));
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.list.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
        
        public int compareTo(final BigList<? extends Byte> o) {
            return this.list.compareTo(o);
        }
        
        @Override
        public boolean addAll(final long index, final ByteCollection c) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final ByteBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean addAll(final long index, final ByteBigList l) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte get(final long i) {
            return this.list.get(i);
        }
        
        @Deprecated
        @Override
        public void add(final long i, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte set(final long index, final Byte k) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public Byte remove(final long i) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        @Override
        public long indexOf(final Object o) {
            return this.list.indexOf(o);
        }
        
        @Deprecated
        @Override
        public long lastIndexOf(final Object o) {
            return this.list.lastIndexOf(o);
        }
    }
    
    public static class ListBigList extends AbstractByteBigList implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        private final ByteList list;
        
        protected ListBigList(final ByteList list) {
            this.list = list;
        }
        
        private int intIndex(final long index) {
            if (index >= 2147483647L) {
                throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices");
            }
            return (int)index;
        }
        
        public long size64() {
            return this.list.size();
        }
        
        @Override
        public void size(final long size) {
            this.list.size(this.intIndex(size));
        }
        
        @Override
        public ByteBigListIterator iterator() {
            return ByteBigListIterators.asBigListIterator(this.list.iterator());
        }
        
        @Override
        public ByteBigListIterator listIterator() {
            return ByteBigListIterators.asBigListIterator(this.list.listIterator());
        }
        
        @Override
        public ByteBigListIterator listIterator(final long index) {
            return ByteBigListIterators.asBigListIterator(this.list.listIterator(this.intIndex(index)));
        }
        
        @Override
        public boolean addAll(final long index, final Collection<? extends Byte> c) {
            return this.list.addAll(this.intIndex(index), (Collection)c);
        }
        
        @Override
        public ByteBigList subList(final long from, final long to) {
            return new ListBigList(this.list.subList(this.intIndex(from), this.intIndex(to)));
        }
        
        @Override
        public boolean contains(final byte key) {
            return this.list.contains(key);
        }
        
        public byte[] toByteArray() {
            return this.list.toByteArray();
        }
        
        @Override
        public void removeElements(final long from, final long to) {
            this.list.removeElements(this.intIndex(from), this.intIndex(to));
        }
        
        @Deprecated
        public byte[] toByteArray(final byte[] a) {
            return this.list.toArray(a);
        }
        
        @Override
        public boolean addAll(final long index, final ByteCollection c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final ByteCollection c) {
            return this.list.addAll(c);
        }
        
        @Override
        public boolean addAll(final long index, final ByteBigList c) {
            return this.list.addAll(this.intIndex(index), c);
        }
        
        @Override
        public boolean addAll(final ByteBigList c) {
            return this.list.addAll(c);
        }
        
        public boolean containsAll(final ByteCollection c) {
            return this.list.containsAll(c);
        }
        
        public boolean removeAll(final ByteCollection c) {
            return this.list.removeAll(c);
        }
        
        public boolean retainAll(final ByteCollection c) {
            return this.list.retainAll(c);
        }
        
        @Override
        public void add(final long index, final byte key) {
            this.list.add(this.intIndex(index), key);
        }
        
        @Override
        public boolean add(final byte key) {
            return this.list.add(key);
        }
        
        public byte getByte(final long index) {
            return this.list.getByte(this.intIndex(index));
        }
        
        @Override
        public long indexOf(final byte k) {
            return this.list.indexOf(k);
        }
        
        @Override
        public long lastIndexOf(final byte k) {
            return this.list.lastIndexOf(k);
        }
        
        @Override
        public byte removeByte(final long index) {
            return this.list.removeByte(this.intIndex(index));
        }
        
        @Override
        public byte set(final long index, final byte k) {
            return this.list.set(this.intIndex(index), k);
        }
        
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
        
        public <T> T[] toArray(final T[] a) {
            return (T[])this.list.toArray((Object[])a);
        }
        
        public boolean containsAll(final Collection<?> c) {
            return this.list.containsAll((Collection)c);
        }
        
        @Override
        public boolean addAll(final Collection<? extends Byte> c) {
            return this.list.addAll((Collection)c);
        }
        
        public boolean removeAll(final Collection<?> c) {
            return this.list.removeAll((Collection)c);
        }
        
        public boolean retainAll(final Collection<?> c) {
            return this.list.retainAll((Collection)c);
        }
        
        @Override
        public void clear() {
            this.list.clear();
        }
        
        @Override
        public int hashCode() {
            return this.list.hashCode();
        }
    }
}
