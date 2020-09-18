package it.unimi.dsi.fastutil.bytes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.IntToDoubleFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Byte2DoubleFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Byte2DoubleFunctions() {
    }
    
    public static Byte2DoubleFunction singleton(final byte key, final double value) {
        return new Singleton(key, value);
    }
    
    public static Byte2DoubleFunction singleton(final Byte key, final Double value) {
        return new Singleton(key, value);
    }
    
    public static Byte2DoubleFunction synchronize(final Byte2DoubleFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Byte2DoubleFunction synchronize(final Byte2DoubleFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Byte2DoubleFunction unmodifiable(final Byte2DoubleFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Byte2DoubleFunction primitive(final Function<? super Byte, ? extends Double> f) {
        Objects.requireNonNull(f);
        if (f instanceof Byte2DoubleFunction) {
            return (Byte2DoubleFunction)f;
        }
        if (f instanceof IntToDoubleFunction) {
            final IntToDoubleFunction intToDoubleFunction = (IntToDoubleFunction)f;
            Objects.requireNonNull(intToDoubleFunction);
            return intToDoubleFunction::applyAsDouble;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractByte2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public double get(final byte k) {
            return 0.0;
        }
        
        public boolean containsKey(final byte k) {
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
            return Byte2DoubleFunctions.EMPTY_FUNCTION;
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
            return Byte2DoubleFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractByte2DoubleFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final byte key;
        protected final double value;
        
        protected Singleton(final byte key, final double value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final byte k) {
            return this.key == k;
        }
        
        public double get(final byte k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Byte2DoubleFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2DoubleFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Byte2DoubleFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Byte2DoubleFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Deprecated
        public double applyAsDouble(final int operand) {
            synchronized (this.sync) {
                return this.function.applyAsDouble(operand);
            }
        }
        
        @Deprecated
        public Double apply(final Byte key) {
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
        
        public boolean containsKey(final byte k) {
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
        
        public double put(final byte k, final double v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public double get(final byte k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public double remove(final byte k) {
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
        public Double put(final Byte k, final Double v) {
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
    
    public static class UnmodifiableFunction extends AbstractByte2DoubleFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2DoubleFunction function;
        
        protected UnmodifiableFunction(final Byte2DoubleFunction f) {
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
        
        public boolean containsKey(final byte k) {
            return this.function.containsKey(k);
        }
        
        public double put(final byte k, final double v) {
            throw new UnsupportedOperationException();
        }
        
        public double get(final byte k) {
            return this.function.get(k);
        }
        
        public double remove(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Double put(final Byte k, final Double v) {
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
    
    public static class PrimitiveFunction implements Byte2DoubleFunction {
        protected final java.util.function.Function<? super Byte, ? extends Double> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Byte, ? extends Double> function) {
            this.function = function;
        }
        
        public boolean containsKey(final byte key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public double get(final byte key) {
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
        public Double put(final Byte key, final Double value) {
            throw new UnsupportedOperationException();
        }
    }
}
