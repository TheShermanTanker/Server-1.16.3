package org.apache.logging.log4j.util;

public interface StringMap extends ReadOnlyStringMap {
    void clear();
    
    boolean equals(final Object object);
    
    void freeze();
    
    int hashCode();
    
    boolean isFrozen();
    
    void putAll(final ReadOnlyStringMap readOnlyStringMap);
    
    void putValue(final String string, final Object object);
    
    void remove(final String string);
}
