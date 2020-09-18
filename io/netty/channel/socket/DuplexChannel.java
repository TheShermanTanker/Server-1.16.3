package io.netty.channel.socket;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;

public interface DuplexChannel extends Channel {
    boolean isInputShutdown();
    
    ChannelFuture shutdownInput();
    
    ChannelFuture shutdownInput(final ChannelPromise channelPromise);
    
    boolean isOutputShutdown();
    
    ChannelFuture shutdownOutput();
    
    ChannelFuture shutdownOutput(final ChannelPromise channelPromise);
    
    boolean isShutdown();
    
    ChannelFuture shutdown();
    
    ChannelFuture shutdown(final ChannelPromise channelPromise);
}
