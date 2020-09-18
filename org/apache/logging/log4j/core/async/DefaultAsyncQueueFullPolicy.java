package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public class DefaultAsyncQueueFullPolicy implements AsyncQueueFullPolicy {
    public EventRoute getRoute(final long backgroundThreadId, final Level level) {
        return EventRoute.SYNCHRONOUS;
    }
}
