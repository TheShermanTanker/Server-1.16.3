package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Suppliers {
    private Suppliers() {
    }
    
    public static <F, T> Supplier<T> compose(final Function<? super F, T> function, final Supplier<F> supplier) {
        Preconditions.<Function<? super F, T>>checkNotNull(function);
        Preconditions.<Supplier<F>>checkNotNull(supplier);
        return new SupplierComposition<Object, T>(function, supplier);
    }
    
    public static <T> Supplier<T> memoize(final Supplier<T> delegate) {
        if (delegate instanceof NonSerializableMemoizingSupplier || delegate instanceof MemoizingSupplier) {
            return delegate;
        }
        return (delegate instanceof Serializable) ? new MemoizingSupplier<T>((Supplier<Object>)delegate) : new NonSerializableMemoizingSupplier<T>((Supplier<Object>)delegate);
    }
    
    public static <T> Supplier<T> memoizeWithExpiration(final Supplier<T> delegate, final long duration, final TimeUnit unit) {
        return new ExpiringMemoizingSupplier<T>(delegate, duration, unit);
    }
    
    public static <T> Supplier<T> ofInstance(@Nullable final T instance) {
        return new SupplierOfInstance<T>(instance);
    }
    
    public static <T> Supplier<T> synchronizedSupplier(final Supplier<T> delegate) {
        return new ThreadSafeSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
    }
    
    public static <T> Function<Supplier<T>, T> supplierFunction() {
        final SupplierFunction<T> sf = (SupplierFunction<T>)SupplierFunctionImpl.INSTANCE;
        return sf;
    }
    
    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
        final Function<? super F, T> function;
        final Supplier<F> supplier;
        private static final long serialVersionUID = 0L;
        
        SupplierComposition(final Function<? super F, T> function, final Supplier<F> supplier) {
            this.function = function;
            this.supplier = supplier;
        }
        
        public T get() {
            return this.function.apply(this.supplier.get());
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof SupplierComposition) {
                final SupplierComposition<?, ?> that = obj;
                return this.function.equals(that.function) && this.supplier.equals(that.supplier);
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.function, this.supplier);
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.compose(").append(this.function).append(", ").append(this.supplier).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        transient volatile boolean initialized;
        transient T value;
        private static final long serialVersionUID = 0L;
        
        MemoizingSupplier(final Supplier<T> delegate) {
            this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
        }
        
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        final T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }
            return this.value;
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.memoize(").append(this.delegate).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class NonSerializableMemoizingSupplier<T> implements Supplier<T> {
        volatile Supplier<T> delegate;
        volatile boolean initialized;
        T value;
        
        NonSerializableMemoizingSupplier(final Supplier<T> delegate) {
            this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
        }
        
        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        final T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        this.delegate = null;
                        return t;
                    }
                }
            }
            return this.value;
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.memoize(").append(this.delegate).append(")").toString();
        }
    }
    
    @VisibleForTesting
    static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        final long durationNanos;
        transient volatile T value;
        transient volatile long expirationNanos;
        private static final long serialVersionUID = 0L;
        
        ExpiringMemoizingSupplier(final Supplier<T> delegate, final long duration, final TimeUnit unit) {
            this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument(duration > 0L);
        }
        
        public T get() {
            long nanos = this.expirationNanos;
            final long now = Platform.systemNanoTime();
            if (nanos == 0L || now - nanos >= 0L) {
                synchronized (this) {
                    if (nanos == this.expirationNanos) {
                        final T t = this.delegate.get();
                        this.value = t;
                        nanos = now + this.durationNanos;
                        this.expirationNanos = ((nanos == 0L) ? 1L : nanos);
                        return t;
                    }
                }
            }
            return this.value;
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.memoizeWithExpiration(").append(this.delegate).append(", ").append(this.durationNanos).append(", NANOS)").toString();
        }
    }
    
    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
        final T instance;
        private static final long serialVersionUID = 0L;
        
        SupplierOfInstance(@Nullable final T instance) {
            this.instance = instance;
        }
        
        public T get() {
            return this.instance;
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof SupplierOfInstance) {
                final SupplierOfInstance<?> that = obj;
                return Objects.equal(this.instance, that.instance);
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.instance);
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.ofInstance(").append(this.instance).append(")").toString();
        }
    }
    
    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
        final Supplier<T> delegate;
        private static final long serialVersionUID = 0L;
        
        ThreadSafeSupplier(final Supplier<T> delegate) {
            this.delegate = delegate;
        }
        
        public T get() {
            synchronized (this.delegate) {
                return this.delegate.get();
            }
        }
        
        public String toString() {
            return new StringBuilder().append("Suppliers.synchronizedSupplier(").append(this.delegate).append(")").toString();
        }
    }
    
    private enum SupplierFunctionImpl implements SupplierFunction<Object> {
        INSTANCE;
        
        public Object apply(final Supplier<Object> input) {
            return input.get();
        }
        
        public String toString() {
            return "Suppliers.supplierFunction()";
        }
    }
    
    private interface SupplierFunction<T> extends Function<Supplier<T>, T> {
    }
}
