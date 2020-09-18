package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.ToDoubleFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Object2FloatFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2FloatFunctions() {
    }
    
    public static <K> Object2FloatFunction<K> singleton(final K key, final float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2FloatFunction<K> singleton(final K key, final Float value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2FloatFunction<K> synchronize(final Object2FloatFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2FloatFunction<K> synchronize(final Object2FloatFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2FloatFunction<K> unmodifiable(final Object2FloatFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Object2FloatFunction<K> primitive(final Function<? super K, ? extends Float> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2FloatFunction) {
            return (Object2FloatFunction<K>)(Object2FloatFunction)f;
        }
        if (f instanceof ToDoubleFunction) {
            return key -> SafeMath.safeDoubleToFloat(((ToDoubleFunction)f).applyAsDouble(key));
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2FloatFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public float getFloat(final Object k) {
            return 0.0f;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public float defaultReturnValue() {
            return 0.0f;
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Object2FloatFunctions.EMPTY_FUNCTION;
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
            return Object2FloatFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2FloatFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final float value;
        
        protected Singleton(final K key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        public float getFloat(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Object2FloatFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2FloatFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2FloatFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2FloatFunction<K> f) {
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
        public Float apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public float defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final float defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public float put(final K k, final float v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public float getFloat(final Object k) {
            synchronized (this.sync) {
                return this.function.getFloat(k);
            }
        }
        
        public float removeFloat(final Object k) {
            synchronized (this.sync) {
                return this.function.removeFloat(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Float put(final K k, final Float v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Float get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Float remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractObject2FloatFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2FloatFunction<K> function;
        
        protected UnmodifiableFunction(final Object2FloatFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public float defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public float put(final K k, final float v) {
            throw new UnsupportedOperationException();
        }
        
        public float getFloat(final Object k) {
            return this.function.getFloat(k);
        }
        
        public float removeFloat(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Float put(final K k, final Float v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Float get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Float remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Object2FloatFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Float> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Float> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public float getFloat(final Object key) {
            final Float v = (Float)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Float get(final Object key) {
            return (Float)this.function.apply(key);
        }
        
        @Deprecated
        public Float put(final K key, final Float value) {
            throw new UnsupportedOperationException();
        }
    }
}
