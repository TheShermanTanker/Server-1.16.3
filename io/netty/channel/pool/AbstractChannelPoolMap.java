package io.netty.channel.pool;

import io.netty.util.internal.ReadOnlyIterator;
import java.util.Iterator;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;
import java.io.Closeable;
import java.util.Map;

public abstract class AbstractChannelPoolMap<K, P extends ChannelPool> implements ChannelPoolMap<K, P>, Iterable<Map.Entry<K, P>>, Closeable {
    private final ConcurrentMap<K, P> map;
    
    public AbstractChannelPoolMap() {
        this.map = PlatformDependent.<K, P>newConcurrentHashMap();
    }
    
    public final P get(final K key) {
        P pool = (P)this.map.get(ObjectUtil.<K>checkNotNull(key, "key"));
        if (pool == null) {
            pool = this.newPool(key);
            final P old = (P)this.map.putIfAbsent(key, pool);
            if (old != null) {
                pool.close();
                pool = old;
            }
        }
        return pool;
    }
    
    public final boolean remove(final K key) {
        final P pool = (P)this.map.remove(ObjectUtil.<K>checkNotNull(key, "key"));
        if (pool != null) {
            pool.close();
            return true;
        }
        return false;
    }
    
    public final Iterator<Map.Entry<K, P>> iterator() {
        return (Iterator<Map.Entry<K, P>>)new ReadOnlyIterator((java.util.Iterator<?>)this.map.entrySet().iterator());
    }
    
    public final int size() {
        return this.map.size();
    }
    
    public final boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public final boolean contains(final K key) {
        return this.map.containsKey(ObjectUtil.<K>checkNotNull(key, "key"));
    }
    
    protected abstract P newPool(final K object);
    
    public final void close() {
        for (final K key : this.map.keySet()) {
            this.remove(key);
        }
    }
}
