package it.unimi.dsi.fastutil.shorts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Short2ShortFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Short2ShortFunctions() {
    }
    
    public static Short2ShortFunction singleton(final short key, final short value) {
        return new Singleton(key, value);
    }
    
    public static Short2ShortFunction singleton(final Short key, final Short value) {
        return new Singleton(key, value);
    }
    
    public static Short2ShortFunction synchronize(final Short2ShortFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Short2ShortFunction synchronize(final Short2ShortFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Short2ShortFunction unmodifiable(final Short2ShortFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Short2ShortFunction primitive(final Function<? super Short, ? extends Short> f) {
        Objects.requireNonNull(f);
        if (f instanceof Short2ShortFunction) {
            return (Short2ShortFunction)f;
        }
        if (f instanceof IntUnaryOperator) {
            return key -> SafeMath.safeIntToShort(((IntUnaryOperator)f).applyAsInt((int)key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractShort2ShortFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public short get(final short k) {
            return 0;
        }
        
        public boolean containsKey(final short k) {
            return false;
        }
        
        @Override
        public short defaultReturnValue() {
            return 0;
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Short2ShortFunctions.EMPTY_FUNCTION;
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
            return Short2ShortFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractShort2ShortFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final short key;
        protected final short value;
        
        protected Singleton(final short key, final short value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final short k) {
            return this.key == k;
        }
        
        public short get(final short k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Short2ShortFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ShortFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Short2ShortFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Short2ShortFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Deprecated
        public int applyAsInt(final int operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Short apply(final Short key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public short defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final short defRetValue) {
            synchronized (this.sync) {
                this.function.defaultReturnValue(defRetValue);
            }
        }
        
        public boolean containsKey(final short k) {
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
        
        public short put(final short k, final short v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public short get(final short k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public short remove(final short k) {
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
        public Short put(final Short k, final Short v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Short get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Short remove(final Object k) {
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
    
    public static class UnmodifiableFunction extends AbstractShort2ShortFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ShortFunction function;
        
        protected UnmodifiableFunction(final Short2ShortFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public short defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final short defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final short k) {
            return this.function.containsKey(k);
        }
        
        public short put(final short k, final short v) {
            throw new UnsupportedOperationException();
        }
        
        public short get(final short k) {
            return this.function.get(k);
        }
        
        public short remove(final short k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Short put(final Short k, final Short v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Short get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Short remove(final Object k) {
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
    
    public static class PrimitiveFunction implements Short2ShortFunction {
        protected final java.util.function.Function<? super Short, ? extends Short> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Short, ? extends Short> function) {
            this.function = function;
        }
        
        public boolean containsKey(final short key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public short get(final short key) {
            final Short v = (Short)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Short get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Short)this.function.apply(key);
        }
        
        @Deprecated
        public Short put(final Short key, final Short value) {
            throw new UnsupportedOperationException();
        }
    }
}
