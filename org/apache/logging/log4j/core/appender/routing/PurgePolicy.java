package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.LogEvent;

public interface PurgePolicy {
    void purge();
    
    void update(final String string, final LogEvent logEvent);
    
    void initialize(final RoutingAppender routingAppender);
}
