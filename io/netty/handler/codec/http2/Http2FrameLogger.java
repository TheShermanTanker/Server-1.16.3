package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.ChannelHandlerAdapter;

public class Http2FrameLogger extends ChannelHandlerAdapter {
    private static final int BUFFER_LENGTH_THRESHOLD = 64;
    private final InternalLogger logger;
    private final InternalLogLevel level;
    
    public Http2FrameLogger(final LogLevel level) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(Http2FrameLogger.class));
    }
    
    public Http2FrameLogger(final LogLevel level, final String name) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(name));
    }
    
    public Http2FrameLogger(final LogLevel level, final Class<?> clazz) {
        this(level.toInternalLevel(), InternalLoggerFactory.getInstance(clazz));
    }
    
    private Http2FrameLogger(final InternalLogLevel level, final InternalLogger logger) {
        this.level = ObjectUtil.<InternalLogLevel>checkNotNull(level, "level");
        this.logger = ObjectUtil.<InternalLogger>checkNotNull(logger, "logger");
    }
    
    public void logData(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endStream) {
        this.logger.log(this.level, "{} {} DATA: streamId={} padding={} endStream={} length={} bytes={}", ctx.channel(), direction.name(), streamId, padding, endStream, data.readableBytes(), this.toString(data));
    }
    
    public void logHeaders(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream) {
        this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} padding={} endStream={}", ctx.channel(), direction.name(), streamId, headers, padding, endStream);
    }
    
    public void logHeaders(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) {
        this.logger.log(this.level, "{} {} HEADERS: streamId={} headers={} streamDependency={} weight={} exclusive={} padding={} endStream={}", ctx.channel(), direction.name(), streamId, headers, streamDependency, weight, exclusive, padding, endStream);
    }
    
    public void logPriority(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive) {
        this.logger.log(this.level, "{} {} PRIORITY: streamId={} streamDependency={} weight={} exclusive={}", ctx.channel(), direction.name(), streamId, streamDependency, weight, exclusive);
    }
    
    public void logRstStream(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final long errorCode) {
        this.logger.log(this.level, "{} {} RST_STREAM: streamId={} errorCode={}", ctx.channel(), direction.name(), streamId, errorCode);
    }
    
    public void logSettingsAck(final Direction direction, final ChannelHandlerContext ctx) {
        this.logger.log(this.level, "{} {} SETTINGS: ack=true", ctx.channel(), direction.name());
    }
    
    public void logSettings(final Direction direction, final ChannelHandlerContext ctx, final Http2Settings settings) {
        this.logger.log(this.level, "{} {} SETTINGS: ack=false settings={}", ctx.channel(), direction.name(), settings);
    }
    
    public void logPing(final Direction direction, final ChannelHandlerContext ctx, final long data) {
        this.logger.log(this.level, "{} {} PING: ack=false bytes={}", ctx.channel(), direction.name(), data);
    }
    
    public void logPingAck(final Direction direction, final ChannelHandlerContext ctx, final long data) {
        this.logger.log(this.level, "{} {} PING: ack=true bytes={}", ctx.channel(), direction.name(), data);
    }
    
    public void logPushPromise(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding) {
        this.logger.log(this.level, "{} {} PUSH_PROMISE: streamId={} promisedStreamId={} headers={} padding={}", ctx.channel(), direction.name(), streamId, promisedStreamId, headers, padding);
    }
    
    public void logGoAway(final Direction direction, final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData) {
        this.logger.log(this.level, "{} {} GO_AWAY: lastStreamId={} errorCode={} length={} bytes={}", ctx.channel(), direction.name(), lastStreamId, errorCode, debugData.readableBytes(), this.toString(debugData));
    }
    
    public void logWindowsUpdate(final Direction direction, final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement) {
        this.logger.log(this.level, "{} {} WINDOW_UPDATE: streamId={} windowSizeIncrement={}", ctx.channel(), direction.name(), streamId, windowSizeIncrement);
    }
    
    public void logUnknownFrame(final Direction direction, final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf data) {
        this.logger.log(this.level, "{} {} UNKNOWN: frameType={} streamId={} flags={} length={} bytes={}", ctx.channel(), direction.name(), frameType & 0xFF, streamId, flags.value(), data.readableBytes(), this.toString(data));
    }
    
    private String toString(final ByteBuf buf) {
        if (!this.logger.isEnabled(this.level)) {
            return "";
        }
        if (this.level == InternalLogLevel.TRACE || buf.readableBytes() <= 64) {
            return ByteBufUtil.hexDump(buf);
        }
        final int length = Math.min(buf.readableBytes(), 64);
        return ByteBufUtil.hexDump(buf, buf.readerIndex(), length) + "...";
    }
    
    public enum Direction {
        INBOUND, 
        OUTBOUND;
    }
}
