package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;

public final class Reference2BooleanFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Reference2BooleanFunctions() {
    }
    
    public static <K> Reference2BooleanFunction<K> singleton(final K key, final boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanFunction<K> singleton(final K key, final Boolean value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Reference2BooleanFunction<K> synchronize(final Reference2BooleanFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Reference2BooleanFunction<K> synchronize(final Reference2BooleanFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Reference2BooleanFunction<K> unmodifiable(final Reference2BooleanFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Reference2BooleanFunction<K> primitive(final Function<? super K, ? extends Boolean> f) {
        Objects.requireNonNull(f);
        if (f instanceof Reference2BooleanFunction) {
            return (Reference2BooleanFunction<K>)(Reference2BooleanFunction)f;
        }
        if (f instanceof Predicate) {
            return key -> ((Predicate)f).test(key);
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractReference2BooleanFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public boolean getBoolean(final Object k) {
            return false;
        }
        
        public boolean containsKey(final Object k) {
            return false;
        }
        
        @Override
        public boolean defaultReturnValue() {
            return false;
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Reference2BooleanFunctions.EMPTY_FUNCTION;
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
            return Reference2BooleanFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractReference2BooleanFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final boolean value;
        
        protected Singleton(final K key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return this.key == k;
        }
        
        public boolean getBoolean(final Object k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Reference2BooleanFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Reference2BooleanFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Reference2BooleanFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public boolean test(final K operand) {
            synchronized (this.sync) {
                return this.function.test(operand);
            }
        }
        
        @Deprecated
        public Boolean apply(final K key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public boolean defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final boolean defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public boolean put(final K k, final boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public boolean getBoolean(final Object k) {
            synchronized (this.sync) {
                return this.function.getBoolean(k);
            }
        }
        
        public boolean removeBoolean(final Object k) {
            synchronized (this.sync) {
                return this.function.removeBoolean(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Boolean put(final K k, final Boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Boolean get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Boolean remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractReference2BooleanFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2BooleanFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public boolean defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public boolean put(final K k, final boolean v) {
            throw new UnsupportedOperationException();
        }
        
        public boolean getBoolean(final Object k) {
            return this.function.getBoolean(k);
        }
        
        public boolean removeBoolean(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean put(final K k, final Boolean v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Boolean remove(final Object k) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractReference2BooleanFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2BooleanFunction<K> function;
        
        protected UnmodifiableFunction(final Reference2BooleanFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public boolean defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final boolean defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public boolean put(final K k, final boolean v) {
            throw new UnsupportedOperationException();
        }
        
        public boolean getBoolean(final Object k) {
            return this.function.getBoolean(k);
        }
        
        public boolean removeBoolean(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean put(final K k, final Boolean v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Boolean remove(final Object k) {
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
    
    public static class PrimitiveFunction<K> implements Reference2BooleanFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Boolean> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Boolean> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public boolean getBoolean(final Object key) {
            final Boolean v = (Boolean)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Boolean get(final Object key) {
            return (Boolean)this.function.apply(key);
        }
        
        @Deprecated
        public Boolean put(final K key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
    }
}
