package org.apache.logging.log4j.spi;

import java.util.Map;

public interface ObjectThreadContextMap extends CleanableThreadContextMap {
     <V> V getValue(final String string);
    
     <V> void putValue(final String string, final V object);
    
     <V> void putAllValues(final Map<String, V> map);
}
