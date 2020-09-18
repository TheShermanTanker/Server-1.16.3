package it.unimi.dsi.fastutil.shorts;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;
import java.util.Objects;
import java.util.function.Function;

public final class Short2CharFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Short2CharFunctions() {
    }
    
    public static Short2CharFunction singleton(final short key, final char value) {
        return new Singleton(key, value);
    }
    
    public static Short2CharFunction singleton(final Short key, final Character value) {
        return new Singleton(key, value);
    }
    
    public static Short2CharFunction synchronize(final Short2CharFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Short2CharFunction synchronize(final Short2CharFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Short2CharFunction unmodifiable(final Short2CharFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Short2CharFunction primitive(final Function<? super Short, ? extends Character> f) {
        Objects.requireNonNull(f);
        if (f instanceof Short2CharFunction) {
            return (Short2CharFunction)f;
        }
        if (f instanceof IntUnaryOperator) {
            return key -> SafeMath.safeIntToChar(((IntUnaryOperator)f).applyAsInt((int)key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractShort2CharFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public char get(final short k) {
            return '\0';
        }
        
        public boolean containsKey(final short k) {
            return false;
        }
        
        @Override
        public char defaultReturnValue() {
            return '\0';
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object clone() {
            return Short2CharFunctions.EMPTY_FUNCTION;
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
            return Short2CharFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractShort2CharFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final short key;
        protected final char value;
        
        protected Singleton(final short key, final char value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final short k) {
            return this.key == k;
        }
        
        public char get(final short k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Short2CharFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2CharFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Short2CharFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Short2CharFunction f) {
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
        public Character apply(final Short key) {
            synchronized (this.sync) {
                return this.function.apply(key);
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.function.size();
            }
        }
        
        public char defaultReturnValue() {
            synchronized (this.sync) {
                return this.function.defaultReturnValue();
            }
        }
        
        public void defaultReturnValue(final char defRetValue) {
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
        
        public char put(final short k, final char v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public char get(final short k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public char remove(final short k) {
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
        public Character put(final Short k, final Character v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        @Deprecated
        public Character get(final Object k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        @Deprecated
        public Character remove(final Object k) {
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
    
    public static class UnmodifiableFunction extends AbstractShort2CharFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2CharFunction function;
        
        protected UnmodifiableFunction(final Short2CharFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }
        
        public int size() {
            return this.function.size();
        }
        
        @Override
        public char defaultReturnValue() {
            return this.function.defaultReturnValue();
        }
        
        @Override
        public void defaultReturnValue(final char defRetValue) {
            throw new UnsupportedOperationException();
        }
        
        public boolean containsKey(final short k) {
            return this.function.containsKey(k);
        }
        
        public char put(final short k, final char v) {
            throw new UnsupportedOperationException();
        }
        
        public char get(final short k) {
            return this.function.get(k);
        }
        
        public char remove(final short k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Character put(final Short k, final Character v) {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Character get(final Object k) {
            return this.function.get(k);
        }
        
        @Deprecated
        public Character remove(final Object k) {
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
    
    public static class PrimitiveFunction implements Short2CharFunction {
        protected final java.util.function.Function<? super Short, ? extends Character> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Short, ? extends Character> function) {
            this.function = function;
        }
        
        public boolean containsKey(final short key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public char get(final short key) {
            final Character v = (Character)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Character get(final Object key) {
            if (key == null) {
                return null;
            }
            return (Character)this.function.apply(key);
        }
        
        @Deprecated
        public Character put(final Short key, final Character value) {
            throw new UnsupportedOperationException();
        }
    }
}
