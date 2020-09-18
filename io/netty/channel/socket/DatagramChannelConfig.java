package io.netty.channel.socket;

import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.buffer.ByteBufAllocator;
import java.net.NetworkInterface;
import java.net.InetAddress;
import io.netty.channel.ChannelConfig;

public interface DatagramChannelConfig extends ChannelConfig {
    int getSendBufferSize();
    
    DatagramChannelConfig setSendBufferSize(final int integer);
    
    int getReceiveBufferSize();
    
    DatagramChannelConfig setReceiveBufferSize(final int integer);
    
    int getTrafficClass();
    
    DatagramChannelConfig setTrafficClass(final int integer);
    
    boolean isReuseAddress();
    
    DatagramChannelConfig setReuseAddress(final boolean boolean1);
    
    boolean isBroadcast();
    
    DatagramChannelConfig setBroadcast(final boolean boolean1);
    
    boolean isLoopbackModeDisabled();
    
    DatagramChannelConfig setLoopbackModeDisabled(final boolean boolean1);
    
    int getTimeToLive();
    
    DatagramChannelConfig setTimeToLive(final int integer);
    
    InetAddress getInterface();
    
    DatagramChannelConfig setInterface(final InetAddress inetAddress);
    
    NetworkInterface getNetworkInterface();
    
    DatagramChannelConfig setNetworkInterface(final NetworkInterface networkInterface);
    
    @Deprecated
    DatagramChannelConfig setMaxMessagesPerRead(final int integer);
    
    DatagramChannelConfig setWriteSpinCount(final int integer);
    
    DatagramChannelConfig setConnectTimeoutMillis(final int integer);
    
    DatagramChannelConfig setAllocator(final ByteBufAllocator byteBufAllocator);
    
    DatagramChannelConfig setRecvByteBufAllocator(final RecvByteBufAllocator recvByteBufAllocator);
    
    DatagramChannelConfig setAutoRead(final boolean boolean1);
    
    DatagramChannelConfig setAutoClose(final boolean boolean1);
    
    DatagramChannelConfig setMessageSizeEstimator(final MessageSizeEstimator messageSizeEstimator);
    
    DatagramChannelConfig setWriteBufferWaterMark(final WriteBufferWaterMark writeBufferWaterMark);
}
