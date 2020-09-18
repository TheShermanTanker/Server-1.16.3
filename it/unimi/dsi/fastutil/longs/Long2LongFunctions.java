package it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.LongUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Long2LongFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2LongFunctions() {
    }
    
    public static Long2LongFunction singleton(final long key, final long value) {
        return new Singleton(key, value);
    }
    
    public static Long2LongFunction singleton(final Long key, final Long value) {
        return new Singleton(key, value);
    }
    
    public static Long2LongFunction synchronize(final Long2LongFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2LongFunction synchronize(final Long2LongFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2LongFunction unmodifiable(final Long2LongFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Long2LongFunction primitive(final Function<? super Long, ? extends Long> f) {
        Objects.requireNonNull(f);
        if (f instanceof Long2LongFunction) {
            return (Long2LongFunction)f;
        }
        if (f instanceof LongUnaryOperator) {
            final LongUnaryOperator longUnaryOperator = (LongUnaryOperator)f;
            Objects.requireNonNull(longUnaryOperator);
            return longUnaryOperator::applyAsLong;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2LongFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public long get(final long k) {
            return 0L;
        }
        
        public boolean containsKey(final long k) {
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
            return Long2LongFunctions.EMPTY_FUNCTION;
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
            return Long2LongFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2LongFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final long value;
        
        protected Singleton(final long key, final long value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        public long get(final long k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Long2LongFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2LongFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2LongFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2LongFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public long applyAsLong(final long operand) {
            synchronized (this.sync) {
                return this.function.applyAsLong(operand);
            }
        }
        
        @Deprecated
        public Long apply(final Long key) {
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
        
        public long put(final long k, final long v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public long get(final long k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public long remove(final long k) {
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
        public Long put(final Long k, final Long v) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2LongFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2LongFunction function;
        
        protected UnmodifiableFunction(final Long2LongFunction f) {
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
        
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        public long put(final long k, final long v) {
            throw new UnsupportedOperationException();
        }
        
        public long get(final long k) {
            return this.function.get(k);
        }
        
        public long remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Long put(final Long k, final Long v) {
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
    
    public static class PrimitiveFunction implements Long2LongFunction {
        protected final java.util.function.Function<? super Long, ? extends Long> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Long, ? extends Long> function) {
            this.function = function;
        }
        
        public boolean containsKey(final long key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public long get(final long key) {
            final Long v = (Long)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Long get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Long)this.function.apply(key);
        }
        
        @Deprecated
        public Long put(final Long key, final Long value) {
            throw new UnsupportedOperationException();
        }
    }
}
