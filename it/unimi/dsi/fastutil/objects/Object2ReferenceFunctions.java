package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import it.unimi.dsi.fastutil.Function;
import java.io.Serializable;

public final class Object2ReferenceFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ReferenceFunctions() {
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> singleton(final K key, final V value) {
        return new Singleton<K, V>(key, value);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> synchronize(final Object2ReferenceFunction<K, V> f) {
        return new SynchronizedFunction<K, V>(f);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> synchronize(final Object2ReferenceFunction<K, V> f, final Object sync) {
        return new SynchronizedFunction<K, V>(f, sync);
    }
    
    public static <K, V> Object2ReferenceFunction<K, V> unmodifiable(final Object2ReferenceFunction<K, V> f) {
        return new UnmodifiableFunction<K, V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public V get(final Object k) {
            return null;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public V defaultReturnValue() {
            return null;
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Object2ReferenceFunctions.EMPTY_FUNCTION;
        }
        
        public int hashCode() {
            return 0;
        }
        
        public boolean equals(final Object o) {
            return o instanceof Function && ((Function)o).size() == 0;
        }
        
        public String toString() {
            return "{}";
        }
        
        private Object readResolve() {
            return Object2ReferenceFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final V value;
        
        protected Singleton(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        public V get(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K, V> implements Object2ReferenceFunction<K, V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceFunction<K, V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2ReferenceFunction<K, V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2ReferenceFunction<K, V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public V apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public V defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final V defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public V put(final K k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public V get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public V remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        public int hashCode() {
            synchronized (this.sync) {
                return this.function.hashCode();
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.function.equals(o);
            }
        }
        
        public String toString() {
            synchronized (this.sync) {
                return this.function.toString();
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
    
    public static class UnmodifiableFunction<K, V> extends AbstractObject2ReferenceFunction<K, V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ReferenceFunction<K, V> function;
        
        protected UnmodifiableFunction(final Object2ReferenceFunction<K, V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public V defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final V defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public V put(final K k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        public V get(final Object k) {
            return this.function.get(k);
        }
        
        public V remove(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        public int hashCode() {
            return this.function.hashCode();
        }
        
        public boolean equals(final Object o) {
            return o == this || this.function.equals(o);
        }
        
        public String toString() {
            return this.function.toString();
        }
    }
}
