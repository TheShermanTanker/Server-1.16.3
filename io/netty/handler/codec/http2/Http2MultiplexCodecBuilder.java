package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandler;

public class Http2MultiplexCodecBuilder extends AbstractHttp2ConnectionHandlerBuilder<Http2MultiplexCodec, Http2MultiplexCodecBuilder> {
    final ChannelHandler childHandler;
    
    Http2MultiplexCodecBuilder(final boolean server, final ChannelHandler childHandler) {
        this.server(server);
        this.childHandler = checkSharable(ObjectUtil.<ChannelHandler>checkNotNull(childHandler, "childHandler"));
    }
    
    private static ChannelHandler checkSharable(final ChannelHandler handler) {
        if (handler instanceof ChannelHandlerAdapter && !((ChannelHandlerAdapter)handler).isSharable() && !handler.getClass().isAnnotationPresent((Class)ChannelHandler.Sharable.class)) {
            throw new IllegalArgumentException("The handler must be Sharable");
        }
        return handler;
    }
    
    public static Http2MultiplexCodecBuilder forClient(final ChannelHandler childHandler) {
        return new Http2MultiplexCodecBuilder(false, childHandler);
    }
    
    public static Http2MultiplexCodecBuilder forServer(final ChannelHandler childHandler) {
        return new Http2MultiplexCodecBuilder(true, childHandler);
    }
    
    public Http2Settings initialSettings() {
        return super.initialSettings();
    }
    
    public Http2MultiplexCodecBuilder initialSettings(final Http2Settings settings) {
        return super.initialSettings(settings);
    }
    
    public long gracefulShutdownTimeoutMillis() {
        return super.gracefulShutdownTimeoutMillis();
    }
    
    public Http2MultiplexCodecBuilder gracefulShutdownTimeoutMillis(final long gracefulShutdownTimeoutMillis) {
        return super.gracefulShutdownTimeoutMillis(gracefulShutdownTimeoutMillis);
    }
    
    public boolean isServer() {
        return super.isServer();
    }
    
    public int maxReservedStreams() {
        return super.maxReservedStreams();
    }
    
    public Http2MultiplexCodecBuilder maxReservedStreams(final int maxReservedStreams) {
        return super.maxReservedStreams(maxReservedStreams);
    }
    
    public boolean isValidateHeaders() {
        return super.isValidateHeaders();
    }
    
    public Http2MultiplexCodecBuilder validateHeaders(final boolean validateHeaders) {
        return super.validateHeaders(validateHeaders);
    }
    
    public Http2FrameLogger frameLogger() {
        return super.frameLogger();
    }
    
    public Http2MultiplexCodecBuilder frameLogger(final Http2FrameLogger frameLogger) {
        return super.frameLogger(frameLogger);
    }
    
    public boolean encoderEnforceMaxConcurrentStreams() {
        return super.encoderEnforceMaxConcurrentStreams();
    }
    
    public Http2MultiplexCodecBuilder encoderEnforceMaxConcurrentStreams(final boolean encoderEnforceMaxConcurrentStreams) {
        return super.encoderEnforceMaxConcurrentStreams(encoderEnforceMaxConcurrentStreams);
    }
    
    public Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector() {
        return super.headerSensitivityDetector();
    }
    
    public Http2MultiplexCodecBuilder headerSensitivityDetector(final Http2HeadersEncoder.SensitivityDetector headerSensitivityDetector) {
        return super.headerSensitivityDetector(headerSensitivityDetector);
    }
    
    public Http2MultiplexCodecBuilder encoderIgnoreMaxHeaderListSize(final boolean ignoreMaxHeaderListSize) {
        return super.encoderIgnoreMaxHeaderListSize(ignoreMaxHeaderListSize);
    }
    
    public Http2MultiplexCodecBuilder initialHuffmanDecodeCapacity(final int initialHuffmanDecodeCapacity) {
        return super.initialHuffmanDecodeCapacity(initialHuffmanDecodeCapacity);
    }
    
    public Http2MultiplexCodec build() {
        return super.build();
    }
    
    @Override
    protected Http2MultiplexCodec build(final Http2ConnectionDecoder decoder, final Http2ConnectionEncoder encoder, final Http2Settings initialSettings) {
        return new Http2MultiplexCodec(encoder, decoder, initialSettings, this.childHandler);
    }
}
