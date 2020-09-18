package io.netty.buffer;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import io.netty.util.Recycler;
import java.nio.ByteBuffer;

final class PooledDirectByteBuf extends PooledByteBuf<ByteBuffer> {
    private static final Recycler<PooledDirectByteBuf> RECYCLER;
    
    static PooledDirectByteBuf newInstance(final int maxCapacity) {
        final PooledDirectByteBuf buf = PooledDirectByteBuf.RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }
    
    private PooledDirectByteBuf(final Recycler.Handle<PooledDirectByteBuf> recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    protected ByteBuffer newInternalNioBuffer(final ByteBuffer memory) {
        return memory.duplicate();
    }
    
    @Override
    public boolean isDirect() {
        return true;
    }
    
    @Override
    protected byte _getByte(final int index) {
        return ((ByteBuffer)this.memory).get(this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return ((ByteBuffer)this.memory).getShort(this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return ByteBufUtil.swapShort(this._getShort(index));
    }
    
    @Override
    protected int _getUnsignedMedium(int index) {
        index = this.idx(index);
        return (((ByteBuffer)this.memory).get(index) & 0xFF) << 16 | (((ByteBuffer)this.memory).get(index + 1) & 0xFF) << 8 | (((ByteBuffer)this.memory).get(index + 2) & 0xFF);
    }
    
    @Override
    protected int _getUnsignedMediumLE(int index) {
        index = this.idx(index);
        return (((ByteBuffer)this.memory).get(index) & 0xFF) | (((ByteBuffer)this.memory).get(index + 1) & 0xFF) << 8 | (((ByteBuffer)this.memory).get(index + 2) & 0xFF) << 16;
    }
    
    @Override
    protected int _getInt(final int index) {
        return ((ByteBuffer)this.memory).getInt(this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return ByteBufUtil.swapInt(this._getInt(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return ((ByteBuffer)this.memory).getLong(this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return ByteBufUtil.swapLong(this._getLong(index));
    }
    
    @Override
    public ByteBuf getBytes(int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.checkDstIndex(index, length, dstIndex, dst.capacity());
        if (dst.hasArray()) {
            this.getBytes(index, dst.array(), dst.arrayOffset() + dstIndex, length);
        }
        else if (dst.nioBufferCount() > 0) {
            for (final ByteBuffer bb : dst.nioBuffers(dstIndex, length)) {
                final int bbLen = bb.remaining();
                this.getBytes(index, bb);
                index += bbLen;
            }
        }
        else {
            dst.setBytes(dstIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.getBytes(index, dst, dstIndex, length, false);
        return this;
    }
    
    private void getBytes(int index, final byte[] dst, final int dstIndex, final int length, final boolean internal) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.get(dst, dstIndex, length);
    }
    
    @Override
    public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, dstIndex, length, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.getBytes(index, dst, false);
        return this;
    }
    
    private void getBytes(int index, final ByteBuffer dst, final boolean internal) {
        this.checkIndex(index, dst.remaining());
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + dst.remaining());
        dst.put(tmpBuf);
    }
    
    @Override
    public ByteBuf readBytes(final ByteBuffer dst) {
        final int length = dst.remaining();
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, dst, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public ByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.getBytes(index, out, length, false);
        return this;
    }
    
    private void getBytes(final int index, final OutputStream out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return;
        }
        ByteBufUtil.readBytes(this.alloc(), internal ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate(), this.idx(index), length, out);
    }
    
    @Override
    public ByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.checkReadableBytes(length);
        this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += length;
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.getBytes(index, out, length, false);
    }
    
    private int getBytes(int index, final GatheringByteChannel out, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return 0;
        }
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        }
        else {
            tmpBuf = ((ByteBuffer)this.memory).duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf);
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.getBytes(index, out, position, length, false);
    }
    
    private int getBytes(int index, final FileChannel out, final long position, final int length, final boolean internal) throws IOException {
        this.checkIndex(index, length);
        if (length == 0) {
            return 0;
        }
        final ByteBuffer tmpBuf = internal ? this.internalNioBuffer() : ((ByteBuffer)this.memory).duplicate();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        return out.write(tmpBuf, position);
    }
    
    @Override
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        this.checkReadableBytes(length);
        final int readBytes = this.getBytes(this.readerIndex, out, position, length, true);
        this.readerIndex += readBytes;
        return readBytes;
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        ((ByteBuffer)this.memory).put(this.idx(index), (byte)value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        ((ByteBuffer)this.memory).putShort(this.idx(index), (short)value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        this._setShort(index, ByteBufUtil.swapShort((short)value));
    }
    
    @Override
    protected void _setMedium(int index, final int value) {
        index = this.idx(index);
        ((ByteBuffer)this.memory).put(index, (byte)(value >>> 16));
        ((ByteBuffer)this.memory).put(index + 1, (byte)(value >>> 8));
        ((ByteBuffer)this.memory).put(index + 2, (byte)value);
    }
    
    @Override
    protected void _setMediumLE(int index, final int value) {
        index = this.idx(index);
        ((ByteBuffer)this.memory).put(index, (byte)value);
        ((ByteBuffer)this.memory).put(index + 1, (byte)(value >>> 8));
        ((ByteBuffer)this.memory).put(index + 2, (byte)(value >>> 16));
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        ((ByteBuffer)this.memory).putInt(this.idx(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        this._setInt(index, ByteBufUtil.swapInt(value));
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        ((ByteBuffer)this.memory).putLong(this.idx(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        this._setLong(index, ByteBufUtil.swapLong(value));
    }
    
    @Override
    public ByteBuf setBytes(int index, final ByteBuf src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.capacity());
        if (src.hasArray()) {
            this.setBytes(index, src.array(), src.arrayOffset() + srcIndex, length);
        }
        else if (src.nioBufferCount() > 0) {
            for (final ByteBuffer bb : src.nioBuffers(srcIndex, length)) {
                final int bbLen = bb.remaining();
                this.setBytes(index, bb);
                index += bbLen;
            }
        }
        else {
            src.getBytes(srcIndex, this, index, length);
        }
        return this;
    }
    
    @Override
    public ByteBuf setBytes(int index, final byte[] src, final int srcIndex, final int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.put(src, srcIndex, length);
        return this;
    }
    
    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        this.checkIndex(index, src.remaining());
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        if (src == tmpBuf) {
            src = src.duplicate();
        }
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + src.remaining());
        tmpBuf.put(src);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        this.checkIndex(index, length);
        final byte[] tmp = new byte[length];
        final int readBytes = in.read(tmp);
        if (readBytes <= 0) {
            return readBytes;
        }
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(this.idx(index));
        tmpBuf.put(tmp, 0, readBytes);
        return readBytes;
    }
    
    @Override
    public int setBytes(int index, final ScatteringByteChannel in, final int length) throws IOException {
        this.checkIndex(index, length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf);
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public int setBytes(int index, final FileChannel in, final long position, final int length) throws IOException {
        this.checkIndex(index, length);
        final ByteBuffer tmpBuf = this.internalNioBuffer();
        index = this.idx(index);
        tmpBuf.clear().position(index).limit(index + length);
        try {
            return in.read(tmpBuf, position);
        }
        catch (ClosedChannelException ignored) {
            return -1;
        }
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        this.checkIndex(index, length);
        final ByteBuf copy = this.alloc().directBuffer(length, this.maxCapacity());
        copy.writeBytes(this, index, length);
        return copy;
    }
    
    @Override
    public int nioBufferCount() {
        return 1;
    }
    
    @Override
    public ByteBuffer nioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return ((ByteBuffer)((ByteBuffer)this.memory).duplicate().position(index).limit(index + length)).slice();
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return new ByteBuffer[] { this.nioBuffer(index, length) };
    }
    
    @Override
    public ByteBuffer internalNioBuffer(int index, final int length) {
        this.checkIndex(index, length);
        index = this.idx(index);
        return (ByteBuffer)this.internalNioBuffer().clear().position(index).limit(index + length);
    }
    
    @Override
    public boolean hasArray() {
        return false;
    }
    
    @Override
    public byte[] array() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public int arrayOffset() {
        throw new UnsupportedOperationException("direct buffer");
    }
    
    @Override
    public boolean hasMemoryAddress() {
        return false;
    }
    
    @Override
    public long memoryAddress() {
        throw new UnsupportedOperationException();
    }
    
    static {
        RECYCLER = new Recycler<PooledDirectByteBuf>() {
            @Override
            protected PooledDirectByteBuf newObject(final Handle<PooledDirectByteBuf> handle) {
                return new PooledDirectByteBuf(handle, 0, null);
            }
        };
    }
}
