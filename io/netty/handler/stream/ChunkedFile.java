package io.netty.handler.stream;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import io.netty.buffer.ByteBuf;

public class ChunkedFile implements ChunkedInput<ByteBuf> {
    private final RandomAccessFile file;
    private final long startOffset;
    private final long endOffset;
    private final int chunkSize;
    private long offset;
    
    public ChunkedFile(final File file) throws IOException {
        this(file, 8192);
    }
    
    public ChunkedFile(final File file, final int chunkSize) throws IOException {
        this(new RandomAccessFile(file, "r"), chunkSize);
    }
    
    public ChunkedFile(final RandomAccessFile file) throws IOException {
        this(file, 8192);
    }
    
    public ChunkedFile(final RandomAccessFile file, final int chunkSize) throws IOException {
        this(file, 0L, file.length(), chunkSize);
    }
    
    public ChunkedFile(final RandomAccessFile file, final long offset, final long length, final int chunkSize) throws IOException {
        if (file == null) {
            throw new NullPointerException("file");
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
        this.file = file;
        this.startOffset = offset;
        this.offset = offset;
        this.endOffset = offset + length;
        this.chunkSize = chunkSize;
        file.seek(offset);
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
        return this.offset >= this.endOffset || !this.file.getChannel().isOpen();
    }
    
    public void close() throws Exception {
        this.file.close();
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
        final ByteBuf buf = allocator.heapBuffer(chunkSize);
        boolean release = true;
        try {
            this.file.readFully(buf.array(), buf.arrayOffset(), chunkSize);
            buf.writerIndex(chunkSize);
            this.offset = offset + chunkSize;
            release = false;
            return buf;
        }
        finally {
            if (release) {
                buf.release();
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
