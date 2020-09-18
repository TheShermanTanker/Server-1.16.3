package io.netty.channel.sctp;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import java.util.Set;
import java.net.InetSocketAddress;
import io.netty.channel.ServerChannel;

public interface SctpServerChannel extends ServerChannel {
    SctpServerChannelConfig config();
    
    InetSocketAddress localAddress();
    
    Set<InetSocketAddress> allLocalAddresses();
    
    ChannelFuture bindAddress(final InetAddress inetAddress);
    
    ChannelFuture bindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise);
    
    ChannelFuture unbindAddress(final InetAddress inetAddress);
    
    ChannelFuture unbindAddress(final InetAddress inetAddress, final ChannelPromise channelPromise);
}
