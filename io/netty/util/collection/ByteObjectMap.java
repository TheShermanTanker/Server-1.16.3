package io.netty.util.collection;

import java.util.Map;

public interface ByteObjectMap<V> extends Map<Byte, V> {
    V get(final byte byte1);
    
    V put(final byte byte1, final V object);
    
    V remove(final byte byte1);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final byte byte1);
    
    public interface PrimitiveEntry<V> {
        byte key();
        
        V value();
        
        void setValue(final V object);
    }
}
