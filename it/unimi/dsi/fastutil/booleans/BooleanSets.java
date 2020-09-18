package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public final class BooleanSets {
    public static final EmptySet EMPTY_SET;
    
    private BooleanSets() {
    }
    
    public static BooleanSet singleton(final boolean element) {
        return new Singleton(element);
    }
    
    public static BooleanSet singleton(final Boolean element) {
        return new Singleton(element);
    }
    
    public static BooleanSet synchronize(final BooleanSet s) {
        return new SynchronizedSet(s);
    }
    
    public static BooleanSet synchronize(final BooleanSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static BooleanSet unmodifiable(final BooleanSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends BooleanCollections.EmptyCollection implements BooleanSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final boolean ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return BooleanSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        @Deprecated
        @Override
        public boolean rem(final boolean k) {
            return super.rem(k);
        }
        
        private Object readResolve() {
            return BooleanSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractBooleanSet implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final boolean element;
        
        protected Singleton(final boolean element) {
            this.element = element;
        }
        
        public boolean contains(final boolean k) {
            return k == this.element;
        }
        
        @Override
        public boolean remove(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public BooleanListIterator iterator() {
            return BooleanIterators.singleton(this.element);
        }
        
        public int size() {
            return 1;
        }
        
        public boolean addAll(final Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean addAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final BooleanCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends BooleanCollections.SynchronizedCollection implements BooleanSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final BooleanSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final BooleanSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final boolean k) {
            synchronized (this.sync) {
                return this.collection.rem(k);
            }
        }
        
        @Deprecated
        @Override
        public boolean rem(final boolean k) {
            return super.rem(k);
        }
    }
    
    public static class UnmodifiableSet extends BooleanCollections.UnmodifiableCollection implements BooleanSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final BooleanSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final boolean k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || this.collection.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }
        
        @Deprecated
        @Override
        public boolean rem(final boolean k) {
            return super.rem(k);
        }
    }
}
