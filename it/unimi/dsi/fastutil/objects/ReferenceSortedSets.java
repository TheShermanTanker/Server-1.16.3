package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.Comparator;

public final class ReferenceSortedSets {
    public static final EmptySet EMPTY_SET;
    
    private ReferenceSortedSets() {
    }
    
    public static <K> ReferenceSet<K> emptySet() {
        return (ReferenceSet<K>)ReferenceSortedSets.EMPTY_SET;
    }
    
    public static <K> ReferenceSortedSet<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ReferenceSortedSet<K> singleton(final K element, final Comparator<? super K> comparator) {
        return new Singleton<K>(element, comparator);
    }
    
    public static <K> ReferenceSortedSet<K> synchronize(final ReferenceSortedSet<K> s) {
        return new SynchronizedSortedSet<K>(s);
    }
    
    public static <K> ReferenceSortedSet<K> synchronize(final ReferenceSortedSet<K> s, final Object sync) {
        return new SynchronizedSortedSet<K>(s, sync);
    }
    
    public static <K> ReferenceSortedSet<K> unmodifiable(final ReferenceSortedSet<K> s) {
        return new UnmodifiableSortedSet<K>(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet<K> extends ReferenceSets.EmptySet<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K from) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ReferenceSortedSets.EMPTY_SET;
        }
    }
    
    public static class EmptySet<K> extends ReferenceSets.EmptySet<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K from) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ReferenceSortedSets.EMPTY_SET;
        }
    }
    
    public static class EmptySet<K> extends ReferenceSets.EmptySet<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K from) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ReferenceSortedSets.EMPTY_SET;
        }
    }
    
    public static class EmptySet<K> extends ReferenceSets.EmptySet<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return (ObjectBidirectionalIterator<K>)ObjectIterators.EMPTY_ITERATOR;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K from) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K to) {
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            throw new NoSuchElementException();
        }
        
        public K last() {
            throw new NoSuchElementException();
        }
        
        public Comparator<? super K> comparator() {
            return null;
        }
        
        @Override
        public Object clone() {
            return ReferenceSortedSets.EMPTY_SET;
        }
        
        private Object readResolve() {
            return ReferenceSortedSets.EMPTY_SET;
        }
    }
    
    public static class Singleton<K> extends ReferenceSets.Singleton<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;
        
        protected Singleton(final K element, final Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final K element) {
            this(element, (java.util.Comparator<? super Object>)null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            final ObjectBidirectionalIterator<K> i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            return this.element;
        }
        
        public K last() {
            return this.element;
        }
    }
    
    public static class Singleton<K> extends ReferenceSets.Singleton<K> implements ReferenceSortedSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        final Comparator<? super K> comparator;
        
        protected Singleton(final K element, final Comparator<? super K> comparator) {
            super(element);
            this.comparator = comparator;
        }
        
        private Singleton(final K element) {
            this(element, (java.util.Comparator<? super Object>)null);
        }
        
        final int compare(final K k1, final K k2) {
            return (this.comparator == null) ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            final ObjectBidirectionalIterator<K> i = this.iterator();
            if (this.compare(this.element, from) <= 0) {
                i.next();
            }
            return i;
        }
        
        public Comparator<? super K> comparator() {
            return this.comparator;
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            if (this.compare(from, this.element) <= 0 && this.compare(this.element, to) < 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            if (this.compare(this.element, to) < 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            if (this.compare(from, this.element) <= 0) {
                return this;
            }
            return (ReferenceSortedSet<K>)ReferenceSortedSets.EMPTY_SET;
        }
        
        public K first() {
            return this.element;
        }
        
        public K last() {
            return this.element;
        }
    }
    
    public static class SynchronizedSortedSet<K> extends ReferenceSets.SynchronizedSet<K> implements ReferenceSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceSortedSet<K> sortedSet;
        
        protected SynchronizedSortedSet(final ReferenceSortedSet<K> s, final Object sync) {
            super(s, sync);
            this.sortedSet = s;
        }
        
        protected SynchronizedSortedSet(final ReferenceSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }
        
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.sortedSet.comparator();
            }
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return new SynchronizedSortedSet((ReferenceSortedSet<Object>)this.sortedSet.subSet(from, to), this.sync);
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return new SynchronizedSortedSet((ReferenceSortedSet<Object>)this.sortedSet.headSet(to), this.sync);
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return new SynchronizedSortedSet((ReferenceSortedSet<Object>)this.sortedSet.tailSet(from), this.sync);
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return this.sortedSet.iterator();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return this.sortedSet.iterator(from);
        }
        
        public K first() {
            synchronized (this.sync) {
                return (K)this.sortedSet.first();
            }
        }
        
        public K last() {
            synchronized (this.sync) {
                return (K)this.sortedSet.last();
            }
        }
    }
    
    public static class UnmodifiableSortedSet<K> extends ReferenceSets.UnmodifiableSet<K> implements ReferenceSortedSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final ReferenceSortedSet<K> sortedSet;
        
        protected UnmodifiableSortedSet(final ReferenceSortedSet<K> s) {
            super(s);
            this.sortedSet = s;
        }
        
        public Comparator<? super K> comparator() {
            return this.sortedSet.comparator();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return new UnmodifiableSortedSet((ReferenceSortedSet<Object>)this.sortedSet.subSet(from, to));
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return new UnmodifiableSortedSet((ReferenceSortedSet<Object>)this.sortedSet.headSet(to));
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return new UnmodifiableSortedSet((ReferenceSortedSet<Object>)this.sortedSet.tailSet(from));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return ObjectIterators.<K>unmodifiable(this.sortedSet.iterator());
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return ObjectIterators.<K>unmodifiable(this.sortedSet.iterator(from));
        }
        
        public K first() {
            return (K)this.sortedSet.first();
        }
        
        public K last() {
            return (K)this.sortedSet.last();
        }
    }
}
