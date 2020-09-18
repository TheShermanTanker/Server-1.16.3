package com.google.common.collect;

import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible
public abstract class ForwardingConcurrentMap<K, V> extends ForwardingMap<K, V> implements ConcurrentMap<K, V> {
    protected ForwardingConcurrentMap() {
    }
    
    protected abstract ConcurrentMap<K, V> delegate();
    
    @CanIgnoreReturnValue
    public V putIfAbsent(final K key, final V value) {
        return (V)this.delegate().putIfAbsent(key, value);
    }
    
    @CanIgnoreReturnValue
    public boolean remove(final Object key, final Object value) {
        return this.delegate().remove(key, value);
    }
    
    @CanIgnoreReturnValue
    public V replace(final K key, final V value) {
        return (V)this.delegate().replace(key, value);
    }
    
    @CanIgnoreReturnValue
    public boolean replace(final K key, final V oldValue, final V newValue) {
        return this.delegate().replace(key, oldValue, newValue);
    }
}
