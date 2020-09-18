package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2FrameWriter extends Http2DataWriter, Closeable {
    ChannelFuture writeHeaders(final ChannelHandlerContext channelHandlerContext, final int integer2, final Http2Headers http2Headers, final int integer4, final boolean boolean5, final ChannelPromise channelPromise);
    
    ChannelFuture writeHeaders(final ChannelHandlerContext channelHandlerContext, final int integer2, final Http2Headers http2Headers, final int integer4, final short short5, final boolean boolean6, final int integer7, final boolean boolean8, final ChannelPromise channelPromise);
    
    ChannelFuture writePriority(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3, final short short4, final boolean boolean5, final ChannelPromise channelPromise);
    
    ChannelFuture writeRstStream(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3, final ChannelPromise channelPromise);
    
    ChannelFuture writeSettings(final ChannelHandlerContext channelHandlerContext, final Http2Settings http2Settings, final ChannelPromise channelPromise);
    
    ChannelFuture writeSettingsAck(final ChannelHandlerContext channelHandlerContext, final ChannelPromise channelPromise);
    
    ChannelFuture writePing(final ChannelHandlerContext channelHandlerContext, final boolean boolean2, final long long3, final ChannelPromise channelPromise);
    
    ChannelFuture writePushPromise(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3, final Http2Headers http2Headers, final int integer5, final ChannelPromise channelPromise);
    
    ChannelFuture writeGoAway(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3, final ByteBuf byteBuf, final ChannelPromise channelPromise);
    
    ChannelFuture writeWindowUpdate(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3, final ChannelPromise channelPromise);
    
    ChannelFuture writeFrame(final ChannelHandlerContext channelHandlerContext, final byte byte2, final int integer, final Http2Flags http2Flags, final ByteBuf byteBuf, final ChannelPromise channelPromise);
    
    Configuration configuration();
    
    void close();
    
    public interface Configuration {
        Http2HeadersEncoder.Configuration headersConfiguration();
        
        Http2FrameSizePolicy frameSizePolicy();
    }
}
