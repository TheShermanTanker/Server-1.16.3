package it.unimi.dsi.fastutil.ints;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.IntPredicate;
import java.util.Objects;
import java.util.function.Function;

public final class Int2BooleanFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Int2BooleanFunctions() {
    }
    
    public static Int2BooleanFunction singleton(final int key, final boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanFunction singleton(final Integer key, final Boolean value) {
        return new Singleton(key, value);
    }
    
    public static Int2BooleanFunction synchronize(final Int2BooleanFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Int2BooleanFunction synchronize(final Int2BooleanFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Int2BooleanFunction unmodifiable(final Int2BooleanFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Int2BooleanFunction primitive(final Function<? super Integer, ? extends Boolean> f) {
        Objects.requireNonNull(f);
        if (f instanceof Int2BooleanFunction) {
            return (Int2BooleanFunction)f;
        }
        if (f instanceof IntPredicate) {
            final IntPredicate intPredicate = (IntPredicate)f;
            Objects.requireNonNull(intPredicate);
            return intPredicate::test;
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractInt2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public boolean get(final int k) {
            return false;
        }
        
        public boolean containsKey(final int k) {
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
            return Int2BooleanFunctions.EMPTY_FUNCTION;
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
            return Int2BooleanFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractInt2BooleanFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final boolean value;
        
        protected Singleton(final int key, final boolean value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final int k) {
            return this.key == k;
        }
        
        public boolean get(final int k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Int2BooleanFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Int2BooleanFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Int2BooleanFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public boolean test(final int operand) {
            synchronized (this.sync) {
                return this.function.test(operand);
            }
        }
        
        @Deprecated
        public Boolean apply(final Integer key) {
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
        
        public boolean containsKey(final int k) {
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
        
        public boolean put(final int k, final boolean v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public boolean get(final int k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public boolean remove(final int k) {
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
        public Boolean put(final Integer k, final Boolean v) {
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
    
    public static class UnmodifiableFunction extends AbstractInt2BooleanFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Int2BooleanFunction function;
        
        protected UnmodifiableFunction(final Int2BooleanFunction f) {
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
        
        public boolean containsKey(final int k) {
            return this.function.containsKey(k);
        }
        
        public boolean put(final int k, final boolean v) {
            throw new UnsupportedOperationException();
        }
        
        public boolean get(final int k) {
            return this.function.get(k);
        }
        
        public boolean remove(final int k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Boolean put(final Integer k, final Boolean v) {
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
    
    public static class PrimitiveFunction implements Int2BooleanFunction {
        protected final java.util.function.Function<? super Integer, ? extends Boolean> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Integer, ? extends Boolean> function) {
            this.function = function;
        }
        
        public boolean containsKey(final int key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public boolean get(final int key) {
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
        public Boolean put(final Integer key, final Boolean value) {
            throw new UnsupportedOperationException();
        }
    }
}
