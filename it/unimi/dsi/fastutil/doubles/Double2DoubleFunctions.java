package it.unimi.dsi.fastutil.doubles;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.DoubleUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Double2DoubleFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Double2DoubleFunctions() {
    }
    
    public static Double2DoubleFunction singleton(final double key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleFunction singleton(final Double key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Double2DoubleFunction synchronize(final Double2DoubleFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Double2DoubleFunction synchronize(final Double2DoubleFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Double2DoubleFunction unmodifiable(final Double2DoubleFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Double2DoubleFunction primitive(final Function<? super Double, ? extends Double> f) {
        Objects.requireNonNull(f);
        if (f instanceof Double2DoubleFunction) {
            return (Double2DoubleFunction)f;
        }
        if (f instanceof DoubleUnaryOperator) {
            final DoubleUnaryOperator doubleUnaryOperator = (DoubleUnaryOperator)f;
            Objects.requireNonNull(doubleUnaryOperator);
            return doubleUnaryOperator::applyAsDouble;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractDouble2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public double get(final double k) {
            return 0.0;
        }
        
        public boolean containsKey(final double k) {
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
            return Double2DoubleFunctions.EMPTY_FUNCTION;
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
            return Double2DoubleFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractDouble2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double key;
        protected final double value;
        
        protected Singleton(final double key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
        }
        
        public double get(final double k) {
            return (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k)) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Double2DoubleFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Double2DoubleFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Double2DoubleFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public double applyAsDouble(final double operand) {
            synchronized (this.sync) {
                return this.function.applyAsDouble(operand);
            }
        }
        
        @Deprecated
        public Double apply(final Double key) {
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
        
        public boolean containsKey(final double k) {
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
        
        public double put(final double k, final double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public double get(final double k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public double remove(final double k) {
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
        public Double put(final Double k, final Double v) {
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
    
    public static class UnmodifiableFunction extends AbstractDouble2DoubleFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2DoubleFunction function;
        
        protected UnmodifiableFunction(final Double2DoubleFunction f) {
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
        
        public boolean containsKey(final double k) {
            return this.function.containsKey(k);
        }
        
        public double put(final double k, final double v) {
            throw new UnsupportedOperationException();
        }
        
        public double get(final double k) {
            return this.function.get(k);
        }
        
        public double remove(final double k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Double put(final Double k, final Double v) {
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
    
    public static class PrimitiveFunction implements Double2DoubleFunction {
        protected final java.util.function.Function<? super Double, ? extends Double> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Double, ? extends Double> function) {
            this.function = function;
        }
        
        public boolean containsKey(final double key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public double get(final double key) {
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
        public Double put(final Double key, final Double value) {
            throw new UnsupportedOperationException();
        }
    }
}
