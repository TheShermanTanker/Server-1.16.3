package com.google.common.hash;

import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.OutputStream;
import com.google.common.annotations.Beta;
import java.io.FilterOutputStream;

@Beta
public final class HashingOutputStream extends FilterOutputStream {
    private final Hasher hasher;
    
    public HashingOutputStream(final HashFunction hashFunction, final OutputStream out) {
        super((OutputStream)Preconditions.<OutputStream>checkNotNull(out));
        this.hasher = Preconditions.<Hasher>checkNotNull(hashFunction.newHasher());
    }
    
    public void write(final int b) throws IOException {
        this.hasher.putByte((byte)b);
        this.out.write(b);
    }
    
    public void write(final byte[] bytes, final int off, final int len) throws IOException {
        this.hasher.putBytes(bytes, off, len);
        this.out.write(bytes, off, len);
    }
    
    public HashCode hash() {
        return this.hasher.hash();
    }
    
    public void close() throws IOException {
        this.out.close();
    }
}
