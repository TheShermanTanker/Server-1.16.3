package io.netty.channel;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBufAllocator;

public final class PreferHeapByteBufAllocator implements ByteBufAllocator {
    private final ByteBufAllocator allocator;
    
    public PreferHeapByteBufAllocator(final ByteBufAllocator allocator) {
        this.allocator = ObjectUtil.<ByteBufAllocator>checkNotNull(allocator, "allocator");
    }
    
    public ByteBuf buffer() {
        return this.allocator.heapBuffer();
    }
    
    public ByteBuf buffer(final int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }
    
    public ByteBuf buffer(final int initialCapacity, final int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }
    
    public ByteBuf ioBuffer() {
        return this.allocator.heapBuffer();
    }
    
    public ByteBuf ioBuffer(final int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }
    
    public ByteBuf ioBuffer(final int initialCapacity, final int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }
    
    public ByteBuf heapBuffer() {
        return this.allocator.heapBuffer();
    }
    
    public ByteBuf heapBuffer(final int initialCapacity) {
        return this.allocator.heapBuffer(initialCapacity);
    }
    
    public ByteBuf heapBuffer(final int initialCapacity, final int maxCapacity) {
        return this.allocator.heapBuffer(initialCapacity, maxCapacity);
    }
    
    public ByteBuf directBuffer() {
        return this.allocator.directBuffer();
    }
    
    public ByteBuf directBuffer(final int initialCapacity) {
        return this.allocator.directBuffer(initialCapacity);
    }
    
    public ByteBuf directBuffer(final int initialCapacity, final int maxCapacity) {
        return this.allocator.directBuffer(initialCapacity, maxCapacity);
    }
    
    public CompositeByteBuf compositeBuffer() {
        return this.allocator.compositeHeapBuffer();
    }
    
    public CompositeByteBuf compositeBuffer(final int maxNumComponents) {
        return this.allocator.compositeHeapBuffer(maxNumComponents);
    }
    
    public CompositeByteBuf compositeHeapBuffer() {
        return this.allocator.compositeHeapBuffer();
    }
    
    public CompositeByteBuf compositeHeapBuffer(final int maxNumComponents) {
        return this.allocator.compositeHeapBuffer(maxNumComponents);
    }
    
    public CompositeByteBuf compositeDirectBuffer() {
        return this.allocator.compositeDirectBuffer();
    }
    
    public CompositeByteBuf compositeDirectBuffer(final int maxNumComponents) {
        return this.allocator.compositeDirectBuffer(maxNumComponents);
    }
    
    public boolean isDirectBufferPooled() {
        return this.allocator.isDirectBufferPooled();
    }
    
    public int calculateNewCapacity(final int minNewCapacity, final int maxCapacity) {
        return this.allocator.calculateNewCapacity(minNewCapacity, maxCapacity);
    }
}
