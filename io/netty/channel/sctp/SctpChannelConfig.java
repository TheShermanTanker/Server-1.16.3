package io.netty.channel.sctp;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelConfig;

public interface SctpChannelConfig extends ChannelConfig {
    boolean isSctpNoDelay();
    
    SctpChannelConfig setSctpNoDelay(final boolean boolean1);
    
    int getSendBufferSize();
    
    SctpChannelConfig setSendBufferSize(final int integer);
    
    int getReceiveBufferSize();
    
    SctpChannelConfig setReceiveBufferSize(final int integer);
    
    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
    
    SctpChannelConfig setInitMaxStreams(final SctpStandardSocketOptions.InitMaxStreams initMaxStreams);
    
    SctpChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    SctpChannelConfig setMaxMessagesPerRead(final int integer);
    
    SctpChannelConfig setWriteSpinCount(final int integer);
    
    SctpChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    SctpChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    SctpChannelConfig setAutoRead(final boolean boolean1);
    
    SctpChannelConfig setAutoClose(final boolean boolean1);
    
    SctpChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    SctpChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    SctpChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    SctpChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
}
