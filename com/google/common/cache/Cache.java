package com.google.common.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Cache<K, V> {
    @Nullable
    V getIfPresent(final Object object);
    
    V get(final K object, final Callable<? extends V> callable) throws ExecutionException;
    
    ImmutableMap<K, V> getAllPresent(final Iterable<?> iterable);
    
    void put(final K object1, final V object2);
    
    void putAll(final Map<? extends K, ? extends V> map);
    
    void invalidate(final Object object);
    
    void invalidateAll(final Iterable<?> iterable);
    
    void invalidateAll();
    
    long size();
    
    CacheStats stats();
    
    ConcurrentMap<K, V> asMap();
    
    void cleanUp();
}
