package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicSafeInitializer<T> implements ConcurrentInitializer<T> {
    private final AtomicReference<AtomicSafeInitializer<T>> factory;
    private final AtomicReference<T> reference;
    
    public AtomicSafeInitializer() {
        this.factory = (AtomicReference<AtomicSafeInitializer<T>>)new AtomicReference();
        this.reference = (AtomicReference<T>)new AtomicReference();
    }
    
    public final T get() throws ConcurrentException {
        T result;
        while ((result = (T)this.reference.get()) == null) {
            if (this.factory.compareAndSet(null, this)) {
                this.reference.set(this.initialize());
            }
        }
        return result;
    }
    
    protected abstract T initialize() throws ConcurrentException;
}
