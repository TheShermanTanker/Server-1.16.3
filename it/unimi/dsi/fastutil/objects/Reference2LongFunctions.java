package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.ToLongFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Reference2LongFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Reference2LongFunctions() {
    }
    
    public static <K> Reference2LongFunction<K> singleton(final K key, final long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongFunction<K> singleton(final K key, final Long value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2LongFunction<K> synchronize(final Reference2LongFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Reference2LongFunction<K> synchronize(final Reference2LongFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Reference2LongFunction<K> unmodifiable(final Reference2LongFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Reference2LongFunction<K> primitive(final Function<? super K, ? extends Long> f) {
        Objects.requireNonNull(f);
        if (f instanceof Reference2LongFunction) {
            return (Reference2LongFunction<K>)(Reference2LongFunction)f;
        }
        if (f instanceof ToLongFunction) {
            return key -> ((ToLongFunction)f).applyAsLong(key);
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractReference2LongFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public long getLong(final Object k) {
            return 0L;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public long defaultReturnValue() {
            return 0L;
        }
        
        @Override
        public void defaultReturnValue(final long defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Reference2LongFunctions.EMPTY_FUNCTION;
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
            return Reference2LongFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractReference2LongFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final long value;
        
        protected Singleton(final K key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return this.key == k;
        }
        
        public long getLong(final Object k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Reference2LongFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Reference2LongFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Reference2LongFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public long applyAsLong(final K operand) {
            synchronized (this.sync) {
                return this.function.applyAsLong(operand);
            }
        }
        
        @Deprecated
        public Long apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public long defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final long defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public long put(final K k, final long v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public long getLong(final Object k) {
            synchronized (this.sync) {
                return this.function.getLong(k);
            }
        }
        
        public long removeLong(final Object k) {
            synchronized (this.sync) {
                return this.function.removeLong(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Long put(final K k, final Long v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Long get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Long remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractReference2LongFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2LongFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2LongFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public long defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final long defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public long put(final K k, final long v) {
            throw new UnsupportedOperationException();
        }
        
        public long getLong(final Object k) {
            return this.function.getLong(k);
        }
        
        public long removeLong(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Long put(final K k, final Long v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Long get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Long remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Reference2LongFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Long> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Long> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public long getLong(final Object key) {
            final Long v = (Long)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Long get(final Object key) {
            return (Long)this.function.apply(key);
        }
        
        @Deprecated
        public Long put(final K key, final Long value) {
            throw new UnsupportedOperationException();
        }
    }
}
