package io.netty.channel.socket.oio;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.SocketChannelConfig;

public interface OioSocketChannelConfig extends SocketChannelConfig {
    OioSocketChannelConfig setSoTimeout(final int integer);
    
    int getSoTimeout();
    
    OioSocketChannelConfig setTcpNoDelay(final boolean boolean1);
    
    OioSocketChannelConfig setSoLinger(final int integer);
    
    OioSocketChannelConfig setSendBufferSize(final int integer);
    
    OioSocketChannelConfig setReceiveBufferSize(final int integer);
    
    OioSocketChannelConfig setKeepAlive(final boolean boolean1);
    
    OioSocketChannelConfig setTrafficClass(final int integer);
    
    OioSocketChannelConfig setReuseAddress(final boolean boolean1);
    
    OioSocketChannelConfig setPerformancePreferences(final int integer1, final int integer2, final int integer3);
    
    OioSocketChannelConfig setAllowHalfClosure(final boolean boolean1);
    
    OioSocketChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    OioSocketChannelConfig setMaxMessagesPerRead(final int integer);
    
    OioSocketChannelConfig setWriteSpinCount(final int integer);
    
    OioSocketChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    OioSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    OioSocketChannelConfig setAutoRead(final boolean boolean1);
    
    OioSocketChannelConfig setAutoClose(final boolean boolean1);
    
    OioSocketChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    OioSocketChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    OioSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    OioSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
}
