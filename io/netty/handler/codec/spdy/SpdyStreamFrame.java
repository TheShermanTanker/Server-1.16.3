package io.netty.handler.codec.spdy;

public interface SpdyStreamFrame extends SpdyFrame {
    int streamId();
    
    SpdyStreamFrame setStreamId(final int integer);
    
    boolean isLast();
    
    SpdyStreamFrame setLast(final boolean boolean1);
}
