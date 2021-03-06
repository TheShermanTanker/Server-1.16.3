package org.apache.logging.log4j.core.appender.rolling;

public interface RolloverStrategy {
    RolloverDescription rollover(final RollingFileManager rollingFileManager) throws SecurityException;
}
