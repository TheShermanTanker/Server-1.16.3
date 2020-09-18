package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.Serializable;

public final class FloatSets {
    public static final EmptySet EMPTY_SET;
    
    private FloatSets() {
    }
    
    public static FloatSet singleton(final float element) {
        return new Singleton(element);
    }
    
    public static FloatSet singleton(final Float element) {
        return new Singleton(element);
    }
    
    public static FloatSet synchronize(final FloatSet s) {
        return new SynchronizedSet(s);
    }
    
    public static FloatSet synchronize(final FloatSet s, final Object sync) {
        return new SynchronizedSet(s, sync);
    }
    
    public static FloatSet unmodifiable(final FloatSet s) {
        return new UnmodifiableSet(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet extends FloatCollections.EmptyCollection implements FloatSet, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        @Override
        public boolean remove(final float ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return FloatSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        @Deprecated
        @Override
        public boolean rem(final float k) {
            return super.rem(k);
        }
        
        private Object readResolve() {
            return FloatSets.EMPTY_SET;
        }
    }
    
    public static class Singleton extends AbstractFloatSet implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final float element;
        
        protected Singleton(final float element) {
            this.element = element;
        }
        
        public boolean contains(final float k) {
            return Float.floatToIntBits(k) == Float.floatToIntBits(this.element);
        }
        
        @Override
        public boolean remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public FloatListIterator iterator() {
            return FloatIterators.singleton(this.element);
        }
        
        public int size() {
            return 1;
        }
        
        public boolean addAll(final Collection<? extends Float> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean addAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final FloatCollection c) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet extends FloatCollections.SynchronizedCollection implements FloatSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final FloatSet s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final FloatSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final float k) {
            synchronized (this.sync) {
                return this.collection.rem(k);
            }
        }
        
        @Deprecated
        @Override
        public boolean rem(final float k) {
            return super.rem(k);
        }
    }
    
    public static class UnmodifiableSet extends FloatCollections.UnmodifiableCollection implements FloatSet, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final FloatSet s) {
            super(s);
        }
        
        @Override
        public boolean remove(final float k) {
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
        public boolean rem(final float k) {
            return super.rem(k);
        }
    }
}
