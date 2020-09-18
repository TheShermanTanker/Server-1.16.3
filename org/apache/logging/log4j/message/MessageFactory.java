package org.apache.logging.log4j.message;

public interface MessageFactory {
    Message newMessage(final Object object);
    
    Message newMessage(final String string);
    
    Message newMessage(final String string, final Object... arr);
}
