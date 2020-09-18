package io.netty.channel.socket.oio;

import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.channel.socket.DatagramChannelConfig;

public interface OioDatagramChannelConfig extends DatagramChannelConfig {
    OioDatagramChannelConfig setSoTimeout(final int integer);
    
    int getSoTimeout();
    
    OioDatagramChannelConfig setSendBufferSize(final int integer);
    
    OioDatagramChannelConfig setReceiveBufferSize(final int integer);
    
    OioDatagramChannelConfig setTrafficClass(final int integer);
    
    OioDatagramChannelConfig setReuseAddress(final boolean boolean1);
    
    OioDatagramChannelConfig setBroadcast(final boolean boolean1);
    
    OioDatagramChannelConfig setLoopbackModeDisabled(final boolean boolean1);
    
    OioDatagramChannelConfig setTimeToLive(final int integer);
    
    OioDatagramChannelConfig setInterface(final InetAddress inetAddress);
    
    OioDatagramChannelConfig setNetworkInterface(final NetworkInterface networkInterface);
    
    OioDatagramChannelConfig setMaxMessagesPerRead(final int integer);
    
    OioDatagramChannelConfig setWriteSpinCount(final int integer);
    
    OioDatagramChannelConfig setConnectTimeoutMillis(final int integer);
    
    OioDatagramChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    OioDatagramChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    OioDatagramChannelConfig setAutoRead(final boolean boolean1);
    
    OioDatagramChannelConfig setAutoClose(final boolean boolean1);
    
    OioDatagramChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    OioDatagramChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
    
    OioDatagramChannelConfig setWriteBufferHighWaterMark(final int integer);
    
    OioDatagramChannelConfig setWriteBufferLowWaterMark(final int integer);
}
