package org.apache.logging.log4j.simple;

import java.net.URI;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.spi.LoggerContextFactory;

public class SimpleLoggerContextFactory implements LoggerContextFactory {
    private static LoggerContext context;
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext) {
        return SimpleLoggerContextFactory.context;
    }
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final Object externalContext, final boolean currentContext, final URI configLocation, final String name) {
        return SimpleLoggerContextFactory.context;
    }
    
    public void removeContext(final LoggerContext removeContext) {
    }
    
    static {
        SimpleLoggerContextFactory.context = new SimpleLoggerContext();
    }
}
