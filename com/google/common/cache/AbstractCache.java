package com.google.common.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Callable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class AbstractCache<K, V> implements Cache<K, V> {
    protected AbstractCache() {
    }
    
    public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
        throw new UnsupportedOperationException();
    }
    
    public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
        final Map<K, V> result = Maps.newLinkedHashMap();
        for (final Object key : keys) {
            if (!result.containsKey(key)) {
                final K castKey = (K)key;
                final V value = this.getIfPresent(key);
                if (value == null) {
                    continue;
                }
                result.put(castKey, value);
            }
        }
        return ImmutableMap.<K, V>copyOf((java.util.Map<? extends K, ? extends V>)result);
    }
    
    public void put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void cleanUp() {
    }
    
    public long size() {
        throw new UnsupportedOperationException();
    }
    
    public void invalidate(final Object key) {
        throw new UnsupportedOperationException();
    }
    
    public void invalidateAll(final Iterable<?> keys) {
        for (final Object key : keys) {
            this.invalidate(key);
        }
    }
    
    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }
    
    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }
    
    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }
    
    public static final class SimpleStatsCounter implements StatsCounter {
        private final LongAddable hitCount;
        private final LongAddable missCount;
        private final LongAddable loadSuccessCount;
        private final LongAddable loadExceptionCount;
        private final LongAddable totalLoadTime;
        private final LongAddable evictionCount;
        
        public SimpleStatsCounter() {
            this.hitCount = LongAddables.create();
            this.missCount = LongAddables.create();
            this.loadSuccessCount = LongAddables.create();
            this.loadExceptionCount = LongAddables.create();
            this.totalLoadTime = LongAddables.create();
            this.evictionCount = LongAddables.create();
        }
        
        public void recordHits(final int count) {
            this.hitCount.add(count);
        }
        
        public void recordMisses(final int count) {
            this.missCount.add(count);
        }
        
        public void recordLoadSuccess(final long loadTime) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        public void recordLoadException(final long loadTime) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(loadTime);
        }
        
        public void recordEviction() {
            this.evictionCount.increment();
        }
        
        public CacheStats snapshot() {
            return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
        }
        
        public void incrementBy(final StatsCounter other) {
            final CacheStats otherStats = other.snapshot();
            this.hitCount.add(otherStats.hitCount());
            this.missCount.add(otherStats.missCount());
            this.loadSuccessCount.add(otherStats.loadSuccessCount());
            this.loadExceptionCount.add(otherStats.loadExceptionCount());
            this.totalLoadTime.add(otherStats.totalLoadTime());
            this.evictionCount.add(otherStats.evictionCount());
        }
    }
    
    public interface StatsCounter {
        void recordHits(final int integer);
        
        void recordMisses(final int integer);
        
        void recordLoadSuccess(final long long1);
        
        void recordLoadException(final long long1);
        
        void recordEviction();
        
        CacheStats snapshot();
    }
}
