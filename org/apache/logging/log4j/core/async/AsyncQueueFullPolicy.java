package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.Level;

public interface AsyncQueueFullPolicy {
    EventRoute getRoute(final long long1, final Level level);
}
