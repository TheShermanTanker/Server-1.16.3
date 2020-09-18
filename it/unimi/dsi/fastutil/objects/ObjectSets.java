package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.io.Serializable;

public final class ObjectSets {
    public static final EmptySet EMPTY_SET;
    
    private ObjectSets() {
    }
    
    public static <K> ObjectSet<K> emptySet() {
        return (ObjectSet<K>)ObjectSets.EMPTY_SET;
    }
    
    public static <K> ObjectSet<K> singleton(final K element) {
        return new Singleton<K>(element);
    }
    
    public static <K> ObjectSet<K> synchronize(final ObjectSet<K> s) {
        return new SynchronizedSet<K>(s);
    }
    
    public static <K> ObjectSet<K> synchronize(final ObjectSet<K> s, final Object sync) {
        return new SynchronizedSet<K>(s, sync);
    }
    
    public static <K> ObjectSet<K> unmodifiable(final ObjectSet<K> s) {
        return new UnmodifiableSet<K>(s);
    }
    
    static {
        EMPTY_SET = new EmptySet();
    }
    
    public static class EmptySet<K> extends ObjectCollections.EmptyCollection<K> implements ObjectSet<K>, Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptySet() {
        }
        
        public boolean remove(final Object ok) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return ObjectSets.EMPTY_SET;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }
        
        private Object readResolve() {
            return ObjectSets.EMPTY_SET;
        }
    }
    
    public static class Singleton<K> extends AbstractObjectSet<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K element;
        
        protected Singleton(final K element) {
            this.element = element;
        }
        
        public boolean contains(final Object k) {
            return Objects.equals(k, this.element);
        }
        
        public boolean remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ObjectListIterator<K> iterator() {
            return ObjectIterators.<K>singleton(this.element);
        }
        
        public int size() {
            return 1;
        }
        
        public boolean addAll(final Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedSet<K> extends ObjectCollections.SynchronizedCollection<K> implements ObjectSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected SynchronizedSet(final ObjectSet<K> s, final Object sync) {
            super(s, sync);
        }
        
        protected SynchronizedSet(final ObjectSet<K> s) {
            super(s);
        }
        
        @Override
        public boolean remove(final Object k) {
            synchronized (this.sync) {
                return this.collection.remove(k);
            }
        }
    }
    
    public static class UnmodifiableSet<K> extends ObjectCollections.UnmodifiableCollection<K> implements ObjectSet<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected UnmodifiableSet(final ObjectSet<K> s) {
            super(s);
        }
        
        @Override
        public boolean remove(final Object k) {
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
    }
}
