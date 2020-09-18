package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelFuture;

public interface Http2LifecycleManager {
    void closeStreamLocal(final Http2Stream http2Stream, final ChannelFuture channelFuture);
    
    void closeStreamRemote(final Http2Stream http2Stream, final ChannelFuture channelFuture);
    
    void closeStream(final Http2Stream http2Stream, final ChannelFuture channelFuture);
    
    ChannelFuture resetStream(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3, final ChannelPromise channelPromise);
    
    ChannelFuture goAway(final ChannelHandlerContext channelHandlerContext, final int integer, final long long3, final ByteBuf byteBuf, final ChannelPromise channelPromise);
    
    void onError(final ChannelHandlerContext channelHandlerContext, final boolean boolean2, final Throwable throwable);
}
