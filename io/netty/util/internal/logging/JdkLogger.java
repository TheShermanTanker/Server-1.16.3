package io.netty.util.internal.logging;

import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.logging.Logger;

class JdkLogger extends AbstractInternalLogger {
    private static final long serialVersionUID = -1767272577989225979L;
    final transient Logger logger;
    static final String SELF;
    static final String SUPER;
    
    JdkLogger(final Logger logger) {
        super(logger.getName());
        this.logger = logger;
    }
    
    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINEST);
    }
    
    public void trace(final String msg) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(JdkLogger.SELF, Level.FINEST, msg, null);
        }
    }
    
    public void trace(final String format, final Object arg) {
        if (this.logger.isLoggable(Level.FINEST)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(JdkLogger.SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object argA, final Object argB) {
        if (this.logger.isLoggable(Level.FINEST)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(JdkLogger.SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String format, final Object... argArray) {
        if (this.logger.isLoggable(Level.FINEST)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(JdkLogger.SELF, Level.FINEST, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void trace(final String msg, final Throwable t) {
        if (this.logger.isLoggable(Level.FINEST)) {
            this.log(JdkLogger.SELF, Level.FINEST, msg, t);
        }
    }
    
    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }
    
    public void debug(final String msg) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(JdkLogger.SELF, Level.FINE, msg, null);
        }
    }
    
    public void debug(final String format, final Object arg) {
        if (this.logger.isLoggable(Level.FINE)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(JdkLogger.SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object argA, final Object argB) {
        if (this.logger.isLoggable(Level.FINE)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(JdkLogger.SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String format, final Object... argArray) {
        if (this.logger.isLoggable(Level.FINE)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(JdkLogger.SELF, Level.FINE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void debug(final String msg, final Throwable t) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.log(JdkLogger.SELF, Level.FINE, msg, t);
        }
    }
    
    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }
    
    public void info(final String msg) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(JdkLogger.SELF, Level.INFO, msg, null);
        }
    }
    
    public void info(final String format, final Object arg) {
        if (this.logger.isLoggable(Level.INFO)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(JdkLogger.SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object argA, final Object argB) {
        if (this.logger.isLoggable(Level.INFO)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(JdkLogger.SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String format, final Object... argArray) {
        if (this.logger.isLoggable(Level.INFO)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(JdkLogger.SELF, Level.INFO, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void info(final String msg, final Throwable t) {
        if (this.logger.isLoggable(Level.INFO)) {
            this.log(JdkLogger.SELF, Level.INFO, msg, t);
        }
    }
    
    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }
    
    public void warn(final String msg) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(JdkLogger.SELF, Level.WARNING, msg, null);
        }
    }
    
    public void warn(final String format, final Object arg) {
        if (this.logger.isLoggable(Level.WARNING)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(JdkLogger.SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object argA, final Object argB) {
        if (this.logger.isLoggable(Level.WARNING)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(JdkLogger.SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String format, final Object... argArray) {
        if (this.logger.isLoggable(Level.WARNING)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.log(JdkLogger.SELF, Level.WARNING, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void warn(final String msg, final Throwable t) {
        if (this.logger.isLoggable(Level.WARNING)) {
            this.log(JdkLogger.SELF, Level.WARNING, msg, t);
        }
    }
    
    public boolean isErrorEnabled() {
        return this.logger.isLoggable(Level.SEVERE);
    }
    
    public void error(final String msg) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(JdkLogger.SELF, Level.SEVERE, msg, null);
        }
    }
    
    public void error(final String format, final Object arg) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.log(JdkLogger.SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object argA, final Object argB) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.log(JdkLogger.SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String format, final Object... arguments) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.log(JdkLogger.SELF, Level.SEVERE, ft.getMessage(), ft.getThrowable());
        }
    }
    
    public void error(final String msg, final Throwable t) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            this.log(JdkLogger.SELF, Level.SEVERE, msg, t);
        }
    }
    
    private void log(final String callerFQCN, final Level level, final String msg, final Throwable t) {
        final LogRecord record = new LogRecord(level, msg);
        record.setLoggerName(this.name());
        record.setThrown(t);
        fillCallerData(callerFQCN, record);
        this.logger.log(record);
    }
    
    private static void fillCallerData(final String callerFQCN, final LogRecord record) {
        final StackTraceElement[] steArray = new Throwable().getStackTrace();
        int selfIndex = -1;
        for (int i = 0; i < steArray.length; ++i) {
            final String className = steArray[i].getClassName();
            if (className.equals(callerFQCN) || className.equals(JdkLogger.SUPER)) {
                selfIndex = i;
                break;
            }
        }
        int found = -1;
        for (int j = selfIndex + 1; j < steArray.length; ++j) {
            final String className2 = steArray[j].getClassName();
            if (!className2.equals(callerFQCN) && !className2.equals(JdkLogger.SUPER)) {
                found = j;
                break;
            }
        }
        if (found != -1) {
            final StackTraceElement ste = steArray[found];
            record.setSourceClassName(ste.getClassName());
            record.setSourceMethodName(ste.getMethodName());
        }
    }
    
    static {
        SELF = JdkLogger.class.getName();
        SUPER = AbstractInternalLogger.class.getName();
    }
}
