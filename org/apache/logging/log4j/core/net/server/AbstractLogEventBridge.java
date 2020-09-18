package org.apache.logging.log4j.core.net.server;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import java.io.InputStream;

public abstract class AbstractLogEventBridge<T extends InputStream> implements LogEventBridge<T> {
    protected static final int END = -1;
    protected static final Logger logger;
    
    public T wrapStream(final InputStream inputStream) throws IOException {
        return (T)inputStream;
    }
    
    static {
        logger = StatusLogger.getLogger();
    }
}
