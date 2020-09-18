package io.netty.channel;

import java.net.SocketAddress;

public interface ChannelOutboundInvoker {
    ChannelFuture bind(final SocketAddress socketAddress);
    
    ChannelFuture connect(final SocketAddress socketAddress);
    
    ChannelFuture connect(final SocketAddress socketAddress1, final SocketAddress socketAddress2);
    
    ChannelFuture disconnect();
    
    ChannelFuture close();
    
    ChannelFuture deregister();
    
    ChannelFuture bind(final SocketAddress socketAddress, final ChannelPromise channelPromise);
    
    ChannelFuture connect(final SocketAddress socketAddress, final ChannelPromise channelPromise);
    
    ChannelFuture connect(final SocketAddress socketAddress1, final SocketAddress socketAddress2, final ChannelPromise channelPromise);
    
    ChannelFuture disconnect(final ChannelPromise channelPromise);
    
    ChannelFuture close(final ChannelPromise channelPromise);
    
    ChannelFuture deregister(final ChannelPromise channelPromise);
    
    ChannelOutboundInvoker read();
    
    ChannelFuture write(final Object object);
    
    ChannelFuture write(final Object object, final ChannelPromise channelPromise);
    
    ChannelOutboundInvoker flush();
    
    ChannelFuture writeAndFlush(final Object object, final ChannelPromise channelPromise);
    
    ChannelFuture writeAndFlush(final Object object);
    
    ChannelPromise newPromise();
    
    ChannelProgressivePromise newProgressivePromise();
    
    ChannelFuture newSucceededFuture();
    
    ChannelFuture newFailedFuture(final Throwable throwable);
    
    ChannelPromise voidPromise();
}
