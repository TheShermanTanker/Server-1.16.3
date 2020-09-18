package it.unimi.dsi.fastutil.bytes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntToDoubleFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Byte2FloatFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Byte2FloatFunctions() {
    }
    
    public static Byte2FloatFunction singleton(final byte key, final float value) {
        return new Singleton(key, value);
    }
    
    public static Byte2FloatFunction singleton(final Byte key, final Float value) {
        return new Singleton(key, value);
    }
    
    public static Byte2FloatFunction synchronize(final Byte2FloatFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Byte2FloatFunction synchronize(final Byte2FloatFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Byte2FloatFunction unmodifiable(final Byte2FloatFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Byte2FloatFunction primitive(final Function<? super Byte, ? extends Float> f) {
        Objects.requireNonNull(f);
        if (f instanceof Byte2FloatFunction) {
            return (Byte2FloatFunction)f;
        }
        if (f instanceof IntToDoubleFunction) {
            return key -> SafeMath.safeDoubleToFloat(((IntToDoubleFunction)f).applyAsDouble((int)key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractByte2FloatFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public float get(final byte k) {
            return 0.0f;
        }
        
        public boolean containsKey(final byte k) {
            return false;
        }
        
        @Override
        public float defaultReturnValue() {
            return 0.0f;
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Byte2FloatFunctions.EMPTY_FUNCTION;
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
            return Byte2FloatFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractByte2FloatFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final byte key;
        protected final float value;
        
        protected Singleton(final byte key, final float value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final byte k) {
            return this.key == k;
        }
        
        public float get(final byte k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Byte2FloatFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2FloatFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Byte2FloatFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Byte2FloatFunction f) {
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
        public Float apply(final Byte key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public float defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final float defRetValue) {
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
        
        public float put(final byte k, final float v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public float get(final byte k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public float remove(final byte k) {
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
        public Float put(final Byte k, final Float v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Float get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Float remove(final Object k) {
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
    
    public static class UnmodifiableFunction extends AbstractByte2FloatFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Byte2FloatFunction function;
        
        protected UnmodifiableFunction(final Byte2FloatFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public float defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final float defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final byte k) {
            return this.function.containsKey(k);
        }
        
        public float put(final byte k, final float v) {
            throw new UnsupportedOperationException();
        }
        
        public float get(final byte k) {
            return this.function.get(k);
        }
        
        public float remove(final byte k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Float put(final Byte k, final Float v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Float get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Float remove(final Object k) {
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
    
    public static class PrimitiveFunction implements Byte2FloatFunction {
        protected final java.util.function.Function<? super Byte, ? extends Float> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Byte, ? extends Float> function) {
            this.function = function;
        }
        
        public boolean containsKey(final byte key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public float get(final byte key) {
            final Float v = (Float)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Float get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Float)this.function.apply(key);
        }
        
        @Deprecated
        public Float put(final Byte key, final Float value) {
            throw new UnsupportedOperationException();
        }
    }
}
