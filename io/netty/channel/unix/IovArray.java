package io.netty.channel.unix;

import io.netty.util.internal.ObjectUtil;
import java.nio.ByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.channel.ChannelOutboundBuffer;

public final class IovArray implements ChannelOutboundBuffer.MessageProcessor {
    private static final int ADDRESS_SIZE;
    private static final int IOV_SIZE;
    private static final int CAPACITY;
    private final long memoryAddress;
    private int count;
    private long size;
    private long maxBytes;
    
    public IovArray() {
        this.maxBytes = Limits.SSIZE_MAX;
        this.memoryAddress = PlatformDependent.allocateMemory(IovArray.CAPACITY);
    }
    
    public void clear() {
        this.count = 0;
        this.size = 0L;
    }
    
    public boolean add(final ByteBuf buf) {
        if (this.count == Limits.IOV_MAX) {
            return false;
        }
        if (buf.hasMemoryAddress() && buf.nioBufferCount() == 1) {
            final int len = buf.readableBytes();
            return len == 0 || this.add(buf.memoryAddress(), buf.readerIndex(), len);
        }
        final ByteBuffer[] nioBuffers;
        final ByteBuffer[] buffers = nioBuffers = buf.nioBuffers();
        for (final ByteBuffer nioBuffer : nioBuffers) {
            final int len2 = nioBuffer.remaining();
            if (len2 != 0 && (!this.add(PlatformDependent.directBufferAddress(nioBuffer), nioBuffer.position(), len2) || this.count == Limits.IOV_MAX)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean add(final long addr, final int offset, final int len) {
        final long baseOffset = this.memoryAddress(this.count);
        final long lengthOffset = baseOffset + IovArray.ADDRESS_SIZE;
        if (this.maxBytes - len < this.size && this.count > 0) {
            return false;
        }
        this.size += len;
        ++this.count;
        if (IovArray.ADDRESS_SIZE == 8) {
            PlatformDependent.putLong(baseOffset, addr + offset);
            PlatformDependent.putLong(lengthOffset, len);
        }
        else {
            assert IovArray.ADDRESS_SIZE == 4;
            PlatformDependent.putInt(baseOffset, (int)addr + offset);
            PlatformDependent.putInt(lengthOffset, len);
        }
        return true;
    }
    
    public int count() {
        return this.count;
    }
    
    public long size() {
        return this.size;
    }
    
    public void maxBytes(final long maxBytes) {
        this.maxBytes = Math.min(Limits.SSIZE_MAX, ObjectUtil.checkPositive(maxBytes, "maxBytes"));
    }
    
    public long maxBytes() {
        return this.maxBytes;
    }
    
    public long memoryAddress(final int offset) {
        return this.memoryAddress + IovArray.IOV_SIZE * offset;
    }
    
    public void release() {
        PlatformDependent.freeMemory(this.memoryAddress);
    }
    
    public boolean processMessage(final Object msg) throws Exception {
        return msg instanceof ByteBuf && this.add((ByteBuf)msg);
    }
    
    static {
        ADDRESS_SIZE = PlatformDependent.addressSize();
        IOV_SIZE = 2 * IovArray.ADDRESS_SIZE;
        CAPACITY = Limits.IOV_MAX * IovArray.IOV_SIZE;
    }
}
