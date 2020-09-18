package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface LongAddable {
    void increment();
    
    void add(final long long1);
    
    long sum();
}
