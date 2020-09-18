package io.netty.channel;

import java.net.SocketAddress;

public interface ChannelOutboundHandler extends ChannelHandler {
    void bind(final ChannelHandlerContext channelHandlerContext, final SocketAddress socketAddress, final ChannelPromise channelPromise) throws Exception;
    
    void connect(final ChannelHandlerContext channelHandlerContext, final SocketAddress socketAddress2, final SocketAddress socketAddress3, final ChannelPromise channelPromise) throws Exception;
    
    void disconnect(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise) throws Exception;
    
    void close(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise) throws Exception;
    
    void deregister(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise) throws Exception;
    
    void read(final ChannelHandlerContext channelHandlerContext) throws Exception;
    
    void write(final ChannelHandlerContext channelHandlerContext, final Object object, final ChannelPromise channelPromise) throws Exception;
    
    void flush(final ChannelHandlerContext channelHandlerContext) throws Exception;
}
