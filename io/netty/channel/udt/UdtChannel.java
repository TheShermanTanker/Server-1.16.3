package io.netty.channel.udt;

import java.net.InetSocketAddress;
import io.netty.channel.Channel;

@Deprecated
public interface UdtChannel extends Channel {
    UdtChannelConfig config();
    
    InetSocketAddress localAddress();
    
    InetSocketAddress remoteAddress();
}