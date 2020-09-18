package io.netty.buffer;

import io.netty.util.ByteProcessor;
import java.nio.channels.ScatteringByteChannel;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import io.netty.util.ReferenceCounted;

public abstract class ByteBuf implements ReferenceCounted, Comparable<ByteBuf> {
    public abstract int capacity();
    
    public abstract ByteBuf capacity(final int integer);
    
    public abstract int maxCapacity();
    
    public abstract ByteBufAllocator alloc();
    
    @Deprecated
    public abstract ByteOrder order();
    
    @Deprecated
    public abstract ByteBuf order(final ByteOrder byteOrder);
    
    public abstract ByteBuf unwrap();
    
    public abstract boolean isDirect();
    
    public abstract boolean isReadOnly();
    
    public abstract ByteBuf asReadOnly();
    
    public abstract int readerIndex();
    
    public abstract ByteBuf readerIndex(final int integer);
    
    public abstract int writerIndex();
    
    public abstract ByteBuf writerIndex(final int integer);
    
    public abstract ByteBuf setIndex(final int integer1, final int integer2);
    
    public abstract int readableBytes();
    
    public abstract int writableBytes();
    
    public abstract int maxWritableBytes();
    
    public abstract boolean isReadable();
    
    public abstract boolean isReadable(final int integer);
    
    public abstract boolean isWritable();
    
    public abstract boolean isWritable(final int integer);
    
    public abstract ByteBuf clear();
    
    public abstract ByteBuf markReaderIndex();
    
    public abstract ByteBuf resetReaderIndex();
    
    public abstract ByteBuf markWriterIndex();
    
    public abstract ByteBuf resetWriterIndex();
    
    public abstract ByteBuf discardReadBytes();
    
    public abstract ByteBuf discardSomeReadBytes();
    
    public abstract ByteBuf ensureWritable(final int integer);
    
    public abstract int ensureWritable(final int integer, final boolean boolean2);
    
    public abstract boolean getBoolean(final int integer);
    
    public abstract byte getByte(final int integer);
    
    public abstract short getUnsignedByte(final int integer);
    
    public abstract short getShort(final int integer);
    
    public abstract short getShortLE(final int integer);
    
    public abstract int getUnsignedShort(final int integer);
    
    public abstract int getUnsignedShortLE(final int integer);
    
    public abstract int getMedium(final int integer);
    
    public abstract int getMediumLE(final int integer);
    
    public abstract int getUnsignedMedium(final int integer);
    
    public abstract int getUnsignedMediumLE(final int integer);
    
    public abstract int getInt(final int integer);
    
    public abstract int getIntLE(final int integer);
    
    public abstract long getUnsignedInt(final int integer);
    
    public abstract long getUnsignedIntLE(final int integer);
    
    public abstract long getLong(final int integer);
    
    public abstract long getLongLE(final int integer);
    
    public abstract char getChar(final int integer);
    
    public abstract float getFloat(final int integer);
    
    public float getFloatLE(final int index) {
        return Float.intBitsToFloat(this.getIntLE(index));
    }
    
    public abstract double getDouble(final int integer);
    
    public double getDoubleLE(final int index) {
        return Double.longBitsToDouble(this.getLongLE(index));
    }
    
    public abstract ByteBuf getBytes(final int integer, final ByteBuf byteBuf);
    
    public abstract ByteBuf getBytes(final int integer1, final ByteBuf byteBuf, final int integer3);
    
    public abstract ByteBuf getBytes(final int integer1, final ByteBuf byteBuf, final int integer3, final int integer4);
    
    public abstract ByteBuf getBytes(final int integer, final byte[] arr);
    
    public abstract ByteBuf getBytes(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    public abstract ByteBuf getBytes(final int integer, final ByteBuffer byteBuffer);
    
    public abstract ByteBuf getBytes(final int integer1, final OutputStream outputStream, final int integer3) throws IOException;
    
    public abstract int getBytes(final int integer1, final GatheringByteChannel gatheringByteChannel, final int integer3) throws IOException;
    
    public abstract int getBytes(final int integer1, final FileChannel fileChannel, final long long3, final int integer4) throws IOException;
    
    public abstract CharSequence getCharSequence(final int integer1, final int integer2, final Charset charset);
    
    public abstract ByteBuf setBoolean(final int integer, final boolean boolean2);
    
    public abstract ByteBuf setByte(final int integer1, final int integer2);
    
    public abstract ByteBuf setShort(final int integer1, final int integer2);
    
    public abstract ByteBuf setShortLE(final int integer1, final int integer2);
    
    public abstract ByteBuf setMedium(final int integer1, final int integer2);
    
    public abstract ByteBuf setMediumLE(final int integer1, final int integer2);
    
    public abstract ByteBuf setInt(final int integer1, final int integer2);
    
    public abstract ByteBuf setIntLE(final int integer1, final int integer2);
    
    public abstract ByteBuf setLong(final int integer, final long long2);
    
    public abstract ByteBuf setLongLE(final int integer, final long long2);
    
    public abstract ByteBuf setChar(final int integer1, final int integer2);
    
    public abstract ByteBuf setFloat(final int integer, final float float2);
    
    public ByteBuf setFloatLE(final int index, final float value) {
        return this.setIntLE(index, Float.floatToRawIntBits(value));
    }
    
    public abstract ByteBuf setDouble(final int integer, final double double2);
    
    public ByteBuf setDoubleLE(final int index, final double value) {
        return this.setLongLE(index, Double.doubleToRawLongBits(value));
    }
    
    public abstract ByteBuf setBytes(final int integer, final ByteBuf byteBuf);
    
    public abstract ByteBuf setBytes(final int integer1, final ByteBuf byteBuf, final int integer3);
    
    public abstract ByteBuf setBytes(final int integer1, final ByteBuf byteBuf, final int integer3, final int integer4);
    
    public abstract ByteBuf setBytes(final int integer, final byte[] arr);
    
    public abstract ByteBuf setBytes(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    public abstract ByteBuf setBytes(final int integer, final ByteBuffer byteBuffer);
    
    public abstract int setBytes(final int integer1, final InputStream inputStream, final int integer3) throws IOException;
    
    public abstract int setBytes(final int integer1, final ScatteringByteChannel scatteringByteChannel, final int integer3) throws IOException;
    
    public abstract int setBytes(final int integer1, final FileChannel fileChannel, final long long3, final int integer4) throws IOException;
    
    public abstract ByteBuf setZero(final int integer1, final int integer2);
    
    public abstract int setCharSequence(final int integer, final CharSequence charSequence, final Charset charset);
    
    public abstract boolean readBoolean();
    
    public abstract byte readByte();
    
    public abstract short readUnsignedByte();
    
    public abstract short readShort();
    
    public abstract short readShortLE();
    
    public abstract int readUnsignedShort();
    
    public abstract int readUnsignedShortLE();
    
    public abstract int readMedium();
    
    public abstract int readMediumLE();
    
    public abstract int readUnsignedMedium();
    
    public abstract int readUnsignedMediumLE();
    
    public abstract int readInt();
    
    public abstract int readIntLE();
    
    public abstract long readUnsignedInt();
    
    public abstract long readUnsignedIntLE();
    
    public abstract long readLong();
    
    public abstract long readLongLE();
    
    public abstract char readChar();
    
    public abstract float readFloat();
    
    public float readFloatLE() {
        return Float.intBitsToFloat(this.readIntLE());
    }
    
    public abstract double readDouble();
    
    public double readDoubleLE() {
        return Double.longBitsToDouble(this.readLongLE());
    }
    
    public abstract ByteBuf readBytes(final int integer);
    
    public abstract ByteBuf readSlice(final int integer);
    
    public abstract ByteBuf readRetainedSlice(final int integer);
    
    public abstract ByteBuf readBytes(final ByteBuf byteBuf);
    
    public abstract ByteBuf readBytes(final ByteBuf byteBuf, final int integer);
    
    public abstract ByteBuf readBytes(final ByteBuf byteBuf, final int integer2, final int integer3);
    
    public abstract ByteBuf readBytes(final byte[] arr);
    
    public abstract ByteBuf readBytes(final byte[] arr, final int integer2, final int integer3);
    
    public abstract ByteBuf readBytes(final ByteBuffer byteBuffer);
    
    public abstract ByteBuf readBytes(final OutputStream outputStream, final int integer) throws IOException;
    
    public abstract int readBytes(final GatheringByteChannel gatheringByteChannel, final int integer) throws IOException;
    
    public abstract CharSequence readCharSequence(final int integer, final Charset charset);
    
    public abstract int readBytes(final FileChannel fileChannel, final long long2, final int integer) throws IOException;
    
    public abstract ByteBuf skipBytes(final int integer);
    
    public abstract ByteBuf writeBoolean(final boolean boolean1);
    
    public abstract ByteBuf writeByte(final int integer);
    
    public abstract ByteBuf writeShort(final int integer);
    
    public abstract ByteBuf writeShortLE(final int integer);
    
    public abstract ByteBuf writeMedium(final int integer);
    
    public abstract ByteBuf writeMediumLE(final int integer);
    
    public abstract ByteBuf writeInt(final int integer);
    
    public abstract ByteBuf writeIntLE(final int integer);
    
    public abstract ByteBuf writeLong(final long long1);
    
    public abstract ByteBuf writeLongLE(final long long1);
    
    public abstract ByteBuf writeChar(final int integer);
    
    public abstract ByteBuf writeFloat(final float float1);
    
    public ByteBuf writeFloatLE(final float value) {
        return this.writeIntLE(Float.floatToRawIntBits(value));
    }
    
    public abstract ByteBuf writeDouble(final double double1);
    
    public ByteBuf writeDoubleLE(final double value) {
        return this.writeLongLE(Double.doubleToRawLongBits(value));
    }
    
    public abstract ByteBuf writeBytes(final ByteBuf byteBuf);
    
    public abstract ByteBuf writeBytes(final ByteBuf byteBuf, final int integer);
    
    public abstract ByteBuf writeBytes(final ByteBuf byteBuf, final int integer2, final int integer3);
    
    public abstract ByteBuf writeBytes(final byte[] arr);
    
    public abstract ByteBuf writeBytes(final byte[] arr, final int integer2, final int integer3);
    
    public abstract ByteBuf writeBytes(final ByteBuffer byteBuffer);
    
    public abstract int writeBytes(final InputStream inputStream, final int integer) throws IOException;
    
    public abstract int writeBytes(final ScatteringByteChannel scatteringByteChannel, final int integer) throws IOException;
    
    public abstract int writeBytes(final FileChannel fileChannel, final long long2, final int integer) throws IOException;
    
    public abstract ByteBuf writeZero(final int integer);
    
    public abstract int writeCharSequence(final CharSequence charSequence, final Charset charset);
    
    public abstract int indexOf(final int integer1, final int integer2, final byte byte3);
    
    public abstract int bytesBefore(final byte byte1);
    
    public abstract int bytesBefore(final int integer, final byte byte2);
    
    public abstract int bytesBefore(final int integer1, final int integer2, final byte byte3);
    
    public abstract int forEachByte(final ByteProcessor byteProcessor);
    
    public abstract int forEachByte(final int integer1, final int integer2, final ByteProcessor byteProcessor);
    
    public abstract int forEachByteDesc(final ByteProcessor byteProcessor);
    
    public abstract int forEachByteDesc(final int integer1, final int integer2, final ByteProcessor byteProcessor);
    
    public abstract ByteBuf copy();
    
    public abstract ByteBuf copy(final int integer1, final int integer2);
    
    public abstract ByteBuf slice();
    
    public abstract ByteBuf retainedSlice();
    
    public abstract ByteBuf slice(final int integer1, final int integer2);
    
    public abstract ByteBuf retainedSlice(final int integer1, final int integer2);
    
    public abstract ByteBuf duplicate();
    
    public abstract ByteBuf retainedDuplicate();
    
    public abstract int nioBufferCount();
    
    public abstract ByteBuffer nioBuffer();
    
    public abstract ByteBuffer nioBuffer(final int integer1, final int integer2);
    
    public abstract ByteBuffer internalNioBuffer(final int integer1, final int integer2);
    
    public abstract ByteBuffer[] nioBuffers();
    
    public abstract ByteBuffer[] nioBuffers(final int integer1, final int integer2);
    
    public abstract boolean hasArray();
    
    public abstract byte[] array();
    
    public abstract int arrayOffset();
    
    public abstract boolean hasMemoryAddress();
    
    public abstract long memoryAddress();
    
    public abstract String toString(final Charset charset);
    
    public abstract String toString(final int integer1, final int integer2, final Charset charset);
    
    public abstract int hashCode();
    
    public abstract boolean equals(final Object object);
    
    public abstract int compareTo(final ByteBuf byteBuf);
    
    public abstract String toString();
    
    public abstract ByteBuf retain(final int integer);
    
    public abstract ByteBuf retain();
    
    public abstract ByteBuf touch();
    
    public abstract ByteBuf touch(final Object object);
}
