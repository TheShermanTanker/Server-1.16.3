package io.netty.handler.codec.spdy;

public interface SpdyGoAwayFrame extends SpdyFrame {
    int lastGoodStreamId();
    
    SpdyGoAwayFrame setLastGoodStreamId(final int integer);
    
    SpdySessionStatus status();
    
    SpdyGoAwayFrame setStatus(final SpdySessionStatus spdySessionStatus);
}
