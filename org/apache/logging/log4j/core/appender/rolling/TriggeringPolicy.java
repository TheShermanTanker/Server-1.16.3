package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;

public interface TriggeringPolicy {
    void initialize(final RollingFileManager rollingFileManager);
    
    boolean isTriggeringEvent(final LogEvent logEvent);
}
