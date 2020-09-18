package io.netty.channel.socket;

import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface ServerSocketChannelConfig extends ChannelConfig {
    int getBacklog();
    
    ServerSocketChannelConfig setBacklog(final int integer);
    
    boolean isReuseAddress();
    
    ServerSocketChannelConfig setReuseAddress(final boolean boolean1);
    
    int getReceiveBufferSize();
    
    ServerSocketChannelConfig setReceiveBufferSize(final int integer);
    
    ServerSocketChannelConfig setPerformancePreferences(final int integer1, final int integer2, final int integer3);
    
    ServerSocketChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    ServerSocketChannelConfig setMaxMessagesPerRead(final int integer);
    
    ServerSocketChannelConfig setWriteSpinCount(final int integer);
    
    ServerSocketChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    ServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    ServerSocketChannelConfig setAutoRead(final boolean boolean1);
    
    ServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    ServerSocketChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    ServerSocketChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    ServerSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
}
