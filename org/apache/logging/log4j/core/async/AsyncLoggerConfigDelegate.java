package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.jmx.RingBufferAdmin;

public interface AsyncLoggerConfigDelegate {
    RingBufferAdmin createRingBufferAdmin(final String string1, final String string2);
    
    EventRoute getEventRoute(final Level level);
    
    void enqueueEvent(final LogEvent logEvent, final AsyncLoggerConfig asyncLoggerConfig);
    
    boolean tryEnqueue(final LogEvent logEvent, final AsyncLoggerConfig asyncLoggerConfig);
    
    void setLogEventFactory(final LogEventFactory logEventFactory);
}
