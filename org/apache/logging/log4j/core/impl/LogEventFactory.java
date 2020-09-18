package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;

public interface LogEventFactory {
    LogEvent createEvent(final String string1, final Marker marker, final String string3, final Level level, final Message message, final List<Property> list, final Throwable throwable);
}
