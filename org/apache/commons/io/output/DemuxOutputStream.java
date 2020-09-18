package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class DemuxOutputStream extends OutputStream {
    private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal;
    
    public DemuxOutputStream() {
        this.outputStreamThreadLocal = (InheritableThreadLocal<OutputStream>)new InheritableThreadLocal();
    }
    
    public OutputStream bindStream(final OutputStream output) {
        final OutputStream stream = (OutputStream)this.outputStreamThreadLocal.get();
        this.outputStreamThreadLocal.set(output);
        return stream;
    }
    
    public void close() throws IOException {
        final OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.close();
        }
    }
    
    public void flush() throws IOException {
        final OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.flush();
        }
    }
    
    public void write(final int ch) throws IOException {
        final OutputStream output = (OutputStream)this.outputStreamThreadLocal.get();
        if (null != output) {
            output.write(ch);
        }
    }
}
