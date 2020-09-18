package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2FrameReader extends Closeable {
    void readFrame(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final Http2FrameListener http2FrameListener) throws Http2Exception;
    
    Configuration configuration();
    
    void close();
    
    public interface Configuration {
        Http2HeadersDecoder.Configuration headersConfiguration();
        
        Http2FrameSizePolicy frameSizePolicy();
    }
}
