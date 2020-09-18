package io.netty.handler.stream;

import java.nio.channels.ScatteringByteChannel;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.nio.channels.FileChannel;
import io.netty.buffer.ByteBuf;

public class ChunkedNioFile implements ChunkedInput<ByteBuf> {
    private final FileChannel in;
    private final long startOffset;
    private final long endOffset;
    private final int chunkSize;
    private long offset;
    
    public ChunkedNioFile(final File in) throws IOException {
        this(new FileInputStream(in).getChannel());
    }
    
    public ChunkedNioFile(final File in, final int chunkSize) throws IOException {
        this(new FileInputStream(in).getChannel(), chunkSize);
    }
    
    public ChunkedNioFile(final FileChannel in) throws IOException {
        this(in, 8192);
    }
    
    public ChunkedNioFile(final FileChannel in, final int chunkSize) throws IOException {
        this(in, 0L, in.size(), chunkSize);
    }
    
    public ChunkedNioFile(final FileChannel in, final long offset, final long length, final int chunkSize) throws IOException {
        if (in == null) {
            throw new NullPointerException("in");
        }
        if (offset < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("offset: ").append(offset).append(" (expected: 0 or greater)").toString());
        }
        if (length < 0L) {
            throw new IllegalArgumentException(new StringBuilder().append("length: ").append(length).append(" (expected: 0 or greater)").toString());
        }
        if (chunkSize <= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("chunkSize: ").append(chunkSize).append(" (expected: a positive integer)").toString());
        }
        if (offset != 0L) {
            in.position(offset);
        }
        this.in = in;
        this.chunkSize = chunkSize;
        this.startOffset = offset;
        this.offset = offset;
        this.endOffset = offset + length;
    }
    
    public long startOffset() {
        return this.startOffset;
    }
    
    public long endOffset() {
        return this.endOffset;
    }
    
    public long currentOffset() {
        return this.offset;
    }
    
    public boolean isEndOfInput() throws Exception {
        return this.offset >= this.endOffset || !this.in.isOpen();
    }
    
    public void close() throws Exception {
        this.in.close();
    }
    
    @Deprecated
    public ByteBuf readChunk(final ChannelHandlerContext ctx) throws Exception {
        return this.readChunk(ctx.alloc());
    }
    
    public ByteBuf readChunk(final ByteBufAllocator allocator) throws Exception {
        final long offset = this.offset;
        if (offset >= this.endOffset) {
            return null;
        }
        final int chunkSize = (int)Math.min((long)this.chunkSize, this.endOffset - offset);
        final ByteBuf buffer = allocator.buffer(chunkSize);
        boolean release = true;
        try {
            int readBytes = 0;
            do {
                final int localReadBytes = buffer.writeBytes((ScatteringByteChannel)this.in, chunkSize - readBytes);
                if (localReadBytes < 0) {
                    break;
                }
                readBytes += localReadBytes;
            } while (readBytes != chunkSize);
            this.offset += readBytes;
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
        return this.endOffset - this.startOffset;
    }
    
    public long progress() {
        return this.offset - this.startOffset;
    }
}
