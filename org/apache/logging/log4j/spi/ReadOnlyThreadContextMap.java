package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.StringMap;
import java.util.Map;

public interface ReadOnlyThreadContextMap {
    void clear();
    
    boolean containsKey(final String string);
    
    String get(final String string);
    
    Map<String, String> getCopy();
    
    Map<String, String> getImmutableMapOrNull();
    
    StringMap getReadOnlyContextData();
    
    boolean isEmpty();
}
