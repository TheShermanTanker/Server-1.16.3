package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2DataWriter {
    ChannelFuture writeData(final ChannelHandlerContext channelHandlerContext, final int integer2, final ByteBuf byteBuf, final int integer4, final boolean boolean5, final ChannelPromise channelPromise);
}
