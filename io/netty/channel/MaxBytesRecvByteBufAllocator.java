package io.netty.channel;

import java.util.Map;

public interface MaxBytesRecvByteBufAllocator extends RecvByteBufAllocator {
    int maxBytesPerRead();
    
    MaxBytesRecvByteBufAllocator maxBytesPerRead(final int integer);
    
    int maxBytesPerIndividualRead();
    
    MaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(final int integer);
    
    Map.Entry<Integer, Integer> maxBytesPerReadPair();
    
    MaxBytesRecvByteBufAllocator maxBytesPerReadPair(final int integer1, final int integer2);
}
