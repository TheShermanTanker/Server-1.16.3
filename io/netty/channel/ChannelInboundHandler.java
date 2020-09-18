package io.netty.channel;

public interface ChannelInboundHandler extends ChannelHandler {
    void channelRegistered(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void channelUnregistered(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void channelActive(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void channelInactive(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void channelRead(final ChannelHandlerContext channelHandlerContext, final Object object) throws Exception;
    
    void channelReadComplete(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void userEventTriggered(final ChannelHandlerContext channelHandlerContext, final Object object) throws Exception;
    
    void channelWritabilityChanged(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void exceptionCaught(final ChannelHandlerContext channelHandlerContext, final Throwable throwable) throws Exception;
}
