package org.apache.logging.log4j.message;

public interface LoggerNameAwareMessage {
    void setLoggerName(final String string);
    
    String getLoggerName();
}
