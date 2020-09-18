package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2FlowController {
    void channelHandlerContext(final ChannelHandlerContext channelHandlerContext) throws Http2Exception;
    
    void initialWindowSize(final int integer) throws Http2Exception;
    
    int initialWindowSize();
    
    int windowSize(final Http2Stream http2Stream);
    
    void incrementWindowSize(final Http2Stream http2Stream, final int integer) throws Http2Exception;
}
