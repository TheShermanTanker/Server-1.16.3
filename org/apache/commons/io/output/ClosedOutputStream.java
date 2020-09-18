package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class ClosedOutputStream extends OutputStream {
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM;
    
    public void write(final int b) throws IOException {
        throw new IOException(new StringBuilder().append("write(").append(b).append(") failed: stream is closed").toString());
    }
    
    static {
        CLOSED_OUTPUT_STREAM = new ClosedOutputStream();
    }
}
