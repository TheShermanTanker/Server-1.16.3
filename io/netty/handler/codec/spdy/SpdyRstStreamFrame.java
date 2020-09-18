package io.netty.handler.codec.spdy;

public interface SpdyRstStreamFrame extends SpdyStreamFrame {
    SpdyStreamStatus status();
    
    SpdyRstStreamFrame setStatus(final SpdyStreamStatus spdyStreamStatus);
    
    SpdyRstStreamFrame setStreamId(final int integer);
    
    SpdyRstStreamFrame setLast(final boolean boolean1);
}
