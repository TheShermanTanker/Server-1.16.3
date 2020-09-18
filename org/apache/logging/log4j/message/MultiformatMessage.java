package org.apache.logging.log4j.message;

public interface MultiformatMessage extends Message {
    String getFormattedMessage(final String[] arr);
    
    String[] getFormats();
}
