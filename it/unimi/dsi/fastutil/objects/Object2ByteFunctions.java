package it.unimi.dsi.fastutil.objects;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.ToIntFunction;
import java.util.Objects;
import java.util.function.Function;

public final class Object2ByteFunctions {
    public static final EmptyFunction EMPTY_FUNCTION;
    
    private Object2ByteFunctions() {
    }
    
    public static <K> Object2ByteFunction<K> singleton(final K key, final byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteFunction<K> singleton(final K key, final Byte value) {
        return new Singleton<K>(key, value);
    }
    
    public static <K> Object2ByteFunction<K> synchronize(final Object2ByteFunction<K> f) {
        return new SynchronizedFunction<K>(f);
    }
    
    public static <K> Object2ByteFunction<K> synchronize(final Object2ByteFunction<K> f, final Object sync) {
        return new SynchronizedFunction<K>(f, sync);
    }
    
    public static <K> Object2ByteFunction<K> unmodifiable(final Object2ByteFunction<K> f) {
        return new UnmodifiableFunction<K>(f);
    }
    
    public static <K> Object2ByteFunction<K> primitive(final Function<? super K, ? extends Byte> f) {
        Objects.requireNonNull(f);
        if (f instanceof Object2ByteFunction) {
            return (Object2ByteFunction<K>)(Object2ByteFunction)f;
        }
        if (f instanceof ToIntFunction) {
            return key -> SafeMath.safeIntToByte(((ToIntFunction)f).applyAsInt(key));
        }
        return new PrimitiveFunction<K>(f);
    }
    
    static {
        EMPTY_FUNCTION = new EmptyFunction();
    }
    
    public static class EmptyFunction<K> extends AbstractObject2ByteFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        
        protected EmptyFunction() {
        }
        
        public byte getByte(final Object k) {
            return 0;
        }
        
        public boolean containsKey(final Object k) {
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
            return Object2ByteFunctions.EMPTY_FUNCTION;
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
            return Object2ByteFunctions.EMPTY_FUNCTION;
        }
    }
    
    public static class Singleton<K> extends AbstractObject2ByteFunction<K> implements Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final byte value;
        
        protected Singleton(final K key, final byte value) {
            this.key = key;
            this.value = value;
        }
        
        public boolean containsKey(final Object k) {
            return Objects.equals(this.key, k);
        }
        
        public byte getByte(final Object k) {
            return Objects.equals(this.key, k) ? this.value : this.defRetValue;
        }
        
        public int size() {
            return 1;
        }
        
        public Object clone() {
            return this;
        }
    }
    
    public static class SynchronizedFunction<K> implements Object2ByteFunction<K>, Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteFunction<K> function;
        protected final Object sync;
        
        protected SynchronizedFunction(final Object2ByteFunction<K> f, final Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }
        
        protected SynchronizedFunction(final Object2ByteFunction<K> f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }
        
        public int applyAsInt(final K operand) {
            synchronized (this.sync) {
                return this.function.applyAsInt(operand);
            }
        }
        
        @Deprecated
        public Byte apply(final K key) {
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
        
        public boolean containsKey(final Object k) {
            synchronized (this.sync) {
                return this.function.containsKey(k);
            }
        }
        
        public byte put(final K k, final byte v) {
            synchronized (this.sync) {
                return this.function.put(k, v);
            }
        }
        
        public byte getByte(final Object k) {
            synchronized (this.sync) {
                return this.function.getByte(k);
            }
        }
        
        public byte removeByte(final Object k) {
            synchronized (this.sync) {
                return this.function.removeByte(k);
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.function.clear();
            }
        }
        
        @Deprecated
        public Byte put(final K k, final Byte v) {
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
    
    public static class UnmodifiableFunction<K> extends AbstractObject2ByteFunction<K> implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Object2ByteFunction<K> function;
        
        protected UnmodifiableFunction(final Object2ByteFunction<K> f) {
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
        
        public boolean containsKey(final Object k) {
            return this.function.containsKey(k);
        }
        
        public byte put(final K k, final byte v) {
            throw new UnsupportedOperationException();
        }
        
        public byte getByte(final Object k) {
            return this.function.getByte(k);
        }
        
        public byte removeByte(final Object k) {
            throw new UnsupportedOperationException();
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Deprecated
        public Byte put(final K k, final Byte v) {
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
    
    public static class PrimitiveFunction<K> implements Object2ByteFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Byte> function;
        
        protected PrimitiveFunction(final java.util.function.Function<? super K, ? extends Byte> function) {
            this.function = function;
        }
        
        public boolean containsKey(final Object key) {
            return this.function.apply(key) != null;
        }
        
        public byte getByte(final Object key) {
            final Byte v = (Byte)this.function.apply(key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }
        
        @Deprecated
        public Byte get(final Object key) {
            return (Byte)this.function.apply(key);
        }
        
        @Deprecated
        public Byte put(final K key, final Byte value) {
            throw new UnsupportedOperationException();
        }
    }
}
