package io.netty.channel.pool;

import io.netty.channel.Channel;

public interface ChannelPoolHandler {
    void channelReleased(final Channel channel) throws Exception;
    
    void channelAcquired(final Channel channel) throws Exception;
    
    void channelCreated(final Channel channel) throws Exception;
}
