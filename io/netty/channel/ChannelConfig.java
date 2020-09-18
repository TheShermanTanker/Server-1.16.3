package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.util.Map;

public interface ChannelConfig {
    Map<ChannelOption<?>, Object> getOptions();
    
    boolean setOptions(final Map<ChannelOption<?>, ?> map);
    
     <T> T getOption(final ChannelOption<T> channelOption);
    
     <T> boolean setOption(final ChannelOption<T> channelOption, final T object);
    
    int getConnectTimeoutMillis();
    
    ChannelConfig setConnectTimeoutMillis(final int integer);
    
    @Deprecated
    int getMaxMessagesPerRead();
    
    @Deprecated
    ChannelConfig setMaxMessagesPerRead(final int integer);
    
    int getWriteSpinCount();
    
    ChannelConfig setWriteSpinCount(final int integer);
    
    ByteBufAllocator getAllocator();
    
    ChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
     <T extends RecvByteBufAllocator> T getRecvByteBufAllocator();
    
    ChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    boolean isAutoRead();
    
    ChannelConfig setAutoRead(final boolean boolean1);
    
    @Deprecated
    boolean isAutoClose();
    
    @Deprecated
    ChannelConfig setAutoClose(final boolean boolean1);
    
    int getWriteBufferHighWaterMark();
    
    ChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    int getWriteBufferLowWaterMark();
    
    ChannelConfig setWriteBufferLowWaterMark(final int integer);
    
    MessageSizeEstimator getMessageSizeEstimator();
    
    ChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    WriteBufferWaterMark getWriteBufferWaterMark();
    
    ChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
}
