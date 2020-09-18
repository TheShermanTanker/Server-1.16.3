package io.netty.buffer;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.Recycler;

final class PooledUnsafeHeapByteBuf extends PooledHeapByteBuf {
    private static final Recycler<PooledUnsafeHeapByteBuf> RECYCLER;
    
    static PooledUnsafeHeapByteBuf newUnsafeInstance(final int maxCapacity) {
        final PooledUnsafeHeapByteBuf buf = PooledUnsafeHeapByteBuf.RECYCLER.get();
        buf.reuse(maxCapacity);
        return buf;
    }
    
    private PooledUnsafeHeapByteBuf(final Recycler.Handle<PooledUnsafeHeapByteBuf> recyclerHandle, final int maxCapacity) {
        super(recyclerHandle, maxCapacity);
    }
    
    @Override
    protected byte _getByte(final int index) {
        return UnsafeByteBufUtil.getByte((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShort(final int index) {
        return UnsafeByteBufUtil.getShort((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected short _getShortLE(final int index) {
        return UnsafeByteBufUtil.getShortLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMedium(final int index) {
        return UnsafeByteBufUtil.getUnsignedMedium((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getUnsignedMediumLE(final int index) {
        return UnsafeByteBufUtil.getUnsignedMediumLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getInt(final int index) {
        return UnsafeByteBufUtil.getInt((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected int _getIntLE(final int index) {
        return UnsafeByteBufUtil.getIntLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLong(final int index) {
        return UnsafeByteBufUtil.getLong((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected long _getLongLE(final int index) {
        return UnsafeByteBufUtil.getLongLE((byte[])this.memory, this.idx(index));
    }
    
    @Override
    protected void _setByte(final int index, final int value) {
        UnsafeByteBufUtil.setByte((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShort(final int index, final int value) {
        UnsafeByteBufUtil.setShort((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setShortLE(final int index, final int value) {
        UnsafeByteBufUtil.setShortLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMedium(final int index, final int value) {
        UnsafeByteBufUtil.setMedium((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setMediumLE(final int index, final int value) {
        UnsafeByteBufUtil.setMediumLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setInt(final int index, final int value) {
        UnsafeByteBufUtil.setInt((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setIntLE(final int index, final int value) {
        UnsafeByteBufUtil.setIntLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLong(final int index, final long value) {
        UnsafeByteBufUtil.setLong((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    protected void _setLongLE(final int index, final long value) {
        UnsafeByteBufUtil.setLongLE((byte[])this.memory, this.idx(index), value);
    }
    
    @Override
    public ByteBuf setZero(final int index, final int length) {
        if (PlatformDependent.javaVersion() >= 7) {
            this.checkIndex(index, length);
            UnsafeByteBufUtil.setZero((byte[])this.memory, this.idx(index), length);
            return this;
        }
        return super.setZero(index, length);
    }
    
    @Override
    public ByteBuf writeZero(final int length) {
        if (PlatformDependent.javaVersion() >= 7) {
            this.ensureWritable(length);
            final int wIndex = this.writerIndex;
            UnsafeByteBufUtil.setZero((byte[])this.memory, this.idx(wIndex), length);
            this.writerIndex = wIndex + length;
            return this;
        }
        return super.writeZero(length);
    }
    
    @Deprecated
    @Override
    protected SwappedByteBuf newSwappedByteBuf() {
        if (PlatformDependent.isUnaligned()) {
            return new UnsafeHeapSwappedByteBuf(this);
        }
        return super.newSwappedByteBuf();
    }
    
    static {
        RECYCLER = new Recycler<PooledUnsafeHeapByteBuf>() {
            @Override
            protected PooledUnsafeHeapByteBuf newObject(final Handle<PooledUnsafeHeapByteBuf> handle) {
                return new PooledUnsafeHeapByteBuf(handle, 0, null);
            }
        };
    }
}
