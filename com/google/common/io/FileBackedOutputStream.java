package com.google.common.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.io.OutputStream;

@Beta
@GwtIncompatible
public final class FileBackedOutputStream extends OutputStream {
    private final int fileThreshold;
    private final boolean resetOnFinalize;
    private final ByteSource source;
    private OutputStream out;
    private MemoryOutput memory;
    private File file;
    
    @VisibleForTesting
    synchronized File getFile() {
        return this.file;
    }
    
    public FileBackedOutputStream(final int fileThreshold) {
        this(fileThreshold, false);
    }
    
    public FileBackedOutputStream(final int fileThreshold, final boolean resetOnFinalize) {
        this.fileThreshold = fileThreshold;
        this.resetOnFinalize = resetOnFinalize;
        this.memory = new MemoryOutput();
        this.out = (OutputStream)this.memory;
        if (resetOnFinalize) {
            this.source = new ByteSource() {
                @Override
                public InputStream openStream() throws IOException {
                    return FileBackedOutputStream.this.openInputStream();
                }
                
                protected void finalize() {
                    try {
                        FileBackedOutputStream.this.reset();
                    }
                    catch (Throwable t) {
                        t.printStackTrace(System.err);
                    }
                }
            };
        }
        else {
            this.source = new ByteSource() {
                @Override
                public InputStream openStream() throws IOException {
                    return FileBackedOutputStream.this.openInputStream();
                }
            };
        }
    }
    
    public ByteSource asByteSource() {
        return this.source;
    }
    
    private synchronized InputStream openInputStream() throws IOException {
        if (this.file != null) {
            return (InputStream)new FileInputStream(this.file);
        }
        return (InputStream)new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
    }
    
    public synchronized void reset() throws IOException {
        try {
            this.close();
        }
        finally {
            if (this.memory == null) {
                this.memory = new MemoryOutput();
            }
            else {
                this.memory.reset();
            }
            this.out = (OutputStream)this.memory;
            if (this.file != null) {
                final File deleteMe = this.file;
                this.file = null;
                if (!deleteMe.delete()) {
                    throw new IOException(new StringBuilder().append("Could not delete: ").append(deleteMe).toString());
                }
            }
        }
    }
    
    public synchronized void write(final int b) throws IOException {
        this.update(1);
        this.out.write(b);
    }
    
    public synchronized void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    public synchronized void write(final byte[] b, final int off, final int len) throws IOException {
        this.update(len);
        this.out.write(b, off, len);
    }
    
    public synchronized void close() throws IOException {
        this.out.close();
    }
    
    public synchronized void flush() throws IOException {
        this.out.flush();
    }
    
    private void update(final int len) throws IOException {
        if (this.file == null && this.memory.getCount() + len > this.fileThreshold) {
            final File temp = File.createTempFile("FileBackedOutputStream", (String)null);
            if (this.resetOnFinalize) {
                temp.deleteOnExit();
            }
            final FileOutputStream transfer = new FileOutputStream(temp);
            transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
            transfer.flush();
            this.out = (OutputStream)transfer;
            this.file = temp;
            this.memory = null;
        }
    }
    
    private static class MemoryOutput extends ByteArrayOutputStream {
        byte[] getBuffer() {
            return this.buf;
        }
        
        int getCount() {
            return this.count;
        }
    }
}
