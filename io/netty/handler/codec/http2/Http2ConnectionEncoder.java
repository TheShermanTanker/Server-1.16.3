package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Http2ConnectionEncoder extends Http2FrameWriter {
    void lifecycleManager(final Http2LifecycleManager http2LifecycleManager);
    
    Http2Connection connection();
    
    Http2RemoteFlowController flowController();
    
    Http2FrameWriter frameWriter();
    
    Http2Settings pollSentSettings();
    
    void remoteSettings(final Http2Settings http2Settings) throws Http2Exception;
    
    ChannelFuture writeFrame(final ChannelHandlerContext channelHandlerContext, final byte byte2, final int integer, final Http2Flags http2Flags, final ByteBuf byteBuf, final ChannelPromise channelPromise);
}
