package io.netty.util.internal.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

class Log4J2Logger extends ExtendedLoggerWrapper implements InternalLogger {
    private static final long serialVersionUID = 5485418394879791397L;
    private static final String EXCEPTION_MESSAGE = "Unexpected exception:";
    
    Log4J2Logger(final Logger logger) {
        super((ExtendedLogger)logger, logger.getName(), logger.<MessageFactory>getMessageFactory());
    }
    
    @Override
    public String name() {
        return this.getName();
    }
    
    @Override
    public void trace(final Throwable t) {
        this.log(Level.TRACE, "Unexpected exception:", t);
    }
    
    @Override
    public void debug(final Throwable t) {
        this.log(Level.DEBUG, "Unexpected exception:", t);
    }
    
    @Override
    public void info(final Throwable t) {
        this.log(Level.INFO, "Unexpected exception:", t);
    }
    
    @Override
    public void warn(final Throwable t) {
        this.log(Level.WARN, "Unexpected exception:", t);
    }
    
    @Override
    public void error(final Throwable t) {
        this.log(Level.ERROR, "Unexpected exception:", t);
    }
    
    @Override
    public boolean isEnabled(final InternalLogLevel level) {
        return this.isEnabled(this.toLevel(level));
    }
    
    @Override
    public void log(final InternalLogLevel level, final String msg) {
        this.log(this.toLevel(level), msg);
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object arg) {
        this.log(this.toLevel(level), format, arg);
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object argA, final Object argB) {
        this.log(this.toLevel(level), format, argA, argB);
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object... arguments) {
        this.log(this.toLevel(level), format, arguments);
    }
    
    @Override
    public void log(final InternalLogLevel level, final String msg, final Throwable t) {
        this.log(this.toLevel(level), msg, t);
    }
    
    @Override
    public void log(final InternalLogLevel level, final Throwable t) {
        this.log(this.toLevel(level), "Unexpected exception:", t);
    }
    
    protected Level toLevel(final InternalLogLevel level) {
        switch (level) {
            case INFO: {
                return Level.INFO;
            }
            case DEBUG: {
                return Level.DEBUG;
            }
            case WARN: {
                return Level.WARN;
            }
            case ERROR: {
                return Level.ERROR;
            }
            case TRACE: {
                return Level.TRACE;
            }
            default: {
                throw new Error();
            }
        }
    }
}
