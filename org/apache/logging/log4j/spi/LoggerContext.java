package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext {
    Object getExternalContext();
    
    ExtendedLogger getLogger(final String string);
    
    ExtendedLogger getLogger(final String string, final MessageFactory messageFactory);
    
    boolean hasLogger(final String string);
    
    boolean hasLogger(final String string, final MessageFactory messageFactory);
    
    boolean hasLogger(final String string, final Class<? extends MessageFactory> class2);
}
