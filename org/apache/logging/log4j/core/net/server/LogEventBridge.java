package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import org.apache.logging.log4j.core.LogEventListener;
import java.io.InputStream;

public interface LogEventBridge<T extends InputStream> {
    void logEvents(final T inputStream, final LogEventListener logEventListener) throws IOException;
    
    T wrapStream(final InputStream inputStream) throws IOException;
}
