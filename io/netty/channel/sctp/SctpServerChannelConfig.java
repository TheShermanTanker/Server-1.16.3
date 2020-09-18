package io.netty.channel.sctp;

import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelConfig;

public interface SctpServerChannelConfig extends ChannelConfig {
    int getBacklog();
    
    SctpServerChannelConfig setBacklog(final int integer);
    
    int getSendBufferSize();
    
    SctpServerChannelConfig setSendBufferSize(final int integer);
    
    int getReceiveBufferSize();
    
    SctpServerChannelConfig setReceiveBufferSize(final int integer);
    
    SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams();
    
    SctpServerChannelConfig setInitMaxStreams(final SctpStandardSocketOptions.InitMaxStreams initMaxStreams);
    
    @Deprecated
    SctpServerChannelConfig setMaxMessagesPerRead(final int integer);
    
    SctpServerChannelConfig setWriteSpinCount(final int integer);
    
    SctpServerChannelConfig setConnectTimeoutMillis(final int integer);
    
    SctpServerChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    SctpServerChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    SctpServerChannelConfig setAutoRead(final boolean boolean1);
    
    SctpServerChannelConfig setAutoClose(final boolean boolean1);
    
    SctpServerChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    SctpServerChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    SctpServerChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    SctpServerChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
}
