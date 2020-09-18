package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@CanIgnoreReturnValue
abstract class AbstractByteHasher extends AbstractHasher {
    private final ByteBuffer scratch;
    
    AbstractByteHasher() {
        this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    protected abstract void update(final byte byte1);
    
    protected void update(final byte[] b) {
        this.update(b, 0, b.length);
    }
    
    protected void update(final byte[] b, final int off, final int len) {
        for (int i = off; i < off + len; ++i) {
            this.update(b[i]);
        }
    }
    
    public Hasher putByte(final byte b) {
        this.update(b);
        return this;
    }
    
    public Hasher putBytes(final byte[] bytes) {
        Preconditions.<byte[]>checkNotNull(bytes);
        this.update(bytes);
        return this;
    }
    
    public Hasher putBytes(final byte[] bytes, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        this.update(bytes, off, len);
        return this;
    }
    
    private Hasher update(final int bytes) {
        try {
            this.update(this.scratch.array(), 0, bytes);
        }
        finally {
            this.scratch.clear();
        }
        return this;
    }
    
    public Hasher putShort(final short s) {
        this.scratch.putShort(s);
        return this.update(2);
    }
    
    public Hasher putInt(final int i) {
        this.scratch.putInt(i);
        return this.update(4);
    }
    
    public Hasher putLong(final long l) {
        this.scratch.putLong(l);
        return this.update(8);
    }
    
    public Hasher putChar(final char c) {
        this.scratch.putChar(c);
        return this.update(2);
    }
    
    public <T> Hasher putObject(final T instance, final Funnel<? super T> funnel) {
        funnel.funnel(instance, (PrimitiveSink)this);
        return this;
    }
}
