package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ThreadContextMap {
    void clear();
    
    boolean containsKey(final String string);
    
    String get(final String string);
    
    Map<String, String> getCopy();
    
    Map<String, String> getImmutableMapOrNull();
    
    boolean isEmpty();
    
    void put(final String string1, final String string2);
    
    void remove(final String string);
}
