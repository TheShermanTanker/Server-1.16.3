package org.apache.logging.log4j.util;

public interface IndexedReadOnlyStringMap extends ReadOnlyStringMap {
    String getKeyAt(final int integer);
    
     <V> V getValueAt(final int integer);
    
    int indexOfKey(final String string);
}
