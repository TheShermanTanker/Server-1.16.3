package org.apache.commons.io.input;

public interface TailerListener {
    void init(final Tailer tailer);
    
    void fileNotFound();
    
    void fileRotated();
    
    void handle(final String string);
    
    void handle(final Exception exception);
}
