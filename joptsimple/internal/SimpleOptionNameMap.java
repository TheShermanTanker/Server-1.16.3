package joptsimple.internal;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class SimpleOptionNameMap<V> implements OptionNameMap<V> {
    private final Map<String, V> map;
    
    public SimpleOptionNameMap() {
        this.map = (Map<String, V>)new HashMap();
    }
    
    public boolean contains(final String key) {
        return this.map.containsKey(key);
    }
    
    public V get(final String key) {
        return (V)this.map.get(key);
    }
    
    public void put(final String key, final V newValue) {
        this.map.put(key, newValue);
    }
    
    public void putAll(final Iterable<String> keys, final V newValue) {
        for (final String each : keys) {
            this.map.put(each, newValue);
        }
    }
    
    public void remove(final String key) {
        this.map.remove(key);
    }
    
    public Map<String, V> toJavaUtilMap() {
        return (Map<String, V>)new HashMap((Map)this.map);
    }
}
