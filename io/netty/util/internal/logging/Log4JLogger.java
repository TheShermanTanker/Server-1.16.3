package io.netty.util.internal.logging;

import org.apache.log4j.Priority;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class Log4JLogger extends AbstractInternalLogger {
    private static final long serialVersionUID = 2851357342488183058L;
    final transient Logger logger;
    static final String FQCN;
    final boolean traceCapable;
    
    Log4JLogger(final Logger logger) {
        super(logger.getName());
        this.logger = logger;
        this.traceCapable = this.isTraceCapable();
    }
    
    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        }
        catch (NoSuchMethodError ignored) {
            return false;
        }
    }
    
    public boolean isTraceEnabled() {
        if (this.traceCapable) {
            return this.logger.isTraceEnabled();
        }
        return this.logger.isDebugEnabled();
    }
    
    public void trace(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), msg, (Throwable)null);
    }
    
    public void trace(final String format, final Object arg) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object argA, final Object argB) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object... arguments) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), msg, t);
    }
    
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    public void debug(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, msg, (Throwable)null);
    }
    
    public void debug(final String format, final Object arg) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object argA, final Object argB) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, msg, t);
    }
    
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }
    
    public void info(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, msg, (Throwable)null);
    }
    
    public void info(final String format, final Object arg) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object argA, final Object argB) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object... argArray) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, msg, t);
    }
    
    public boolean isWarnEnabled() {
        return this.logger.isEnabledFor((Priority)Level.WARN);
    }
    
    public void warn(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, msg, (Throwable)null);
    }
    
    public void warn(final String format, final Object arg) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object argA, final Object argB) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object... argArray) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, msg, t);
    }
    
    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor((Priority)Level.ERROR);
    }
    
    public void error(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, msg, (Throwable)null);
    }
    
    public void error(final String format, final Object arg) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object argA, final Object argB) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object... argArray) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, msg, t);
    }
    
    static {
        FQCN = Log4JLogger.class.getName();
    }
}
