package io.netty.util.collection;

import java.util.Map;

public interface CharObjectMap<V> extends Map<Character, V> {
    V get(final char character);
    
    V put(final char character, final V object);
    
    V remove(final char character);
    
    Iterable<PrimitiveEntry<V>> entries();
    
    boolean containsKey(final char character);
    
    public interface PrimitiveEntry<V> {
        char key();
        
        V value();
        
        void setValue(final V object);
    }
}
