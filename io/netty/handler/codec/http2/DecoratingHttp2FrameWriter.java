package io.netty.handler.codec.http2;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class DecoratingHttp2FrameWriter implements Http2FrameWriter {
    private final Http2FrameWriter delegate;
    
    public DecoratingHttp2FrameWriter(final Http2FrameWriter delegate) {
        this.delegate = ObjectUtil.<Http2FrameWriter>checkNotNull(delegate, "delegate");
    }
    
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeData(ctx, streamId, data, padding, endStream, promise);
    }
    
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
    }
    
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.delegate.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
    }
    
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        return this.delegate.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
    }
    
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        return this.delegate.writeRstStream(ctx, streamId, errorCode, promise);
    }
    
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        return this.delegate.writeSettings(ctx, settings, promise);
    }
    
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        return this.delegate.writeSettingsAck(ctx, promise);
    }
    
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final long data, final ChannelPromise promise) {
        return this.delegate.writePing(ctx, ack, data, promise);
    }
    
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        return this.delegate.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
    }
    
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        return this.delegate.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
    }
    
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        return this.delegate.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
    }
    
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        return this.delegate.writeFrame(ctx, frameType, streamId, flags, payload, promise);
    }
    
    public Configuration configuration() {
        return this.delegate.configuration();
    }
    
    public void close() {
        this.delegate.close();
    }
}
