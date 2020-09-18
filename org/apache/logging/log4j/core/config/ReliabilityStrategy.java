package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.Supplier;

public interface ReliabilityStrategy {
    void log(final Supplier<LoggerConfig> supplier, final String string2, final String string3, final Marker marker, final Level level, final Message message, final Throwable throwable);
    
    void log(final Supplier<LoggerConfig> supplier, final LogEvent logEvent);
    
    LoggerConfig getActiveLoggerConfig(final Supplier<LoggerConfig> supplier);
    
    void afterLogEvent();
    
    void beforeStopAppenders();
    
    void beforeStopConfiguration(final Configuration configuration);
}
