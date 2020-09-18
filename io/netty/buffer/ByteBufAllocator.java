package io.netty.buffer;

public interface ByteBufAllocator {
    public static final ByteBufAllocator DEFAULT = ByteBufUtil.DEFAULT_ALLOCATOR;
    
    ByteBuf buffer();
    
    ByteBuf buffer(final int integer);
    
    ByteBuf buffer(final int integer1, final int integer2);
    
    ByteBuf ioBuffer();
    
    ByteBuf ioBuffer(final int integer);
    
    ByteBuf ioBuffer(final int integer1, final int integer2);
    
    ByteBuf heapBuffer();
    
    ByteBuf heapBuffer(final int integer);
    
    ByteBuf heapBuffer(final int integer1, final int integer2);
    
    ByteBuf directBuffer();
    
    ByteBuf directBuffer(final int integer);
    
    ByteBuf directBuffer(final int integer1, final int integer2);
    
    CompositeByteBuf compositeBuffer();
    
    CompositeByteBuf compositeBuffer(final int integer);
    
    CompositeByteBuf compositeHeapBuffer();
    
    CompositeByteBuf compositeHeapBuffer(final int integer);
    
    CompositeByteBuf compositeDirectBuffer();
    
    CompositeByteBuf compositeDirectBuffer(final int integer);
    
    boolean isDirectBufferPooled();
    
    int calculateNewCapacity(final int integer1, final int integer2);
}
