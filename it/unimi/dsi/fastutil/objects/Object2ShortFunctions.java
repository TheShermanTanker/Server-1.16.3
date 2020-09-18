package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.ToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Object2ShortFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ShortFunctions() {
    }
    
    public static <K> Object2ShortFunction<K> singleton(final K key, final short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortFunction<K> singleton(final K key, final Short value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ShortFunction<K> synchronize(final Object2ShortFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2ShortFunction<K> synchronize(final Object2ShortFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2ShortFunction<K> unmodifiable(final Object2ShortFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Object2ShortFunction<K> primitive(final Function<? super K, ? extends Short> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2ShortFunction) {
            return (Object2ShortFunction<K>)(Object2ShortFunction)f;
        }
        if (f instanceof ToIntFunction) {
            return key -> SafeMath.safeIntToShort(((ToIntFunction)f).applyAsInt(key));
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2ShortFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public short getShort(final Object k) {
            return 0;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public short defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Object2ShortFunctions.EMPTY_FUNCTION;
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
            return Object2ShortFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2ShortFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final short value;
        
        protected Singleton(final K key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        public short getShort(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Object2ShortFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2ShortFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2ShortFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final K operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Short apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public short defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final short defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public short put(final K k, final short v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public short getShort(final Object k) {
            synchronized (this.sync) {
                return this.function.getShort(k);
            }
        }
        
        public short removeShort(final Object k) {
            synchronized (this.sync) {
                return this.function.removeShort(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Short put(final K k, final Short v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Short get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Short remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractObject2ShortFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ShortFunction<K> function;
        
        protected UnmodifiableFunction(final Object2ShortFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public short defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public short put(final K k, final short v) {
            throw new UnsupportedOperationException();
        }
        
        public short getShort(final Object k) {
            return this.function.getShort(k);
        }
        
        public short removeShort(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Short put(final K k, final Short v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Short get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Short remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Object2ShortFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Short> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Short> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public short getShort(final Object key) {
            final Short v = (Short)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Short get(final Object key) {
            return (Short)this.function.apply(key);
        }
        
        @Deprecated
        public Short put(final K key, final Short value) {
            throw new UnsupportedOperationException();
        }
    }
}
