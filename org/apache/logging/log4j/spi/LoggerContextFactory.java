package org.apache.logging.log4j.spi;

import java.net.URI;

public interface LoggerContextFactory {
    LoggerContext getContext(final String string, final ClassLoader classLoader, final Object object, final boolean boolean4);
    
    LoggerContext getContext(final String string1, final ClassLoader classLoader, final Object object, final boolean boolean4, final URI uRI, final String string6);
    
    void removeContext(final LoggerContext loggerContext);
}
