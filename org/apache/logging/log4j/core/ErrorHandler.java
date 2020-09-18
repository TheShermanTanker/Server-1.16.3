package org.apache.logging.log4j.core;

public interface ErrorHandler {
    void error(final String string);
    
    void error(final String string, final Throwable throwable);
    
    void error(final String string, final LogEvent logEvent, final Throwable throwable);
}
