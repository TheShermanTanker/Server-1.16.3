package com.google.common.hash;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.InputStream;
import com.google.common.annotations.Beta;
import java.io.FilterInputStream;

@Beta
public final class HashingInputStream extends FilterInputStream {
    private final Hasher hasher;
    
    public HashingInputStream(final HashFunction hashFunction, final InputStream in) {
        super((InputStream)Preconditions.<InputStream>checkNotNull(in));
        this.hasher = Preconditions.<Hasher>checkNotNull(hashFunction.newHasher());
    }
    
    @CanIgnoreReturnValue
    public int read() throws IOException {
        final int b = this.in.read();
        if (b != -1) {
            this.hasher.putByte((byte)b);
        }
        return b;
    }
    
    @CanIgnoreReturnValue
    public int read(final byte[] bytes, final int off, final int len) throws IOException {
        final int numOfBytesRead = this.in.read(bytes, off, len);
        if (numOfBytesRead != -1) {
            this.hasher.putBytes(bytes, off, numOfBytesRead);
        }
        return numOfBytesRead;
    }
    
    public boolean markSupported() {
        return false;
    }
    
    public void mark(final int readlimit) {
    }
    
    public void reset() throws IOException {
        throw new IOException("reset not supported");
    }
    
    public HashCode hash() {
        return this.hasher.hash();
    }
}
