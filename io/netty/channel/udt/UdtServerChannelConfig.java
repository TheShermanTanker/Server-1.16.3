package io.netty.channel.udt;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;

@Deprecated
public interface UdtServerChannelConfig extends UdtChannelConfig {
    int getBacklog();
    
    UdtServerChannelConfig setBacklog(final int integer);
    
    UdtServerChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    UdtServerChannelConfig setMaxMessagesPerRead(final int integer);
    
    UdtServerChannelConfig setWriteSpinCount(final int integer);
    
    UdtServerChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    UdtServerChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    UdtServerChannelConfig setAutoRead(final boolean boolean1);
    
    UdtServerChannelConfig setAutoClose(final boolean boolean1);
    
    UdtServerChannelConfig setProtocolReceiveBufferSize(final int integer);
    
    UdtServerChannelConfig setProtocolSendBufferSize(final int integer);
    
    UdtServerChannelConfig setReceiveBufferSize(final int integer);
    
    UdtServerChannelConfig setReuseAddress(final boolean boolean1);
    
    UdtServerChannelConfig setSendBufferSize(final int integer);
    
    UdtServerChannelConfig setSoLinger(final int integer);
    
    UdtServerChannelConfig setSystemReceiveBufferSize(final int integer);
    
    UdtServerChannelConfig setSystemSendBufferSize(final int integer);
    
    UdtServerChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    UdtServerChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    UdtServerChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    UdtServerChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
}
