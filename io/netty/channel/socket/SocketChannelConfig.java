package io.netty.channel.socket;

import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface SocketChannelConfig extends ChannelConfig {
    boolean isTcpNoDelay();
    
    SocketChannelConfig setTcpNoDelay(final boolean boolean1);
    
    int getSoLinger();
    
    SocketChannelConfig setSoLinger(final int integer);
    
    int getSendBufferSize();
    
    SocketChannelConfig setSendBufferSize(final int integer);
    
    int getReceiveBufferSize();
    
    SocketChannelConfig setReceiveBufferSize(final int integer);
    
    boolean isKeepAlive();
    
    SocketChannelConfig setKeepAlive(final boolean boolean1);
    
    int getTrafficClass();
    
    SocketChannelConfig setTrafficClass(final int integer);
    
    boolean isReuseAddress();
    
    SocketChannelConfig setReuseAddress(final boolean boolean1);
    
    SocketChannelConfig setPerformancePreferences(final int integer1, final int integer2, final int integer3);
    
    boolean isAllowHalfClosure();
    
    SocketChannelConfig setAllowHalfClosure(final boolean boolean1);
    
    SocketChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    SocketChannelConfig setMaxMessagesPerRead(final int integer);
    
    SocketChannelConfig setWriteSpinCount(final int integer);
    
    SocketChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    SocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    SocketChannelConfig setAutoRead(final boolean boolean1);
    
    SocketChannelConfig setAutoClose(final boolean boolean1);
    
    SocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    SocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
}
