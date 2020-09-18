package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.Supplier;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReadWriteLock;

public class LockingReliabilityStrategy implements ReliabilityStrategy {
    private final LoggerConfig loggerConfig;
    private final ReadWriteLock reconfigureLock;
    private volatile boolean isStopping;
    
    public LockingReliabilityStrategy(final LoggerConfig loggerConfig) {
        this.reconfigureLock = (ReadWriteLock)new ReentrantReadWriteLock();
        this.isStopping = false;
        this.loggerConfig = (LoggerConfig)Objects.requireNonNull(loggerConfig, "loggerConfig was null");
    }
    
    public void log(final Supplier<LoggerConfig> reconfigured, final String loggerName, final String fqcn, final Marker marker, final Level level, final Message data, final Throwable t) {
        final LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(loggerName, fqcn, marker, level, data, t);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }
    
    public void log(final Supplier<LoggerConfig> reconfigured, final LogEvent event) {
        final LoggerConfig config = this.getActiveLoggerConfig(reconfigured);
        try {
            config.log(event);
        }
        finally {
            config.getReliabilityStrategy().afterLogEvent();
        }
    }
    
    public LoggerConfig getActiveLoggerConfig(final Supplier<LoggerConfig> next) {
        LoggerConfig result = this.loggerConfig;
        if (!this.beforeLogEvent()) {
            result = next.get();
            return result.getReliabilityStrategy().getActiveLoggerConfig(next);
        }
        return result;
    }
    
    private boolean beforeLogEvent() {
        this.reconfigureLock.readLock().lock();
        if (this.isStopping) {
            this.reconfigureLock.readLock().unlock();
            return false;
        }
        return true;
    }
    
    public void afterLogEvent() {
        this.reconfigureLock.readLock().unlock();
    }
    
    public void beforeStopAppenders() {
        this.reconfigureLock.writeLock().lock();
        try {
            this.isStopping = true;
        }
        finally {
            this.reconfigureLock.writeLock().unlock();
        }
    }
    
    public void beforeStopConfiguration(final Configuration configuration) {
    }
}
