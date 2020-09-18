package com.google.common.cache;

import java.util.concurrent.ConcurrentMap;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;

@GwtCompatible
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
    V get(final K object) throws ExecutionException;
    
    V getUnchecked(final K object);
    
    ImmutableMap<K, V> getAll(final Iterable<? extends K> iterable) throws ExecutionException;
    
    @Deprecated
    V apply(final K object);
    
    void refresh(final K object);
    
    ConcurrentMap<K, V> asMap();
}
