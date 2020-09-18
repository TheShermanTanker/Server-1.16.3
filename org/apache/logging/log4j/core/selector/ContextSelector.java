package org.apache.logging.log4j.core.selector;

import java.util.List;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public interface ContextSelector {
    LoggerContext getContext(final String string, final ClassLoader classLoader, final boolean boolean3);
    
    LoggerContext getContext(final String string, final ClassLoader classLoader, final boolean boolean3, final URI uRI);
    
    List<LoggerContext> getLoggerContexts();
    
    void removeContext(final LoggerContext loggerContext);
}
