package io.netty.handler.stream;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import java.io.InputStream;
import java.io.PushbackInputStream;
import io.netty.buffer.ByteBuf;

public class ChunkedStream implements ChunkedInput<ByteBuf> {
    static final int DEFAULT_CHUNK_SIZE = 8192;
    private final PushbackInputStream in;
    private final int chunkSize;
    private long offset;
    private boolean closed;
    
    public ChunkedStream(final InputStream in) {
        this(in, 8192);
    }
    
    public ChunkedStream(final InputStream in, final int chunkSize) {
        if (in == null) {
            throw new NullPointerException("in");
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("chunkSize: ").append(chunkSize).append(" (expected: a positive integer)").toString());
        }
        if (in instanceof PushbackInputStream) {
            this.in = (PushbackInputStream)in;
        }
        else {
            this.in = new PushbackInputStream(in);
        }
        this.chunkSize = chunkSize;
    }
    
    public long transferredBytes() {
        return this.offset;
    }
    
    public boolean isEndOfInput() throws Exception {
        if (this.closed) {
            return true;
        }
        final int b = this.in.read();
        if (b < 0) {
            return true;
        }
        this.in.unread(b);
        return false;
    }
    
    public void close() throws Exception {
        this.closed = true;
        this.in.close();
    }
    
    @Deprecated
    public ByteBuf readChunk(final ChannelHandlerContext ctx) throws Exception {
        return this.readChunk(ctx.alloc());
    }
    
    public ByteBuf readChunk(final ByteBufAllocator allocator) throws Exception {
        if (this.isEndOfInput()) {
            return null;
        }
        final int availableBytes = this.in.available();
        int chunkSize;
        if (availableBytes <= 0) {
            chunkSize = this.chunkSize;
        }
        else {
            chunkSize = Math.min(this.chunkSize, this.in.available());
        }
        boolean release = true;
        final ByteBuf buffer = allocator.buffer(chunkSize);
        try {
            this.offset += buffer.writeBytes((InputStream)this.in, chunkSize);
            release = false;
            return buffer;
        }
        finally {
            if (release) {
                buffer.release();
            }
        }
    }
    
    public long length() {
        return -1L;
    }
    
    public long progress() {
        return this.offset;
    }
}
