package it.unimi.dsi.fastutil.doubles;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.DoubleToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Double2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Double2IntFunctions() {
    }
    
    public static Double2IntFunction singleton(final double key, final int value) {
        return new Singleton(key, value);
    }
    
    public static Double2IntFunction singleton(final Double key, final Integer value) {
        return new Singleton(key, value);
    }
    
    public static Double2IntFunction synchronize(final Double2IntFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Double2IntFunction synchronize(final Double2IntFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Double2IntFunction unmodifiable(final Double2IntFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Double2IntFunction primitive(final Function<? super Double, ? extends Integer> f) {
        Objects.requireNonNull(f);
        if (f instanceof Double2IntFunction) {
            return (Double2IntFunction)f;
        }
        if (f instanceof DoubleToIntFunction) {
            final DoubleToIntFunction doubleToIntFunction = (DoubleToIntFunction)f;
            Objects.requireNonNull(doubleToIntFunction);
            return doubleToIntFunction::applyAsInt;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractDouble2IntFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public int get(final double k) {
            return 0;
        }
        
        public boolean containsKey(final double k) {
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
            return Double2IntFunctions.EMPTY_FUNCTION;
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
            return Double2IntFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractDouble2IntFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double key;
        protected final int value;
        
        protected Singleton(final double key, final int value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
        }
        
        public int get(final double k) {
            return (Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k)) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Double2IntFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2IntFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Double2IntFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Double2IntFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final double operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Integer apply(final Double key) {
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
        
        public int put(final double k, final int v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public int get(final double k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public int remove(final double k) {
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
        public Integer put(final Double k, final Integer v) {
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
    
    public static class UnmodifiableFunction extends AbstractDouble2IntFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2IntFunction function;
        
        protected UnmodifiableFunction(final Double2IntFunction f) {
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
        
        public boolean containsKey(final double k) {
            return this.function.containsKey(k);
        }
        
        public int put(final double k, final int v) {
            throw new UnsupportedOperationException();
        }
        
        public int get(final double k) {
            return this.function.get(k);
        }
        
        public int remove(final double k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Integer put(final Double k, final Integer v) {
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
    
    public static class PrimitiveFunction implements Double2IntFunction {
        protected final java.util.function.Function<? super Double, ? extends Integer> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Double, ? extends Integer> function) {
            this.function = function;
        }
        
        public boolean containsKey(final double key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public int get(final double key) {
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
        public Integer put(final Double key, final Integer value) {
            throw new UnsupportedOperationException();
        }
    }
}
