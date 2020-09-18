package com.google.common.graph;

import java.util.Iterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Map;

class MapIteratorCache<K, V> {
    private final Map<K, V> backingMap;
    @Nullable
    private transient Map.Entry<K, V> entrySetCache;
    
    MapIteratorCache(final Map<K, V> backingMap) {
        this.backingMap = Preconditions.<Map<K, V>>checkNotNull(backingMap);
    }
    
    @CanIgnoreReturnValue
    public V put(@Nullable final K key, @Nullable final V value) {
        this.clearCache();
        return (V)this.backingMap.put(key, value);
    }
    
    @CanIgnoreReturnValue
    public V remove(@Nullable final Object key) {
        this.clearCache();
        return (V)this.backingMap.remove(key);
    }
    
    public void clear() {
        this.clearCache();
        this.backingMap.clear();
    }
    
    public V get(@Nullable final Object key) {
        final V value = this.getIfCached(key);
        return (value != null) ? value : this.getWithoutCaching(key);
    }
    
    public final V getWithoutCaching(@Nullable final Object key) {
        return (V)this.backingMap.get(key);
    }
    
    public final boolean containsKey(@Nullable final Object key) {
        return this.getIfCached(key) != null || this.backingMap.containsKey(key);
    }
    
    public final Set<K> unmodifiableKeySet() {
        return (Set<K>)new AbstractSet<K>() {
            public UnmodifiableIterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> entryIterator = (Iterator<Map.Entry<K, V>>)MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>() {
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }
                    
                    public K next() {
                        final Map.Entry<K, V> entry = (Map.Entry<K, V>)entryIterator.next();
                        MapIteratorCache.this.entrySetCache = entry;
                        return (K)entry.getKey();
                    }
                };
            }
            
            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }
            
            public boolean contains(@Nullable final Object key) {
                return MapIteratorCache.this.containsKey(key);
            }
        };
    }
    
    protected V getIfCached(@Nullable final Object key) {
        final Map.Entry<K, V> entry = this.entrySetCache;
        if (entry != null && entry.getKey() == key) {
            return (V)entry.getValue();
        }
        return null;
    }
    
    protected void clearCache() {
        this.entrySetCache = null;
    }
}
