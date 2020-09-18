package io.netty.buffer;

import io.netty.util.ReferenceCounted;
import java.nio.channels.FileChannel;
import java.io.OutputStream;
import java.util.List;
import java.util.Iterator;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import io.netty.util.ByteProcessor;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class WrappedCompositeByteBuf extends CompositeByteBuf {
    private final CompositeByteBuf wrapped;
    
    WrappedCompositeByteBuf(final CompositeByteBuf wrapped) {
        super(wrapped.alloc());
        this.wrapped = wrapped;
    }
    
    @Override
    public boolean release() {
        return this.wrapped.release();
    }
    
    @Override
    public boolean release(final int decrement) {
        return this.wrapped.release(decrement);
    }
    
    public final int maxCapacity() {
        return this.wrapped.maxCapacity();
    }
    
    public final int readerIndex() {
        return this.wrapped.readerIndex();
    }
    
    public final int writerIndex() {
        return this.wrapped.writerIndex();
    }
    
    public final boolean isReadable() {
        return this.wrapped.isReadable();
    }
    
    public final boolean isReadable(final int numBytes) {
        return this.wrapped.isReadable(numBytes);
    }
    
    public final boolean isWritable() {
        return this.wrapped.isWritable();
    }
    
    public final boolean isWritable(final int numBytes) {
        return this.wrapped.isWritable(numBytes);
    }
    
    public final int readableBytes() {
        return this.wrapped.readableBytes();
    }
    
    public final int writableBytes() {
        return this.wrapped.writableBytes();
    }
    
    public final int maxWritableBytes() {
        return this.wrapped.maxWritableBytes();
    }
    
    public int ensureWritable(final int minWritableBytes, final boolean force) {
        return this.wrapped.ensureWritable(minWritableBytes, force);
    }
    
    public ByteBuf order(final ByteOrder endianness) {
        return this.wrapped.order(endianness);
    }
    
    public boolean getBoolean(final int index) {
        return this.wrapped.getBoolean(index);
    }
    
    public short getUnsignedByte(final int index) {
        return this.wrapped.getUnsignedByte(index);
    }
    
    public short getShort(final int index) {
        return this.wrapped.getShort(index);
    }
    
    public short getShortLE(final int index) {
        return this.wrapped.getShortLE(index);
    }
    
    public int getUnsignedShort(final int index) {
        return this.wrapped.getUnsignedShort(index);
    }
    
    public int getUnsignedShortLE(final int index) {
        return this.wrapped.getUnsignedShortLE(index);
    }
    
    public int getUnsignedMedium(final int index) {
        return this.wrapped.getUnsignedMedium(index);
    }
    
    public int getUnsignedMediumLE(final int index) {
        return this.wrapped.getUnsignedMediumLE(index);
    }
    
    public int getMedium(final int index) {
        return this.wrapped.getMedium(index);
    }
    
    public int getMediumLE(final int index) {
        return this.wrapped.getMediumLE(index);
    }
    
    public int getInt(final int index) {
        return this.wrapped.getInt(index);
    }
    
    public int getIntLE(final int index) {
        return this.wrapped.getIntLE(index);
    }
    
    public long getUnsignedInt(final int index) {
        return this.wrapped.getUnsignedInt(index);
    }
    
    public long getUnsignedIntLE(final int index) {
        return this.wrapped.getUnsignedIntLE(index);
    }
    
    public long getLong(final int index) {
        return this.wrapped.getLong(index);
    }
    
    public long getLongLE(final int index) {
        return this.wrapped.getLongLE(index);
    }
    
    public char getChar(final int index) {
        return this.wrapped.getChar(index);
    }
    
    public float getFloat(final int index) {
        return this.wrapped.getFloat(index);
    }
    
    public double getDouble(final int index) {
        return this.wrapped.getDouble(index);
    }
    
    public ByteBuf setShortLE(final int index, final int value) {
        return this.wrapped.setShortLE(index, value);
    }
    
    public ByteBuf setMediumLE(final int index, final int value) {
        return this.wrapped.setMediumLE(index, value);
    }
    
    public ByteBuf setIntLE(final int index, final int value) {
        return this.wrapped.setIntLE(index, value);
    }
    
    public ByteBuf setLongLE(final int index, final long value) {
        return this.wrapped.setLongLE(index, value);
    }
    
    public byte readByte() {
        return this.wrapped.readByte();
    }
    
    public boolean readBoolean() {
        return this.wrapped.readBoolean();
    }
    
    public short readUnsignedByte() {
        return this.wrapped.readUnsignedByte();
    }
    
    public short readShort() {
        return this.wrapped.readShort();
    }
    
    public short readShortLE() {
        return this.wrapped.readShortLE();
    }
    
    public int readUnsignedShort() {
        return this.wrapped.readUnsignedShort();
    }
    
    public int readUnsignedShortLE() {
        return this.wrapped.readUnsignedShortLE();
    }
    
    public int readMedium() {
        return this.wrapped.readMedium();
    }
    
    public int readMediumLE() {
        return this.wrapped.readMediumLE();
    }
    
    public int readUnsignedMedium() {
        return this.wrapped.readUnsignedMedium();
    }
    
    public int readUnsignedMediumLE() {
        return this.wrapped.readUnsignedMediumLE();
    }
    
    public int readInt() {
        return this.wrapped.readInt();
    }
    
    public int readIntLE() {
        return this.wrapped.readIntLE();
    }
    
    public long readUnsignedInt() {
        return this.wrapped.readUnsignedInt();
    }
    
    public long readUnsignedIntLE() {
        return this.wrapped.readUnsignedIntLE();
    }
    
    public long readLong() {
        return this.wrapped.readLong();
    }
    
    public long readLongLE() {
        return this.wrapped.readLongLE();
    }
    
    public char readChar() {
        return this.wrapped.readChar();
    }
    
    public float readFloat() {
        return this.wrapped.readFloat();
    }
    
    public double readDouble() {
        return this.wrapped.readDouble();
    }
    
    public ByteBuf readBytes(final int length) {
        return this.wrapped.readBytes(length);
    }
    
    public ByteBuf slice() {
        return this.wrapped.slice();
    }
    
    public ByteBuf retainedSlice() {
        return this.wrapped.retainedSlice();
    }
    
    public ByteBuf slice(final int index, final int length) {
        return this.wrapped.slice(index, length);
    }
    
    public ByteBuf retainedSlice(final int index, final int length) {
        return this.wrapped.retainedSlice(index, length);
    }
    
    public ByteBuffer nioBuffer() {
        return this.wrapped.nioBuffer();
    }
    
    public String toString(final Charset charset) {
        return this.wrapped.toString(charset);
    }
    
    public String toString(final int index, final int length, final Charset charset) {
        return this.wrapped.toString(index, length, charset);
    }
    
    public int indexOf(final int fromIndex, final int toIndex, final byte value) {
        return this.wrapped.indexOf(fromIndex, toIndex, value);
    }
    
    public int bytesBefore(final byte value) {
        return this.wrapped.bytesBefore(value);
    }
    
    public int bytesBefore(final int length, final byte value) {
        return this.wrapped.bytesBefore(length, value);
    }
    
    public int bytesBefore(final int index, final int length, final byte value) {
        return this.wrapped.bytesBefore(index, length, value);
    }
    
    public int forEachByte(final ByteProcessor processor) {
        return this.wrapped.forEachByte(processor);
    }
    
    public int forEachByte(final int index, final int length, final ByteProcessor processor) {
        return this.wrapped.forEachByte(index, length, processor);
    }
    
    public int forEachByteDesc(final ByteProcessor processor) {
        return this.wrapped.forEachByteDesc(processor);
    }
    
    public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
        return this.wrapped.forEachByteDesc(index, length, processor);
    }
    
    public final int hashCode() {
        return this.wrapped.hashCode();
    }
    
    public final boolean equals(final Object o) {
        return this.wrapped.equals(o);
    }
    
    public final int compareTo(final ByteBuf that) {
        return this.wrapped.compareTo(that);
    }
    
    @Override
    public final int refCnt() {
        return this.wrapped.refCnt();
    }
    
    public ByteBuf duplicate() {
        return this.wrapped.duplicate();
    }
    
    public ByteBuf retainedDuplicate() {
        return this.wrapped.retainedDuplicate();
    }
    
    public ByteBuf readSlice(final int length) {
        return this.wrapped.readSlice(length);
    }
    
    public ByteBuf readRetainedSlice(final int length) {
        return this.wrapped.readRetainedSlice(length);
    }
    
    public int readBytes(final GatheringByteChannel out, final int length) throws IOException {
        return this.wrapped.readBytes(out, length);
    }
    
    public ByteBuf writeShortLE(final int value) {
        return this.wrapped.writeShortLE(value);
    }
    
    public ByteBuf writeMediumLE(final int value) {
        return this.wrapped.writeMediumLE(value);
    }
    
    public ByteBuf writeIntLE(final int value) {
        return this.wrapped.writeIntLE(value);
    }
    
    public ByteBuf writeLongLE(final long value) {
        return this.wrapped.writeLongLE(value);
    }
    
    public int writeBytes(final InputStream in, final int length) throws IOException {
        return this.wrapped.writeBytes(in, length);
    }
    
    public int writeBytes(final ScatteringByteChannel in, final int length) throws IOException {
        return this.wrapped.writeBytes(in, length);
    }
    
    public ByteBuf copy() {
        return this.wrapped.copy();
    }
    
    @Override
    public CompositeByteBuf addComponent(final ByteBuf buffer) {
        this.wrapped.addComponent(buffer);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final ByteBuf... buffers) {
        this.wrapped.addComponents(buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final Iterable<ByteBuf> buffers) {
        this.wrapped.addComponents(buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponent(final int cIndex, final ByteBuf buffer) {
        this.wrapped.addComponent(cIndex, buffer);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final int cIndex, final ByteBuf... buffers) {
        this.wrapped.addComponents(cIndex, buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final int cIndex, final Iterable<ByteBuf> buffers) {
        this.wrapped.addComponents(cIndex, buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final ByteBuf buffer) {
        this.wrapped.addComponent(increaseWriterIndex, buffer);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final ByteBuf... buffers) {
        this.wrapped.addComponents(increaseWriterIndex, buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponents(final boolean increaseWriterIndex, final Iterable<ByteBuf> buffers) {
        this.wrapped.addComponents(increaseWriterIndex, buffers);
        return this;
    }
    
    @Override
    public CompositeByteBuf addComponent(final boolean increaseWriterIndex, final int cIndex, final ByteBuf buffer) {
        this.wrapped.addComponent(increaseWriterIndex, cIndex, buffer);
        return this;
    }
    
    @Override
    public CompositeByteBuf removeComponent(final int cIndex) {
        this.wrapped.removeComponent(cIndex);
        return this;
    }
    
    @Override
    public CompositeByteBuf removeComponents(final int cIndex, final int numComponents) {
        this.wrapped.removeComponents(cIndex, numComponents);
        return this;
    }
    
    @Override
    public Iterator<ByteBuf> iterator() {
        return this.wrapped.iterator();
    }
    
    @Override
    public List<ByteBuf> decompose(final int offset, final int length) {
        return this.wrapped.decompose(offset, length);
    }
    
    @Override
    public final boolean isDirect() {
        return this.wrapped.isDirect();
    }
    
    @Override
    public final boolean hasArray() {
        return this.wrapped.hasArray();
    }
    
    @Override
    public final byte[] array() {
        return this.wrapped.array();
    }
    
    @Override
    public final int arrayOffset() {
        return this.wrapped.arrayOffset();
    }
    
    @Override
    public final boolean hasMemoryAddress() {
        return this.wrapped.hasMemoryAddress();
    }
    
    @Override
    public final long memoryAddress() {
        return this.wrapped.memoryAddress();
    }
    
    @Override
    public final int capacity() {
        return this.wrapped.capacity();
    }
    
    @Override
    public CompositeByteBuf capacity(final int newCapacity) {
        this.wrapped.capacity(newCapacity);
        return this;
    }
    
    @Override
    public final ByteBufAllocator alloc() {
        return this.wrapped.alloc();
    }
    
    @Override
    public final ByteOrder order() {
        return this.wrapped.order();
    }
    
    @Override
    public final int numComponents() {
        return this.wrapped.numComponents();
    }
    
    @Override
    public final int maxNumComponents() {
        return this.wrapped.maxNumComponents();
    }
    
    @Override
    public final int toComponentIndex(final int offset) {
        return this.wrapped.toComponentIndex(offset);
    }
    
    @Override
    public final int toByteIndex(final int cIndex) {
        return this.wrapped.toByteIndex(cIndex);
    }
    
    @Override
    public byte getByte(final int index) {
        return this.wrapped.getByte(index);
    }
    
    @Override
    protected final byte _getByte(final int index) {
        return this.wrapped._getByte(index);
    }
    
    @Override
    protected final short _getShort(final int index) {
        return this.wrapped._getShort(index);
    }
    
    @Override
    protected final short _getShortLE(final int index) {
        return this.wrapped._getShortLE(index);
    }
    
    @Override
    protected final int _getUnsignedMedium(final int index) {
        return this.wrapped._getUnsignedMedium(index);
    }
    
    @Override
    protected final int _getUnsignedMediumLE(final int index) {
        return this.wrapped._getUnsignedMediumLE(index);
    }
    
    @Override
    protected final int _getInt(final int index) {
        return this.wrapped._getInt(index);
    }
    
    @Override
    protected final int _getIntLE(final int index) {
        return this.wrapped._getIntLE(index);
    }
    
    @Override
    protected final long _getLong(final int index) {
        return this.wrapped._getLong(index);
    }
    
    @Override
    protected final long _getLongLE(final int index) {
        return this.wrapped._getLongLE(index);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
        this.wrapped.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuffer dst) {
        this.wrapped.getBytes(index, dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
        this.wrapped.getBytes(index, dst, dstIndex, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final GatheringByteChannel out, final int length) throws IOException {
        return this.wrapped.getBytes(index, out, length);
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final OutputStream out, final int length) throws IOException {
        this.wrapped.getBytes(index, out, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf setByte(final int index, final int value) {
        this.wrapped.setByte(index, value);
        return this;
    }
    
    @Override
    protected final void _setByte(final int index, final int value) {
        this.wrapped._setByte(index, value);
    }
    
    @Override
    public CompositeByteBuf setShort(final int index, final int value) {
        this.wrapped.setShort(index, value);
        return this;
    }
    
    @Override
    protected final void _setShort(final int index, final int value) {
        this.wrapped._setShort(index, value);
    }
    
    @Override
    protected final void _setShortLE(final int index, final int value) {
        this.wrapped._setShortLE(index, value);
    }
    
    @Override
    public CompositeByteBuf setMedium(final int index, final int value) {
        this.wrapped.setMedium(index, value);
        return this;
    }
    
    @Override
    protected final void _setMedium(final int index, final int value) {
        this.wrapped._setMedium(index, value);
    }
    
    @Override
    protected final void _setMediumLE(final int index, final int value) {
        this.wrapped._setMediumLE(index, value);
    }
    
    @Override
    public CompositeByteBuf setInt(final int index, final int value) {
        this.wrapped.setInt(index, value);
        return this;
    }
    
    @Override
    protected final void _setInt(final int index, final int value) {
        this.wrapped._setInt(index, value);
    }
    
    @Override
    protected final void _setIntLE(final int index, final int value) {
        this.wrapped._setIntLE(index, value);
    }
    
    @Override
    public CompositeByteBuf setLong(final int index, final long value) {
        this.wrapped.setLong(index, value);
        return this;
    }
    
    @Override
    protected final void _setLong(final int index, final long value) {
        this.wrapped._setLong(index, value);
    }
    
    @Override
    protected final void _setLongLE(final int index, final long value) {
        this.wrapped._setLongLE(index, value);
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
        this.wrapped.setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuffer src) {
        this.wrapped.setBytes(index, src);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
        this.wrapped.setBytes(index, src, srcIndex, length);
        return this;
    }
    
    @Override
    public int setBytes(final int index, final InputStream in, final int length) throws IOException {
        return this.wrapped.setBytes(index, in, length);
    }
    
    @Override
    public int setBytes(final int index, final ScatteringByteChannel in, final int length) throws IOException {
        return this.wrapped.setBytes(index, in, length);
    }
    
    @Override
    public ByteBuf copy(final int index, final int length) {
        return this.wrapped.copy(index, length);
    }
    
    @Override
    public final ByteBuf component(final int cIndex) {
        return this.wrapped.component(cIndex);
    }
    
    @Override
    public final ByteBuf componentAtOffset(final int offset) {
        return this.wrapped.componentAtOffset(offset);
    }
    
    @Override
    public final ByteBuf internalComponent(final int cIndex) {
        return this.wrapped.internalComponent(cIndex);
    }
    
    @Override
    public final ByteBuf internalComponentAtOffset(final int offset) {
        return this.wrapped.internalComponentAtOffset(offset);
    }
    
    @Override
    public int nioBufferCount() {
        return this.wrapped.nioBufferCount();
    }
    
    @Override
    public ByteBuffer internalNioBuffer(final int index, final int length) {
        return this.wrapped.internalNioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer nioBuffer(final int index, final int length) {
        return this.wrapped.nioBuffer(index, length);
    }
    
    @Override
    public ByteBuffer[] nioBuffers(final int index, final int length) {
        return this.wrapped.nioBuffers(index, length);
    }
    
    @Override
    public CompositeByteBuf consolidate() {
        this.wrapped.consolidate();
        return this;
    }
    
    @Override
    public CompositeByteBuf consolidate(final int cIndex, final int numComponents) {
        this.wrapped.consolidate(cIndex, numComponents);
        return this;
    }
    
    @Override
    public CompositeByteBuf discardReadComponents() {
        this.wrapped.discardReadComponents();
        return this;
    }
    
    @Override
    public CompositeByteBuf discardReadBytes() {
        this.wrapped.discardReadBytes();
        return this;
    }
    
    @Override
    public final String toString() {
        return this.wrapped.toString();
    }
    
    @Override
    public final CompositeByteBuf readerIndex(final int readerIndex) {
        this.wrapped.readerIndex(readerIndex);
        return this;
    }
    
    @Override
    public final CompositeByteBuf writerIndex(final int writerIndex) {
        this.wrapped.writerIndex(writerIndex);
        return this;
    }
    
    @Override
    public final CompositeByteBuf setIndex(final int readerIndex, final int writerIndex) {
        this.wrapped.setIndex(readerIndex, writerIndex);
        return this;
    }
    
    @Override
    public final CompositeByteBuf clear() {
        this.wrapped.clear();
        return this;
    }
    
    @Override
    public final CompositeByteBuf markReaderIndex() {
        this.wrapped.markReaderIndex();
        return this;
    }
    
    @Override
    public final CompositeByteBuf resetReaderIndex() {
        this.wrapped.resetReaderIndex();
        return this;
    }
    
    @Override
    public final CompositeByteBuf markWriterIndex() {
        this.wrapped.markWriterIndex();
        return this;
    }
    
    @Override
    public final CompositeByteBuf resetWriterIndex() {
        this.wrapped.resetWriterIndex();
        return this;
    }
    
    @Override
    public CompositeByteBuf ensureWritable(final int minWritableBytes) {
        this.wrapped.ensureWritable(minWritableBytes);
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst) {
        this.wrapped.getBytes(index, dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
        this.wrapped.getBytes(index, dst, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf getBytes(final int index, final byte[] dst) {
        this.wrapped.getBytes(index, dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBoolean(final int index, final boolean value) {
        this.wrapped.setBoolean(index, value);
        return this;
    }
    
    @Override
    public CompositeByteBuf setChar(final int index, final int value) {
        this.wrapped.setChar(index, value);
        return this;
    }
    
    @Override
    public CompositeByteBuf setFloat(final int index, final float value) {
        this.wrapped.setFloat(index, value);
        return this;
    }
    
    @Override
    public CompositeByteBuf setDouble(final int index, final double value) {
        this.wrapped.setDouble(index, value);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src) {
        this.wrapped.setBytes(index, src);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final ByteBuf src, final int length) {
        this.wrapped.setBytes(index, src, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf setBytes(final int index, final byte[] src) {
        this.wrapped.setBytes(index, src);
        return this;
    }
    
    @Override
    public CompositeByteBuf setZero(final int index, final int length) {
        this.wrapped.setZero(index, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst) {
        this.wrapped.readBytes(dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int length) {
        this.wrapped.readBytes(dst, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
        this.wrapped.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst) {
        this.wrapped.readBytes(dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
        this.wrapped.readBytes(dst, dstIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final ByteBuffer dst) {
        this.wrapped.readBytes(dst);
        return this;
    }
    
    @Override
    public CompositeByteBuf readBytes(final OutputStream out, final int length) throws IOException {
        this.wrapped.readBytes(out, length);
        return this;
    }
    
    @Override
    public int getBytes(final int index, final FileChannel out, final long position, final int length) throws IOException {
        return this.wrapped.getBytes(index, out, position, length);
    }
    
    @Override
    public int setBytes(final int index, final FileChannel in, final long position, final int length) throws IOException {
        return this.wrapped.setBytes(index, in, position, length);
    }
    
    public boolean isReadOnly() {
        return this.wrapped.isReadOnly();
    }
    
    public ByteBuf asReadOnly() {
        return this.wrapped.asReadOnly();
    }
    
    protected SwappedByteBuf newSwappedByteBuf() {
        return this.wrapped.newSwappedByteBuf();
    }
    
    public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
        return this.wrapped.getCharSequence(index, length, charset);
    }
    
    public CharSequence readCharSequence(final int length, final Charset charset) {
        return this.wrapped.readCharSequence(length, charset);
    }
    
    public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
        return this.wrapped.setCharSequence(index, sequence, charset);
    }
    
    public int readBytes(final FileChannel out, final long position, final int length) throws IOException {
        return this.wrapped.readBytes(out, position, length);
    }
    
    public int writeBytes(final FileChannel in, final long position, final int length) throws IOException {
        return this.wrapped.writeBytes(in, position, length);
    }
    
    public int writeCharSequence(final CharSequence sequence, final Charset charset) {
        return this.wrapped.writeCharSequence(sequence, charset);
    }
    
    @Override
    public CompositeByteBuf skipBytes(final int length) {
        this.wrapped.skipBytes(length);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBoolean(final boolean value) {
        this.wrapped.writeBoolean(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeByte(final int value) {
        this.wrapped.writeByte(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeShort(final int value) {
        this.wrapped.writeShort(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeMedium(final int value) {
        this.wrapped.writeMedium(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeInt(final int value) {
        this.wrapped.writeInt(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeLong(final long value) {
        this.wrapped.writeLong(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeChar(final int value) {
        this.wrapped.writeChar(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeFloat(final float value) {
        this.wrapped.writeFloat(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeDouble(final double value) {
        this.wrapped.writeDouble(value);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src) {
        this.wrapped.writeBytes(src);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int length) {
        this.wrapped.writeBytes(src, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
        this.wrapped.writeBytes(src, srcIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src) {
        this.wrapped.writeBytes(src);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
        this.wrapped.writeBytes(src, srcIndex, length);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeBytes(final ByteBuffer src) {
        this.wrapped.writeBytes(src);
        return this;
    }
    
    @Override
    public CompositeByteBuf writeZero(final int length) {
        this.wrapped.writeZero(length);
        return this;
    }
    
    @Override
    public CompositeByteBuf retain(final int increment) {
        this.wrapped.retain(increment);
        return this;
    }
    
    @Override
    public CompositeByteBuf retain() {
        this.wrapped.retain();
        return this;
    }
    
    @Override
    public CompositeByteBuf touch() {
        this.wrapped.touch();
        return this;
    }
    
    @Override
    public CompositeByteBuf touch(final Object hint) {
        this.wrapped.touch(hint);
        return this;
    }
    
    @Override
    public ByteBuffer[] nioBuffers() {
        return this.wrapped.nioBuffers();
    }
    
    @Override
    public CompositeByteBuf discardSomeReadBytes() {
        this.wrapped.discardSomeReadBytes();
        return this;
    }
    
    public final void deallocate() {
        this.wrapped.deallocate();
    }
    
    @Override
    public final ByteBuf unwrap() {
        return this.wrapped;
    }
}
