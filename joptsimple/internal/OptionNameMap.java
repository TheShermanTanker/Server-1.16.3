package joptsimple.internal;

import java.util.Map;

public interface OptionNameMap<V> {
    boolean contains(final String string);
    
    V get(final String string);
    
    void put(final String string, final V object);
    
    void putAll(final Iterable<String> iterable, final V object);
    
    void remove(final String string);
    
    Map<String, V> toJavaUtilMap();
}
