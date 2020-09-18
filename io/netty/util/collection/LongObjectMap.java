package io.netty.util.collection;

import java.util.Map;

public interface LongObjectMap<V> extends Map<Long, V> {
    V get(final long long1);
    
    V put(final long long1, final V object);
    
    V remove(final long long1);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final long long1);
    
    public interface PrimitiveEntry<V> {
        long key();
        
        V value();
        
        void setValue(final V object);
    }
}
