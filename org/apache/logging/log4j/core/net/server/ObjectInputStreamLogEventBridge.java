package org.apache.logging.log4j.core.net.server;

import java.io.InputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LogEventListener;
import java.io.ObjectInputStream;

public class ObjectInputStreamLogEventBridge extends AbstractLogEventBridge<ObjectInputStream> {
    public void logEvents(final ObjectInputStream inputStream, final LogEventListener logEventListener) throws IOException {
        try {
            logEventListener.log((LogEvent)inputStream.readObject());
        }
        catch (ClassNotFoundException e) {
            throw new IOException((Throwable)e);
        }
    }
    
    @Override
    public ObjectInputStream wrapStream(final InputStream inputStream) throws IOException {
        return new ObjectInputStream(inputStream);
    }
}
