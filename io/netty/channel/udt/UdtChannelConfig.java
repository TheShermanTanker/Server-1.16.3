package io.netty.channel.udt;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;

@Deprecated
public interface UdtChannelConfig extends ChannelConfig {
    int getProtocolReceiveBufferSize();
    
    int getProtocolSendBufferSize();
    
    int getReceiveBufferSize();
    
    int getSendBufferSize();
    
    int getSoLinger();
    
    int getSystemReceiveBufferSize();
    
    int getSystemSendBufferSize();
    
    boolean isReuseAddress();
    
    UdtChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    UdtChannelConfig setMaxMessagesPerRead(final int integer);
    
    UdtChannelConfig setWriteSpinCount(final int integer);
    
    UdtChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    UdtChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    UdtChannelConfig setAutoRead(final boolean boolean1);
    
    UdtChannelConfig setAutoClose(final boolean boolean1);
    
    UdtChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    UdtChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    UdtChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    UdtChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    UdtChannelConfig setProtocolReceiveBufferSize(final int integer);
    
    UdtChannelConfig setProtocolSendBufferSize(final int integer);
    
    UdtChannelConfig setReceiveBufferSize(final int integer);
    
    UdtChannelConfig setReuseAddress(final boolean boolean1);
    
    UdtChannelConfig setSendBufferSize(final int integer);
    
    UdtChannelConfig setSoLinger(final int integer);
    
    UdtChannelConfig setSystemReceiveBufferSize(final int integer);
    
    UdtChannelConfig setSystemSendBufferSize(final int integer);
}
