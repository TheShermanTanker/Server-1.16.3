package it.unimi.dsi.fastutil.floats;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.DoublePredicate;
import java.util.Objects;
import java.util.function.Function;

public final class Float2BooleanFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Float2BooleanFunctions() {
    }
    
    public static Float2BooleanFunction singleton(final float key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Float2BooleanFunction singleton(final Float key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Float2BooleanFunction synchronize(final Float2BooleanFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Float2BooleanFunction synchronize(final Float2BooleanFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Float2BooleanFunction unmodifiable(final Float2BooleanFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Float2BooleanFunction primitive(final Function<? super Float, ? extends Boolean> f) {
        Objects.requireNonNull(f);
        if (f instanceof Float2BooleanFunction) {
            return (Float2BooleanFunction)f;
        }
        if (f instanceof DoublePredicate) {
            final DoublePredicate doublePredicate = (DoublePredicate)f;
            Objects.requireNonNull(doublePredicate);
            return doublePredicate::test;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractFloat2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public boolean get(final float k) {
            return false;
        }
        
        public boolean containsKey(final float k) {
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
            return Float2BooleanFunctions.EMPTY_FUNCTION;
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
            return Float2BooleanFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractFloat2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final float key;
        protected final boolean value;
        
        protected Singleton(final float key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final float k) {
            return Float.floatToIntBits(this.key) == Float.floatToIntBits(k);
        }
        
        public boolean get(final float k) {
            return (Float.floatToIntBits(this.key) == Float.floatToIntBits(k)) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Float2BooleanFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2BooleanFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Float2BooleanFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Float2BooleanFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        @Deprecated
        public boolean test(final double operand) {
            synchronized (this.sync) {
                return this.function.test(operand);
            }
        }
        
        @Deprecated
        public Boolean apply(final Float key) {
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
        
        public boolean put(final float k, final boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public boolean get(final float k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public boolean remove(final float k) {
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
        public Boolean put(final Float k, final Boolean v) {
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
    
    public static class UnmodifiableFunction extends AbstractFloat2BooleanFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2BooleanFunction function;
        
        protected UnmodifiableFunction(final Float2BooleanFunction f) {
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
        
        public boolean containsKey(final float k) {
            return this.function.containsKey(k);
        }
        
        public boolean put(final float k, final boolean v) {
            throw new UnsupportedOperationException();
        }
        
        public boolean get(final float k) {
            return this.function.get(k);
        }
        
        public boolean remove(final float k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean put(final Float k, final Boolean v) {
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
    
    public static class PrimitiveFunction implements Float2BooleanFunction {
        protected final java.util.function.Function<? super Float, ? extends Boolean> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Float, ? extends Boolean> function) {
            this.function = function;
        }
        
        public boolean containsKey(final float key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public boolean get(final float key) {
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
        public Boolean put(final Float key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
    }
}
