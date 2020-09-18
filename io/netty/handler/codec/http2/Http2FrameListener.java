package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2FrameListener {
    int onDataRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final ByteBuf byteBuf, final int integer4, final boolean boolean5) throws Http2Exception;
    
    void onHeadersRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final Http2Headers http2Headers, final int integer4, final boolean boolean5) throws Http2Exception;
    
    void onHeadersRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final Http2Headers http2Headers, final int integer4, final short short5, final boolean boolean6, final int integer7, final boolean boolean8) throws Http2Exception;
    
    void onPriorityRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3, final short short4, final boolean boolean5) throws Http2Exception;
    
    void onRstStreamRead(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3) throws Http2Exception;
    
    void onSettingsAckRead(final ChannelHandlerContext channelHandlerContext) throws Http2Exception;
    
    void onSettingsRead(final ChannelHandlerContext channelHandlerContext, final Http2Settings http2Settings) throws Http2Exception;
    
    void onPingRead(final ChannelHandlerContext channelHandlerContext, final long long2) throws Http2Exception;
    
    void onPingAckRead(final ChannelHandlerContext channelHandlerContext, final long long2) throws Http2Exception;
    
    void onPushPromiseRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3, final Http2Headers http2Headers, final int integer5) throws Http2Exception;
    
    void onGoAwayRead(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3, final ByteBuf byteBuf) throws Http2Exception;
    
    void onWindowUpdateRead(final ChannelHandlerContext channelHandlerContext, final int integer2, final int integer3) throws Http2Exception;
    
    void onUnknownFrame(final ChannelHandlerContext channelHandlerContext, final byte byte2, final int integer, final Http2Flags http2Flags, final ByteBuf byteBuf) throws Http2Exception;
}
