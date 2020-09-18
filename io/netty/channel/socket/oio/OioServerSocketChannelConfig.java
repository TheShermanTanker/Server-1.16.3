package io.netty.channel.socket.oio;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.ServerSocketChannelConfig;

public interface OioServerSocketChannelConfig extends ServerSocketChannelConfig {
    OioServerSocketChannelConfig setSoTimeout(final int integer);
    
    int getSoTimeout();
    
    OioServerSocketChannelConfig setBacklog(final int integer);
    
    OioServerSocketChannelConfig setReuseAddress(final boolean boolean1);
    
    OioServerSocketChannelConfig setReceiveBufferSize(final int integer);
    
    OioServerSocketChannelConfig setPerformancePreferences(final int integer1, final int integer2, final int integer3);
    
    OioServerSocketChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    OioServerSocketChannelConfig setMaxMessagesPerRead(final int integer);
    
    OioServerSocketChannelConfig setWriteSpinCount(final int integer);
    
    OioServerSocketChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    OioServerSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    OioServerSocketChannelConfig setAutoRead(final boolean boolean1);
    
    OioServerSocketChannelConfig setAutoClose(final boolean boolean1);
    
    OioServerSocketChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    OioServerSocketChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    OioServerSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    OioServerSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
}
