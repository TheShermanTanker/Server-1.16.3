package it.unimi.dsi.fastutil.longs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.LongToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Long2ByteFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Long2ByteFunctions() {
    }
    
    public static Long2ByteFunction singleton(final long key, final byte value) {
        return new Singleton(key, value);
    }
    
    public static Long2ByteFunction singleton(final Long key, final Byte value) {
        return new Singleton(key, value);
    }
    
    public static Long2ByteFunction synchronize(final Long2ByteFunction f) {
        return new SynchronizedFunction(f);
    }
    
    public static Long2ByteFunction synchronize(final Long2ByteFunction f, final Object sync) {
        return new SynchronizedFunction(f, sync);
    }
    
    public static Long2ByteFunction unmodifiable(final Long2ByteFunction f) {
        return new UnmodifiableFunction(f);
    }
    
    public static Long2ByteFunction primitive(final Function<? super Long, ? extends Byte> f) {
        Objects.requireNonNull(f);
        if (f instanceof Long2ByteFunction) {
            return (Long2ByteFunction)f;
        }
        if (f instanceof LongToIntFunction) {
            return key -> SafeMath.safeIntToByte(((LongToIntFunction)f).applyAsInt(key));
        }
        return new PrimitiveFunction(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction extends AbstractLong2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public byte get(final long k) {
            return 0;
        }
        
        public boolean containsKey(final long k) {
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
            return Long2ByteFunctions.EMPTY_FUNCTION;
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
            return Long2ByteFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton extends AbstractLong2ByteFunction implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final long key;
        protected final byte value;
        
        protected Singleton(final long key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final long k) {
            return this.key == k;
        }
        
        public byte get(final long k) {
            return (this.key == k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction implements Long2ByteFunction, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ByteFunction function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Long2ByteFunction f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Long2ByteFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final long operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Byte apply(final Long key) {
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
        
        public byte put(final long k, final byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public byte get(final long k) {
            synchronized (this.sync) {
                return this.function.get(k);
            }
        }
        
        public byte remove(final long k) {
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
        public Byte put(final Long k, final Byte v) {
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
    
    public static class UnmodifiableFunction extends AbstractLong2ByteFunction implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ByteFunction function;
        
        protected UnmodifiableFunction(final Long2ByteFunction f) {
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
        
        public boolean containsKey(final long k) {
            return this.function.containsKey(k);
        }
        
        public byte put(final long k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        public byte get(final long k) {
            return this.function.get(k);
        }
        
        public byte remove(final long k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte put(final Long k, final Byte v) {
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
    
    public static class PrimitiveFunction implements Long2ByteFunction {
        protected final java.util.function.Function<? super Long, ? extends Byte> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super Long, ? extends Byte> function) {
            this.function = function;
        }
        
        public boolean containsKey(final long key) {
            return this.function.apply(key) != null;
        }
        
        @Deprecated
        public boolean containsKey(final Object key) {
            return key != null && this.function.apply(key) != null;
        }
        
        public byte get(final long key) {
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
        public Byte put(final Long key, final Byte value) {
            throw new UnsupportedOperationException();
        }
    }
}
