package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class NullInputStream extends InputStream {
    private final long size;
    private long position;
    private long mark;
    private long readlimit;
    private boolean eof;
    private final boolean throwEofException;
    private final boolean markSupported;
    
    public NullInputStream(final long size) {
        this(size, true, false);
    }
    
    public NullInputStream(final long size, final boolean markSupported, final boolean throwEofException) {
        this.mark = -1L;
        this.size = size;
        this.markSupported = markSupported;
        this.throwEofException = throwEofException;
    }
    
    public long getPosition() {
        return this.position;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public int available() {
        final long avail = this.size - this.position;
        if (avail <= 0L) {
            return 0;
        }
        if (avail > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)avail;
    }
    
    public void close() throws IOException {
        this.eof = false;
        this.position = 0L;
        this.mark = -1L;
    }
    
    public synchronized void mark(final int readlimit) {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        this.mark = this.position;
        this.readlimit = readlimit;
    }
    
    public boolean markSupported() {
        return this.markSupported;
    }
    
    public int read() throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        ++this.position;
        return this.processByte();
    }
    
    public int read(final byte[] bytes) throws IOException {
        return this.read(bytes, 0, bytes.length);
    }
    
    public int read(final byte[] bytes, final int offset, final int length) throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        this.position += length;
        int returnLength = length;
        if (this.position > this.size) {
            returnLength = length - (int)(this.position - this.size);
            this.position = this.size;
        }
        this.processBytes(bytes, offset, returnLength);
        return returnLength;
    }
    
    public synchronized void reset() throws IOException {
        if (!this.markSupported) {
            throw new UnsupportedOperationException("Mark not supported");
        }
        if (this.mark < 0L) {
            throw new IOException("No position has been marked");
        }
        if (this.position > this.mark + this.readlimit) {
            throw new IOException(new StringBuilder().append("Marked position [").append(this.mark).append("] is no longer valid - passed the read limit [").append(this.readlimit).append("]").toString());
        }
        this.position = this.mark;
        this.eof = false;
    }
    
    public long skip(final long numberOfBytes) throws IOException {
        if (this.eof) {
            throw new IOException("Skip after end of file");
        }
        if (this.position == this.size) {
            return this.doEndOfFile();
        }
        this.position += numberOfBytes;
        long returnLength = numberOfBytes;
        if (this.position > this.size) {
            returnLength = numberOfBytes - (this.position - this.size);
            this.position = this.size;
        }
        return returnLength;
    }
    
    protected int processByte() {
        return 0;
    }
    
    protected void processBytes(final byte[] bytes, final int offset, final int length) {
    }
    
    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (this.throwEofException) {
            throw new EOFException();
        }
        return -1;
    }
}
