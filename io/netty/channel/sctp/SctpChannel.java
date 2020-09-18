package io.netty.channel.sctp;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.util.Set;
import java.net.InetSocketAddress;
import com.sun.nio.sctp.Association;
import io.netty.channel.Channel;

public interface SctpChannel extends Channel {
    SctpServerChannel parent();
    
    Association association();
    
    InetSocketAddress localAddress();
    
    Set<InetSocketAddress> allLocalAddresses();
    
    SctpChannelConfig config();
    
    InetSocketAddress remoteAddress();
    
    Set<InetSocketAddress> allRemoteAddresses();
    
    ChannelFuture bindAddress(final InetAddress inetAddress);
    
    ChannelFuture bindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise);
    
    ChannelFuture unbindAddress(final InetAddress inetAddress);
    
    ChannelFuture unbindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise);
}
