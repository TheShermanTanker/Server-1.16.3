package org.apache.commons.lang3.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.Validate;
import java.util.concurrent.ExecutionException;

public class ConcurrentUtils {
    private ConcurrentUtils() {
    }
    
    public static ConcurrentException extractCause(final ExecutionException ex) {
        if (ex == null || ex.getCause() == null) {
            return null;
        }
        throwCause(ex);
        return new ConcurrentException(ex.getMessage(), ex.getCause());
    }
    
    public static ConcurrentRuntimeException extractCauseUnchecked(final ExecutionException ex) {
        if (ex == null || ex.getCause() == null) {
            return null;
        }
        throwCause(ex);
        return new ConcurrentRuntimeException(ex.getMessage(), ex.getCause());
    }
    
    public static void handleCause(final ExecutionException ex) throws ConcurrentException {
        final ConcurrentException cex = extractCause(ex);
        if (cex != null) {
            throw cex;
        }
    }
    
    public static void handleCauseUnchecked(final ExecutionException ex) {
        final ConcurrentRuntimeException crex = extractCauseUnchecked(ex);
        if (crex != null) {
            throw crex;
        }
    }
    
    static Throwable checkedException(final Throwable ex) {
        Validate.isTrue(ex != null && !(ex instanceof RuntimeException) && !(ex instanceof Error), new StringBuilder().append("Not a checked exception: ").append(ex).toString());
        return ex;
    }
    
    private static void throwCause(final ExecutionException ex) {
        if (ex.getCause() instanceof RuntimeException) {
            throw (RuntimeException)ex.getCause();
        }
        if (ex.getCause() instanceof Error) {
            throw (Error)ex.getCause();
        }
    }
    
    public static <T> T initialize(final ConcurrentInitializer<T> initializer) throws ConcurrentException {
        return (initializer != null) ? initializer.get() : null;
    }
    
    public static <T> T initializeUnchecked(final ConcurrentInitializer<T> initializer) {
        try {
            return (T)ConcurrentUtils.initialize((ConcurrentInitializer<Object>)initializer);
        }
        catch (ConcurrentException cex) {
            throw new ConcurrentRuntimeException(cex.getCause());
        }
    }
    
    public static <K, V> V putIfAbsent(final ConcurrentMap<K, V> map, final K key, final V value) {
        if (map == null) {
            return null;
        }
        final V result = (V)map.putIfAbsent(key, value);
        return (result != null) ? result : value;
    }
    
    public static <K, V> V createIfAbsent(final ConcurrentMap<K, V> map, final K key, final ConcurrentInitializer<V> init) throws ConcurrentException {
        if (map == null || init == null) {
            return null;
        }
        final V value = (V)map.get(key);
        if (value == null) {
            return ConcurrentUtils.<K, V>putIfAbsent(map, key, init.get());
        }
        return value;
    }
    
    public static <K, V> V createIfAbsentUnchecked(final ConcurrentMap<K, V> map, final K key, final ConcurrentInitializer<V> init) {
        try {
            return (V)ConcurrentUtils.createIfAbsent((java.util.concurrent.ConcurrentMap<Object, Object>)map, key, (ConcurrentInitializer<Object>)init);
        }
        catch (ConcurrentException cex) {
            throw new ConcurrentRuntimeException(cex.getCause());
        }
    }
    
    public static <T> Future<T> constantFuture(final T value) {
        return (Future<T>)new ConstantFuture(value);
    }
    
    static final class ConstantFuture<T> implements Future<T> {
        private final T value;
        
        ConstantFuture(final T value) {
            this.value = value;
        }
        
        public boolean isDone() {
            return true;
        }
        
        public T get() {
            return this.value;
        }
        
        public T get(final long timeout, final TimeUnit unit) {
            return this.value;
        }
        
        public boolean isCancelled() {
            return false;
        }
        
        public boolean cancel(final boolean mayInterruptIfRunning) {
            return false;
        }
    }
}
