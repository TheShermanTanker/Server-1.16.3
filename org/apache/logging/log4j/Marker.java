package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker extends Serializable {
    Marker addParents(final Marker... arr);
    
    boolean equals(final Object object);
    
    String getName();
    
    Marker[] getParents();
    
    int hashCode();
    
    boolean hasParents();
    
    boolean isInstanceOf(final Marker marker);
    
    boolean isInstanceOf(final String string);
    
    boolean remove(final Marker marker);
    
    Marker setParents(final Marker... arr);
}
