package org.apache.logging.log4j.core;

import java.io.Serializable;

public interface Appender extends LifeCycle {
    public static final String ELEMENT_TYPE = "appender";
    
    void append(final LogEvent logEvent);
    
    String getName();
    
    Layout<? extends Serializable> getLayout();
    
    boolean ignoreExceptions();
    
    ErrorHandler getHandler();
    
    void setHandler(final ErrorHandler errorHandler);
}
