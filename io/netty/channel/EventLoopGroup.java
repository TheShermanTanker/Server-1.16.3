package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

public interface EventLoopGroup extends EventExecutorGroup {
    EventLoop next();
    
    ChannelFuture register(final Channel channel);
    
    ChannelFuture register(final ChannelPromise channelPromise);
    
    @Deprecated
    ChannelFuture register(final Channel channel, final ChannelPromise channelPromise);
}
