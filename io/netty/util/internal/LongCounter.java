package io.netty.util.internal;

public interface LongCounter {
    void add(final long long1);
    
    void increment();
    
    void decrement();
    
    long value();
}
