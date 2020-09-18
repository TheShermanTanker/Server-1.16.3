package it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.LongToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Long2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2IntFunctions() {
    }
    
    public static Long2IntFunction singleton(final long key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Long2IntFunction singleton(final Long key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Long2IntFunction synchronize(final Long2IntFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2IntFunction synchronize(final Long2IntFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2IntFunction unmodifiable(final Long2IntFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Long2IntFunction primitive(final Function<? super Long, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Long2IntFunction) {
            return (Long2IntFunction)f;
        }
        if (f instanceof LongToIntFunction) {
            final LongToIntFunction longToIntFunction = (LongToIntFunction)f;
            Objects.requireNonNull(longToIntFunction);
            return longToIntFunction::applyAsInt;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2IntFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public int get(final long k) {
            return 0;
        }
        
        public boolean containsKey(final long k) {
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
            return Long2IntFunctions.EMPTY_FUNCTION;
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
            return Long2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2IntFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final int value;
        
        protected Singleton(final long key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        public int get(final long k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Long2IntFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2IntFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2IntFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2IntFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final long operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Integer apply(final Long key) {
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
        
        public int put(final long k, final int v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public int get(final long k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public int remove(final long k) {
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
        public Integer put(final Long k, final Integer v) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2IntFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2IntFunction function;
        
        protected UnmodifiableFunction(final Long2IntFunction f) {
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
        
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        public int put(final long k, final int v) {
            throw new UnsupportedOperationException();
        }
        
        public int get(final long k) {
            return this.function.get(k);
        }
        
        public int remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Integer put(final Long k, final Integer v) {
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
    
    public static class PrimitiveFunction implements Long2IntFunction {
        protected final java.util.function.Function<? super Long, ? extends Integer> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Long, ? extends Integer> function) {
            this.function = function;
        }
        
        public boolean containsKey(final long key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public int get(final long key) {
            final Integer v = (Integer)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Integer get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Integer)this.function.apply(key);
        }
        
        @Deprecated
        public Integer put(final Long key, final Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
