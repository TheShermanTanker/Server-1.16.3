package it.unimi.dsi.fastutil.ints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Int2ObjectFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2ObjectFunctions() {
    }
    
    public static <V> Int2ObjectFunction<V> singleton(final int key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectFunction<V> singleton(final Integer key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Int2ObjectFunction<V> synchronize(final Int2ObjectFunction<V> f) {
        return new SynchronizedFunction<V>(f);
    }
    
    public static <V> Int2ObjectFunction<V> synchronize(final Int2ObjectFunction<V> f, final Object sync) {
        return new SynchronizedFunction<V>(f, sync);
    }
    
    public static <V> Int2ObjectFunction<V> unmodifiable(final Int2ObjectFunction<V> f) {
        return new UnmodifiableFunction<V>(f);
    }
    
    public static <V> Int2ObjectFunction<V> primitive(final Function<? super Integer, ? extends V> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2ObjectFunction) {
            return (Int2ObjectFunction<V>)(Int2ObjectFunction)f;
        }
        if (f instanceof IntFunction) {
            final IntFunction intFunction = (IntFunction)f;
            Objects.requireNonNull(intFunction);
            return (Int2ObjectFunction<V>)intFunction::apply;
        }
        return new PrimitiveFunction<V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public V get(final int k) {
            return null;
        }
        
        public boolean containsKey(final int k) {
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
            return Int2ObjectFunctions.EMPTY_FUNCTION;
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
            return Int2ObjectFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<V> extends AbstractInt2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final V value;
        
        protected Singleton(final int key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        public V get(final int k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<V> implements Int2ObjectFunction<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Int2ObjectFunction<V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Int2ObjectFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public V apply(final int operand) {
            synchronized (this.sync) {
                return this.function.apply(operand);
            }
        }
        
        @Deprecated
        public V apply(final Integer key) {
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
        
        public boolean containsKey(final int k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        @Deprecated
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public V put(final int k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public V get(final int k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public V remove(final int k) {
            synchronized (this.sync) {
                return this.function.remove(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public V put(final Integer k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public V get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public V remove(final Object k) {
            synchronized (this.sync) {
                return this.function.remove(k);
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
    
    public static class UnmodifiableFunction<V> extends AbstractInt2ObjectFunction<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2ObjectFunction<V> function;
        
        protected UnmodifiableFunction(final Int2ObjectFunction<V> f) {
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
        
        public boolean containsKey(final int k) {
            return this.function.containsKey(k);
        }
        
        public V put(final int k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        public V get(final int k) {
            return this.function.get(k);
        }
        
        public V remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public V put(final Integer k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public V get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public V remove(final Object k) {
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
    
    public static class PrimitiveFunction<V> implements Int2ObjectFunction<V> {
        protected final java.util.function.Function<? super Integer, ? extends V> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Integer, ? extends V> function) {
            this.function = function;
        }
        
        public boolean containsKey(final int key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public V get(final int key) {
            final V v = (V)this.function.apply(key);
            if (v == null) {
                return null;
            }
            return v;
        }
        
        @Deprecated
        public V get(final Object key) {
            if (key == null) {
                return null;
            }
            return (V)this.function.apply(key);
        }
        
        @Deprecated
        public V put(final Integer key, final V value) {
            throw new UnsupportedOperationException();
        }
    }
}
