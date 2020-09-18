package it.unimi.dsi.fastutil.shorts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.IntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Short2ObjectFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Short2ObjectFunctions() {
    }
    
    public static <V> Short2ObjectFunction<V> singleton(final short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ObjectFunction<V> singleton(final Short key, final V value) {
        return new Singleton<V>(key, value);
    }
    
    public static <V> Short2ObjectFunction<V> synchronize(final Short2ObjectFunction<V> f) {
        return new SynchronizedFunction<V>(f);
    }
    
    public static <V> Short2ObjectFunction<V> synchronize(final Short2ObjectFunction<V> f, final Object sync) {
        return new SynchronizedFunction<V>(f, sync);
    }
    
    public static <V> Short2ObjectFunction<V> unmodifiable(final Short2ObjectFunction<V> f) {
        return new UnmodifiableFunction<V>(f);
    }
    
    public static <V> Short2ObjectFunction<V> primitive(final Function<? super Short, ? extends V> f) {
        Objects.requireNonNull(f);
        if (f instanceof Short2ObjectFunction) {
            return (Short2ObjectFunction<V>)(Short2ObjectFunction)f;
        }
        if (f instanceof IntFunction) {
            final IntFunction intFunction = (IntFunction)f;
            Objects.requireNonNull(intFunction);
            return (Short2ObjectFunction<V>)intFunction::apply;
        }
        return new PrimitiveFunction<V>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<V> extends AbstractShort2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public V get(final short k) {
            return null;
        }
        
        public boolean containsKey(final short k) {
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
            return Short2ObjectFunctions.EMPTY_FUNCTION;
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
            return Short2ObjectFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<V> extends AbstractShort2ObjectFunction<V> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final short key;
        protected final V value;
        
        protected Singleton(final short key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final short k) {
            return this.key == k;
        }
        
        public V get(final short k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<V> implements Short2ObjectFunction<V>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ObjectFunction<V> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Short2ObjectFunction<V> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Short2ObjectFunction<V> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Deprecated
        public V apply(final int operand) {
            synchronized (this.sync) {
                return this.function.apply(operand);
            }
        }
        
        @Deprecated
        public V apply(final Short key) {
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
        
        public boolean containsKey(final short k) {
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
        
        public V put(final short k, final V v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public V get(final short k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public V remove(final short k) {
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
        public V put(final Short k, final V v) {
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
    
    public static class UnmodifiableFunction<V> extends AbstractShort2ObjectFunction<V> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ObjectFunction<V> function;
        
        protected UnmodifiableFunction(final Short2ObjectFunction<V> f) {
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
        
        public boolean containsKey(final short k) {
            return this.function.containsKey(k);
        }
        
        public V put(final short k, final V v) {
            throw new UnsupportedOperationException();
        }
        
        public V get(final short k) {
            return this.function.get(k);
        }
        
        public V remove(final short k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public V put(final Short k, final V v) {
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
    
    public static class PrimitiveFunction<V> implements Short2ObjectFunction<V> {
        protected final java.util.function.Function<? super Short, ? extends V> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Short, ? extends V> function) {
            this.function = function;
        }
        
        public boolean containsKey(final short key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public V get(final short key) {
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
        public V put(final Short key, final V value) {
            throw new UnsupportedOperationException();
        }
    }
}
