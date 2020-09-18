package io.netty.handler.codec.spdy;

public interface SpdySynReplyFrame extends SpdyHeadersFrame {
    SpdySynReplyFrame setStreamId(final int integer);
    
    SpdySynReplyFrame setLast(final boolean boolean1);
    
    SpdySynReplyFrame setInvalid();
}
