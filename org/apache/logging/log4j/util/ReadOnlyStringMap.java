package org.apache.logging.log4j.util;

import java.util.Map;
import java.io.Serializable;

public interface ReadOnlyStringMap extends Serializable {
    Map<String, String> toMap();
    
    boolean containsKey(final String string);
    
     <V> void forEach(final BiConsumer<String, ? super V> biConsumer);
    
     <V, S> void forEach(final TriConsumer<String, ? super V, S> triConsumer, final S object);
    
     <V> V getValue(final String string);
    
    boolean isEmpty();
    
    int size();
}
