package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {
    private final InheritableThreadLocal<InputStream> m_streams;
    
    public DemuxInputStream() {
        this.m_streams = (InheritableThreadLocal<InputStream>)new InheritableThreadLocal();
    }
    
    public InputStream bindStream(final InputStream input) {
        final InputStream oldValue = (InputStream)this.m_streams.get();
        this.m_streams.set(input);
        return oldValue;
    }
    
    public void close() throws IOException {
        final InputStream input = (InputStream)this.m_streams.get();
        if (null != input) {
            input.close();
        }
    }
    
    public int read() throws IOException {
        final InputStream input = (InputStream)this.m_streams.get();
        if (null != input) {
            return input.read();
        }
        return -1;
    }
}
