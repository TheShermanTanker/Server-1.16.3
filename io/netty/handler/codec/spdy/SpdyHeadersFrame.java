package io.netty.handler.codec.spdy;

public interface SpdyHeadersFrame extends SpdyStreamFrame {
    boolean isInvalid();
    
    SpdyHeadersFrame setInvalid();
    
    boolean isTruncated();
    
    SpdyHeadersFrame setTruncated();
    
    SpdyHeaders headers();
    
    SpdyHeadersFrame setStreamId(final int integer);
    
    SpdyHeadersFrame setLast(final boolean boolean1);
}
