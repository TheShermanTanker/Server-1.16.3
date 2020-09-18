package it.unimi.dsi.fastutil.floats;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Float2ByteFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Float2ByteFunctions() {
    }
    
    public static Float2ByteFunction singleton(final float key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Float2ByteFunction singleton(final Float key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Float2ByteFunction synchronize(final Float2ByteFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Float2ByteFunction synchronize(final Float2ByteFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Float2ByteFunction unmodifiable(final Float2ByteFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Float2ByteFunction primitive(final Function<? super Float, ? extends Byte> f) {
        Objects.requireNonNull(f);
        if (f instanceof Float2ByteFunction) {
            return (Float2ByteFunction)f;
        }
        if (f instanceof DoubleToIntFunction) {
            return key -> SafeMath.safeIntToByte(((DoubleToIntFunction)f).applyAsInt((double)key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractFloat2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public byte get(final float k) {
            return 0;
        }
        
        public boolean containsKey(final float k) {
            return false;
        }
        
        @Override
        public byte defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Float2ByteFunctions.EMPTY_FUNCTION;
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
            return Float2ByteFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractFloat2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final float key;
        protected final byte value;
        
        protected Singleton(final float key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final float k) {
            return Float.floatToIntBits(this.key) == Float.floatToIntBits(k);
        }
        
        public byte get(final float k) {
            return (Float.floatToIntBits(this.key) == Float.floatToIntBits(k)) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Float2ByteFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ByteFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Float2ByteFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Float2ByteFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Deprecated
        public int applyAsInt(final double operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Byte apply(final Float key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public byte defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final byte defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final float k) {
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
        
        public byte put(final float k, final byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public byte get(final float k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public byte remove(final float k) {
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
        public Byte put(final Float k, final Byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Byte get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Byte remove(final Object k) {
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
    
    public static class UnmodifiableFunction extends AbstractFloat2ByteFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ByteFunction function;
        
        protected UnmodifiableFunction(final Float2ByteFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public byte defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final byte defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final float k) {
            return this.function.containsKey(k);
        }
        
        public byte put(final float k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        public byte get(final float k) {
            return this.function.get(k);
        }
        
        public byte remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte put(final Float k, final Byte v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Byte remove(final Object k) {
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
    
    public static class PrimitiveFunction implements Float2ByteFunction {
        protected final java.util.function.Function<? super Float, ? extends Byte> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Float, ? extends Byte> function) {
            this.function = function;
        }
        
        public boolean containsKey(final float key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public byte get(final float key) {
            final Byte v = (Byte)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Byte get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Byte)this.function.apply(key);
        }
        
        @Deprecated
        public Byte put(final Float key, final Byte value) {
            throw new UnsupportedOperationException();
        }
    }
}
