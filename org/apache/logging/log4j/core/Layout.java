package org.apache.logging.log4j.core;

import java.util.Map;
import org.apache.logging.log4j.core.layout.Encoder;
import java.io.Serializable;

public interface Layout<T extends Serializable> extends Encoder<LogEvent> {
    public static final String ELEMENT_TYPE = "layout";
    
    byte[] getFooter();
    
    byte[] getHeader();
    
    byte[] toByteArray(final LogEvent logEvent);
    
    T toSerializable(final LogEvent logEvent);
    
    String getContentType();
    
    Map<String, String> getContentFormat();
}
