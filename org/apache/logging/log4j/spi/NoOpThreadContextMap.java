package org.apache.logging.log4j.spi;

import java.util.HashMap;
import java.util.Map;

public class NoOpThreadContextMap implements ThreadContextMap {
    public void clear() {
    }
    
    public boolean containsKey(final String key) {
        return false;
    }
    
    public String get(final String key) {
        return null;
    }
    
    public Map<String, String> getCopy() {
        return (Map<String, String>)new HashMap();
    }
    
    public Map<String, String> getImmutableMapOrNull() {
        return null;
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    public void put(final String key, final String value) {
    }
    
    public void remove(final String key) {
    }
}
