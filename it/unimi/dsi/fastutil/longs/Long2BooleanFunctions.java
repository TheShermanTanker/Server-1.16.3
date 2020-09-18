package it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.LongPredicate;
import java.util.Objects;
import java.util.function.Function;

public final class Long2BooleanFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2BooleanFunctions() {
    }
    
    public static Long2BooleanFunction singleton(final long key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Long2BooleanFunction singleton(final Long key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Long2BooleanFunction synchronize(final Long2BooleanFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2BooleanFunction synchronize(final Long2BooleanFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2BooleanFunction unmodifiable(final Long2BooleanFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Long2BooleanFunction primitive(final Function<? super Long, ? extends Boolean> f) {
        Objects.requireNonNull(f);
        if (f instanceof Long2BooleanFunction) {
            return (Long2BooleanFunction)f;
        }
        if (f instanceof LongPredicate) {
            final LongPredicate longPredicate = (LongPredicate)f;
            Objects.requireNonNull(longPredicate);
            return longPredicate::test;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public boolean get(final long k) {
            return false;
        }
        
        public boolean containsKey(final long k) {
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
            return Long2BooleanFunctions.EMPTY_FUNCTION;
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
            return Long2BooleanFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final boolean value;
        
        protected Singleton(final long key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        public boolean get(final long k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Long2BooleanFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2BooleanFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2BooleanFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2BooleanFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public boolean test(final long operand) {
            synchronized (this.sync) {
                return this.function.test(operand);
            }
        }
        
        @Deprecated
        public Boolean apply(final Long key) {
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
        
        public boolean containsKey(final long k) {
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
        
        public boolean put(final long k, final boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public boolean get(final long k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public boolean remove(final long k) {
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
        public Boolean put(final Long k, final Boolean v) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2BooleanFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2BooleanFunction function;
        
        protected UnmodifiableFunction(final Long2BooleanFunction f) {
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
        
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        public boolean put(final long k, final boolean v) {
            throw new UnsupportedOperationException();
        }
        
        public boolean get(final long k) {
            return this.function.get(k);
        }
        
        public boolean remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean put(final Long k, final Boolean v) {
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
    
    public static class PrimitiveFunction implements Long2BooleanFunction {
        protected final java.util.function.Function<? super Long, ? extends Boolean> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Long, ? extends Boolean> function) {
            this.function = function;
        }
        
        public boolean containsKey(final long key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public boolean get(final long key) {
            final Boolean v = (Boolean)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Boolean get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Boolean)this.function.apply(key);
        }
        
        @Deprecated
        public Boolean put(final Long key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
    }
}
