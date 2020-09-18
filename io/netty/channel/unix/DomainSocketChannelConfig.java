package io.netty.channel.unix;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

public interface DomainSocketChannelConfig extends ChannelConfig {
    @Deprecated
    DomainSocketChannelConfig setMaxMessagesPerRead(final int integer);
    
    DomainSocketChannelConfig setConnectTimeoutMillis(final int integer);
    
    DomainSocketChannelConfig setWriteSpinCount(final int integer);
    
    DomainSocketChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    DomainSocketChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    DomainSocketChannelConfig setAutoRead(final boolean boolean1);
    
    DomainSocketChannelConfig setAutoClose(final boolean boolean1);
    
    @Deprecated
    DomainSocketChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    @Deprecated
    DomainSocketChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    DomainSocketChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    DomainSocketChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    DomainSocketChannelConfig setReadMode(final DomainSocketReadMode domainSocketReadMode);
    
    DomainSocketReadMode getReadMode();
}
