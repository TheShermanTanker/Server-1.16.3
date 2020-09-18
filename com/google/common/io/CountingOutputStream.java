package com.google.common.io;

import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.FilterOutputStream;

@Beta
@GwtIncompatible
public final class CountingOutputStream extends FilterOutputStream {
    private long count;
    
    public CountingOutputStream(final OutputStream out) {
        super((OutputStream)Preconditions.<OutputStream>checkNotNull(out));
    }
    
    public long getCount() {
        return this.count;
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
        this.count += len;
    }
    
    public void write(final int b) throws IOException {
        this.out.write(b);
        ++this.count;
    }
    
    public void close() throws IOException {
        this.out.close();
    }
}
