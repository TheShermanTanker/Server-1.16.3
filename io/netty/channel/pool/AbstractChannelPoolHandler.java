package io.netty.channel.pool;

import io.netty.channel.Channel;

public abstract class AbstractChannelPoolHandler implements ChannelPoolHandler {
    public void channelAcquired(final Channel ch) throws Exception {
    }
    
    public void channelReleased(final Channel ch) throws Exception {
    }
}
