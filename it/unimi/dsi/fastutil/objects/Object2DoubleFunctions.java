package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.ToDoubleFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Object2DoubleFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2DoubleFunctions() {
    }
    
    public static <K> Object2DoubleFunction<K> singleton(final K key, final double value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2DoubleFunction<K> singleton(final K key, final Double value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2DoubleFunction<K> synchronize(final Object2DoubleFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2DoubleFunction<K> synchronize(final Object2DoubleFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2DoubleFunction<K> unmodifiable(final Object2DoubleFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Object2DoubleFunction<K> primitive(final Function<? super K, ? extends Double> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2DoubleFunction) {
            return (Object2DoubleFunction<K>)(Object2DoubleFunction)f;
        }
        if (f instanceof ToDoubleFunction) {
            return key -> ((ToDoubleFunction)f).applyAsDouble(key);
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2DoubleFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public double getDouble(final Object k) {
            return 0.0;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public double defaultReturnValue() {
            return 0.0;
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Object2DoubleFunctions.EMPTY_FUNCTION;
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
            return Object2DoubleFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2DoubleFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final double value;
        
        protected Singleton(final K key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        public double getDouble(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Object2DoubleFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2DoubleFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2DoubleFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2DoubleFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public double applyAsDouble(final K operand) {
            synchronized (this.sync) {
                return this.function.applyAsDouble(operand);
            }
        }
        
        @Deprecated
        public Double apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public double defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final double defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public double put(final K k, final double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public double getDouble(final Object k) {
            synchronized (this.sync) {
                return this.function.getDouble(k);
            }
        }
        
        public double removeDouble(final Object k) {
            synchronized (this.sync) {
                return this.function.removeDouble(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Double put(final K k, final Double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Double get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Double remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractObject2DoubleFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2DoubleFunction<K> function;
        
        protected UnmodifiableFunction(final Object2DoubleFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public double defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final double defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public double put(final K k, final double v) {
            throw new UnsupportedOperationException();
        }
        
        public double getDouble(final Object k) {
            return this.function.getDouble(k);
        }
        
        public double removeDouble(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Double put(final K k, final Double v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Double get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Double remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Object2DoubleFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Double> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Double> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public double getDouble(final Object key) {
            final Double v = (Double)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Double get(final Object key) {
            return (Double)this.function.apply(key);
        }
        
        @Deprecated
        public Double put(final K key, final Double value) {
            throw new UnsupportedOperationException();
        }
    }
}
