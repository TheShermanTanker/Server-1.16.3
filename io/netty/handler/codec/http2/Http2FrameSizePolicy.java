package io.netty.handler.codec.http2;

public interface Http2FrameSizePolicy {
    void maxFrameSize(final int integer) throws Http2Exception;
    
    int maxFrameSize();
}
