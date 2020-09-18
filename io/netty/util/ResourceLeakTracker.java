package io.netty.util;

public interface ResourceLeakTracker<T> {
    void record();
    
    void record(final Object object);
    
    boolean close(final T object);
}
