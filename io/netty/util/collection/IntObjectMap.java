package io.netty.util.collection;

import java.util.Map;

public interface IntObjectMap<V> extends Map<Integer, V> {
    V get(final int integer);
    
    V put(final int integer, final V object);
    
    V remove(final int integer);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final int integer);
    
    public interface PrimitiveEntry<V> {
        int key();
        
        V value();
        
        void setValue(final V object);
    }
}
