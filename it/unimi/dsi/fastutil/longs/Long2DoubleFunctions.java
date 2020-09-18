package it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.LongToDoubleFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Long2DoubleFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2DoubleFunctions() {
    }
    
    public static Long2DoubleFunction singleton(final long key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Long2DoubleFunction singleton(final Long key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Long2DoubleFunction synchronize(final Long2DoubleFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2DoubleFunction synchronize(final Long2DoubleFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2DoubleFunction unmodifiable(final Long2DoubleFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Long2DoubleFunction primitive(final Function<? super Long, ? extends Double> f) {
        Objects.requireNonNull(f);
        if (f instanceof Long2DoubleFunction) {
            return (Long2DoubleFunction)f;
        }
        if (f instanceof LongToDoubleFunction) {
            final LongToDoubleFunction longToDoubleFunction = (LongToDoubleFunction)f;
            Objects.requireNonNull(longToDoubleFunction);
            return longToDoubleFunction::applyAsDouble;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public double get(final long k) {
            return 0.0;
        }
        
        public boolean containsKey(final long k) {
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
            return Long2DoubleFunctions.EMPTY_FUNCTION;
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
            return Long2DoubleFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final double value;
        
        protected Singleton(final long key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        public double get(final long k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Long2DoubleFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2DoubleFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2DoubleFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2DoubleFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public double applyAsDouble(final long operand) {
            synchronized (this.sync) {
                return this.function.applyAsDouble(operand);
            }
        }
        
        @Deprecated
        public Double apply(final Long key) {
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
        
        public double put(final long k, final double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public double get(final long k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public double remove(final long k) {
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
        public Double put(final Long k, final Double v) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2DoubleFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2DoubleFunction function;
        
        protected UnmodifiableFunction(final Long2DoubleFunction f) {
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
        
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        public double put(final long k, final double v) {
            throw new UnsupportedOperationException();
        }
        
        public double get(final long k) {
            return this.function.get(k);
        }
        
        public double remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Double put(final Long k, final Double v) {
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
    
    public static class PrimitiveFunction implements Long2DoubleFunction {
        protected final java.util.function.Function<? super Long, ? extends Double> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Long, ? extends Double> function) {
            this.function = function;
        }
        
        public boolean containsKey(final long key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public double get(final long key) {
            final Double v = (Double)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Double get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Double)this.function.apply(key);
        }
        
        @Deprecated
        public Double put(final Long key, final Double value) {
            throw new UnsupportedOperationException();
        }
    }
}
