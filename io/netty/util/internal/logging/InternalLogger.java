package io.netty.util.internal.logging;

public interface InternalLogger {
    String name();
    
    boolean isTraceEnabled();
    
    void trace(final String string);
    
    void trace(final String string, final Object object);
    
    void trace(final String string, final Object object2, final Object object3);
    
    void trace(final String string, final Object... arr);
    
    void trace(final String string, final Throwable throwable);
    
    void trace(final Throwable throwable);
    
    boolean isDebugEnabled();
    
    void debug(final String string);
    
    void debug(final String string, final Object object);
    
    void debug(final String string, final Object object2, final Object object3);
    
    void debug(final String string, final Object... arr);
    
    void debug(final String string, final Throwable throwable);
    
    void debug(final Throwable throwable);
    
    boolean isInfoEnabled();
    
    void info(final String string);
    
    void info(final String string, final Object object);
    
    void info(final String string, final Object object2, final Object object3);
    
    void info(final String string, final Object... arr);
    
    void info(final String string, final Throwable throwable);
    
    void info(final Throwable throwable);
    
    boolean isWarnEnabled();
    
    void warn(final String string);
    
    void warn(final String string, final Object object);
    
    void warn(final String string, final Object... arr);
    
    void warn(final String string, final Object object2, final Object object3);
    
    void warn(final String string, final Throwable throwable);
    
    void warn(final Throwable throwable);
    
    boolean isErrorEnabled();
    
    void error(final String string);
    
    void error(final String string, final Object object);
    
    void error(final String string, final Object object2, final Object object3);
    
    void error(final String string, final Object... arr);
    
    void error(final String string, final Throwable throwable);
    
    void error(final Throwable throwable);
    
    boolean isEnabled(final InternalLogLevel internalLogLevel);
    
    void log(final InternalLogLevel internalLogLevel, final String string);
    
    void log(final InternalLogLevel internalLogLevel, final String string, final Object object);
    
    void log(final InternalLogLevel internalLogLevel, final String string, final Object object3, final Object object4);
    
    void log(final InternalLogLevel internalLogLevel, final String string, final Object... arr);
    
    void log(final InternalLogLevel internalLogLevel, final String string, final Throwable throwable);
    
    void log(final InternalLogLevel internalLogLevel, final Throwable throwable);
}
