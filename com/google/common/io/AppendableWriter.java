package com.google.common.io;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.io.Writer;

@GwtIncompatible
class AppendableWriter extends Writer {
    private final Appendable target;
    private boolean closed;
    
    AppendableWriter(final Appendable target) {
        this.target = Preconditions.<Appendable>checkNotNull(target);
    }
    
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.checkNotClosed();
        this.target.append((CharSequence)new String(cbuf, off, len));
    }
    
    public void flush() throws IOException {
        this.checkNotClosed();
        if (this.target instanceof Flushable) {
            ((Flushable)this.target).flush();
        }
    }
    
    public void close() throws IOException {
        this.closed = true;
        if (this.target instanceof Closeable) {
            ((Closeable)this.target).close();
        }
    }
    
    public void write(final int c) throws IOException {
        this.checkNotClosed();
        this.target.append((char)c);
    }
    
    public void write(@Nullable final String str) throws IOException {
        this.checkNotClosed();
        this.target.append((CharSequence)str);
    }
    
    public void write(@Nullable final String str, final int off, final int len) throws IOException {
        this.checkNotClosed();
        this.target.append((CharSequence)str, off, off + len);
    }
    
    public Writer append(final char c) throws IOException {
        this.checkNotClosed();
        this.target.append(c);
        return this;
    }
    
    public Writer append(@Nullable final CharSequence charSeq) throws IOException {
        this.checkNotClosed();
        this.target.append(charSeq);
        return this;
    }
    
    public Writer append(@Nullable final CharSequence charSeq, final int start, final int end) throws IOException {
        this.checkNotClosed();
        this.target.append(charSeq, start, end);
        return this;
    }
    
    private void checkNotClosed() throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed writer.");
        }
    }
}
