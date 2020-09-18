package io.netty.handler.codec.spdy;

public interface SpdyWindowUpdateFrame extends SpdyFrame {
    int streamId();
    
    SpdyWindowUpdateFrame setStreamId(final int integer);
    
    int deltaWindowSize();
    
    SpdyWindowUpdateFrame setDeltaWindowSize(final int integer);
}
