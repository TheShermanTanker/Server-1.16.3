package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.ToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Reference2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Reference2IntFunctions() {
    }
    
    public static <K> Reference2IntFunction<K> singleton(final K key, final int value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2IntFunction<K> singleton(final K key, final Integer value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2IntFunction<K> synchronize(final Reference2IntFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Reference2IntFunction<K> synchronize(final Reference2IntFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Reference2IntFunction<K> unmodifiable(final Reference2IntFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Reference2IntFunction<K> primitive(final Function<? super K, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Reference2IntFunction) {
            return (Reference2IntFunction<K>)(Reference2IntFunction)f;
        }
        if (f instanceof ToIntFunction) {
            return key -> ((ToIntFunction)f).applyAsInt(key);
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractReference2IntFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public int getInt(final Object k) {
            return 0;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public int defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Reference2IntFunctions.EMPTY_FUNCTION;
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
            return Reference2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractReference2IntFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final int value;
        
        protected Singleton(final K key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return this.key == k;
        }
        
        public int getInt(final Object k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Reference2IntFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2IntFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Reference2IntFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Reference2IntFunction<K> f) {
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
        public Integer apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public int defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final int defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public int put(final K k, final int v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public int getInt(final Object k) {
            synchronized (this.sync) {
                return this.function.getInt(k);
            }
        }
        
        public int removeInt(final Object k) {
            synchronized (this.sync) {
                return this.function.removeInt(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Integer put(final K k, final Integer v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Integer get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Integer remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractReference2IntFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2IntFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2IntFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public int defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final int defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public int put(final K k, final int v) {
            throw new UnsupportedOperationException();
        }
        
        public int getInt(final Object k) {
            return this.function.getInt(k);
        }
        
        public int removeInt(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Integer put(final K k, final Integer v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Integer get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Integer remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Reference2IntFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Integer> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Integer> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public int getInt(final Object key) {
            final Integer v = (Integer)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Integer get(final Object key) {
            return (Integer)this.function.apply(key);
        }
        
        @Deprecated
        public Integer put(final K key, final Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
