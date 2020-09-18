package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.Writer;

public class CloseShieldWriter extends Writer {
    private final Writer delegate;
    
    public CloseShieldWriter(final Writer delegate) {
        this.delegate = delegate;
    }
    
    public void close() throws IOException {
    }
    
    public void flush() throws IOException {
        this.delegate.flush();
    }
    
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.delegate.write(cbuf, off, len);
    }
}
