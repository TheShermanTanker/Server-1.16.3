package io.netty.util.collection;

import java.util.Map;

public interface ShortObjectMap<V> extends Map<Short, V> {
    V get(final short short1);
    
    V put(final short short1, final V object);
    
    V remove(final short short1);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final short short1);
    
    public interface PrimitiveEntry<V> {
        short key();
        
        V value();
        
        void setValue(final V object);
    }
}
