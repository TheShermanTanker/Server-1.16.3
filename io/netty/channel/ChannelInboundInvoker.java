package io.netty.channel;

public interface ChannelInboundInvoker {
    ChannelInboundInvoker fireChannelRegistered();
    
    ChannelInboundInvoker fireChannelUnregistered();
    
    ChannelInboundInvoker fireChannelActive();
    
    ChannelInboundInvoker fireChannelInactive();
    
    ChannelInboundInvoker fireExceptionCaught(final Throwable throwable);
    
    ChannelInboundInvoker fireUserEventTriggered(final Object object);
    
    ChannelInboundInvoker fireChannelRead(final Object object);
    
    ChannelInboundInvoker fireChannelReadComplete();
    
    ChannelInboundInvoker fireChannelWritabilityChanged();
}
