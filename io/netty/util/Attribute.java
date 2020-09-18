package io.netty.util;

public interface Attribute<T> {
    AttributeKey<T> key();
    
    T get();
    
    void set(final T object);
    
    T getAndSet(final T object);
    
    T setIfAbsent(final T object);
    
    @Deprecated
    T getAndRemove();
    
    boolean compareAndSet(final T object1, final T object2);
    
    @Deprecated
    void remove();
}
